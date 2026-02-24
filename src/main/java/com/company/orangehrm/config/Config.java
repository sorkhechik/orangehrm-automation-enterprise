package com.company.orangehrm.config;

public record Config(
        String baseUrl,

        int timeout,
        int pageLoadTimeout,

        boolean networkLogging,
        boolean demoMode,

        // Browser settings (extracted to dedicated record)
        BrowserConfig browser,

        // Trace
        String traceOutputDir,
        String traceNamePattern,
        CaptureMode traceCaptureMode,
        boolean traceIncludeScreenshots,
        boolean traceIncludeSnapshots,
        boolean traceIncludeSources,

        // Screenshot
        String screenshotOutputDir,
        String screenshotNamePattern,
        CaptureMode screenshotCaptureMode
) {

    /* ---------- Timeouts ---------- */

    public int timeoutMs() {
        return timeout;
    }

    public int pageLoadTimeoutMs() {
        return pageLoadTimeout;
    }

    /* ---------- Feature flags ---------- */

    public boolean networkLoggingEnabled() {
        return networkLogging;
    }

    public boolean demoModeEnabled() {
        return demoMode;
    }

    /* ---------- Shortcuts ---------- */

    public String baseUrl() {
        return baseUrl;
    }
}
