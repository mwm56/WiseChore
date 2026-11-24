package com.wisechore;

import com.wisechore.models.Chore;
import com.wisechore.models.Frequency;
import com.wisechore.services.ChoreService;
import com.wisechore.services.OverdueService;
import com.wisechore.services.RotationService;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Text menu for the Sprint 1 workflow: add a chore, view chores, mark one done. */
public class ConsoleApp {

    private final ChoreService choreService;
    private final RotationService rotationService;
    private final OverdueService overdueService;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleApp(ChoreService choreService, RotationService rotationService,
                      OverdueService overdueService, InputStream input, PrintStream out) {
        this.choreService = choreService;
        this.rotationService = rotationService;
        this.overdueService = overdueService;
        this.in = new Scanner(input);
        this.out = out;
    }

    public void run() {
        out.println("=== WiseChore ===");
        while (true) {
            out.println();
            out.println("1) Add a chore");
            out.println("2) View chores");
            out.println("3) Mark a chore done");
            out.println("4) Exit");
            String choice = prompt("Choose an option: ");
            if (choice == null) return; // end of input
            switch (choice.trim()) {
                case "1" -> addChore();
                case "2" -> viewChores();
                case "3" -> markDone();
                case "4" -> { out.println("Goodbye!"); return; }
                default -> out.println("Please enter 1, 2, 3, or 4.");
            }
        }
    }

    private void addChore() {
        String name = prompt("Chore name: ");
        if (name == null) return;

        out.println("Frequency: 1) Daily  2) Weekly  3) Biweekly  4) Monthly");
        String freqInput = prompt("Choose frequency (1-4): ");
        if (freqInput == null) return;
        Frequency frequency = switch (freqInput.trim()) {
            case "1" -> Frequency.DAILY;
            case "2" -> Frequency.WEEKLY;
            case "3" -> Frequency.BIWEEKLY;
            case "4" -> Frequency.MONTHLY;
            default -> null;
        };

        String roommatesInput = prompt("Roommates in rotation order (comma-separated, first one is assigned first): ");
        if (roommatesInput == null) return;

        String dateInput = prompt("First due date (YYYY-MM-DD): ");
        if (dateInput == null) return;
        LocalDate dueDate = null;
        try {
            dueDate = LocalDate.parse(dateInput.trim());
        } catch (DateTimeParseException e) {
            out.println("Error: due date must look like 2026-11-15.");
            return;
        }

        try {
            Chore chore = choreService.addChore(name, frequency,
                    Arrays.asList(roommatesInput.split(",")), dueDate);
            out.println("Added: " + chore.getName() + " (assigned to " + chore.getCurrentRoommate() + ")");
        } catch (IllegalArgumentException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void viewChores() {
        overdueService.refreshOverdueStatuses();
        List<Chore> chores = choreService.listChoresByDueDate();
        if (chores.isEmpty()) {
            out.println("No chores yet. Choose option 1 to add one.");
            return;
        }
        out.println();
        out.printf("%-3s %-22s %-10s %-12s %-12s %s%n", "#", "Chore", "Frequency", "Assigned to", "Due", "Status");
        int n = 1;
        for (Chore c : chores) {
            out.printf("%-3d %-22s %-10s %-12s %-12s %s%n", n++, c.getName(),
                    c.getFrequency(), c.getCurrentRoommate(), c.getNextDueDate(), c.getStatus());
        }
    }

    private void markDone() {
        overdueService.refreshOverdueStatuses();
        List<Chore> chores = choreService.listChoresByDueDate();
        if (chores.isEmpty()) {
            out.println("No chores to complete yet.");
            return;
        }
        viewChores();
        String input = prompt("Number of the chore to mark done: ");
        if (input == null) return;
        int index;
        try {
            index = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            out.println("Error: please enter a number from the list.");
            return;
        }
        if (index < 1 || index > chores.size()) {
            out.println("Error: there is no chore number " + index + ".");
            return;
        }
        Chore updated = rotationService.markDone(chores.get(index - 1).getId());
        out.println("Done! '" + updated.getName() + "' is now assigned to " + updated.getCurrentRoommate()
                + ", due " + updated.getNextDueDate() + ".");
    }

    private String prompt(String message) {
        out.print(message);
        return in.hasNextLine() ? in.nextLine() : null;
    }
}
