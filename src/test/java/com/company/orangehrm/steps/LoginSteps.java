package com.company.orangehrm.steps;

import com.company.orangehrm.flows.LoginFlow;
import com.company.orangehrm.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class LoginSteps extends BaseStep {

    private LoginFlow loginFlow;
    private boolean   loginResult;
    private String    errorMessage;

    // ─── Given ───────────────────────────────────────────────────

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        LoginPage loginPage = new LoginPage();
        this.loginFlow = new LoginFlow(loginPage);
    }

    // ─── When ────────────────────────────────────────────────────

    /**
     * هر دو سناریو (valid و invalid) از همین Step استفاده می‌کنند.
     * پارامترها مستقیم از feature دریافت می‌شوند.
     */
    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        loginResult  = loginFlow.login(username, password);
        errorMessage = loginFlow.getErrorMessage();
    }

    // ─── Then ────────────────────────────────────────────────────

    @Then("the user should be redirected to the dashboard")
    public void theUserShouldBeRedirectedToTheDashboard() {
        Assertions.assertTrue(loginResult,
                "Expected dashboard to be visible after successful login, but it was not.");
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        Assertions.assertFalse(loginResult,
                "Expected login to fail, but it succeeded.");
        Assertions.assertNotNull(errorMessage,
                "Expected an error message to be displayed, but none was found.");
        Assertions.assertFalse(errorMessage.isBlank(),
                "Expected a non-empty error message.");
    }
}
