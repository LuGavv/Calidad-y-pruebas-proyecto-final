package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class LoginPage extends BasePage {
    private By email = By.id("input-email");
    private By password = By.id("input-password");
    private By loginBtn = By.cssSelector("input[type='submit'][value='Login']");
    private By accountHeader = By.cssSelector("#content h2");
    private By warning = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver) { super(driver); }
    public void open() { driver.get("https://opencart.abstracta.us/index.php?route=account/login"); }
    public void login(String mail, String pwd) {
        driver.findElement(email).clear();
        driver.findElement(email).sendKeys(mail);
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(loginBtn).click();
    }
    public boolean isLoggedIn() { return WaitUtils.waitForVisible(driver, accountHeader, 6); }
    public boolean isLoginErrorDisplayed() {
        try {
            WaitUtils.waitForVisible(driver, warning, 5);
            return true;
        } catch (Exception e) { return false; }
    }
}
