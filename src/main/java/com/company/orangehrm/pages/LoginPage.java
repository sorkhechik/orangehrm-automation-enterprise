package com.company.orangehrm.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.orangehrm.core.BasePage;

public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    // ─── Locators ────────────────────────────────────────────────
    private static final String USERNAME_INPUT  = "input[name='username']";
    private static final String PASSWORD_INPUT  = "input[name='password']";
    private static final String LOGIN_BUTTON    = "button[type='submit']";
    private static final String DASHBOARD_TITLE = ".oxd-topbar-header-title";
    private static final String ERROR_ALERT     = ".oxd-alert-content-text";

    // ─── Actions ─────────────────────────────────────────────────

    public void enterUsername(String username) {
        log.info("Entering username: {}", username);
        fillField(USERNAME_INPUT, username);
    }

    public void enterPassword(String password) {
        log.info("Entering password");
        fillField(PASSWORD_INPUT, password);
    }

    public void clickLogin() {
        log.info("Clicking login button");
        clickElement(LOGIN_BUTTON);
    }

    // ─── Assertions ──────────────────────────────────────────────

    public boolean isDashboardVisible() {
        try {
            // ابتدا منتظر تغییر URL می‌مانیم
            waitForPage("**/dashboard/**" , 90_000);
            
            // سپس چک می‌کنیم المان اصلی داشبورد لود شده باشد
            return isElementVisible(DASHBOARD_TITLE);
        } catch (Exception e) {
            log.error("Dashboard check failed: {}", e.getMessage());
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            waitForVisible($(ERROR_ALERT));
            return $(ERROR_ALERT).innerText().trim();
        } catch (Exception e) {
            return null;
        }
    }
}

