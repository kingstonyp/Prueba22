package com.mycompany.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
public class CartPage {
    private static final Logger log = LoggerFactory.getLogger(CartPage.class);
    private WebDriver driver;

    private By cartLink = By.cssSelector(".shopping_cart_link");
    private By cartItems = By.cssSelector(".cart_item");
    private By itemName = By.cssSelector(".inventory_item_name");
    private By itemPrice = By.cssSelector(".inventory_item_price");
    private By quantity = By.cssSelector(".cart_quantity");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void goToCart() {
        log.info("Navigating to cart");
        driver.findElement(cartLink).click();
    }

    // raíz del contenedor del carrito para esperar visibilidad desde los steps
    public WebElement getCartRootElement() {
        try {
            return driver.findElement(By.cssSelector(".cart_list, .cart_contents, .cart_container"));
        } catch (Exception e) {
            // fallback: body para que el wait no lance NPE si no encuentra selector
            return driver.findElement(By.tagName("body"));
        }
    }

    public List<String> getItemNames() {
        List<WebElement> items = driver.findElements(cartItems);
        List<String> names = items.stream()
                .map(e -> {
                    try {
                        return e.findElement(itemName).getText();
                    } catch (Exception ex) {
                        return "";
                    }
                })
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        log.info("Cart item names: {}", names);
        return names;
    }

    public List<String> getQuantities() {
        List<WebElement> items = driver.findElements(cartItems);
        List<String> qtys = items.stream()
                .map(e -> {
                    try {
                        return e.findElement(quantity).getText();
                    } catch (Exception ex) {
                        return "";
                    }
                })
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        log.info("Cart quantities: {}", qtys);
        return qtys;
    }

    public List<String> getPrices() {
        List<WebElement> items = driver.findElements(cartItems);
        List<String> prices = items.stream()
                .map(e -> {
                    try {
                        return e.findElement(itemPrice).getText();
                    } catch (Exception ex) {
                        return "";
                    }
                })
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        log.info("Cart prices: {}", prices);
        return prices;
    }
}