package com.mycompany.hooks;

import com.mycompany.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;

public class Hooks {
    @Before
    public void setUp() {
        DriverManager.getDriver();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        takeScreenshot(scenario, false);
    }

    @After
    public void tearDown(Scenario scenario) {
        takeScreenshot(scenario, true);
        DriverManager.quitDriver();
    }

    private void takeScreenshot(Scenario scenario, boolean onFinish) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) return;
            byte[] src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(src, "image/png", (onFinish ? "final_" : "step_") + scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_"));
            Path targetDir = Path.of("target/screenshots");
            Files.createDirectories(targetDir);
            Path out = targetDir.resolve(scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + (onFinish ? "_final.png" : "_" + System.currentTimeMillis() + ".png"));
            Files.write(out, src);
        } catch (Exception e) {
            System.out.println("Could not take screenshot: " + e.getMessage());
        }
    }
}
