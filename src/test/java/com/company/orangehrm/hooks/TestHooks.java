package com.company.orangehrm.hooks;

import com.company.orangehrm.config.CaptureMode;
import com.company.orangehrm.config.Config;
import com.company.orangehrm.driver.PlaywrightManager;
import com.company.orangehrm.utils.NamePattern;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestHooks {
    private static final Logger log = LogManager.getLogger(TestHooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info(">> Starting scenario: {}", scenario.getName());
        PlaywrightManager.start();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            Config cfg = PlaywrightManager.config();
            boolean failed = scenario.isFailed();

            // Screenshot
            CaptureMode ssMode = cfg.screenshotCaptureMode();
            boolean captureScreenshot = ssMode == CaptureMode.ALWAYS ||
                (ssMode == CaptureMode.FAILED && failed);

            if (captureScreenshot) {
                byte[] screenshot = PlaywrightManager.page().screenshot();
                String firstName = scenario.getSourceTagNames().stream()
                    .findFirst().map(t -> t.replace("@", "")).orElse(null);
                String name = NamePattern.resolve(cfg.screenshotNamePattern(), scenario.getName(), firstName);
                Path dir = Paths.get(cfg.screenshotOutputDir());
                Files.createDirectories(dir);
                Files.write(dir.resolve(name + ".png"), screenshot);
                Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
                scenario.attach(screenshot, "image/png", name);
            }

        } catch (IOException e) {
            log.warn("Screenshot capture failed: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("tearDown error: {}", e.getMessage());
        } finally {
            PlaywrightManager.stop();
            log.info(">> Finished: {} [{}]", scenario.getName(), scenario.getStatus());
        }
    }
}
