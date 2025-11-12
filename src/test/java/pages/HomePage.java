package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class HomePage extends BasePage {
    // Localizadores - selectores relativos sin rutas absolutas
    private By searchInput = By.name("search");
    private By searchBtn = By.cssSelector("button.btn.btn-default.btn-lg");
    private By productList = By.cssSelector(".product-thumb h4 a");

    public HomePage(WebDriver driver) { super(driver); }

    /**
     * Abre la página principal de OpenCart.
     */
    public void open() { 
        driver.get("https://opencart.abstracta.us/");
    }

    /**
     * Busca un término en la tienda.
     * 1. Limpia el input de búsqueda
     * 2. Escribe el término
     * 3. Hace clic en buscar
     * @param term Término a buscar
     */
    public void search(String term) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(term);
        driver.findElement(searchBtn).click();
    }

    /**
     * Verifica si un producto es visible en los resultados de búsqueda.
     * @param expectedProductName Nombre del producto esperado
     * @return true si el producto es visible, false si no
     */
    public boolean isProductVisible(String expectedProductName) {
        try {
            WaitUtils.waitForVisible(driver, productList, 6);
            String name = driver.findElement(productList).getText();
            return name.toLowerCase().contains(expectedProductName.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Abre el primer producto en los resultados de búsqueda.
     * Útil para después agregar cantidad y llevar al carrito.
     */
    public void openFirstProduct() {
        if (!WaitUtils.waitForClickable(driver, productList, 6)) {
            throw new RuntimeException("El primer producto no fue clickable en 6 segundos");
        }
        driver.findElement(productList).click();
    }
}
