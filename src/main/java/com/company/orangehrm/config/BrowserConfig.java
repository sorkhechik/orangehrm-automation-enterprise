package com.company.orangehrm.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record BrowserConfig(

        // Core
        String browserType,
        boolean headless,
        int slowMo,
        String executablePath,

        // Viewport
        int viewportWidth,
        int viewportHeight,
        double deviceScaleFactor,
        boolean hasTouch,
        String colorScheme,

        // Context
        List<String> permissions,
        String locale,
        String timezoneId,
        boolean bypassCsp,
        boolean ignoreHttpsErrors,
        boolean acceptDownloads,
        String downloadDir,

        // Proxy
        boolean proxyEnabled,
        String proxyServer,
        String proxyUsername,
        String proxyPassword,
        String proxyBypass,

        // Video
        CaptureMode videoCaptureMode,
        String videoOutputDir,
        int videoWidth,
        int videoHeight,

        // Launch args
        List<String> launchArgs

) {
    public boolean hasExecutablePath() {
        return executablePath != null && !executablePath.isBlank();
    }

    public boolean hasProxy() {
        return proxyEnabled && proxyServer != null && !proxyServer.isBlank();
    }

    public boolean videoEnabled() {
        return videoCaptureMode != CaptureMode.NEVER;
    }

    public static List<String> parseList(String raw) {
        if (raw == null || raw.isBlank()) return Collections.emptyList();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
