package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class ProductPage extends BasePage {
    // Localizadores - selectores relativos sin rutas absolutas
    private By quantityInput = By.id("input-quantity");
    private By addToCartBtn = By.id("button-cart");
    private By successAlert = By.cssSelector(".alert.alert-success");

    public ProductPage(WebDriver driver) { super(driver); }

    /**
     * Establece la cantidad de un producto.
     * 1. Espera que el input de cantidad sea visible
     * 2. Limpia el contenido actual
     * 3. Escribe la nueva cantidad
     * @param qty Cantidad a establecer
     */
    public void setQuantity(int qty) {
        if (!WaitUtils.waitForVisible(driver, quantityInput, 5)) {
            throw new RuntimeException("El input de cantidad no fue visible en 5 segundos");
        }
        driver.findElement(quantityInput).clear();
        driver.findElement(quantityInput).sendKeys(String.valueOf(qty));
    }

    /**
     * Añade el producto al carrito.
     * Espera que el botón sea clickable antes de hacer clic.
     */
    public void addToCart() {
        if (!WaitUtils.waitForClickable(driver, addToCartBtn, 5)) {
            throw new RuntimeException("El botón 'Agregar al carrito' no fue clickable en 5 segundos");
        }
        driver.findElement(addToCartBtn).click();
    }

    /**
     * Verifica si el producto fue agregado exitosamente al carrito.
     * Espera a que aparezca la alerta de éxito.
     * @return true si la alerta de éxito fue visible, false en caso contrario
     */
    public boolean isAddedSuccessfully() {
        return WaitUtils.waitForVisible(driver, successAlert, 6);
    }
}
