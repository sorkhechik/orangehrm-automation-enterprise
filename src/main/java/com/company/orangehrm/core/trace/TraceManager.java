package com.company.orangehrm.core.trace;

import com.company.orangehrm.config.CaptureMode;
import com.company.orangehrm.driver.PlaywrightManager;
import com.microsoft.playwright.Tracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class TraceManager {

    private static final Logger log = LoggerFactory.getLogger(TraceManager.class);

    private String scenarioName;
    private CaptureMode captureMode;

    private TraceManager() {}

    public static TraceManager from(String scenarioName, CaptureMode captureMode) {
        TraceManager manager = new TraceManager();
        manager.scenarioName  = scenarioName;
        manager.captureMode   = captureMode;
        log.debug("TraceManager created for scenario: {} | mode: {}", scenarioName, captureMode);
        return manager;
    }

    public void start() {
        // در حالت FAILED هم باید trace شروع بشه تا بتونیم در صورت شکست ذخیره‌اش کنیم
        if (captureMode == CaptureMode.ALWAYS || captureMode == CaptureMode.FAILED) {
            log.info("Starting trace for scenario: {}", scenarioName);
            PlaywrightManager.context().tracing().start(
                new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false)
            );
        }
    }

    public void stop(boolean failed) {
        boolean shouldSave = (captureMode == CaptureMode.ALWAYS)
                          || (captureMode == CaptureMode.FAILED && failed);

        if (!shouldSave) {
            // اگر نیازی به ذخیره نیست، فقط trace رو بدون ذخیره متوقف کن
            if (captureMode == CaptureMode.FAILED) {
                try {
                    PlaywrightManager.context().tracing().stop();
                } catch (Exception e) {
                    log.warn("Could not stop trace cleanly: {}", e.getMessage());
                }
            }
            return;
        }

        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String path = "target/traces/" + safeName + "-" + System.currentTimeMillis() + ".zip";

        log.info("Saving trace for scenario: {} → {}", scenarioName, path);
        try {
            PlaywrightManager.context().tracing().stop(
                new Tracing.StopOptions().setPath(Paths.get(path))
            );
            log.info("Trace saved: {}", path);
        } catch (Exception e) {
            log.error("Failed to save trace for scenario '{}': {}", scenarioName, e.getMessage());
        }
    }
}
