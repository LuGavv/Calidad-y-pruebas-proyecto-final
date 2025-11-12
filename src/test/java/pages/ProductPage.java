package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class ProductPage extends BasePage {
    private By quantityInput = By.id("input-quantity");
    private By addToCartBtn = By.id("button-cart");
    private By successAlert = By.cssSelector(".alert.alert-success");

    public ProductPage(WebDriver driver) { super(driver); }

    public void setQuantity(int qty) {
        WaitUtils.waitForVisible(driver, quantityInput, 5);
        driver.findElement(quantityInput).clear();
        driver.findElement(quantityInput).sendKeys(String.valueOf(qty));
    }

    public void addToCart() {
        driver.findElement(addToCartBtn).click();
    }

    public boolean isAddedSuccessfully() {
        return WaitUtils.waitForVisible(driver, successAlert, 6);
    }
}
