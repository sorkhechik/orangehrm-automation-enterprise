# 🤝 Contributing to OrangeHRM Automation Enterprise

Thank you for taking the time to contribute! This document provides all the guidelines you need to contribute effectively to this enterprise-grade BDD test automation framework.

---

## 📑 Table of Contents

1. [Code of Conduct](#-code-of-conduct)
2. [Getting Started](#-getting-started)
3. [Project Architecture Quick Reference](#-project-architecture-quick-reference)
4. [Development Workflow](#-development-workflow)
5. [Branching Strategy](#-branching-strategy)
6. [Commit Convention](#-commit-convention)
7. [Writing Feature Files](#-writing-feature-files)
8. [Writing Step Definitions](#-writing-step-definitions)
9. [Writing Page Objects](#-writing-page-objects)
10. [Writing Flows](#-writing-flows)
11. [Code Style Guidelines](#-code-style-guidelines)
12. [Pull Request Process](#-pull-request-process)
13. [Reporting Bugs](#-reporting-bugs)
14. [Suggesting Features](#-suggesting-features)

---

## 📜 Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment. We expect all contributors to:

- Use welcoming and inclusive language
- Respect differing viewpoints and experiences
- Accept constructive criticism gracefully
- Focus on what is best for the project and team

---

## 🚀 Getting Started

### 1. Fork & Clone
```bash
# Fork via GitHub UI, then:
git clone https://github.com/YOUR_USERNAME/orangehrm-automation-enterprise.git
cd orangehrm-automation-enterprise
```

### 2. Add upstream remote

```bash
git remote add upstream https://github.com/sorkhechik/orangehrm-automation-enterprise.git
git remote -v  # verify
```

### 3. Install dependencies & browsers
```bash
# Install Playwright browser binaries (first time only)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="install --with-deps"
```

# Verify build
```bash
mvn clean compile
```

### 4. Run the test suite to confirm everything works

```bash
mvn clean test -Denv=uat -Dheadless=true
```
---

## 🏛 Project Architecture Quick Reference

Before contributing, understand the layer responsibilities:

```
Feature Files  (.feature)        ← BDD scenarios in Gherkin — what to test
↓
Step Definitions  (steps/)       ← Glue code — how Gherkin maps to Java
↓
Flows  (flows/)                  ← Reusable business workflows (e.g. LoginFlow)
↓
Page Objects  (pages/)           ← UI interactions per page, extends BasePage
↓
PlaywrightManager + BasePage     ← Browser lifecycle & shared utilities
```
**Key rule:** each layer should only call the layer directly below it.  
Step definitions call flows or page objects — **never** Playwright API directly.

---

## 🔄 Development Workflow

```bash
# 1. Sync your fork with upstream before starting
git fetch upstream
git checkout main
git merge upstream/main
```

# 2. Create a feature branch (see Branching Strategy)
```bash
git checkout -b feat/login-sso-scenarios
```

# 3. Make your changes following the guidelines below

# 4. Run affected tests
```bash
mvn clean test -Denv=uat -Dheadless=true -Dcucumber.filter.tags="@Login"
```

# 5. Run full suite to catch regressions
```bash
mvn clean test -Denv=uat -Dheadless=true
```

# 6. Commit using Conventional Commits format
```bash
git add .
git commit -m "feat(login): add SSO login scenarios"
```

# 7. Push and open Pull Request
```bash
git push origin feat/login-sso-scenarios
```

---

## 🌿 Branching Strategy

| Branch | Purpose |
|---|---|
| `main` | Stable, production-ready code |
| `feat/<scope>` | New feature or new test scenario |
| `fix/<scope>` | Bug fix |
| `refactor/<scope>` | Code restructure without behavior change |
| `chore/<scope>` | Dependency updates, config, CI changes |
| `docs/<scope>` | Documentation only changes |

### Examples

```bash
git checkout -b feat/employee-leave-scenarios
git checkout -b fix/login-page-selector-timeout
git checkout -b refactor/base-page-wait-helpers
git checkout -b chore/bump-playwright-1.59.0
git checkout -b docs/update-contributing-guide
```
**Rules:**
- Branch off from `main` only
- One concern per branch — keep branches focused and short-lived
- Delete your branch after the PR is merged

---

## ✍ Commit Convention

This project follows **[Conventional Commits](https://www.conventionalcommits.org/)**.

### Format


`type>(<scope>): <short description>`

[optional body]

[optional footer]

### Types

| Type | When to use |
|---|---|
| `feat` | New feature, new scenario, new page object |
| `fix` | Bug fix in automation code or test logic |
| `refactor` | Code restructure with no behavior change |
| `test` | Adding or updating test scenarios |
| `chore` | Build config, dependency bump, CI |
| `docs` | Documentation updates only |
| `style` | Formatting, whitespace (no logic change) |
| `perf` | Performance improvement |

### Scopes

Use the feature area or module name:

`login` · `employee` · `leave` · `config` · `hooks` · `driver` · `pages` · `flows` · `deps` · `ci`

### Examples

```bash
git commit -m "feat(login): add negative login with invalid credentials"
git commit -m "fix(hooks): handle screenshot IOException on Windows paths"
git commit -m "refactor(pages): extract LoginPage selectors to constants"
git commit -m "chore(deps): bump playwright to 1.59.0"
git commit -m "docs(readme): add allure report screenshots"
git commit -m "test(employee): add PIM module smoke scenarios"
```

### Rules

- Subject line max **72 characters**
- Use **imperative mood**: "add", "fix", "update" — not "added", "fixed"
- No period at the end of subject line
- Reference issues in footer: `Closes #42` or `Refs #15`

---

## 📝 Writing Feature Files

Feature files live in the flowwing path:
	`src/test/resources/features/`

### Naming

- One feature file per application module: `login.feature`, `employee.feature`, `leave.feature`
- Use **snake_case** for filenames

### Structure Template

```gherkin
@ModuleName
Feature: <Module> - <Brief capability description>
  As a <role>
  I want to <goal>
  So that <benefit>

  Background:
	Given the user is on the OrangeHRM login page

  @Smoke @Login
  Scenario: Successful login with valid admin credentials
	When the user enters username "Admin" and password "admin123"
	And the user clicks the Login button
	Then the user should be redirected to the Dashboard

  @Regression @Login @Negative
  Scenario Outline: Login fails with invalid credentials
	When the user enters username "<username>" and password "<password>"
	And the user clicks the Login button
	Then an error message "<message>" should be displayed

Examples:
| username | password  | message                          |
| Admin    | wrongpass | Invalid credentials              |
|          | admin123  | Required                         |
| Admin    |           | Required                         |
```

### Tagging Strategy

| Tag | Purpose |
|---|---|
| `@Smoke` | Critical path — must always pass |
| `@Regression` | Full regression suite |
| `@Negative` | Negative / error path scenarios |
| `@WIP` | Work in progress — excluded from CI |
| `@ModuleName` | e.g. `@Login`, `@Employee`, `@Leave` |

### Rules

- Steps must read like plain English — no technical jargon in Gherkin
- Use `Background` for steps shared across all scenarios in a feature
- Use `Scenario Outline` + `Examples` for data-driven cases
- Never call methods or reference code inside `.feature` files
- Tag every scenario with at least one suite tag and one module tag

---

## 🔧 Writing Step Definitions

Step definitions live in the following path:
	`src/test/java/com/company/orangehrm/steps/`

### Naming

- One step class per module: `LoginSteps.java`, `EmployeeSteps.java`
- Extend `BaseStep` to access shared context

### Template

```java
package com.company.orangehrm.steps;

import com.company.orangehrm.flows.LoginFlow;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps extends BaseStep {

	private final LoginFlow loginFlow;

	public LoginSteps() {
	this.loginFlow = new LoginFlow(page);
	}

	@When("the user enters username {string} and password {string}")
	public void theUserEntersCredentials(String username, String password) {
	loginFlow.enterCredentials(username, password);
	}

	@And("the user clicks the Login button")
	public void theUserClicksLogin() {
	loginFlow.clickLogin();
	}

	@Then("the user should be redirected to the Dashboard")
	public void theUserShouldSeeTheDashboard() {
	loginFlow.verifyDashboardVisible();
	}
}
```

### Rules

- Steps are **thin glue** — delegate all logic to flows or page objects
- No Playwright API calls (`page.click()`, `page.fill()`) inside step definitions
- Reuse existing step methods across features where possible
- Use `{string}`, `{int}`, `{word}` Cucumber expressions — not regex unless necessary

---

## 🖥 Writing Page Objects

Page objects live in `src/main/java/com/company/orangehrm/pages/`.  
All page objects must extend `BasePage`.

### Template

```java
package com.company.orangehrm.pages;

import com.company.orangehrm.core.BasePage;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

	// --- Selectors (define as constants) ---
	private static final String USERNAME_INPUT  = "input[name='username']";
	private static final String PASSWORD_INPUT  = "input[name='password']";
	private static final String LOGIN_BUTTON    = "button[type='submit']";
	private static final String ERROR_MESSAGE   = ".oxd-alert-content-text";

	public LoginPage(Page page) {
	super(page);
	}

	// --- Actions ---
	public void enterUsername(String username) {
	page.fill(USERNAME_INPUT, username);
	}

	public void enterPassword(String password) {
	page.fill(PASSWORD_INPUT, password);
	}

	public void clickLoginButton() {
	page.click(LOGIN_BUTTON);
	}

	// --- Assertions / Queries ---
	public String getErrorMessage() {
	return page.textContent(ERROR_MESSAGE);
	}

	public boolean isDashboardVisible() {
	return page.isVisible(".oxd-topbar-header");
	}
}
```
### Rules

- **All selectors are constants** — never hardcode strings inside methods
- Methods have a single responsibility
- No `Thread.sleep()` — use Playwright's built-in waiting mechanisms via `BasePage`
- No assertion logic inside page objects — assertions belong in flows or steps

---

## 🔀 Writing Flows

Flows live in the following path:
	`src/main/java/com/company/orangehrm/flows/`
Flows compose page object actions into complete business workflows.

### Template

```java
package com.company.orangehrm.flows;

import com.company.orangehrm.pages.LoginPage;
import com.microsoft.playwright.Page;
import org.assertj.core.api.Assertions;

public class LoginFlow {

	private final LoginPage loginPage;

	public LoginFlow(Page page) {
	this.loginPage = new LoginPage(page);
	}

	public void enterCredentials(String username, String password) {
	loginPage.enterUsername(username);
	loginPage.enterPassword(password);
	}

	public void clickLogin() {
	loginPage.clickLoginButton();
	}

	public void loginAs(String username, String password) {
	enterCredentials(username, password);
	clickLogin();
	}

	public void verifyDashboardVisible() {
	Assertions.assertThat(loginPage.isDashboardVisible())
	.as("Dashboard should be visible after login")
	.isTrue();
	}
}
```

### Rules

- Flows may contain **assertions** (using AssertJ)
- Flows should read like a business use-case, not a list of clicks
- Reusable login/logout flows should be accessible from `BaseStep` or hooks

---

## 🎨 Code Style Guidelines

### General

- **Java 21** language features are welcome (records, text blocks, pattern matching)
- Follow standard **Google Java Style Guide** naming conventions
- Classes: `PascalCase` · Methods & variables: `camelCase` · Constants: `UPPER_SNAKE_CASE`
- Max line length: **120 characters**
- Always add `@Override` annotation where applicable

### Imports

- No wildcard imports (`import com.example.*`)
- Group imports: standard Java → third-party → project internal
- Remove all unused imports before committing

### Logging

Use Log4j2 (SLF4J facade) — never `System.out.println()`:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger log = LoggerFactory.getLogger(YourClass.class);

log.info("Starting login flow for user: {}", username);
log.debug("Selector resolved: {}", USERNAME_INPUT);
log.error("Login failed unexpectedly", exception);
```
### Test Data

Always use `FakerFactory` for dynamic test data — **never hardcode** personal data:

```java
import com.company.orangehrm.data.FakerFactory;

String email    = FakerFactory.get().internet().emailAddress();
String fullName = FakerFactory.get().name().fullName();
```

---

## 🔍 Pull Request Process

### Before opening a PR

- [ ] All existing tests pass: `mvn clean test -Denv=uat -Dheadless=true`
- [ ] New scenarios are tagged correctly
- [ ] No unused imports or dead code
- [ ] No `Thread.sleep()` or hardcoded credentials
- [ ] `@WIP` tag removed from finished scenarios
- [ ] Allure report reviewed locally: `mvn allure:serve`

### PR Title

Follow the same Conventional Commits format:
```markdown
feat(leave): add leave balance validation scenarios
fix(hooks): prevent NullPointerException on trace save
```

### PR Description Template

## Summary
Brief description of what this PR does.

## Changes
```markdown
- Added `LeaveBalancePage` with balance assertion methods
- Added `LeaveFlow.verifyBalance()` flow method
- Added 3 Gherkin scenarios covering balance edge cases
```

## Test Evidence
- [ ] Ran full suite locally — all green
- [ ] Allure report attached (screenshot)

## Related Issues
`Closes #<issue_number>`

### Review process
1. At least **1 approval** required before merging
2. Address all review comments — resolve threads after fixing
3. Squash commits if the PR has noisy WIP commits before merge
4. Delete your branch after merge

---

## 🐛 Reporting Bugs

Open a GitHub Issue using the **Bug Report** template and include:

- **Framework version** (check `pom.xml` for dependency versions)
- **OS & Java version**: `java -version`, `mvn -v`
- **Browser**: chromium / firefox / webkit
- **Reproduction steps** — minimal scenario to reproduce
- **Expected behavior**
- **Actual behavior**
- **Logs / screenshots** — paste relevant `log4j2` output or Allure trace

---

## 💡 Suggesting Features

Open a GitHub Issue using the **Feature Request** template and include:

- **Problem statement**: what pain point does this solve?
- **Proposed solution**: describe the behavior you want
- **Alternatives considered**
- **Additional context**: links, screenshots, references

---

## 📄 License

---

*Happy testing! 🍊*
