package com.jminiapp.examples.timer;

import com.jminiapp.core.adapters.JSONAdapter;

/**
 * JSON adapter for TimeEntry objects.
 *
 * <p>This adapter enables the Timer app to import and export recorded time entries
 * to/from JSON files, leveraging the framework's default Gson implementation.</p>
 */
public class TimerJSONAdapter implements JSONAdapter<TimeEntry> {

    @Override
    public Class<TimeEntry> getstateClass() {
        return TimeEntry.class;
    }
}
