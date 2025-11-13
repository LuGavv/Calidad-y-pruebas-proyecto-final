package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import utils.ExcelUtils;
import utils.CSVUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

/**
 * Test de Compra Completa: Login + Búsqueda + Agregación + Verificación en Carrito.
 */
public class CompraCompleteTest extends BaseTest {

    /**
     * Test Principal: Compra completa de productos (Usuario Registrado)
     * Lee Excel para usuario, CSV para productos, loguea usuario, busca, agrega y verifica en carrito
     */
    @Test(priority = 1)
    public void testCompraCompleta() throws IOException, InterruptedException {
        System.out.println("\n========================================");
        System.out.println("INICIANDO TEST: COMPRA COMPLETA (LOGUEADO)");
        System.out.println("========================================\n");

        String excelPath = "src/test/resources/inputData.xlsx";
        String csvPath = "src/test/resources/inputData.csv";

        // PASO 1: LEER USUARIO REGISTRADO DESDE EXCEL
        System.out.println("[PASO 1] Leyendo usuario registrado desde Excel...");
        ExcelUtils excelUtils = new ExcelUtils(excelPath);
        List<Map<String,String>> usuarios = excelUtils.readSheetAsMap("UsuariosRegistro");
        excelUtils.close();

        Assert.assertNotNull(usuarios, "No se pudo leer la hoja UsuariosRegistro");
        Assert.assertFalse(usuarios.isEmpty(), "La hoja UsuariosRegistro está vacía");

        Map<String,String> usuarioRegistrado = usuarios.get(0);
        String emailUsuario = usuarioRegistrado.get("E-Mail");
        String passwordUsuario = usuarioRegistrado.get("Password");

        Assert.assertNotNull(emailUsuario, "Email no encontrado en Excel");
        Assert.assertNotNull(passwordUsuario, "Password no encontrado en Excel");

        System.out.println("✓ Usuario registrado encontrado");
        System.out.println("  Email: " + emailUsuario + "\n");

        // PASO 2: LEER PRODUCTOS DESDE CSV
        System.out.println("[PASO 2] Leyendo productos desde CSV...");
        List<Map<String,String>> csvData = CSVUtils.readCSV(csvPath);

        Assert.assertNotNull(csvData, "No se pudo leer el archivo CSV");
        Assert.assertFalse(csvData.isEmpty(), "El archivo CSV está vacío");

        // Los productos están en el CSV (puede que la primera fila sea también usuario o directamente productos)
        List<Map<String,String>> products = new ArrayList<>();
        for (Map<String,String> row : csvData) {
            if (row.containsKey("Producto") || row.containsKey("Nombre") || row.containsKey("Product")) {
                products.add(row);
            }
        }
        if (products.isEmpty()) {
            products = new ArrayList<>(csvData);
        }

        Assert.assertFalse(products.isEmpty(), "No hay productos en CSV - agrega productos");
        System.out.println("✓ Se encontraron " + products.size() + " productos\n");

        // PASO 3: INICIALIZAR PAGE OBJECTS
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // PASO 4: LOGUEAR COMO USUARIO REGISTRADO
        System.out.println("[PASO 3] Logueando como usuario registrado...");
        loginPage.open();
        Thread.sleep(500);
        loginPage.login(emailUsuario, passwordUsuario);
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(!currentUrl.contains("login"),
            "El login falló - usuario no se logueó correctamente");

        System.out.println("✓ Usuario logueado exitosamente");
        System.out.println("  URL: " + currentUrl + "\n");

        List<String> productosAgregados = new ArrayList<>();

        // PASO 5: CICLO PRINCIPAL - PROCESAR CADA PRODUCTO (LOGUEADO)
        System.out.println("[PASO 4-5] Procesando cada producto...\n");

        for (int i = 0; i < products.size(); i++) {
            Map<String,String> product = products.get(i);

            String nombreProducto = product.containsKey("Producto") ? product.get("Producto")
                    : (product.containsKey("Nombre") ? product.get("Nombre") : product.get("Product"));

            // normalizar y trim
            if (nombreProducto != null) nombreProducto = nombreProducto.trim();

            String cantidadStr = product.containsKey("Cantidad") ? product.get("Cantidad")
                    : product.get("Quantity");

            int cantidad = 1;
            if (cantidadStr != null && !cantidadStr.isEmpty()) {
                try { cantidad = Integer.parseInt(cantidadStr.trim()); } catch (NumberFormatException ignored) { cantidad = 1; }
            }

            Assert.assertNotNull(nombreProducto, "Producto sin nombre en fila " + (i + 2));

            System.out.println("Procesando: " + nombreProducto + " x" + cantidad);

            try {
                homePage.open();
                Thread.sleep(500);
                homePage.search(nombreProducto);
                Thread.sleep(800);

                Assert.assertTrue(homePage.isProductVisible(nombreProducto), "Producto no visible en resultados: " + nombreProducto);
                homePage.openFirstProduct();
                Thread.sleep(700);

                // Manejar opciones del producto si existen (select / radio / checkbox)
                handleProductOptionsIfAny();

                productPage.setQuantity(cantidad);

                // Intentar agregar con reintentos y espera explícita por alerta de éxito
                boolean added = tryAddToCartWithRetry(3, 3);
                Assert.assertTrue(added, "Producto '" + nombreProducto + "' no se agregó exitosamente");

                productosAgregados.add(nombreProducto);
                System.out.println("✓ Agregado: " + nombreProducto + "\n");

            } catch (AssertionError ae) {
                throw ae;
            } catch (Exception e) {
                System.out.println("✗ Error procesando producto '" + nombreProducto + "': " + e.getMessage());
                Assert.fail("Error procesando producto '" + nombreProducto + "': " + e.getMessage());
            }
        }

        // VERIFICACIÓN EN CARRITO: esperar tabla con fallback y normalizar nombres
        homePage.open();
        Thread.sleep(500);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean cartVisible = false;
        try {
            // intentar abrir mini-carrito o ver carrito
            cartPage.openCart();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table.table-striped")));
            cartVisible = true;
        } catch (Exception e) {
            try {
                WebElement viewCart = driver.findElement(By.cssSelector("a[href*='route=checkout/cart']"));
                viewCart.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table.table-striped")));
                cartVisible = true;
            } catch (Exception ex) {
                driver.navigate().refresh();
                try {
                    Thread.sleep(1000);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table.table-striped")));
                    cartVisible = true;
                } catch (Exception ignored) {}
            }
        }

        Assert.assertTrue(cartVisible, "La tabla del carrito no fue visible en 10 segundos");

        for (String producto : productosAgregados) {
            String esperado = producto != null ? producto.trim() : producto;
            boolean inCart = cartPage.isProductInCart(esperado);
            Assert.assertTrue(inCart, "Producto no encontrado en carrito: " + esperado);
            int qtyInCart = cartPage.getQuantityForProduct(esperado);
            Assert.assertTrue(qtyInCart >= 1, "Cantidad en carrito inválida para " + esperado);
            System.out.println("✓ Verificado en carrito: " + esperado + " (qty=" + qtyInCart + ")");
        }

        System.out.println("\n✓✓✓ TEST COMPLETADO EXITOSAMENTE ✓✓✓");
    }

