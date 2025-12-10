package com.jminiapp.examples.timer;

import com.jminiapp.core.engine.JMiniAppRunner;

public class TimerAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(TimerApp.class)
            .withState(TimeEntry.class)
            .withAdapters(new TimerJSONAdapter())
            .named("TimerApp") // Set the application name
            .run(args);
    }
}
