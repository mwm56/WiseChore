# WiseChore

**Author:** MARK MONKE
**Course project:** CS 319 Sprint 1

WiseChore helps roommates share recurring chores fairly. This first
version lets a user add chores, see which are overdue, and mark a chore
done so it automatically rotates to the next roommate.

## Technologies
- Java (plain JDK, no external libraries)
- Text-file storage (`data/chores.txt`, `data/logs.txt`)

## Prerequisites
- JDK 17 or newer (developed on JDK 21). Check with: `javac -version`

## Installation
No dependencies to install. Clone the repository and open a terminal in
the project's root folder (the one containing this README).

## Configuration
None required. Saved data goes in a `data/` folder, created
automatically in the folder you run the app from. To use a different
folder, pass it as an argument (see below).

## Running the application
From the project root folder:

```bash
mkdir out
javac -d out $(find src/main -name "*.java")
java -cp out com.wisechore.Main
```

On Windows (PowerShell):

```powershell
mkdir out
javac -d out (Get-ChildItem -Recurse src/main -Filter *.java).FullName
java -cp out com.wisechore.Main
```

You should see a menu starting with `=== WiseChore ===`.

## Trying the Sprint 1 functionality
1. Choose **1** and add a chore: name `Take out trash`, frequency `2`,
   roommates `Alex, Sam, Jordan`, first due date a past date such as `2020-01-01`.
2. Choose **2**. The chore shows as **OVERDUE**, assigned to Alex.
3. Choose **3**, enter `1`. It is now assigned to Sam with a new due date.
4. Choose **4** to exit, then run the app again and choose **2**.
   The chore and its new assignee are still there.
5. Try invalid input in option 1 (empty name, no roommates, duplicate
   roommate, bad date) and confirm an error message appears.

## Architecture (SOLID)
- `models/` hold data only.
- `repositories/ChoreRepository` is an interface. `FileChoreRepository`
  (used now) and `InMemoryChoreRepository` both implement it; switching
  storage is a one-line change in `Main`.
- `services/` each own one rule: `ChoreService` (validation and sorting),
  `RotationService` (mark done and rotate), `OverdueService` (overdue
  detection). They depend on the repository interface, not a concrete class.
- `ConsoleApp` handles only input and output.

## Known limitations
- Console interface only; no web UI yet.
- Fairness summary, editing/deleting chores, and chore history view are
  planned for later sprints.
- A chore is marked overdue only when the app checks (on view / mark done).
- Monthly frequency is treated as 30 days.
- Names cannot contain the `|` character (used in the data file).
