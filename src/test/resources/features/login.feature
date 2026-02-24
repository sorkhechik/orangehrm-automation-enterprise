Feature: Login

  @smoke @login
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with username "Admin" and password "admin123"
    Then the user should be redirected to the dashboard

  @negative @login
  Scenario: Login fails with invalid credentials
    Given the user is on the login page
    When the user logs in with username "invalid_user" and password "wrong_pass"
    Then an error message should be displayed
