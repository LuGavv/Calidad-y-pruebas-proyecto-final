package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.WaitUtils;

public class LoginPage extends BasePage {
    private By email = By.id("input-email");
    private By password = By.id("input-password");
    private By loginBtn = By.cssSelector("input[type='submit'][value='Login']");
    private By accountHeader = By.cssSelector("#content h2");
    private By warning = By.cssSelector(".alert.alert-danger");
    private By logoutLink = By.linkText("Logout");
    private By myAccountLink = By.linkText("My Account");

    public LoginPage(WebDriver driver) { super(driver); }

    public void open() { 
        driver.get("https://opencart.abstracta.us/index.php?route=account/login");
        System.out.println("[LoginPage] Página de login abierta: " + driver.getCurrentUrl());
        
        // Esperar a que el formulario de login esté presente en la página
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(email));
            System.out.println("[LoginPage] Formulario de login detectado");
        } catch (Exception e) {
            System.out.println("[LoginPage] Advertencia: Formulario de login no detectado en 10s");
            System.out.println("[LoginPage] URL: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Title: " + driver.getTitle());
        }
    }

    public void login(String mail, String pwd) {
        try {
            System.out.println("[LoginPage] Intentando hacer login con: " + mail);
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            
            // Usar WebDriverWait explícito para esperar a que el elemento sea interactivo
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Esperar y rellenar email
            System.out.println("[LoginPage] Esperando campo de email...");
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(email));
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(mail);
            System.out.println("[LoginPage] Email rellenado");
            
            // Esperar y rellenar password
            System.out.println("[LoginPage] Esperando campo de password...");
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(password));
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(pwd);
            System.out.println("[LoginPage] Password rellenado");
            
            // Esperar a que el botón sea clickeable y hacer click
            System.out.println("[LoginPage] Esperando botón de login...");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
            loginButton.click();
            System.out.println("[LoginPage] Login enviado para: " + mail);
            
        } catch (Exception e) {
            System.out.println("[LoginPage] ERROR en login(): " + e.getMessage());
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Título: " + driver.getTitle());
            System.out.println("[LoginPage] Page source contains 'input-email': " + driver.getPageSource().contains("input-email"));
            e.printStackTrace();
            throw new RuntimeException("Fallo en login para: " + mail, e);
        }
    }

    public boolean isLoggedIn() {
        try {
            // Check 1: Header visible (ej: "My Account")
            boolean headerVisible = WaitUtils.waitForVisible(driver, accountHeader, 8);
            if (headerVisible) {
                String headerText = driver.findElement(accountHeader).getText();
                System.out.println("[LoginPage] ÉXITO - Header detectado: " + headerText);
            }

            // Check 2: Logout link visible (solo aparece cuando está logeado)
            boolean logoutVisible = WaitUtils.waitForVisible(driver, logoutLink, 5);
            if (logoutVisible) {
                System.out.println("[LoginPage] ÉXITO - Link 'Logout' detectado");
            }

            // Check 3: My Account link visible
            boolean myAccountVisible = WaitUtils.waitForVisible(driver, myAccountLink, 5);
            if (myAccountVisible) {
                System.out.println("[LoginPage] ÉXITO - Link 'My Account' detectado");
            }

            // Check 4: URL ha cambiado a dashboard/account
            String currentUrl = driver.getCurrentUrl();
            boolean urlOk = currentUrl.contains("dashboard") || currentUrl.contains("account");
            System.out.println("[LoginPage] URL actual: " + currentUrl + " (account/dashboard? " + urlOk + ")");

            boolean result = headerVisible || logoutVisible || myAccountVisible || urlOk;
            System.out.println("[LoginPage] isLoggedIn() = " + result);
            return result;

        } catch (Exception e) {
            System.out.println("[LoginPage] Error en isLoggedIn(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            // Pequeña pausa para que la página responda
            Thread.sleep(2000);
            
            System.out.println("[LoginPage] Verificando si hay error de login...");
            System.out.println("[LoginPage] URL: " + driver.getCurrentUrl());
            
            // Check 1: Alert danger visible
            boolean visible = WaitUtils.waitForVisible(driver, warning, 6);
            if (visible) {
                String errorText = driver.findElement(warning).getText();
                System.out.println("[LoginPage] ✓ ERROR DETECTADO: " + errorText);
                return true;
            }
            System.out.println("[LoginPage] No hay .alert-danger visible");

            // Check 2: URL aún en login (no redirigió) = indica fallo
            String url = driver.getCurrentUrl();
            boolean stillInLogin = url.contains("/login");
            if (stillInLogin) {
                System.out.println("[LoginPage] ✓ URL aún en /login: " + url + " → login falló (esperado)");
                return true;
            }
            System.out.println("[LoginPage] URL no está en /login: " + url);

            // Check 3: Buscar cualquier div de warning/error
            try {
                java.util.List<WebElement> warnings = driver.findElements(By.cssSelector("[class*='alert'], [class*='warning'], [class*='error']"));
                if (!warnings.isEmpty()) {
                    for (WebElement w : warnings) {
                        String text = w.getText();
                        if (!text.isEmpty() && text.toLowerCase().contains("warning") || text.toLowerCase().contains("error")) {
                            System.out.println("[LoginPage] ✓ Elemento de warning/error detectado: " + text);
                            return true;
                        }
                    }
                }
            } catch (Exception ignored) {}

            System.out.println("[LoginPage] ✗ No se detectó error de login");
            return false;

        } catch (Exception e) {
            System.out.println("[LoginPage] Error en isLoginErrorDisplayed(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getLoginErrorText() {
        try {
            if (WaitUtils.waitForVisible(driver, warning, 3)) {
                return driver.findElement(warning).getText();
            }
        } catch (Exception ignored) {}
        return "No error message found";
    }
}
