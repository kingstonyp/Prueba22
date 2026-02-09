package com.mycompany.steps;

import com.mycompany.pages.CartPage;
import com.mycompany.pages.ProductPage;
import com.mycompany.utils.DriverManager;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartSteps {
    private WebDriver driver = DriverManager.getDriver();
    private ProductPage productPage = new ProductPage(driver);
    private CartPage cartPage = new CartPage(driver);
    private String selectedProductName;

    @When("selecciono el producto {string}")
    public void selecciono_el_producto(String name) {
        // guardamos el nombre seleccionado, no hacemos click
        selectedProductName = name;
    }

    @When("lo agrego al carrito")
    public void lo_agrego_al_carrito() {
        // añadimos directamente por nombre (lista o detalle)
        productPage.addToCartByName(selectedProductName);
    }

    @When("voy al carrito")
    public void voy_al_carrito() {
        cartPage.goToCart();
    }

    @Then("el carrito debe contener el producto {string} con cantidad {string}")
    public void el_carrito_debe_contener_el_producto_con_cantidad(String expectedName, String expectedQty) {
        List<String> names = cartPage.getItemNames();
        List<String> qtys = cartPage.getQuantities();
        Assert.assertTrue("El carrito no contiene el producto esperado: " + expectedName, names.contains(expectedName));
        Assert.assertTrue("No se encontró cantidad " + expectedQty, qtys.contains(expectedQty));
    }

    @Then("el precio del producto debe ser visible y mayor que {string}")
    public void el_precio_del_producto_debe_ser_visible_y_mayor_que(String minStr) {
        List<String> prices = cartPage.getPrices();
        Assert.assertFalse("No se encontraron precios en el carrito", prices.isEmpty());
        for (String p : prices) {
            String clean = p.replaceAll("[$€£\\s]", "").replace(",", ".");
            double val = Double.parseDouble(clean);
            Assert.assertTrue("Precio esperado mayor que " + minStr + " pero fue " + val, val > Double.parseDouble(minStr));
        }
    }
}
