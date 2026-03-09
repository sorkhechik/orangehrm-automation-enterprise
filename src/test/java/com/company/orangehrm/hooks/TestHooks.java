package com.company.orangehrm.hooks;

import com.company.orangehrm.core.trace.TraceManager;
import com.company.orangehrm.driver.PlaywrightManager;
import com.company.orangehrm.config.CaptureMode;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class TestHooks {

	private static final Logger log = LoggerFactory.getLogger(TestHooks.class);
	private TraceManager traceManager;

	@Before
	public void setUp(Scenario scenario) {
		log.info("▶ Starting scenario: {}", scenario.getName());
		PlaywrightManager.start();

		CaptureMode captureMode = PlaywrightManager.config().screenshotCaptureMode();
		traceManager = TraceManager.from(scenario.getName(), captureMode);
		traceManager.start();
	}

	@After
	public void tearDown(Scenario scenario) {
		boolean failed = scenario.isFailed();
		log.info("{} Scenario: {}", failed ? "❌ FAILED" : "✅ PASSED", scenario.getName());

		// ── 1) Screenshot (همیشه برای Cucumber کار می‌کند) ──────────────────────
		try {
			if (PlaywrightManager.page() != null) {
				byte[] screenshot = PlaywrightManager.page().screenshot();
				String name = (failed ? "FAILED" : "PASSED") + " — " + scenario.getName();

				// Cucumber attachment — همیشه کار می‌کند
				scenario.attach(screenshot, "image/png", name);

				// Allure attachment — ممکن است context نداشته باشد
				try {
					Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
				} catch (Exception allureEx) {
					log.warn("Allure attachment skipped (no active test context): {}", allureEx.getMessage());
				}
			}
		} catch (Exception screenshotEx) {
			log.error("Screenshot failed: {}", screenshotEx.getMessage());
		}

		// ── 2) Trace ─────────────────────────────────────────────────────────────
		try {
			traceManager.stop(failed);
		} catch (Exception traceEx) {
			log.error("Trace stop failed: {}", traceEx.getMessage());
		}

		// ── 3) Browser cleanup ───────────────────────────────────────────────────
		try {
			PlaywrightManager.stop();
		} catch (Exception cleanupEx) {
			log.error("Browser cleanup failed: {}", cleanupEx.getMessage());
		}
	}
}
