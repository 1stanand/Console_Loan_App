# LibraryDesk

LibraryDesk is a simple console based library management application written in Java 17. It demonstrates basic OOP design, collections and exception handling with a clean architecture that is easy to extend.

## Build & Run

Requires Maven and JDK 17.

```bash
mvn package
java -jar target/librarydesk-1.0-SNAPSHOT.jar
```

## Features
- Manage books: add, list with paging, search, remove.
- Manage members: register, list, search.
- Borrow and return books with automatic availability tracking.
- List active and overdue loans.
- Reports: inventory summary and top loaned books.
- Colorful ANSI console UI with input validation and helpful prompts.

## Navigation
On start the application shows a banner and the main menu:

```
1) Books 2) Members 3) Loans 4) Reports 0) Exit
```

Each section has its own submenu. Tips are shown inside menus to guide usage. Sample data is seeded on first run for quick testing.

## Testing
Run unit tests with:

```bash
mvn test
```

## Future Roadmap
- Persist data to CSV/JSON files using a pluggable `StateStore` (stub `JsonFileStateStore` provided).
- Support fines for overdue loans.
- Authentication/roles for staff vs patrons.
- REST API wrapper.
- GUI front-end using JavaFX.
