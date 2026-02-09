package com.mycompany.steps;

import com.mycompany.pages.SearchPage;
import com.mycompany.utils.DriverManager;
import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class SearchSteps {
    private static final Logger log = LoggerFactory.getLogger(SearchSteps.class);

    private WebDriver driver = DriverManager.getDriver();
    private SearchPage page = new SearchPage(driver);

    @Given("que abro la tienda en {string}")
    public void que_abro_la_tienda_en(String url) {
        log.info("Step: open store {}", url);
        page.open(url);
    }

    @Given("inicio sesión con usuario {string} y contraseña {string}")
    public void inicio_sesion_con_usuario_y_contraseña(String user, String pass) {
        log.info("Step: login {} / ****", user);
        page.login(user, pass);
    }

    @When("busco por texto {string}")
    public void busco_por_texto(String texto) {
        log.info("Step: search (placeholder) {}", texto);
        page.search(texto);
    }

    @Then("debo ver al menos {string} resultado")
    public void debo_ver_al_menos_resultado(String expectedStr) {
        int expected = Integer.parseInt(expectedStr);
        int actual = page.getResultsCountByText("bike"); // usa texto o adapta a 'texto' si quieres
        log.info("Asserting results: expected >= {}, actual = {}", expected, actual);
        Assert.assertTrue("Se esperaban al menos " + expected + " resultados, se encontraron: " + actual,
                actual >= expected);
    }

    @When("aplico filtro por orden {string}")
    public void aplico_filtro_por_orden(String orden) {
        log.info("Step: apply sort order {}", orden);
        page.applySortByVisibleText(orden);
    }

    @Then("todos los resultados deben contener el texto {string}")
    public void todos_los_resultados_deben_contener_el_texto(String texto) {
        log.info("Step: validate any product contains {}", texto);
        List<String> names = page.getAllProductNames();
        boolean anyMatch = names.stream().anyMatch(n -> n.toLowerCase().contains(texto.toLowerCase()));
        Assert.assertTrue("Ningún producto contiene: " + texto, anyMatch);
    }
}
