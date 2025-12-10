# Timer App Example

A command-line application built with the JMiniApp framework to record and manage multiple time entries (laps) with state persistence.

## Overview

This example demonstrates how to manage a list of complex objects (`TimeEntry` models) across application sessions. It features a basic interactive menu to start/stop a timer, view recorded laps, and use the built-in JSON file I/O capabilities for data import and export.

## Features

- **Start/Stop Timer**: Records the duration of a timed activity.
- **Persistent State**: Automatically saves all recorded entries between application runs.
- **View Entries**: Lists all previously recorded time entries.
- **JSON Export/Import**: Uses `TimerJSONAdapter` for saving and loading data.

## Project Structure
```
counter/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/timer/
    ├── TimerApp.java          # Main application class
    ├── TimerAppRunner.java    # Bootstrap configuration
    ├── TimeEntry.java         # Time entry model
    └── TimerJSONAdapter.java  # JSON format adapter
```

## Key Concepts Demonstrated

- **Complex State Management**: Handling a `List<TimeEntry>` instead of a single primitive value.
- **Application Lifecycle**: Clear implementation of `initialize()`, `run()`, and `shutdown()`.
- **Custom Adapters**: Registering a custom adapter (`TimerJSONAdapter`) for type-safe persistence.
- **File I/O**: Using `context.importData()` and `context.exportData()` for full data management.

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### 1. Build the Project

From the **project root** (the top-level `jminiapp` directory):
```bash
mvn clean install
```

## Usage Example

### Basic Commands
```
=== Timer App ===
Welcome to the Time Entry Manager!
Starting with new, empty entry list.

--- Timer Status: STOPPED ---
1. Start Timer
2. Stop and Save Lap (if running)
3. View Entries (0 total)
4. Import Entries (REPLACE)
5. Export Entries
6. Exit

Choose an option: 1
Timer started at: 2025-12-05T20:30:00Z

Choose an option: 2
Enter a name for this lap: Sprint Review
Lap saved! Duration: 00:00:15. Total entries: 1.

Choose an option: 6
Exiting...
State saved and exported to TimerApp.json.
Goodbye! Total entries recorded: 1
```

## Next Steps
Try extending this example by:
- Adding Custom Import Strategies: Implement a custom merge strategy (like MergeByIdStrategy) in TimerApp to intelligently update existing entries based on their ID.
- CSV Export: Implement and register a TimerCSVAdapter to export data to spreadsheet format.
- Active Timer Display: Modify the run() loop to display the active timer count dynamically until the user stops it.
- Advanced Data Operations: Implement functionality to delete or edit recorded time entries.

## Author Information
- Developed by: Mauricio Ivan May Pech
- Contact: mauricio20may@gmail.com
- GitHub: https://github.com/SRGmau2030/
