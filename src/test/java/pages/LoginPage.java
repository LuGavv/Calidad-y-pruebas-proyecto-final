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
    private By logoutBtn = By.xpath("//a[contains(text(), 'Logout')]");
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
            System.out.println("[LoginPage] Iniciando login...");
            System.out.println("[LoginPage] Email: " + mail);
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Esperar y rellenar email
            System.out.println("[LoginPage] Esperando y rellenando campo de email...");
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(email));
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(mail);
            System.out.println("[LoginPage] ✓ Email rellenado: " + mail);
            
            // Esperar y rellenar password
            System.out.println("[LoginPage] Esperando y rellenando campo de contraseña...");
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(password));
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(pwd);
            System.out.println("[LoginPage] ✓ Contraseña rellenada");
            
            // Esperar a que el botón sea clickeable y hacer click
            System.out.println("[LoginPage] Esperando botón de login...");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
            loginButton.click();
            System.out.println("[LoginPage] ✓ Botón de login clickeado");
            
            // Pequeña pausa para que la página empiece a responder
            Thread.sleep(500);
            System.out.println("[LoginPage] ✓ Login enviado para: " + mail);
            
        } catch (Exception e) {
            System.out.println("[LoginPage] ERROR en login(): " + e.getMessage());
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Título: " + driver.getTitle());
            System.out.println("[LoginPage] Page source contiene 'input-email': " + driver.getPageSource().contains("input-email"));
            e.printStackTrace();
            throw new RuntimeException("Fallo al intentar login para: " + mail, e);
        }
    }

    public boolean isLoggedIn() {
        try {
            System.out.println("[LoginPage] Validando si el usuario está autenticado...");
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            
            // Check 1: Logout link visible (solo aparece cuando está logeado)
            System.out.println("[LoginPage] Buscando link 'Logout'...");
            boolean logoutVisible = WaitUtils.waitForVisible(driver, logoutLink, 5);
            if (logoutVisible) {
                System.out.println("[LoginPage] ✓ ÉXITO - Link 'Logout' detectado → Usuario autenticado");
                return true;
            }
            System.out.println("[LoginPage] Link 'Logout' no visible");

            // Check 2: My Account link visible (solo aparece cuando está logeado)
            System.out.println("[LoginPage] Buscando link 'My Account'...");
            boolean myAccountVisible = WaitUtils.waitForVisible(driver, myAccountLink, 5);
            if (myAccountVisible) {
                System.out.println("[LoginPage] ✓ ÉXITO - Link 'My Account' detectado → Usuario autenticado");
                return true;
            }
            System.out.println("[LoginPage] Link 'My Account' no visible");

            // Check 3: Header visible con "My Account" (dashboard/account)
            System.out.println("[LoginPage] Buscando header h2 con 'My Account'...");
            boolean headerVisible = WaitUtils.waitForVisible(driver, accountHeader, 5);
            if (headerVisible) {
                String headerText = driver.findElement(accountHeader).getText();
                System.out.println("[LoginPage] Header detectado: " + headerText);
                if (headerText.toLowerCase().contains("account")) {
                    System.out.println("[LoginPage] ✓ ÉXITO - Header 'My Account' detectado → Usuario autenticado");
                    return true;
                }
            }
            System.out.println("[LoginPage] Header 'My Account' no encontrado");

            // Check 4: URL ha cambiado a dashboard/account (indicador de autenticación)
            String currentUrl = driver.getCurrentUrl();
            boolean urlChanged = !currentUrl.contains("/login");
            boolean urlHasAccount = currentUrl.contains("dashboard") || currentUrl.contains("account");
            System.out.println("[LoginPage] URL cambió de /login: " + urlChanged);
            System.out.println("[LoginPage] URL contiene 'dashboard' o 'account': " + urlHasAccount);
            
            if (urlChanged && urlHasAccount) {
                System.out.println("[LoginPage] ✓ ÉXITO - URL cambió a página de cuenta → Usuario autenticado");
                return true;
            }

            System.out.println("[LoginPage] ✗ No se detectó autenticación");
            return false;

        } catch (Exception e) {
            System.out.println("[LoginPage] Error en isLoggedIn(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            System.out.println("[LoginPage] Verificando si hay error de login...");
            System.out.println("[LoginPage] URL actual: " + driver.getCurrentUrl());
            
            // Check 1: Alert danger visible (.alert-danger)
            boolean visible = WaitUtils.waitForVisible(driver, warning, 6);
            if (visible) {
                String errorText = driver.findElement(warning).getText();
                System.out.println("[LoginPage] ✓ ERROR DETECTADO (Alert danger): " + errorText);
                return true;
            }
            System.out.println("[LoginPage] No hay .alert-danger visible");

            // Check 2: URL aún en login (no redirigió) = indica fallo
            String url = driver.getCurrentUrl();
            boolean stillInLogin = url.contains("/login");
            if (stillInLogin) {
                System.out.println("[LoginPage] ✓ URL aún en /login: " + url + " → login falló (esperado)");
                // Pequeña pausa adicional para que la página renderice completamente
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return true;
            }
            System.out.println("[LoginPage] URL cambió de /login: " + url);

            // Check 3: Buscar cualquier div de warning/error
            try {
                java.util.List<WebElement> warnings = driver.findElements(
                    By.cssSelector("[class*='alert']:not([class*='success']), [class*='warning'], [class*='error']")
                );
                if (!warnings.isEmpty()) {
                    for (WebElement w : warnings) {
                        String text = w.getText().trim();
                        if (!text.isEmpty()) {
                            boolean isError = text.toLowerCase().contains("warning") || 
                                             text.toLowerCase().contains("error") ||
                                             text.toLowerCase().contains("failed") ||
                                             text.toLowerCase().contains("incorrect");
                            if (isError) {
                                System.out.println("[LoginPage] ✓ Elemento de error detectado: " + text);
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}

            System.out.println("[LoginPage] ✗ No se detectó error de login visible");
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

    /**
     * Cierra la sesión del usuario actual (Logout)
     * Busca y hace click en el link de Logout
     */
    public void logout() {
        try {
            System.out.println("[LoginPage] Intentando hacer logout...");
            
            // Buscar link Logout por diferentes selectores
            WebElement logoutElement = null;
            try {
                logoutElement = WaitUtils.waitForClickable(driver, logoutLink, 5) 
                    ? driver.findElement(logoutLink) 
                    : null;
            } catch (Exception e1) {
                try {
                    logoutElement = WaitUtils.waitForClickable(driver, logoutBtn, 5) 
                        ? driver.findElement(logoutBtn) 
                        : null;
                } catch (Exception e2) {
                    // Intenta otro selector
                }
            }

            if (logoutElement != null && logoutElement.isDisplayed()) {
                logoutElement.click();
                System.out.println("[LoginPage] ✓ Logout clickeado");
                Thread.sleep(1000); // Esperar a que se procese el logout
            } else {
                System.out.println("[LoginPage] Advertencia: Link Logout no encontrado o no visible");
            }
        } catch (Exception e) {
            System.out.println("[LoginPage] Error en logout(): " + e.getMessage());
            // No lanzar excepción aquí, solo logging
        }
    }
}
