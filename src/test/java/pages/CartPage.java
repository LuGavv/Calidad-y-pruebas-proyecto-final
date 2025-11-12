package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class CartPage extends BasePage {
    private By cartTop = By.id("cart");
    private By viewCartLink = By.linkText("View Cart");
    private By cartTable = By.cssSelector(".table.table-bordered");
    private By cartRows = By.cssSelector(".table.table-bordered tbody tr");

    public CartPage(WebDriver driver) { super(driver); }

    public void openCart() {
        driver.findElement(cartTop).click();
        WaitUtils.waitForVisible(driver, viewCartLink, 3);
        driver.findElement(viewCartLink).click();
    }

    public boolean isProductInCart(String productName) {
        WaitUtils.waitForVisible(driver, cartTable, 5);
        return driver.getPageSource().toLowerCase().contains(productName.toLowerCase());
    }

    public int getQuantityForProduct(String productName) {
        WaitUtils.waitForVisible(driver, cartTable, 5);
        for (var row : driver.findElements(cartRows)) {
            String text = row.getText();
            if (text.toLowerCase().contains(productName.toLowerCase())) {
                // En la tabla la cantidad normalmente está en columna; aquí hacemos parse rápido:
                String[] cells = text.split("\\r?\\n");
                // encontrar número (simplificación: buscar pattern \d+)
                for (String c : cells) {
                    if (c.matches("\\d+")) {
                        return Integer.parseInt(c);
                    }
                }
            }
        }
        return 0;
    }
}
