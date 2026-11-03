package com.nttdata.steps;

import com.nttdata.page.LoginPage;
import org.junit.Assert;

public class LoginSteps {
    private final LoginPage loginPage;

    public LoginSteps(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void login(String username, String password) {
        loginPage.open();
        loginPage.login(resolveCredential(username), resolveCredential(password));
    }

    public void validateAuthentication(String expectedResult) {
        if ("success".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue("La autenticación válida no llegó a una sesión iniciada.",
                    loginPage.isAuthenticated());
        } else if ("rejected".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue("Las credenciales inválidas no fueron rechazadas con un mensaje visible.",
                    loginPage.hasAuthenticationError());
        } else {
            throw new IllegalArgumentException("Resultado de autenticación no soportado: " + expectedResult);
        }
    }

    private String resolveCredential(String credential) {
        if ("STORE_VALID_USER".equals(credential)) {
            return requiredCredential("store.user", "STORE_VALID_USER");
        }
        if ("STORE_VALID_PASSWORD".equals(credential)) {
            return requiredCredential("store.password", "STORE_VALID_PASSWORD");
        }
        return credential;
    }

    private String requiredCredential(String property, String environmentVariable) {
        String value = System.getProperty(property, System.getenv(environmentVariable));
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Falta configurar la credencial requerida: " + environmentVariable);
        }
        return value;
    }
}
