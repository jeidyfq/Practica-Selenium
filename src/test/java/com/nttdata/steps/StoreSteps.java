package com.nttdata.steps;

import com.nttdata.page.CartPage;
import com.nttdata.page.HomePage;
import com.nttdata.page.ProductPage;
import org.junit.Assert;

import java.math.BigDecimal;

public class StoreSteps {
    private final HomePage homePage;
    private final ProductPage productPage;
    private final CartPage cartPage;
    private boolean categoryFound;

    public StoreSteps(HomePage homePage, ProductPage productPage, CartPage cartPage) {
        this.homePage = homePage;
        this.productPage = productPage;
        this.cartPage = cartPage;
    }

    public void openStore(String baseUrl) {
        homePage.open(baseUrl);
    }

    public void navigateToCategory(String category, String subcategory) {
        categoryFound = homePage.openCategory(category);
        if (!categoryFound) {
            return;
        }

        if (subcategory == null || subcategory.trim().isEmpty()) {
            return;
        }

        categoryFound = productPage.openSubcategory(subcategory);
        if (categoryFound) {
            Assert.assertTrue("La categoría no mostró productos.", productPage.getProductCount() > 0);
        }

    }

    public void validateCategoryNotFound() {
        Assert.assertFalse("La categoría inexistente fue encontrada; el escenario no debe continuar.",
                categoryFound);
        Assert.assertEquals("Se encontraron productos aunque la navegación debía detenerse.",
                0, productPage.getProductCount());
    }

    public void addFirstProductToCart(int quantity) {
        productPage.addFirstProductToCart(quantity);
        cartPage.waitForProductConfirmation();
    }

    public void validateProductAdded() {
        Assert.assertTrue("El popup no confirmó la incorporación del producto esperado.",
                cartPage.confirmsProduct(productPage.getSelectedProductName()));
    }

    public void validatePopupSubtotal() {
        assertPrice(productPage.getExpectedSubtotal(), cartPage.getPopupSubtotal(), "popup");
    }

    public void openCart() {
        cartPage.openCart();
    }

    public void validateCartPage() {
        String title = cartPage.getTitle().toLowerCase();
        Assert.assertTrue("El título no identifica la página del carrito: " + title,
                title.contains("carrito") || title.contains("cart"));
    }

    public void validateCartSubtotal() {
        assertPrice(productPage.getExpectedSubtotal(), cartPage.getSubtotal(), "carrito");
    }

    private void assertPrice(BigDecimal expected, BigDecimal actual, String location) {
        Assert.assertEquals("El subtotal del " + location + " no coincide. Esperado: " + expected
                        + ", actual: " + actual,
                0, expected.compareTo(actual));
    }
}
