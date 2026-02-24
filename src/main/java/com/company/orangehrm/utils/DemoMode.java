package com.company.orangehrm.utils;

import com.company.orangehrm.driver.PlaywrightManager;

public final class DemoMode {

    private DemoMode() {}

    public static void slowDown(int millis) {
        if (!PlaywrightManager.config().demoModeEnabled()) return;
        try {
            PlaywrightManager.page().waitForTimeout(millis);
        } catch (Exception ignored) {}
    }

    public static boolean isEnabled() {
        return PlaywrightManager.config().demoModeEnabled();
    }
}
