package com.jminiapp.examples.timer;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.io.IOException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A command-line timer application demonstrating the JMiniApp framework.
 *
 * This app allows users to:
 * - Start a new timer
 * - Stop the timer and save the lap as a TimeEntry
 * - View all saved TimeEntries
 * - Export/Import data using the JSON adapter
 */
public class TimerApp extends JMiniApp {
    // List to hold the application state (all recorded time entries)
    private List<TimeEntry> entries;
    
    // Transient field to track the start time (not persisted to file)
    private Instant startTime;
    
    private Scanner scanner;
    private boolean running;

    /**
     * Required constructor for JMiniApp subclass.
     */
    public TimerApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== Timer App ===");
        System.out.println("Welcome to the Time Entry Manager!");

        scanner = new Scanner(System.in);
        running = true;
        startTime = null;

        // Try to load existing entries from context
        // NOTE: We copy data to a new ArrayList to follow the best practice 
        // of not modifying the list returned directly by context.getData().
        List<TimeEntry> data = context.getData();
        if (data != null && !data.isEmpty()) {
            entries = new ArrayList<>(data);
            System.out.println("Loaded " + entries.size() + " previous entries.");
        } else {
            entries = new ArrayList<>();
            System.out.println("Starting with new, empty entry list.");
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // 1. Save the final list of entries to context for persistence
        context.setData(entries);

        // 2. Export the state to the default JSON file (TimerApp.json)
        try {
            // Note: The format "json" is recognized due to TimerJSONAdapter registration
            context.exportData("json"); 
            System.out.println("\nState saved and exported to " + appName + ".json.");
        } catch (IOException e) {
            // It's important to handle save errors gracefully
            System.err.println("\nError saving application state: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.println("\nError: JSON adapter is not correctly configured.");
        }

        // 3. Clean up resources
        if (scanner != null) {
            scanner.close();
        }

        System.out.println("Goodbye! Total entries recorded: " + entries.size());
    }

    private void displayMenu() {
        // Displays the status (RUNNING or STOPPED)
        String status = startTime == null 
            ? "STOPPED" 
            : "RUNNING (since " + startTime.toString().substring(11, 19) + "Z)"; // Show only HH:MM:SS for brevity
            
        System.out.println("\n--- Timer Status: " + status + " ---");
        System.out.println("1. Start Timer");
        System.out.println("2. Stop and Save Lap (if running)");
        System.out.println("3. View Entries (" + entries.size() + " total)");
        System.out.println("4. Import Entries (REPLACE)");
        System.out.println("5. Export Entries");
        System.out.println("6. Exit");
        System.out.print("\nChoose an option: ");
    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    startTimer();
                    break;
                case "2":
                    stopAndSaveLap();
                    break;
                case "3":
                    viewEntries();
                    break;
                case "4":
                    importData();
                    break;
                case "5":
                    exportData();
                    break;
                case "6":
                    running = false;
                    System.out.println("\nExiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } catch (Exception e) {
            System.out.println("Error processing input: " + e.getMessage());
        }
    }

    private void startTimer() {
        if (startTime == null) {
            startTime = Instant.now();
            System.out.println("Timer started at: " + startTime.toString());
        } else {
            System.out.println("Timer is already running since: " + startTime.toString());
        }
    }

    private void stopAndSaveLap() {
        if (startTime == null) {
            System.out.println("Timer is not running. Start it first (Option 1).");
            return;
        }

        Instant endTime = Instant.now();
        // Calculate duration in seconds
        long durationSeconds = Duration.between(startTime, endTime).getSeconds();
        startTime = null; // Reset timer

        System.out.print("Enter a name for this lap: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Lap " + (entries.size() + 1);
        }

        // Use the constructor for TimeEntry, which now handles String for createdAt
        TimeEntry newEntry = new TimeEntry(name, durationSeconds);
        entries.add(newEntry);
        
        System.out.println("Lap saved! Duration: " + newEntry.getFormattedDuration() + ". Total entries: " + entries.size() + ".");
    }
    
    private void viewEntries() {
        if (entries.isEmpty()) {
            System.out.println("No time entries recorded yet.");
            return;
        }
        
        System.out.println("\n--- Recorded Entries ---");
        for (int i = 0; i < entries.size(); i++) {
            TimeEntry entry = entries.get(i);
            // Display simplified ID for readability
            String shortId = entry.getId() != null ? entry.getId().substring(0, 4) : "N/A";
            System.out.println((i + 1) + ". " + entry.getName() + " -> " + entry.getFormattedDuration() + " (ID: " + shortId + ")");
        }
    }

    private void importData() {
        try {
            System.out.print("Enter filename to import (e.g., TimerApp.json): ");
            String fileName = scanner.nextLine().trim();
            
            // The Runner registers the JSON adapter ("json")
            context.importData(fileName, "json");
            
            // Reload local state from context after successful import
            this.entries = context.getData();
            System.out.println("Successfully imported " + entries.size() + " entries from " + fileName + ".");
        } catch (IOException e) {
            System.out.println("Error importing file: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void exportData() {
        try {
            System.out.print("Enter filename to export (e.g., TimerApp.json): ");
            String fileName = scanner.nextLine().trim();
            
            // Save current data to context (ensures consistency)
            context.setData(entries);
            
            // Export data using the registered "json" adapter
            context.exportData(fileName, "json");
            System.out.println("Successfully exported " + entries.size() + " entries to: " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}