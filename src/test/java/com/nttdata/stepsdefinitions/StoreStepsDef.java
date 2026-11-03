package com.nttdata.stepsdefinitions;

import com.nttdata.core.DriverManager;
import com.nttdata.page.CartPage;
import com.nttdata.page.HomePage;
import com.nttdata.page.LoginPage;
import com.nttdata.page.ProductPage;
import com.nttdata.steps.LoginSteps;
import com.nttdata.steps.StoreSteps;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;

public class StoreStepsDef {
    private LoginSteps loginSteps;
    private StoreSteps storeSteps;

    @Dado("estoy en la página de la tienda")
    public void abrirTienda() {
        WebDriver driver = DriverManager.getDriver();
        loginSteps = new LoginSteps(new LoginPage(driver));
        storeSteps = new StoreSteps(new HomePage(driver), new ProductPage(driver), new CartPage(driver));
        String baseUrl = System.getProperty("store.baseUrl",
                System.getenv().getOrDefault("STORE_BASE_URL", "https://qalab.bensg.com/store"));
        storeSteps.openStore(baseUrl);
    }

    @Y("me logueo con mi usuario {string} y clave {string}")
    public void loguearse(String username, String password) {
        loginSteps.login(username, password);
    }

    @Entonces("valido el resultado de autenticación {string}")
    public void validarAutenticacion(String expectedResult) {
        loginSteps.validateAuthentication(expectedResult);
    }

    @Cuando("navego a la categoria {string} y subcategoria {string}")
    public void navegarCategoria(String category, String subcategory) {
        storeSteps.navigateToCategory(category, subcategory);
    }

    @Entonces("valido que la categoria no existe y que la navegación se detuvo")
    public void validarCategoriaInexistente() {
        storeSteps.validateCategoryNotFound();
    }

    @Y("agrego {int} unidades del primer producto al carrito")
    public void agregarProducto(int quantity) {
        storeSteps.addFirstProductToCart(quantity);
    }

    @Entonces("valido en el popup la confirmación del producto agregado")
    public void validarConfirmacionPopup() {
        storeSteps.validateProductAdded();
    }

    @Y("valido en el popup que el monto total sea calculado correctamente")
    public void validarSubtotalPopup() {
        storeSteps.validatePopupSubtotal();
    }

    @Cuando("finalizo la compra")
    public void abrirCarrito() {
        storeSteps.openCart();
    }

    @Entonces("valido el titulo de la pagina del carrito")
    public void validarTituloCarrito() {
        storeSteps.validateCartPage();
    }

    @Y("vuelvo a validar el calculo de precios en el carrito")
    public void validarSubtotalCarrito() {
        storeSteps.validateCartSubtotal();
    }
}
