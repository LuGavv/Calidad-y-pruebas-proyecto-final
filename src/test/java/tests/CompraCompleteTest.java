package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import utils.ExcelUtils;
import java.io.IOException;
import java.util.*;

/**
 * Test de Compra Completa: Login + Búsqueda + Agregación + Verificación en Carrito.
 * 
 * Flujo:
 * 1. Lee usuario registrado desde inputData.xlsx (UsuariosRegistro)
 * 2. Lee productos desde inputData.xlsx (Productos)
 * 3. Loguea como usuario registrado
 * 4. Para cada producto:
 *    - Busca en OpenCart
 *    - Verifica en resultados
 *    - Abre producto
 *    - Establece cantidad
 *    - Agrega al carrito
 *    - Valida éxito
 * 5. Verifica que TODOS los productos están en el carrito del usuario logueado
 * 6. Valida cantidades
 * 
 * @author GitHub Copilot
 * @version 2.0
 */
public class CompraCompleteTest extends BaseTest {

    /**
     * Test Principal: Compra completa de productos (Usuario Registrado)
     * Lee Excel, loguea usuario, busca, agrega y verifica en carrito
     */
    @Test(priority = 1)
    public void testCompraCompleta() throws IOException {
        System.out.println("\n========================================");
        System.out.println("INICIANDO TEST: COMPRA COMPLETA (LOGUEADO)");
        System.out.println("========================================\n");
        
        String excelPath = "src/test/resources/inputData.xlsx";
        
        // PASO 1: LEER USUARIO REGISTRADO DESDE EXCEL
        System.out.println("[PASO 1] Leyendo usuario registrado desde Excel...");
        ExcelUtils excelUtils = new ExcelUtils(excelPath);
        List<Map<String,String>> usuarios = excelUtils.readSheetAsMap("UsuariosRegistro");
        
        Assert.assertNotNull(usuarios, "No se pudo leer la hoja UsuariosRegistro");
        Assert.assertFalse(usuarios.isEmpty(), "La hoja UsuariosRegistro está vacía");
        
        Map<String,String> usuarioRegistrado = usuarios.get(0);
        String emailUsuario = usuarioRegistrado.get("E-Mail");
        String passwordUsuario = usuarioRegistrado.get("Password");
        
        System.out.println("✓ Usuario registrado encontrado");
        System.out.println("  Email: " + emailUsuario + "\n");
        
        // PASO 2: LEER PRODUCTOS DESDE EXCEL
        System.out.println("[PASO 2] Leyendo productos desde Excel...");
        List<Map<String,String>> products = excelUtils.readSheetAsMap("Productos");
        
        Assert.assertNotNull(products, "No se pudo leer la hoja Productos");
        Assert.assertFalse(products.isEmpty(), "La hoja Productos está vacía - agrega productos");
        
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
            String categoria = product.get("Categoria");
            String subCategoria = product.get("SubCategoria");
            String nombreProducto = product.get("Producto");
            int cantidad = Integer.parseInt(product.getOrDefault("Cantidad", "1"));
            
            System.out.println("─────────────────────────────────────");
            System.out.println("PRODUCTO #" + (i + 1) + ": " + nombreProducto);
            System.out.println("Categoría: " + categoria + " > " + subCategoria);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("─────────────────────────────────────");
            
            try {
                // 2a. ABRIR PÁGINA PRINCIPAL
                System.out.println("  1. Abriendo OpenCart...");
                homePage.open();
                Assert.assertNotNull(driver.getTitle(), "Homepage no cargó");
                System.out.println("     ✓ Página cargada\n");
                
                // 2b. BUSCAR PRODUCTO
                System.out.println("  2. Buscando producto: '" + nombreProducto + "'...");
                homePage.search(nombreProducto);
                Thread.sleep(1000); // Espera a que carguen resultados
                System.out.println("     ✓ Búsqueda realizada\n");
                
                // 2c. VERIFICAR EN RESULTADOS
                System.out.println("  3. Verificando que aparece en resultados...");
                boolean productVisible = homePage.isProductVisible(nombreProducto);
                Assert.assertTrue(productVisible, 
                    "Producto '" + nombreProducto + "' no aparece en resultados");
                System.out.println("     ✓ Producto encontrado en resultados\n");
                
                // 2d. ABRIR PRODUCTO
                System.out.println("  4. Abriendo página de producto...");
                homePage.openFirstProduct();
                Thread.sleep(500);
                System.out.println("     ✓ Página de producto abierta\n");
                
                // 2e. ESTABLECER CANTIDAD
                System.out.println("  5. Estableciendo cantidad: " + cantidad);
                productPage.setQuantity(cantidad);
                System.out.println("     ✓ Cantidad establecida\n");
                
                // 2f. AGREGAR AL CARRITO
                System.out.println("  6. Agregando al carrito...");
                productPage.addToCart();
                Thread.sleep(1000);
                System.out.println("     ✓ Botón agregado clickeado\n");
                
                // 2g. VALIDAR ÉXITO
                System.out.println("  7. Validando agregación exitosa...");
                boolean addedSuccessfully = productPage.isAddedSuccessfully();
                Assert.assertTrue(addedSuccessfully, 
                    "Producto '" + nombreProducto + "' no se agregó exitosamente");
                System.out.println("     ✓ PRODUCTO AGREGADO EXITOSAMENTE\n");
                
                productosAgregados.add(nombreProducto);
                
                System.out.println("✓ PRODUCTO #" + (i + 1) + " COMPLETADO\n");
                
            } catch (Exception e) {
                System.out.println("     ✗ ERROR en producto: " + e.getMessage());
                Assert.fail("Error procesando producto '" + nombreProducto + "': " + e.getMessage());
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("FASE 1 COMPLETA: Todos los productos agregados");
        System.out.println("========================================\n");
        
        // PASO 4: VERIFICACIÓN EN CARRITO
        System.out.println("[PASO 5] Verificando productos en carrito...\n");
        
        try {
            // 3a. ABRIR CARRITO
            System.out.println("  1. Abriendo página del carrito...");
            homePage.open();
            Thread.sleep(500);
            cartPage.openCart();
            Thread.sleep(1000);
            Assert.assertTrue(driver.getCurrentUrl().contains("cart"), 
                "No se navegó a la página del carrito");
            System.out.println("     ✓ Carrito abierto\n");
            
            // 3b. VERIFICAR CADA PRODUCTO
            System.out.println("  2. Verificando presencia de productos:\n");
            for (String producto : productosAgregados) {
                System.out.println("     • Verificando '" + producto + "'...");
                boolean enCarrito = cartPage.isProductInCart(producto);
                Assert.assertTrue(enCarrito, 
                    "Producto '" + producto + "' NO ESTÁ en el carrito");
                
                int cantidadEnCarrito = cartPage.getQuantityForProduct(producto);
                Assert.assertTrue(cantidadEnCarrito >= 1, 
                    "Cantidad de '" + producto + "' debe ser >= 1, pero es: " + cantidadEnCarrito);
                
                System.out.println("       ✓ Encontrado (Cantidad: " + cantidadEnCarrito + ")\n");
            }
            
            System.out.println("========================================");
            System.out.println("FASE 2 COMPLETA: Todos los productos verificados");
            System.out.println("========================================\n");
            
        } catch (Exception e) {
            System.out.println("     ✗ ERROR en verificación: " + e.getMessage());
            Assert.fail("Error en verificación de carrito: " + e.getMessage());
        }
        
        System.out.println("\n✓✓✓ TEST COMPLETADO EXITOSAMENTE ✓✓✓");
        System.out.println("Productos procesados: " + productosAgregados.size());
        System.out.println("Todos verificados en carrito\n");
    }
}
