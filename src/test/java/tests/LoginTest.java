package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.RegisterPage;
import utils.ExcelUtils;
import utils.ExcelWriter;

/**
 * Pruebas de inicio de sesión (Login)
 * 
 * Casos de prueba:
 * 1. Login exitoso con credenciales válidas
 * 2. Login fallido con email inválido
 * 3. Login fallido con contraseña incorrecta
 * 4. Validación de mensajes de error
 */
public class LoginTest extends BaseTest {

    /**
     * Prueba: Lectura de credenciales desde Excel y validación de login
     * 
     * Flujo:
     * 1. Leer combinaciones de email/contraseña desde LoginData.xlsx
     * 2. Para cada combinación, intentar login
     * 3. Validar resultado esperado (éxito o error)
     */
    @Test
    public void loginFromExcel() throws IOException {
        String path = "src/test/resources/inputData.xlsx";

        System.out.println("\n[LoginTest] ========== INICIANDO PRUEBAS DE LOGIN DESDE EXCEL ==========");
        
        // Si no existe el archivo o la hoja LoginData está vacía, crear datos de prueba.
        boolean needCreate = false;
        if (!Files.exists(Paths.get(path))) {
            System.out.println("[LoginTest] Archivo no encontrado. Se crearán datos de prueba...");
            needCreate = true;
        } else {
            ExcelUtils tmp = new ExcelUtils(path);
            List<Map<String,String>> existing = tmp.readSheetAsMap("LoginData");
            tmp.close();
            if (existing.isEmpty()) {
                System.out.println("[LoginTest] Sheet LoginData vacío. Se crearán datos de prueba...");
                needCreate = true;
            } else {
                System.out.println("[LoginTest] Datos encontrados en Excel. Total casos: " + existing.size());
            }
        }

        if (needCreate) {
            System.out.println("[LoginTest] Creando cuenta de prueba para validar login exitoso...");
            
            // Crear una cuenta válida para el caso 'success'
            RegisterPage rp = new RegisterPage(driver);
            rp.open();
            String generatedEmail = "test.user+" + System.currentTimeMillis() + "@example.com";
            String password = "Password123";
            System.out.println("[LoginTest] Email generado para prueba: " + generatedEmail);
            
            rp.register("Test", "User", generatedEmail, "5550001111", password);
            Assert.assertTrue(rp.isSuccess(), "No se pudo crear la cuenta de prueba.");
            System.out.println("[LoginTest] ✓ Cuenta de prueba creada exitosamente");

            List<Map<String,String>> rows = new ArrayList<>();
            
            Map<String,String> good = new LinkedHashMap<>();
            good.put("E-Mail", generatedEmail);
            good.put("Password", password);
            good.put("Expected", "success");
            good.put("Descripcion", "Login exitoso con credenciales válidas");
            rows.add(good);

            Map<String,String> bad = new LinkedHashMap<>();
            bad.put("E-Mail", "invalid.user@example.com");
            bad.put("Password", "wrongpass");
            bad.put("Expected", "error");
            bad.put("Descripcion", "Login fallido - email y contraseña inválidos");
            rows.add(bad);

            // CASO 3: Email correcto pero contraseña incorrecta
            Map<String,String> wrongPassword = new LinkedHashMap<>();
            wrongPassword.put("E-Mail", generatedEmail);
            wrongPassword.put("Password", "WrongPassword123");
            wrongPassword.put("Expected", "error");
            wrongPassword.put("Descripcion", "Login fallido - contraseña incorrecta");
            rows.add(wrongPassword);

            ExcelWriter.writeLogs(path, rows, "LoginData");
            System.out.println("[LoginTest] Datos de prueba escritos en: " + path);
        }

        // Leer datos de Excel
        ExcelUtils excel = new ExcelUtils(path);
        List<Map<String,String>> rows = excel.readSheetAsMap("LoginData");
        LoginPage lp = new LoginPage(driver);

        System.out.println("[LoginTest] Total casos a ejecutar: " + rows.size());

        // Ejecutar prueba para cada combinación de credenciales
        int caseNumber = 1;
        for (Map<String,String> r : rows) {
            String email = r.get("E-Mail");
            String password = r.get("Password");
            String expected = r.get("Expected");
            String description = r.getOrDefault("Descripcion", "Sin descripción");

            System.out.println("\n[LoginTest] ========== CASO " + caseNumber + "/" + rows.size() + " ==========");
            System.out.println("[LoginTest] Email: " + email);
            System.out.println("[LoginTest] Contraseña: " + (password != null && !password.isEmpty() ? "***" : "vacía"));
            System.out.println("[LoginTest] Resultado esperado: " + expected);
            System.out.println("[LoginTest] Descripción: " + description);

            try {
                // IMPORTANTE: Hacer logout antes de cada caso para limpiar sesión
                if (caseNumber > 1) {
                    System.out.println("[LoginTest] Limpiando sesión anterior (logout)...");
                    lp.logout();
                    Thread.sleep(1500);
                }

                // Abrir página de login
                lp.open();
                
                // Esperar a que el formulario esté listo
                Thread.sleep(1500);
                
                // Realizar login
                lp.login(email, password);
                
                // Esperar a que la página responda
                Thread.sleep(2500);
                
                // Validar resultado según lo esperado
                if ("success".equalsIgnoreCase(expected)) {
                    System.out.println("[LoginTest] Validando: Login exitoso esperado...");
                    boolean loggedIn = lp.isLoggedIn();
                    Assert.assertTrue(loggedIn, 
                        "[CASO " + caseNumber + "] Login debería ser exitoso para: " + email + 
                        ". " + description);
                    System.out.println("[LoginTest] ✓ ÉXITO - Login validado correctamente");
                } else {
                    System.out.println("[LoginTest] Validando: Error de login esperado...");
                    boolean errorShown = lp.isLoginErrorDisplayed();
                    String errorMsg = lp.getLoginErrorText();
                    Assert.assertTrue(errorShown, 
                        "[CASO " + caseNumber + "] Debería mostrarse error para: " + email + 
                        ". Mensaje recibido: " + errorMsg + ". " + description);
                    System.out.println("[LoginTest] ✓ ÉXITO - Error de login validado correctamente");
                    System.out.println("[LoginTest] Mensaje: " + errorMsg);
                }
                
            } catch (AssertionError ae) {
                System.out.println("[LoginTest] ✗ FALLO - " + ae.getMessage());
                throw ae;
            } catch (Exception e) {
                System.out.println("[LoginTest] ✗ ERROR inesperado: " + e.getMessage());
                e.printStackTrace();
                Assert.fail("[CASO " + caseNumber + "] Error inesperado: " + e.getMessage());
            }
            
            caseNumber++;
        }
        
        excel.close();
        System.out.println("\n[LoginTest] ========== TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE ==========\n");
    }
}
