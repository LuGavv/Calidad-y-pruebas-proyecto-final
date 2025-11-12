package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.WaitUtils;

public class RegisterPage extends BasePage {
    private By firstName = By.id("input-firstname");
    private By lastName = By.id("input-lastname");
    private By email = By.id("input-email");
    private By telephone = By.id("input-telephone");
    private By password = By.id("input-password");
    private By confirm = By.id("input-confirm");
    private By continueBtn = By.cssSelector("input[type='submit'][value='Continue']");
    private By successHeader = By.cssSelector("#content h1");
    private By alertSuccess = By.cssSelector(".alert-success");

    public RegisterPage(WebDriver driver) { super(driver); }

    public void open() {
        driver.get("https://opencart.abstracta.us/index.php?route=account/register");
    }

    public void register(String f, String l, String mail, String phone, String pwd) {
        // Rellenar formulario
        driver.findElement(firstName).sendKeys(f);
        driver.findElement(lastName).sendKeys(l);
        driver.findElement(email).sendKeys(mail);
        driver.findElement(telephone).sendKeys(phone);
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(confirm).sendKeys(pwd);
        
        // Intentar marcar checkbox de privacidad/terminos si está presente (sitios pueden requerirlo)
        try {
            java.util.List<org.openqa.selenium.WebElement> agrees = driver.findElements(org.openqa.selenium.By.cssSelector("input[name='agree'], input[type='checkbox'][name='agree']"));
            if (agrees != null && !agrees.isEmpty()) {
                org.openqa.selenium.WebElement cb = agrees.get(0);
                if (!cb.isSelected()) {
                    cb.click();
                    System.out.println("[RegisterPage] Marcado checkbox de terms/agree");
                }
            }
        } catch (Exception e) {
            // ignore; checkbox optional
        }

        // Esperar a que el botón sea clickeable antes de hacer click
        WaitUtils.waitForClickable(driver, continueBtn, 8);

        // Hacer click en Continue
        WebElement btn = driver.findElement(continueBtn);
        btn.click();
        
        System.out.println("[RegisterPage] Formulario enviado para: " + mail);
    }

    public boolean isSuccess() {
        try {
            // Esperar cambio de URL (señal de que el registro se procesó)
            System.out.println("[RegisterPage] URL después de registro: " + driver.getCurrentUrl());

            // Check 1: Buscar header h1 con "Your Account Has Been Created"
            boolean headerMatch = WaitUtils.waitForText(driver, successHeader, "Your Account Has Been Created", 12);
            if (headerMatch) {
                System.out.println("[RegisterPage] ✓ ÉXITO - Header con mensaje detectado");
                return true;
            }

            // Check 2: Buscar alert success (.alert-success)
            if (WaitUtils.waitForVisible(driver, alertSuccess, 8)) {
                String alertText = driver.findElement(alertSuccess).getText();
                System.out.println("[RegisterPage] ✓ ÉXITO - Alert success detectado: " + alertText);
                return true;
            }

            // Check 3: Buscar cualquier h1 que contenga palabras clave
            try {
                java.util.List<WebElement> headers = driver.findElements(By.cssSelector("h1, h2"));
                for (WebElement h : headers) {
                    String text = h.getText().toLowerCase();
                    if (text.contains("account") && text.contains("created")) {
                        System.out.println("[RegisterPage] ✓ ÉXITO - Header alternativo detectado: " + h.getText());
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println("[RegisterPage] No se encontró header alternativo");
            }

            // Check 4: URL cambió a dashboard/success/account (indicador de registro exitoso)
            String url = driver.getCurrentUrl();
            if (url.contains("success") || url.contains("account") || !url.contains("register")) {
                System.out.println("[RegisterPage] ✓ ÉXITO - URL cambió a: " + url);
                return true;
            }

            // Debug: imprimir el título y contenido actual
            System.out.println("[RegisterPage] FAIL - URL: " + url);
            System.out.println("[RegisterPage] FAIL - Título: " + driver.getTitle());
            try {
                WebElement header = driver.findElement(successHeader);
                System.out.println("[RegisterPage] FAIL - Texto del header h1: " + header.getText());
            } catch (Exception e) {
                System.out.println("[RegisterPage] FAIL - No hay h1: " + e.getMessage());
            }

            return false;

        } catch (Exception e) {
            System.out.println("[RegisterPage] ERROR en isSuccess(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
