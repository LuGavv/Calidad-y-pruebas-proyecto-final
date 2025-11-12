package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class HomePage extends BasePage {
    private By searchInput = By.name("search");
    private By searchBtn = By.cssSelector("button.btn.btn-default.btn-lg");
    private By productList = By.cssSelector(".product-thumb h4 a");

    public HomePage(WebDriver driver) { super(driver); }

    public void open() { driver.get("https://opencart.abstracta.us/"); }

    public void search(String term) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(term);
        driver.findElement(searchBtn).click();
    }

    public boolean isProductVisible(String expectedProductName) {
        try {
            WaitUtils.waitForVisible(driver, productList, 6);
            String name = driver.findElement(productList).getText();
            return name.toLowerCase().contains(expectedProductName.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    // abre la primera tarjeta de producto (útil para después agregar cantidad)
    public void openFirstProduct() {
        WaitUtils.waitForVisible(driver, productList, 6);
        driver.findElement(productList).click();
    }
}
