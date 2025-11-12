package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import utils.ExcelUtils;
import java.io.IOException;
import java.util.*;

public class SearchAndAddTest extends BaseTest {

    @Test
    public void searchAndAddFromExcel() throws IOException {
        ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
        List<Map<String,String>> products = excel.readSheetAsMap("ProductosBusqueda");
        HomePage hp = new HomePage(driver);
        ProductPage pp = new ProductPage(driver);
        CartPage cp = new CartPage(driver);

        List<Map<String,String>> logRows = new ArrayList<>();

        for (Map<String,String> p : products) {
            String prodName = p.get("Producto");
            int qty = 1;
            try { qty = Integer.parseInt(p.getOrDefault("Cantidad","1")); } catch (Exception ignored) {}

            hp.open();
            hp.search(prodName);
            Assert.assertTrue(hp.isProductVisible(prodName), "Producto no visible: " + prodName);

            hp.openFirstProduct();
            pp.setQuantity(qty);
            pp.addToCart();
            boolean added = pp.isAddedSuccessfully();
            Map<String,String> row = new LinkedHashMap<>();
            row.put("Categoria", p.getOrDefault("Categoria",""));
            row.put("SubCategoria", p.getOrDefault("SubCategoria",""));
            row.put("Producto", prodName);
            row.put("Cantidad", String.valueOf(qty));
            row.put("Added", String.valueOf(added));
            row.put("Timestamp", String.valueOf(System.currentTimeMillis()));
            logRows.add(row);

            // opcional: volver a home
        }

        // escribir log al final
        if (!logRows.isEmpty()) {
            utils.ExcelWriter.writeLogs("logs.xlsx", logRows, "AddedProducts");
        }

        excel.close();
    }
}
