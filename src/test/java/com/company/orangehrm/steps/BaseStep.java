package com.company.orangehrm.steps;

import com.company.orangehrm.config.CaptureMode;
import com.company.orangehrm.config.Config;
import com.company.orangehrm.core.trace.TraceManager;
import com.company.orangehrm.driver.PlaywrightManager;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for Step Definitions.
 * Uses lazy access to PlaywrightManager to ensure Cucumber lifecycle safety.
 */
public abstract class BaseStep {

    protected final Logger log = LogManager.getLogger(getClass());

    private TraceManager traceManager;

    protected BaseStep() {
        log.debug("BaseStep created for {}", getClass().getSimpleName());
    }

    protected Page page() {
        return PlaywrightManager.page();
    }

    protected Config config() {
        return PlaywrightManager.config();
    }

    protected TraceManager traceManager() {
        if (traceManager == null) {
            CaptureMode mode = config().traceCaptureMode() != null
                    ? config().traceCaptureMode()
                    : CaptureMode.NEVER;

            traceManager = TraceManager.from(getClass().getSimpleName(), mode);
        }
        return traceManager;
    }

    protected void startTrace() {
        traceManager().start();
    }

    protected void stopTrace(boolean failed) {
        traceManager().stop(failed);
    }

    protected void logStep(String message, Object... args) {
        log.info("" + message, args);
    }
}