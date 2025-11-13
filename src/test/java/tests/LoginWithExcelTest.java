package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import utils.ExcelUtils;

/**
 * Test de Login con Excel: Casos válidos e inválidos.
 * 
 * Lee usuario registrado desde inputData.xlsx (UsuariosRegistro)
 * y prueba:
 * 1. Login válido con credenciales correctas
 * 2. Login inválido con contraseña incorrecta
 * 3. Login inválido con usuario no registrado
 * 
 * @author GitHub Copilot
 * @version 1.0
 */
public class LoginWithExcelTest extends BaseTest {

    private static final String EXCEL_PATH = "src/test/resources/inputData.xlsx";
    private static final String SHEET_NAME = "UsuariosRegistro";

    /**
     * TEST 1: Login Válido
     * 
     * Reads registered user from inputData.xlsx
     * and performs successful login with correct credentials.
     */
    @Test(priority = 1, description = "Login Válido - Usuario Registrado desde Excel")
    public void testLoginValidoDesdeExcel() throws IOException {
        System.out.println("\n========================================");
        System.out.println("TEST 1: LOGIN VÁLIDO - USUARIO DEL EXCEL");
        System.out.println("========================================\n");

        try {
            // PASO 1: LEER USUARIO DEL EXCEL
            System.out.println("[PASO 1] Leyendo usuario registrado de Excel...");
            ExcelUtils excel = new ExcelUtils(EXCEL_PATH);
            List<Map<String, String>> usuarios = excel.readSheetAsMap(SHEET_NAME);

            Assert.assertNotNull(usuarios, "No se pudo leer la hoja " + SHEET_NAME);
            Assert.assertFalse(usuarios.isEmpty(), "La hoja " + SHEET_NAME + " está vacía");

            Map<String, String> usuarioRegistrado = usuarios.get(0);
            String email = usuarioRegistrado.get("E-Mail");
            String password = usuarioRegistrado.get("Password");

            System.out.println("✓ Usuario registrado encontrado");
            System.out.println("  Email: " + email);
            System.out.println("  Password: " + "*".repeat(password.length()) + "\n");

            // PASO 2: ABRIR PÁGINA DE LOGIN
            System.out.println("[PASO 2] Abriendo página de login...");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.open();
            Thread.sleep(500);
            String loginUrl = driver.getCurrentUrl();
            Assert.assertTrue(loginUrl.contains("login"), "No se abrió página de login correctamente");
            System.out.println("✓ Página de login abierta\n");

            // PASO 3: INGRESANDO CREDENCIALES VÁLIDAS
            System.out.println("[PASO 3] Ingresando credenciales válidas...");
            System.out.println("  Email: " + email);
            System.out.println("  Password: [correcta]\n");
            loginPage.login(email, password);
            Thread.sleep(1500);

            // PASO 4: VALIDAR LOGIN EXITOSO
            System.out.println("[PASO 4] Validando que login fue exitoso...");
            String currentUrl = driver.getCurrentUrl();
            System.out.println("  URL actual: " + currentUrl);

            Assert.assertNotNull(currentUrl, "URL no puede ser nula");
            Assert.assertFalse(currentUrl.contains("login"),
                    "Login falló - sigue en página login. URL: " + currentUrl);

            System.out.println("✓ Usuario logueado exitosamente");
            System.out.println("✓ Navegó fuera de página login\n");

            System.out.println("========================================");
            System.out.println("✅ TEST 1 PASÓ: LOGIN VÁLIDO EXITOSO");
            System.out.println("========================================\n");

            excel.close();

        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            Assert.fail("TEST 1 FALLÓ: " + e.getMessage());
        }
    }

