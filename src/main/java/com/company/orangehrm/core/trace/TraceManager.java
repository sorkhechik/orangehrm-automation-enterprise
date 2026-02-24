package com.company.orangehrm.core.trace;

import com.company.orangehrm.config.CaptureMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles Playwright trace recording for each test scenario.
 */
public class TraceManager {

    private static final Logger log = LoggerFactory.getLogger(TraceManager.class);

    private String scenarioName;
    private CaptureMode captureMode;

    private TraceManager() {
    }

    /** Static factory method used by BaseStep / Hooks */
    public static TraceManager from(String scenarioName, CaptureMode captureMode) {
        TraceManager manager = new TraceManager();
        manager.scenarioName = scenarioName;
        manager.captureMode = captureMode;
        log.debug("TraceManager created for '{}' with mode {}", scenarioName, captureMode);
        return manager;
    }

    public void start() {
        if (captureMode == CaptureMode.ALWAYS) {
            log.info("Starting trace for scenario: {}", scenarioName);
            // playwright tracing.start(...)
        }
    }

    public void stop(boolean failed) {
        if (captureMode == CaptureMode.ALWAYS ||
            (captureMode == CaptureMode.FAILED && failed)) {

            log.info("Saving trace for scenario: {}", scenarioName);
            // playwright tracing.stop(...)
        }
    }
}
