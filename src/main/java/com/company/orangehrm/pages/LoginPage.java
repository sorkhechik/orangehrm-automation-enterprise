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
			log.info("Current URL before wait: {}", page().url());
			waitForPage("**/dashboard1/**", 90_000);
			log.info("URL matched dashboard");
			
			log.info("Waiting for dashboard element visibility...");
		    waitForVisible($(DASHBOARD_TITLE));
		    
			boolean visible = isElementVisible(DASHBOARD_TITLE);
			log.info("Dashboard element visible: {}", visible);
			return visible;
		} catch (Exception e) {
			log.warn("Login failed — error: {}", e.getMessage());
			log.warn("URL at failure: {}", page().url());
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

