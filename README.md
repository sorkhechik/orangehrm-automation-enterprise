# 🍊 OrangeHRM Automation Enterprise

> Enterprise-grade BDD test automation framework for [OrangeHRM](https://opensource.orangehrmlive.com/)
> built with **Java · Maven · Playwright · Cucumber · JUnit 5 · Allure**

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.58.0-2EAD33?logo=playwright)](https://playwright.dev/java/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.21.1-23D96C?logo=cucumber)](https://cucumber.io/)
[![JUnit5](https://img.shields.io/badge/JUnit-5.11.4-25A162?logo=junit5)](https://junit.org/junit5/)
[![Allure](https://img.shields.io/badge/Allure-2.29.0-success)](https://allurereport.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📑 Table of Contents

1. [Overview](#-overview)
2. [Tech Stack](#-tech-stack)
3. [Project Structure](#-project-structure)
4. [Architecture](#-architecture)
5. [Prerequisites](#-prerequisites)
6. [Installation](#-installation)
7. [Configuration](#-configuration)
8. [Running Tests](#-running-tests)
9. [Allure Reports](#-allure-reports)
10. [Import in Eclipse](#-import-in-eclipse)
11. [Git Workflow](#-git-workflow)
12. [Contributing](#-contributing)
13. [License](#-license)

---

## 🔍 Overview

This framework automates end-to-end testing of the OrangeHRM web application using a **BDD (Behavior-Driven Development)** approach with Gherkin feature files. It follows enterprise-grade clean architecture principles with clear separation of concerns across layers.

**Key capabilities:**
- Multi-environment execution (`dev`, `uat`, `prod`)
- Multi-browser support (`chromium`, `firefox`, `webkit`)
- Headless / headed execution via CLI flags
- Automatic screenshot capture on failure (or always)
- Playwright trace recording
- Allure reporting with scenario attachments
- Fake test data generation via Datafaker

---

## ✅ Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Build Tool | Apache Maven | 3.8+ |
| Browser Automation | Playwright (Java) | 1.58.0 |
| BDD Framework | Cucumber | 7.21.1 |
| Test Engine | JUnit 5 (Jupiter) | 5.11.4 |
| Assertions | AssertJ | 3.27.3 |
| Reporting | Allure | 2.29.0 |
| Logging | Log4j2 + SLF4J | 2.24.3 / 2.0.16 |
| Test Data | Datafaker | 2.4.2 |
| Serialization | Jackson Databind | 2.16.1 |

---

## 📁 Project Structure
```text
orangehrm-automation-enterprise/
├── pom.xml                                          # Maven build & dependency config
│
└── src/
├── main/java/com/company/orangehrm/
│   ├── config/
│   │   ├── BrowserConfig.java                   # Browser launch options
│   │   ├── CaptureMode.java                     # Enum: ALWAYS / ON_FAILURE / NEVER
│   │   ├── Config.java                          # Config POJO (url, browser, timeouts…)
│   │   ├── ConfigLoader.java                    # Loads .properties per active env
│   │   └── Env.java                             # Enum: DEV / UAT / PROD
│   │
│   ├── core/
│   │   ├── BasePage.java                        # Base class for all Page Objects
│   │   └── trace/
│   │       └── TraceManager.java                # Playwright trace start/stop/save
│   │
│   ├── data/
│   │   └── FakerFactory.java                    # Datafaker wrapper for test data
│   │
│   ├── driver/
│   │   └── PlaywrightManager.java               # Browser/Page lifecycle manager
│   │
│   ├── flows/
│   │   └── LoginFlow.java                       # Reusable high-level login flow
│   │
│   ├── pages/
│   │   └── LoginPage.java                       # Login page object
│   │
│   └── utils/
│       ├── DemoMode.java                        # Slow-motion demo helper
│       └── NamePattern.java                     # Screenshot filename generator
│
└── test/
├── java/com/company/orangehrm/
│   ├── hooks/
│   │   └── TestHooks.java                   # @Before / @After Cucumber hooks
│   ├── runners/
│   │   └── TestRunner.java                  # JUnit Platform Suite entry point
│   └── steps/
│       ├── BaseStep.java                    # Shared step utilities
│       └── LoginSteps.java                  # Login step definitions
│
└── resources/
├── config/
│   └── application-uat.properties       # UAT environment config
├── features/
│   └── login.feature                    # Login BDD scenarios
├── junit-platform.properties            # Cucumber engine properties
└── log4j2.xml                           # Logging configuration
```
---

## 🏛 Architecture

```
┌─────────────────────────────────────────────────────┐
│               Feature Files  (.feature)             │  ← BDD layer (Gherkin)
└────────────────────────┬────────────────────────────┘
│
┌────────────────────────▼────────────────────────────┐
│              Step Definitions  (steps/)             │  ← Glue layer
└────────────────────────┬────────────────────────────┘
│
┌────────────────────────▼────────────────────────────┐
│              Flows  (flows/)                        │  ← Business workflows
└────────────────────────┬────────────────────────────┘
│
┌────────────────────────▼────────────────────────────┐
│              Page Objects  (pages/)                 │  ← UI interaction layer
└────────────────────────┬────────────────────────────┘
│
┌────────────────────────▼────────────────────────────┐
│     PlaywrightManager + BasePage  (driver/core/)    │  ← Browser abstraction
└─────────────────────────────────────────────────────┘
```
**Hooks flow per scenario:**

```
@Before  →  PlaywrightManager.start()  →  [Scenario runs]
→  @After  →  Screenshot (if configured)
→  Allure attachment
→  PlaywrightManager.stop()
```

---

## ✅ Prerequisites

| Software | Version | Check |
|---|---|---|
| Java JDK | 21+ | `java -version` |
| Apache Maven | 3.8+ | `mvn -v` |
| Git | Latest | `git --version` |
| Eclipse IDE | Latest | _(optional)_ |

> **Note:** Playwright browser binaries are downloaded automatically on first run via Maven.

---

## 💾 Installation

bash
# 1. Clone the repository
git clone https://github.com/sorkhechik/orangehrm-automation-enterprise.git
cd orangehrm-automation-enterprise

# 2. Install Playwright browsers (first time only)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="install --with-deps"

# 3. Verify build
mvn clean compile

---

## ⚙ Configuration

Config files live at:


src/test/resources/config/application-<env>.properties

| File | Environment |
|---|---|
| `application-dev.properties` | Local development |
| `application-uat.properties` | UAT (default) |
| `application-prod.properties` | Production |

**Full config reference (`application-uat.properties`):**

properties
# Application
base.url=https://opensource.orangehrmlive.com/

# Browser
browser=chromium
headless=false

# Timeouts (ms)
timeout.implicit.wait=10000
timeout.page.load=30000

# Capture
capture.mode=ON_FAILURE
capture.trace=ON_FAILURE

**`capture.mode` values:**

| Value | Behaviour |
|---|---|
| `ALWAYS` | Screenshot after every scenario |
| `ON_FAILURE` | Screenshot only on failed scenarios |
| `NEVER` | No screenshots |

---

## ▶ Running Tests

### Run all tests (default: UAT)

bash
mvn clean test

### Run against a specific environment

bash
mvn clean test -Denv=uat
mvn clean test -Denv=dev
mvn clean test -Denv=prod

### Run headless

bash
mvn clean test -Denv=uat -Dheadless=true

### Run by Cucumber tag

bash
# Single tag
mvn clean test -Dcucumber.filter.tags="@Smoke"

# Multiple tags (AND)
mvn clean test -Dcucumber.filter.tags="@Smoke and @Login"

# Multiple tags (OR)
mvn clean test -Dcucumber.filter.tags="@Smoke or @Regression"

# Exclude tag
mvn clean test -Dcucumber.filter.tags="not @WIP"

### Run via TestRunner (IDE)

Right-click `TestRunner.java` → **Run As → JUnit Test**

---

## 📊 Allure Reports

### Serve report locally (live server)

bash
mvn allure:serve

### Generate static report

bash
mvn allure:report

### Static report location


target/site/allure-maven-plugin/index.html

**What's included in the report:**
- Scenario pass/fail status
- Step-by-step breakdown
- Screenshots attached on failure
- Playwright traces (when enabled)
- Test execution timeline

---

## 🖥 Import in Eclipse

1. Extract the ZIP (if not cloned)
2. **File → Import → Maven → Existing Maven Projects**
3. Browse to project root (directory containing `pom.xml`)
4. Click **Finish**
5. Right-click project → **Maven → Update Project** (`Alt+F5`)

---

## 🔀 Git Workflow

This project follows **Conventional Commits** and **Semantic Versioning (SemVer)**.

### Commit format


<type>(<scope>): <short description>

| Type | When to use |
|---|---|
| `feat` | New feature or scenario |
| `fix` | Bug fix |
| `refactor` | Code restructure (no behaviour change) |
| `test` | Adding/updating tests |
| `chore` | Build, deps, config changes |
| `docs` | Documentation updates |

### Examples

bash
git commit -m "feat(login): add negative login scenarios"
git commit -m "fix(hooks): handle screenshot IOException"
git commit -m "refactor(pages): extract LoginPage selectors to constants"
git commit -m "chore(deps): bump playwright to 1.58.0"

### Daily workflow

bash
git status
git add .
git commit -m "feat(scope): meaningful message"
git push

### First-time repository setup

bash
git config --global user.name  "Your Name"
git config --global user.email "your.email@example.com"

git init
git add .
git commit -m "chore: initial project setup"
git branch -M main
git remote add origin https://github.com/USERNAME/REPO_NAME.git
git push -u origin main

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit your changes using Conventional Commits
4. Push to your fork: `git push origin feat/your-feature`
5. Open a Pull Request

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for full guidelines.

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.


---
