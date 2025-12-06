---
sidebar_position: 4
---

# Timer App Example Application

A comprehensive example demonstrating how to manage a list of complex state objects across application sessions using JMiniApp's persistence and I/O features.

### Key Concepts Demonstrated

- **Complex State Management**: Persisting and retrieving a `List<TimeEntry>`.
- **Custom State Model**: Defining a model with `String`, `long`, and `Instant` fields.
- **JSON Adapter**: Implementing `JSONAdapter` for seamless file serialization.
- **Application Lifecycle**: Using `initialize`, `run`, and `shutdown` to manage an interactive console application.

## 1. Project Setup and Maven Configuration

The new application resides in the `examples/timer-app` directory. The Maven configuration (`pom.xml`) links to the core framework and defines the `TimerAppRunner` as the execution entry point.

## 2. Define the State Model: `TimeEntry.java`

The model tracks a unique ID, a user-defined name, and the recorded duration, leveraging `java.time` for time tracking.

```java
public class TimeEntry {
    private final String id;
    private String name;
    private long durationSeconds;
    private final Instant createdAt;
    // ... Constructors, getters, and utility methods ...
}
```

## 3. Implement the JSON Adapter: `TimerJSONAdapter.java`
This adapter extends the built-in `JSONAdapter` to handle the `TimeEntry` class, correctly implementing the required method (note the lowercase 's' in getstateClass()).

```java
public class TimerJSONAdapter implements JSONAdapter<TimeEntry> {

    @Override
    public Class<TimeEntry> getstateClass() {
        return TimeEntry.class;
    }
}
```
## 4. Create the Main Application: `TimerApp.java`
The core logic extends `JMiniApp.` The initialize() method loads the persisted state, `run()` manages the interactive loop, and `shutdown()` saves the state.

### Initialize Method

```java
@Override
protected void initialize() {
    // ... setup scanner and initial state ...
    List<TimeEntry> data = context.getData();
    entries = data != null && !data.isEmpty() ? new ArrayList<>(data) : new ArrayList<>();
    // ... log message ...
}
```
### Shutdown Method

```java
@Override
protected void shutdown() {
    context.setData(entries); // Saves the final state to be persisted
    try {
        context.exportData("json"); // Exports to TimerApp.json
    } catch (IOException e) {
        // ... error handling ...
    }
    // ... cleanup ...
}
```

## Booststrap Configuration: `TimerAppRunner.java`
The runner is the application entry point, configuring the state model and registering the file format adapter using the fluent builder pattern.

```java
public class TimerAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(TimerApp.class)
            .withState(TimeEntry.class)             // Declares the model type
            .withAdapters(new TimerJSONAdapter())   // Registers the JSON adapter
            .named("TimerApp")
            .run(args);
    }
}
```





