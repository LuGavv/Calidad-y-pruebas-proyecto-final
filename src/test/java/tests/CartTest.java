package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;

public class CartTest extends BaseTest {

    @Test
    public void verifyCartContainsProducts() {
        CartPage cp = new CartPage(driver);
        cp.openCart();
        // ejemplos. Ajusta nombres según lo agregado en inputData.xlsx
        Assert.assertTrue(cp.isProductInCart("MacBook"), "MacBook no está en el carrito");
        // si quieres validar cantidades:
        int qty = cp.getQuantityForProduct("MacBook");
        Assert.assertTrue(qty >= 1, "Cantidad de MacBook debe ser >=1");
    }
}
