# OrangeHRM Automation Enterprise (Java 21)

## Stack
- Java 21, Maven, Playwright, Cucumber 7, JUnit 5, AssertJ, DataFaker, Log4j2, Allure

## Run
```bash
mvn clean test -Denv=uat
mvn allure:report

Allure report: `target/site/allure-maven-plugin/index.html`

## Import in Eclipse
1. Extract ZIP
2. File → Import → Maven → Existing Maven Projects
3. Right-click project → Maven → Update Project

## Config
- `src/test/resources/config/application-uat.properties`

## Notes
- Headless mode: set `headless=true/false` in properties
- Screenshot on failure: `capture.mode=ON_FAILURE`


فایل ZIP آماده است — شامل تمام ۱۹ فایل پروژه.

برای Import در Eclipse:
1. Extract کن
2. **File → Import → Maven → Existing Maven Projects**
3. پوشه `orangehrm` را انتخاب کن
4. راست‌کلیک روی پروژه → **Maven → Update Project**