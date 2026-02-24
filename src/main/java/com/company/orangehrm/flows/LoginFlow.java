package com.company.orangehrm.flows;

import com.company.orangehrm.driver.PlaywrightManager;
import com.company.orangehrm.pages.LoginPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginFlow {

    private static final Logger log = LogManager.getLogger(LoginFlow.class);

    private final LoginPage loginPage;
    private String          errorMessage;

    public LoginFlow(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    /**
     * @return true اگر login موفق بود و dashboard نمایش داده شد
     */
    public boolean login(String username, String password) {
        String baseUrl = PlaywrightManager.config().baseUrl();
        PlaywrightManager.navigate(baseUrl + "/web/index.php/auth/login");

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        boolean success = loginPage.isDashboardVisible();

        if (!success) {
            errorMessage = loginPage.getErrorMessage();
            log.warn("Login failed for user '{}' — error: {}", username, errorMessage);
        } else {
            log.info("Login successful for user '{}'", username);
        }

        return success;
    }

    /**
     * آخرین پیام خطا را برمی‌گرداند (در صورت failure)
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
