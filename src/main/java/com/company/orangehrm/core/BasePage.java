package com.company.orangehrm.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.orangehrm.driver.PlaywrightManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public abstract class BasePage {

	  private static final Logger log = LogManager.getLogger(BasePage.class);
	  
    /* ---------- Core ---------- */

    protected Page page() {
        return PlaywrightManager.page();
    }

    protected Locator $(String selector) {
        return page().locator(selector);
    }

    @SuppressWarnings("unchecked")
    protected <T extends BasePage> T self() {
        return (T) this;
    }

    /* ---------- Navigation ---------- */

    public <T extends BasePage> T open() {
        page().navigate(
                PlaywrightManager.config().baseUrl()
        );
        return self();
    }

    /* ---------- Navigation Waits ---------- */

    /**
     * منتظر می‌ماند تا URL صفحه با الگوی داده شده و در زمان مشخص مطابقت داشته باشد.
     * @param urlPattern الگوی مورد نظر، مثلاً: {@code "** /dashboard/**"}
     * @param timeoutMs زمان انتظار به میلی‌ثانیه
     */
    protected void waitForPage(String urlPattern) {
        // Uses the default navigation timeout from PlaywrightManager
        log.debug("Waited for URL pattern: {}", urlPattern);
        page().waitForURL(
                urlPattern,
                new Page.WaitForURLOptions()
                        .setTimeout(PlaywrightManager.config().pageLoadTimeoutMs())
        );
    }

    /**
     * Waits for the page URL to match the given pattern with a specific timeout.
     * @param urlPattern The pattern to match against the URL.
     * @param timeoutMs The custom timeout in milliseconds.
     */
    protected void waitForPage(String urlPattern, int timeoutMs) {
        log.debug("Waited for URL pattern: {} with timeout {}ms", urlPattern, timeoutMs);
        page().waitForURL(
                urlPattern,
                new Page.WaitForURLOptions().setTimeout(timeoutMs)
        );
    }

    /* ---------- Actions ---------- */

    protected void fillField(String selector, String text) {
        Locator locator = $(selector);
        waitForVisible(locator);
        locator.fill(text);
    }

    protected void clickElement(String selector) {
        Locator locator = $(selector);
        waitForVisible(locator);
        locator.click();
    }

    /* ---------- Assertions / Queries ---------- */

    protected boolean isElementVisible(String selector) {
        return $(selector).isVisible();
    }

    protected String getText(String selector) {
        return $(selector).textContent();
    }

    /* ---------- Wait helpers ---------- */

    protected void waitForVisible(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );
    }

    protected void waitForHidden(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
        );
    }
}
