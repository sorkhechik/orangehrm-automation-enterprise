# 👋 First-Time Contributor Onboarding

> A quick-start guide for new team members joining the OrangeHRM Automation Enterprise project.

---

## Step 1 — Receive & Accept Repository Access

The repository owner must grant you access first:

> GitHub → Repository → **Settings** → **Collaborators** → **Add people**

You will receive an **email invitation** — accept it before proceeding.

---

## Step 2 — Verify Prerequisites
```bash
java -version
mvn -version
git --version
```
---

## Step 3 — Clone the Repository

```bash
git clone https://github.com/sorkhechik/orangehrm-automation-enterprise.git
cd orangehrm-automation-enterprise
```
---

## Step 4 — Install Dependencies & Browsers

```bash
mvn clean install
```
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="install --with-deps"
```
---

## Step 5 — Run a Smoke Test to Confirm Setup

bash
mvn clean test -Denv=uat -Dheadless=true -Dcucumber.filter.tags="@Smoke"

✅ If the suite passes, your environment is ready.

---

## Step 6 — Start Your First Task

```bash
git pull origin main
git checkout -b feat/your-task-name
git add .
git commit -m "feat(<scope>): describe your change"
git push -u origin feat/your-task-name
```

Then open a **Pull Request** on GitHub and request a review.

---

## Quick Reference

| Topic | Where to look |
|---|---|
| Git workflow & branching | `CONTRIBUTING.md` → Development Workflow |
| Commit message format | `CONTRIBUTING.md` → Commit Convention |
| Writing scenarios | `CONTRIBUTING.md` → Writing Feature Files |
| PR checklist | `CONTRIBUTING.md` → Pull Request Process |

---

*Questions? Reach out to the project maintainer or open a GitHub Discussion.* 🍊
`

---

**موارد اصلاح‌شده:**

- تمام بلاک‌هایی که فقط `bash` بدون بک‌تیک داشتند → به ` 
```bash ` تبدیل شدند
- تمام بلاک‌هایی که فاقد خط پایانی `
``` ` بودند → بسته شدند
- بلاک‌های پشت سر هم که منطقاً جدا بودند → از هم تفکیک شدند تا خوانایی بهتر باشد