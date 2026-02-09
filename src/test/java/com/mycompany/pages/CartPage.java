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

    public List<String> getItemNames() {
        List<String> names = driver.findElements(cartItems).stream()
                .map(e -> e.findElement(itemName).getText())
                .collect(Collectors.toList());
        log.info("Cart item names: {}", names);
        return names;
    }

    public List<String> getQuantities() {
        List<String> qtys = driver.findElements(cartItems).stream()
                .map(e -> e.findElement(quantity).getText())
                .collect(Collectors.toList());
        log.info("Cart quantities: {}", qtys);
        return qtys;
    }

    public List<String> getPrices() {
        List<String> prices = driver.findElements(cartItems).stream()
                .map(e -> e.findElement(itemPrice).getText())
                .collect(Collectors.toList());
        log.info("Cart prices: {}", prices);
        return prices;
    }
}
