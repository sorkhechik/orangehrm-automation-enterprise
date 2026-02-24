package com.company.orangehrm.utils;

public final class NamePattern {
    private NamePattern() {}

    /**
     * Resolves a name pattern by replacing placeholders.
     * Supported: {scenario}, {timestamp}, {tag}
     */
    public static String resolve(String pattern, String scenarioName, String tag) {
        String timestamp = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String safe = toSafeFileName(scenarioName);
        return pattern
            .replace("{scenario}", safe)
            .replace("{timestamp}", timestamp)
            .replace("{tag}", tag != null ? tag : "no-tag");
    }

    public static String toSafeFileName(String input) {
        if (input == null || input.isBlank()) return "unnamed";
        return input.trim()
            .replaceAll("[^a-zA-Z0-9_\\-]", "_")
            .replaceAll("_+", "_")
            .toLowerCase();
    }
}
