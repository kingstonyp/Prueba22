package com.mycompany.steps;
import com.mycompany.pages.CartPage;
import com.mycompany.pages.ProductPage;
import com.mycompany.utils.DriverManager;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
public class CartSteps {
    private WebDriver driver = DriverManager.getDriver();
    private ProductPage productPage = new ProductPage(driver);
    private CartPage cartPage = new CartPage(driver);
    private String selectedProductName;

    @When("selecciono el producto {string}")
    public void selecciono_el_producto(String name) {
        selectedProductName = name;
    }

    @When("lo agrego al carrito")
    public void lo_agrego_al_carrito() {
        productPage.addToCartByName(selectedProductName);
    }

    @When("voy al carrito")
    public void voy_al_carrito() {
        cartPage.goToCart();
    }

    @Then("el carrito debe contener el producto {string} con cantidad {string}")
    public void el_carrito_debe_contener_el_producto_con_cantidad(String expectedName, String expectedQty) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // esperar que al menos un item del carrito sea visible
        try {
            wait.until(ExpectedConditions.visibilityOf(cartPage.getCartRootElement()));
        } catch (Exception ignored) {
        }

        // obtener y normalizar nombres y cantidades
        List<String> names = cartPage.getItemNames().stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());
        List<String> qtys = cartPage.getQuantities().stream()
                .map(String::trim)
                .collect(Collectors.toList());

        String expectedNorm = expectedName.trim().toLowerCase();

        // guardar page source para debug si falla
        try {
            Files.writeString(Paths.get("target/debug-page-source.html"), driver.getPageSource(), StandardOpenOption.CREATE);
        } catch (Exception ignored) {
        }

        Assert.assertTrue("El carrito no contiene el producto esperado: " + expectedName + ". Encontrados: " + names,
                names.contains(expectedNorm));
        Assert.assertTrue("No se encontró cantidad " + expectedQty + ". Encontradas: " + qtys,
                qtys.contains(expectedQty.trim()));
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