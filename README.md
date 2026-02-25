# 📘 OrangeHRM Test Automation Framework

Enterprise-grade test automation framework built with  
**Java + Maven + Playwright + Cucumber + JUnit 5 + Allure**

---

## 📑 Table of Contents
1. Tech Stack
2. Project Structure
3. Prerequisites
4. How to Run Tests
5. Configuration
6. Import Project in Eclipse
7. Git & Version Control
8. Standard Git Workflow
9. Repository Setup
10. Reports & Artifacts
11. Notes
12. License

---

## ✅ Tech Stack
- Java
- Maven
- Playwright (Java)
- Cucumber (BDD)
- JUnit 5
- Allure Report
- Log4j2
- Datafaker

---

## ✅ Project Structure (High-Level)

```text
src/
└── test/
    ├── java/
    │   ├── core/        # Driver, TestContext, Base classes
    │   ├── pages/       # Page Objects
    │   ├── steps/       # Step Definitions
    │   └── hooks/       # Hooks (Before/After)
    └── resources/
        ├── features/    # Gherkin feature files
        └── config/      # Environment configs
```

---

## ✅ Prerequisites

| Software      | Version               | Purpose |
|--------------|----------------------|----------|
| Java JDK     | 11+                  | Runtime & compilation |
| Apache Maven | 3.8+                 | Build & dependency management |
| Git          | Latest               | Version control |
| Eclipse IDE  | Latest (Recommended) | IDE |

Verify installation:

```bash
java -version
mvn -v
git --version
```

---

## ✅ How to Run Tests

Run all tests (UAT):

```bash
mvn clean test -Denv=uat
```

Run headless:

```bash
mvn clean test -Denv=dev -Dheadless=true
```

Run specific tag:

```bash
mvn clean test -Denv=uat -Dcucumber.filter.tags="@Smoke"
```

---

## 📊 Generate Allure Report

Serve locally:

```bash
mvn allure:serve
```

Generate static report:

```bash
mvn allure:report
```

Report location:

```
target/site/allure-maven-plugin/index.html
```

---

## ✅ Configuration

Config files location:

```
src/test/resources/config/application-<env>.properties
```

Example environments:

- application-dev.properties
- application-uat.properties
- application-prod.properties

Sample config:

```properties
base.url=https://uat.orangehrmlive.com/

browser=chromium
headless=false

timeout.implicit.wait=10000
timeout.page.load=30000

capture.mode=ON_FAILURE
capture.trace=ON_FAILURE
```

---

## ✅ Import Project in Eclipse

1. Extract ZIP
2. File → Import → Maven → Existing Maven Projects
3. Select project root (contains pom.xml)
4. Right-click → Maven → Update Project

---

## ✅ Git & Version Control

Follows:

- Conventional Commits
- Semantic Versioning (SemVer)

Commit format:

```
<type>(<scope>): <short description>
```

Example:

```bash
git commit -m "feat(login): add negative login scenarios"
git commit -m "fix(wait): stabilize element visibility"
git commit -m "chore: clean .gitignore"
```

---

## ✅ Standard Git Workflow

```bash
git status
git add .
git commit -m "Meaningful message"
git push
```

---

## ✅ Repository Setup (First Time Only)

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

git init
git add .
git commit -m "Initial project setup"

git branch -M main
git remote add origin https://github.com/USERNAME/REPO_NAME.git
git push -u origin main
```

---

## ✅ Reports & Artifacts

- Allure reports generated locally
- Screenshots attached on failure
- Artifacts excluded via .gitignore
- CI/CD Ready
- Parallel execution supported
- Multi-browser supported

---

## ✅ Notes

- Enterprise-ready structure
- Clean architecture
- CLI-driven execution
- No dedicated runner class required

---

## 📄 License

Specify your license here (MIT, Apache 2.0, etc.)
