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
        
        // Esperar a que el botón sea clickeable antes de hacer click
        WaitUtils.waitForClickable(driver, continueBtn, 5);
        
        // Hacer click en Continue
        WebElement btn = driver.findElement(continueBtn);
        btn.click();
        
        System.out.println("[RegisterPage] Formulario enviado para: " + mail);
    }

    public boolean isSuccess() {
        // Esperar más tiempo para que la página se cargue completamente
        boolean headerMatch = WaitUtils.waitForText(driver, successHeader, "Your Account Has Been Created", 10);
        
        if (headerMatch) {
            System.out.println("[RegisterPage] Mensaje de éxito detectado (header)");
            return true;
        }
        
        // Alternativa: buscar alert de éxito
        if (WaitUtils.waitForVisible(driver, alertSuccess, 10)) {
            String alertText = driver.findElement(alertSuccess).getText();
            System.out.println("[RegisterPage] Alert success encontrado: " + alertText);
            return true;
        }
        
        // Debug: imprimir el título y contenido actual
        System.out.println("[RegisterPage] FAIL - URL actual: " + driver.getCurrentUrl());
        System.out.println("[RegisterPage] FAIL - Título de página: " + driver.getTitle());
        try {
            WebElement header = driver.findElement(successHeader);
            System.out.println("[RegisterPage] FAIL - Texto del header: " + header.getText());
        } catch (Exception e) {
            System.out.println("[RegisterPage] FAIL - Header no encontrado: " + e.getMessage());
        }
        
        return false;
    }
}
