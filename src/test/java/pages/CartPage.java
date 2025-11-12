package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class CartPage extends BasePage {
    // Localizadores - sin rutas absolutas, usando selectores relativos
    private By cartTop = By.id("cart");
    private By viewCartLink = By.linkText("View Cart");
    private By viewCartLinkAlt = By.cssSelector("a[href*='cart']");  // Selector alternativo
    private By cartTable = By.cssSelector(".table.table-bordered");
    private By cartRows = By.cssSelector(".table.table-bordered tbody tr");

    public CartPage(WebDriver driver) { super(driver); }

    /**
     * Abre el carrito desde la página cargada.
     * 1. Valida que el botón del carrito sea clickable
     * 2. Hace clic en el carrito
     * 3. Espera a que aparezca el enlace "View Cart"
     * 4. Hace clic para ver el carrito completo
     */
    public void openCart() {
        // Esperar que el carrito esté clickable antes de interactuar
        if (!WaitUtils.waitForClickable(driver, cartTop, 5)) {
            throw new RuntimeException("El botón del carrito no fue clickable en 5 segundos");
        }
        driver.findElement(cartTop).click();
        
        // Esperar al enlace "View Cart" tras la interacción inicial
        // Intenta primero con linkText, luego con selector alternativo
        boolean viewCartVisible = WaitUtils.waitForVisible(driver, viewCartLink, 10);
        if (!viewCartVisible) {
            // Intenta con selector alternativo
            viewCartVisible = WaitUtils.waitForVisible(driver, viewCartLinkAlt, 10);
            if (!viewCartVisible) {
                throw new RuntimeException("El enlace 'View Cart' no fue visible en 20 segundos (intentos agotados)");
            }
            driver.findElement(viewCartLinkAlt).click();
        } else {
            driver.findElement(viewCartLink).click();
        }
    }

    /**
     * Valida si un producto está en el carrito.
     * Espera a que la tabla esté visible y busca el nombre en la página.
     * @param productName Nombre del producto a validar
     * @return true si el producto está en el carrito, false en caso contrario
     */
    public boolean isProductInCart(String productName) {
        if (!WaitUtils.waitForVisible(driver, cartTable, 5)) {
            throw new RuntimeException("La tabla del carrito no fue visible en 5 segundos");
        }
        return driver.getPageSource().toLowerCase().contains(productName.toLowerCase());
    }

    /**
     * Obtiene la cantidad de un producto específico en el carrito.
     * Busca en las filas de la tabla e intenta extraer la cantidad.
     * @param productName Nombre del producto
     * @return Cantidad del producto, 0 si no encontrado o si no es un número válido
     */
    public int getQuantityForProduct(String productName) {
        if (!WaitUtils.waitForVisible(driver, cartTable, 5)) {
            throw new RuntimeException("La tabla del carrito no fue visible en 5 segundos");
        }
        for (var row : driver.findElements(cartRows)) {
            String text = row.getText();
            if (text.toLowerCase().contains(productName.toLowerCase())) {
                // Dividir el texto en celdas para extraer la cantidad
                String[] cells = text.split("\\r?\\n");
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
