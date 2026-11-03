package com.nttdata.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;

public class CartPage {
    private static final By CART_MODAL = By.id("blockcart-modal");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForProductConfirmation() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_MODAL));
    }

    public boolean confirmsProduct(String productName) {
        WebElement modal = driver.findElement(CART_MODAL);
        String confirmation = modal.findElement(By.cssSelector(".modal-title")).getText().toLowerCase();
        return (confirmation.contains("añad") || confirmation.contains("added"))
                && modal.getText().toLowerCase().contains(productName.toLowerCase());
    }

    public BigDecimal getPopupSubtotal() {
        return parsePrice(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-subtotal .value, .modal-body .subtotal, .subtotal"))).getText());
    }

    public void openCart() {
        driver.findElement(By.cssSelector("#blockcart-modal .cart-content-btn a.btn-primary")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main h1")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#cart-subtotal-products .value")));
    }

    public String getTitle() {
        return driver.findElement(By.cssSelector("#main h1")).getText().trim();
    }

    public boolean isCurrentPage() {
        return driver.getCurrentUrl().contains("/carrito") || driver.getCurrentUrl().contains("/cart");
    }

    public BigDecimal getSubtotal() {
        return parsePrice(driver.findElement(By.cssSelector("#cart-subtotal-products .value")).getText());
    }

    private BigDecimal parsePrice(String value) {
        String normalized = value.replaceAll("[^0-9,.-]", "").replace(',', '.');
        return new BigDecimal(normalized).setScale(2);
    }
}
