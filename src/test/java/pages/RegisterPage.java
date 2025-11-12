package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    public RegisterPage(WebDriver driver) { super(driver); }

    public void open() {
        driver.get("https://opencart.abstracta.us/index.php?route=account/register");
    }

    public void register(String f, String l, String mail, String phone, String pwd) {
        driver.findElement(firstName).sendKeys(f);
        driver.findElement(lastName).sendKeys(l);
        driver.findElement(email).sendKeys(mail);
        driver.findElement(telephone).sendKeys(phone);
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(confirm).sendKeys(pwd);
        driver.findElement(continueBtn).click();
    }

    public boolean isSuccess() {
        return WaitUtils.waitForText(driver, successHeader, "Your Account Has Been Created", 6);
    }
}
