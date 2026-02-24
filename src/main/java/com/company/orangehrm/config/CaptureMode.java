package com.company.orangehrm.config;

public enum CaptureMode {

    ALWAYS,
    FAILED,
    NEVER;

    public static CaptureMode from(String value, CaptureMode def) {
        if (value == null || value.isBlank()) {
            return def;
        }
        try {
            return CaptureMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return def;
        }
    }
}
