package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;

public class CartTest extends BaseTest {

    /**
     * Test: Verificar que el carrito contiene los productos esperados.
     * Flujo:
     * 1. Abrir página principal
     * 2. Abrir carrito
     * 3. Validar que producto "MacBook" está en el carrito
     * 4. Validar que la cantidad es >= 1
     */
    @Test
    public void verifyCartContainsProducts() {
        // Paso 1: Abrir la página principal
        HomePage homePage = new HomePage(driver);
        homePage.open();
        Assert.assertNotNull(driver.getTitle(), "La página no se cargó correctamente");
        
        // Paso 2: Abrir el carrito desde la página principal
        CartPage cartPage = new CartPage(driver);
        cartPage.openCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), 
            "No se navegó a la página del carrito");
        
        // Paso 3: Validar que el producto 'MacBook' está en el carrito
        boolean productExists = cartPage.isProductInCart("MacBook");
        Assert.assertTrue(productExists, "MacBook no está en el carrito");
        
        // Paso 4: Validar cantidad del producto
        int quantity = cartPage.getQuantityForProduct("MacBook");
        Assert.assertTrue(quantity >= 1, 
            "Cantidad de MacBook debe ser >= 1, pero es: " + quantity);
    }
}
