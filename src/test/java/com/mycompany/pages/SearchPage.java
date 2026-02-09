package com.mycompany.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPage {
    private static final Logger log = LoggerFactory.getLogger(SearchPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    // Login locators (Sauce Demo)
    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login-button");

    // Products (Sauce Demo)
    private By productContainers = By.cssSelector(".inventory_item");
    private By productName = By.cssSelector(".inventory_item_name");

    // Sort select (Sauce Demo)
    private By sortSelect = By.cssSelector(".product_sort_container");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String url) {
        log.info("Opening URL: {}", url);
        driver.get(url);
    }

    // login and wait for products to load
    public void login(String user, String pass) {
        log.info("Logging in as {}", user);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).clear();
        driver.findElement(usernameInput).sendKeys(user);
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(pass);
        driver.findElement(loginButton).click();
        // wait for inventory list to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_list, .inventory_container, .inventory_item")));
    }

    // For SauceDemo we don't have a textual search input; this is a placeholder if needed
    public void search(String text) {
        // Could implement filtering in code using getAllProductNames()
        log.info("Search placeholder called with '{}'", text);
    }

    public int getResultsCountByText(String text) {
        return (int) getAllProductNames().stream().filter(n -> n.toLowerCase().contains(text.toLowerCase())).count();
    }

    public List<String> getAllProductNames() {
        List<WebElement> elems = driver.findElements(productName);
        List<String> names = elems.stream().map(WebElement::getText).collect(Collectors.toList());
        log.info("Found product names: {}", names);
        return names;
    }

    public void applySortByVisibleText(String visibleText) {
        log.info("Applying sort: {}", visibleText);
        try {
            Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(sortSelect)));
            select.selectByVisibleText(visibleText);
            // wait briefly for sorting to reflect
            Thread.sleep(800);
        } catch (Exception e) {
            log.warn("Could not apply sort '{}': {}", visibleText, e.getMessage());
        }
    }
}
