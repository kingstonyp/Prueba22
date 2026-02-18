package com.mycompany.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;
public class ProductPage {
    private static final Logger log = LoggerFactory.getLogger(ProductPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By productNameLocator(String name) {
        return By.cssSelector(".inventory_item .inventory_item_name");
    }

    public void selectProduct(String name) {
        log.info("Selecting product (click name) {}", name);
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        for (WebElement item : items) {
            try {
                WebElement nameEl = item.findElement(By.cssSelector(".inventory_item_name"));
                if (nameEl.getText().trim().equalsIgnoreCase(name.trim())) {
                    nameEl.click();
                    return;
                }
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("Product not found to select: " + name);
    }

    public void addToCartByName(String name) {
        log.info("Adding to cart product: {}", name);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".inventory_item")));
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        WebElement target = null;
        for (WebElement item : items) {
            try {
                WebElement nameEl = item.findElement(By.cssSelector(".inventory_item_name"));
                if (nameEl.getText().trim().equalsIgnoreCase(name.trim())) {
                    target = item;
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        if (target == null) {
            log.error("Producto no encontrado en la lista: {}", name);
            throw new RuntimeException("Producto no encontrado en la lista: " + name);
        }

        WebElement btn;
        try {
            btn = target.findElement(By.xpath(".//button[contains(@id,'add-to-cart') or contains(@class,'btn_inventory') or contains(.,'Add to cart')]"));
        } catch (Exception e) {
            btn = target.findElement(By.xpath(".//button"));
        }

        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            wait.until(ExpectedConditions.elementToBeClickable(btn));
            // try normal click first, fallback to JS click
            try {
                btn.click();
            } catch (Exception clickEx) {
                log.warn("Normal click failed, using JS click for {}: {}", name, clickEx.getMessage());
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
            log.info("Clicked add-to-cart button for {}", name);

            // primary confirmation: wait the same button text to change to "Remove"
            try {
                WebDriverWait waitBtn = new WebDriverWait(driver, Duration.ofSeconds(10));
                waitBtn.until(ExpectedConditions.textToBePresentInElement(btn, "Remove"));
                log.info("Add-to-cart confirmed by button text for {}", name);
            } catch (Exception ex) {
                log.warn("Button did not change to 'Remove' after addToCart: {}. Will try badge check then fallback sleep.", ex.getMessage());
                // secondary confirmation: cart badge update
                try {
                    WebDriverWait waitBadge = new WebDriverWait(driver, Duration.ofSeconds(5));
                    waitBadge.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".shopping_cart_badge"), "1"));
                    log.info("Cart badge updated");
                } catch (Exception ex2) {
                    log.warn("Cart badge did not update after addToCart: {}. Falling back to small sleep.", ex2.getMessage());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
            }

        } catch (Exception e) {
            log.warn("Direct click flow failed, using JS click fallback for {}: {}", name, e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            // try confirmations again after JS click
            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.textToBePresentInElement(btn, "Remove"));
                log.info("Cart add confirmed after JS click by button text for {}", name);
            } catch (Exception ex) {
                log.warn("Confirmations failed after JS click: {}. Falling back to small sleep.", ex.getMessage());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public String getProductNameFromList(String name) {
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        for (WebElement item : items) {
            try {
                WebElement nameEl = item.findElement(By.cssSelector(".inventory_item_name"));
                if (nameEl.getText().trim().equalsIgnoreCase(name.trim())) {
                    String found = nameEl.getText();
                    log.info("Found product in list: {}", found);
                    return found;
                }
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("Producto no encontrado: " + name);
    }
}