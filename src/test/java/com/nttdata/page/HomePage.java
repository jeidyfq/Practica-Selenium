package com.nttdata.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private static final By CATEGORY_LINKS = By.cssSelector("#top-menu > li > a");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body#index #main")));
        if (driver.getTitle().trim().isEmpty()) {
            throw new AssertionError("La página de la tienda no tiene título; el servicio podría estar caído.");
        }
    }

    public boolean openCategory(String category) {
        List<WebElement> links = driver.findElements(CATEGORY_LINKS);
        for (WebElement link : links) {
            if (link.isDisplayed() && link.getText().trim().equalsIgnoreCase(category)) {
                link.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#js-product-list-header h1")));
                return true;
            }
        }
        return false;
    }
}
