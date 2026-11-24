package com.wisechore;

import com.wisechore.repositories.ChoreRepository;
import com.wisechore.repositories.FileChoreRepository;
import com.wisechore.services.ChoreService;
import com.wisechore.services.OverdueService;
import com.wisechore.services.RotationService;

import java.nio.file.Path;

/**
 * Composition root: the ONLY place that chooses the concrete repository.
 * To switch storage, change the one line that creates `repository`.
 *
 * Optional argument: a folder for the data files (default: ./data).
 */
public class Main {

    public static void main(String[] args) {
        Path dataDir = Path.of(args.length > 0 ? args[0] : "data");
        ChoreRepository repository = new FileChoreRepository(dataDir);

        ConsoleApp app = new ConsoleApp(
                new ChoreService(repository),
                new RotationService(repository),
                new OverdueService(repository),
                System.in, System.out);
        app.run();
    }
}