    /**
     * TEST 2: Login Inválido - Contraseña Incorrecta
     * 
     * Reads registered user from Excel but uses wrong password.
     * Login should be rejected.
     */
    @Test(priority = 2, description = "Login Inválido - Contraseña Incorrecta")
    public void testLoginContraseñaIncorrecta() throws IOException {
        System.out.println("\n========================================");
        System.out.println("TEST 2: LOGIN INVÁLIDO - CONTRASEÑA INCORRECTA");
        System.out.println("========================================\n");

        try {
            // PASO 1: LEER USUARIO DEL EXCEL
            System.out.println("[PASO 1] Leyendo usuario registrado de Excel...");
            ExcelUtils excel = new ExcelUtils(EXCEL_PATH);
            List<Map<String, String>> usuarios = excel.readSheetAsMap(SHEET_NAME);

            Assert.assertNotNull(usuarios, "No se pudo leer la hoja " + SHEET_NAME);
            Assert.assertFalse(usuarios.isEmpty(), "La hoja " + SHEET_NAME + " está vacía");

            Map<String, String> usuarioRegistrado = usuarios.get(0);
            String email = usuarioRegistrado.get("E-Mail");
            String contraseñaIncorrecta = "WrongPassword123!";

            System.out.println("✓ Usuario registrado encontrado");
            System.out.println("  Email: " + email + " [CORRECTO]");
            System.out.println("  Password: " + contraseñaIncorrecta + " [INCORRECTO]\n");

            // PASO 2: ABRIR PÁGINA DE LOGIN
            System.out.println("[PASO 2] Abriendo página de login...");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.open();
            Thread.sleep(500);
            System.out.println("✓ Página de login abierta\n");

            // PASO 3: INGRESANDO CREDENCIALES INVÁLIDAS
            System.out.println("[PASO 3] Ingresando email correcto con contraseña INCORRECTA...");
            System.out.println("  Email: " + email + " [CORRECTO]");
            System.out.println("  Password: " + contraseñaIncorrecta + " [INCORRECTO]\n");
            loginPage.login(email, contraseñaIncorrecta);
            Thread.sleep(1500);

            // PASO 4: VALIDAR QUE LOGIN FUE RECHAZADO
            System.out.println("[PASO 4] Validando que login fue rechazado...");
            String currentUrl = driver.getCurrentUrl();
            System.out.println("  URL actual: " + currentUrl);

            Assert.assertNotNull(currentUrl, "URL no puede ser nula");
            Assert.assertTrue(currentUrl.contains("login"),
                    "Login debería haber sido rechazado, pero URL cambió a: " + currentUrl);

            System.out.println("✓ Login fue rechazado correctamente");
            System.out.println("✓ Permanece en página login\n");

            System.out.println("========================================");
            System.out.println("✅ TEST 2 PASÓ: LOGIN INVÁLIDO (CONTRASEÑA) CORRECTAMENTE RECHAZADO");
            System.out.println("========================================\n");

            excel.close();

        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            Assert.fail("TEST 2 FALLÓ: " + e.getMessage());
        }
    }

    /**
     * TEST 3: Login Inválido - Usuario No Registrado
     * 
     * Tries to login with a non-existent user email.
     * Login should be rejected.
     */
    @Test(priority = 3, description = "Login Inválido - Usuario No Registrado")
    public void testLoginUsuarioNoRegistrado() throws IOException {
        System.out.println("\n========================================");
        System.out.println("TEST 3: LOGIN INVÁLIDO - USUARIO NO REGISTRADO");
        System.out.println("========================================\n");

        try {
            // PASO 1: PREPARAR USUARIO NO REGISTRADO
            System.out.println("[PASO 1] Preparando usuario que NO está registrado...");
            String emailNoRegistrado = "usuarionoexiste" + System.currentTimeMillis() + "@example.com";
            String password = "SomePassword123!";

            System.out.println("✓ Usuario no registrado preparado");
            System.out.println("  Email: " + emailNoRegistrado + " [NO EXISTE]");
            System.out.println("  Password: " + "*".repeat(password.length()) + "\n");

            // PASO 2: ABRIR PÁGINA DE LOGIN
            System.out.println("[PASO 2] Abriendo página de login...");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.open();
            Thread.sleep(500);
            System.out.println("✓ Página de login abierta\n");

            // PASO 3: INGRESANDO CREDENCIALES DE USUARIO NO REGISTRADO
            System.out.println("[PASO 3] Intentando login con usuario NO REGISTRADO...");
            System.out.println("  Email: " + emailNoRegistrado + " [NO EXISTE]");
            System.out.println("  Password: [alguna contraseña]\n");
            loginPage.login(emailNoRegistrado, password);
            Thread.sleep(1500);

            // PASO 4: VALIDAR QUE LOGIN FUE RECHAZADO
            System.out.println("[PASO 4] Validando que login fue rechazado (usuario no existe)...");
            String currentUrl = driver.getCurrentUrl();
            System.out.println("  URL actual: " + currentUrl);

            Assert.assertNotNull(currentUrl, "URL no puede ser nula");
            Assert.assertTrue(currentUrl.contains("login"),
                    "Login debería haber sido rechazado, pero URL cambió a: " + currentUrl);

            System.out.println("✓ Login fue rechazado correctamente");
            System.out.println("✓ Permanece en página login\n");

            System.out.println("========================================");
            System.out.println("✅ TEST 3 PASÓ: LOGIN INVÁLIDO (USUARIO NO EXISTE) CORRECTAMENTE RECHAZADO");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            Assert.fail("TEST 3 FALLÓ: " + e.getMessage());
        }
    }
}