    // Helper: intenta agregar al carrito con reintentos y espera de alerta de éxito
    private boolean tryAddToCartWithRetry(int maxAttempts, int waitSecondsPerAttempt) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                new ProductPage(driver).addToCart();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSecondsPerAttempt));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
                return true;
            } catch (Exception e) {
                System.out.println("  [WARN] intento " + attempt + " para addToCart falló: " + e.getMessage());
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        return false;
    }

    // Helper: selecciona valores por defecto para selects / radios / checkboxes si están presentes
    private void handleProductOptionsIfAny() {
        try {
            List<WebElement> selects = driver.findElements(By.cssSelector("select"));
            for (WebElement selElem : selects) {
                try {
                    Select sel = new Select(selElem);
                    if (sel.getOptions().size() > 1) {
                        sel.selectByIndex(1);
                    } else {
                        sel.selectByIndex(0);
                    }
                } catch (Exception ignored) {}
            }

            Map<String, Boolean> radioHandled = new HashMap<>();
            List<WebElement> radios = driver.findElements(By.cssSelector("input[type='radio']"));
            for (WebElement r : radios) {
                String name = r.getAttribute("name");
                if (name == null) continue;
                if (!radioHandled.getOrDefault(name, false)) {
                    try {
                        r.click();
                        radioHandled.put(name, true);
                    } catch (Exception ignored) {}
                }
            }

            List<WebElement> checks = driver.findElements(By.cssSelector("input[type='checkbox']"));
            for (WebElement c : checks) {
                try {
                    if (!c.isSelected()) c.click();
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.out.println("  [INFO] no se detectaron/llenaron opciones de producto: " + e.getMessage());
        }
    }
}
