package com.nttdata.page;

import com.nttdata.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class ProductPage {
    private static final By SUBCATEGORY_LINKS = By.cssSelector(
            "#left-column .category-top-menu a, #left-column .category-sub-menu a, #top-menu a");
    private static final By PRODUCT_CARDS = By.cssSelector("#js-product-list article.product-miniature");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private BigDecimal expectedSubtotal;
    private String selectedProductName;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean openSubcategory(String subcategory) {
        WebElement subcategoryLink = wait.until(d -> findVisibleLink(subcategory));
        if (subcategoryLink == null) {
            return false;
        }

        subcategoryLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#js-product-list-header h1")));
        DriverManager.scrollDown();
        return driver.findElement(By.cssSelector("#js-product-list-header h1"))
                .getText().trim().equalsIgnoreCase(subcategory);

    }

    public int getProductCount() {
        return driver.findElements(PRODUCT_CARDS).size();
    }

    public void addFirstProductToCart(int quantity) {

        WebElement firstProduct = wait.until(
                ExpectedConditions.visibilityOfElementLocated(PRODUCT_CARDS));
        firstProduct.findElement(By.cssSelector("a.product-thumbnail")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-or-refresh")));

        selectedProductName = driver.findElement(By.cssSelector("#main h1")).getText().trim();
        BigDecimal unitPrice = new BigDecimal(
                driver.findElement(By.cssSelector(".current-price-value[content]")).getAttribute("content"));
        expectedSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2);

        WebElement quantityInput = driver.findElement(By.id("quantity_wanted"));
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        driver.findElement(By.cssSelector("[data-button-action='add-to-cart']")).click();
    }

    public BigDecimal getExpectedSubtotal() {
        return expectedSubtotal;
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    private WebElement findVisibleLink(String text) {
        List<WebElement> links = driver.findElements(SUBCATEGORY_LINKS);
        for (WebElement link : links) {
            if (link.isDisplayed() && link.getText().trim().equalsIgnoreCase(text)) {
                return link;
            }
        }
        return null;
    }
}
