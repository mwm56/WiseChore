package com.wisechore.repositories;

import com.wisechore.models.Chore;
import com.wisechore.models.ChoreStatus;
import com.wisechore.models.CompletionLog;
import com.wisechore.models.Frequency;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores chores and completion logs in two plain text files so data
 * survives restarting the app.
 *
 * SOLID note (Open/Closed + Liskov): this is a NEW class that plugs into
 * the existing ChoreRepository interface. No service code was modified
 * to add persistence.
 *
 * File format: one record per line, fields separated by '|'.
 *   chores.txt: id|name|frequency|roommate1,roommate2|current|nextDue|status|lastCompleted
 *   logs.txt:   id|choreId|choreName|roommate|completedOn
 * ('-' means "no value").
 */
public class FileChoreRepository extends InMemoryChoreRepository {

    private static final String NONE = "-";

    private final Path choresFile;
    private final Path logsFile;

    public FileChoreRepository(Path dataDirectory) {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create data folder " + dataDirectory, e);
        }
        this.choresFile = dataDirectory.resolve("chores.txt");
        this.logsFile = dataDirectory.resolve("logs.txt");
        load();
    }

    @Override
    public void add(Chore chore) {
        super.add(chore);
        save();
    }

    @Override
    public void update(Chore chore) {
        super.update(chore);
        save();
    }

    @Override
    public void delete(String choreId) {
        super.delete(choreId);
        save();
    }

    @Override
    public void addCompletionLog(CompletionLog log) {
        super.addCompletionLog(log);
        save();
    }

    private void load() {
        try {
            if (Files.exists(choresFile)) {
                for (String line : Files.readAllLines(choresFile)) {
                    if (line.isBlank()) continue;
                    String[] f = line.split("\\|", -1);
                    super.add(new Chore(
                            f[0], f[1], Frequency.valueOf(f[2]),
                            new ArrayList<>(Arrays.asList(f[3].split(","))),
                            f[4], LocalDate.parse(f[5]),
                            ChoreStatus.valueOf(f[6]),
                            f[7].equals(NONE) ? null : LocalDate.parse(f[7])));
                }
            }
            if (Files.exists(logsFile)) {
                for (String line : Files.readAllLines(logsFile)) {
                    if (line.isBlank()) continue;
                    String[] f = line.split("\\|", -1);
                    super.addCompletionLog(new CompletionLog(f[1], f[2], f[3], LocalDate.parse(f[4])));
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Could not read saved data. Check or delete the files in "
                    + choresFile.getParent() + ": " + e.getMessage(), e);
        }
    }

    private void save() {
        List<String> choreLines = new ArrayList<>();
        for (Chore c : getAll()) {
            choreLines.add(String.join("|",
                    c.getId(), c.getName(), c.getFrequency().name(),
                    String.join(",", c.getRoommatesInRotation()),
                    c.getCurrentRoommate(), c.getNextDueDate().toString(),
                    c.getStatus().name(),
                    c.getLastCompletedDate() == null ? NONE : c.getLastCompletedDate().toString()));
        }
        List<String> logLines = new ArrayList<>();
        for (CompletionLog l : getAllCompletionLogs()) {
            logLines.add(String.join("|",
                    l.getId(), l.getChoreId(), l.getChoreName(),
                    l.getRoommate(), l.getCompletedOn().toString()));
        }
        try {
            Files.write(choresFile, choreLines);
            Files.write(logsFile, logLines);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save data", e);
        }
    }
}
