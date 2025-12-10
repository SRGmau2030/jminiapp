package com.jminiapp.examples.timer;

import java.time.Instant;
import java.util.UUID;

/**
 * Model representing a single recorded time entry or lap.
 * * This object is stored in the application state and is persisted
 * across sessions using JSON serialization.
 */
public class TimeEntry {
    private final String id;
    private String name;
    private long durationSeconds;

    private final String createdAt;

    /**
     * Constructor used when creating a new entry from the running timer.
     * * @param name The user-defined name for this time entry.
     * @param durationSeconds The total recorded time in seconds.
     */
    public TimeEntry(String name, long durationSeconds) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.durationSeconds = durationSeconds;
        this.createdAt = Instant.now().toString();
    }

    /**
     * Private constructor used ONLY by the Gson deserialization in JSONAdapter
     * to reconstruct the object from saved data.
     */
    private TimeEntry() {
        this.id = null; // Should be set by Gson if present in JSON
        this.name = "Unnamed Entry";
        this.durationSeconds = 0;
        this.createdAt = Instant.now().toString(); // Will be overwritten by Gson
    }

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // --- Utility Methods ---
    
    public String getFormattedDuration() {
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id.substring(0, 4), name, getFormattedDuration());
    }

    // Since we are using this model in a list and comparing it for SkipExistingStrategy,
    // we need to override equals/hashCode. For simplicity, we just check ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return id != null ? id.equals(timeEntry.id) : timeEntry.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
