package com.mycompany.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DriverManager {
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            log.info("Setting up ChromeDriver via WebDriverManager");
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if ("true".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                options.addArguments("--headless=new");
                log.info("Starting Chrome in headless mode");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            WebDriver d = new ChromeDriver(options);
            d.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.set(d);
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                log.info("Quitting WebDriver");
                driver.get().quit();
            } catch (Exception e) {
                log.warn("Error quitting driver: {}", e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}
