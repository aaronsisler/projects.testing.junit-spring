package com.ebsolutions.spring.junit.shared;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsStopwatch {
    /**
     * Stores the start time (in milliseconds) when an object of the StopWatch class is initialized.
     */
    private final long startTime;

    public MetricsStopwatch() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Logs the elapsed time since the time the object of Stopwatch was initialized.
     *
     * @param callingCodeBlock Function or location that this method is called in
     */
    public void logElapsedTime(String callingCodeBlock) {
        log.info("ELAPSED TIME: {} :: {}", System.currentTimeMillis() - startTime, callingCodeBlock);
    }
}
