package com.nttdata.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private static final By ALERTS = By.cssSelector("#notifications .alert, .login-form .alert, .alert-danger");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#_desktop_user_info .user-info a"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-form")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field-email")));
    }

    public void login(String email, String password) {
        driver.findElement(By.id("field-email")).clear();
        driver.findElement(By.id("field-email")).sendKeys(email);
        driver.findElement(By.id("field-password")).clear();
        driver.findElement(By.id("field-password")).sendKeys(password);
        driver.findElement(By.id("submit-login")).click();
        wait.until(d -> !d.getCurrentUrl().contains("iniciar-sesion") || !d.findElements(ALERTS).isEmpty());
    }

    public boolean isAuthenticated() {
        return !driver.getCurrentUrl().contains("iniciar-sesion")
                && driver.findElements(ALERTS).isEmpty()
                && !driver.findElements(By.cssSelector(
                "#_desktop_user_info .user-info .logout, #_desktop_user_info .user-info a[href*='mylogout']"))
                .isEmpty();
    }

    public boolean hasAuthenticationError() {
        return driver.getCurrentUrl().contains("iniciar-sesion") && !driver.findElements(ALERTS).isEmpty();
    }
}
