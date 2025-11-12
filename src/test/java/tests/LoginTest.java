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

public class LoginTest extends BaseTest {

    @Test
    public void loginFromExcel() throws IOException {
        String path = "src/test/resources/inputData.xlsx";

        // Si no existe el archivo o la hoja LoginData está vacía, crear datos de prueba.
        boolean needCreate = false;
        if (!Files.exists(Paths.get(path))) {
            needCreate = true;
        } else {
            ExcelUtils tmp = new ExcelUtils(path);
            List<Map<String,String>> existing = tmp.readSheetAsMap("LoginData");
            tmp.close();
            if (existing.isEmpty()) needCreate = true;
        }

        if (needCreate) {
            // Crear una cuenta válida para el caso 'success'
            RegisterPage rp = new RegisterPage(driver);
            rp.open();
            String generatedEmail = "test.user+" + System.currentTimeMillis() + "@example.com";
            String password = "Password123";
            rp.register("Test", "User", generatedEmail, "5550001111", password);
            Assert.assertTrue(rp.isSuccess(), "No se pudo crear la cuenta de prueba.");

            // Preparar filas: una esperada success y otra con credenciales inválidas
            List<Map<String,String>> rows = new ArrayList<>();
            Map<String,String> good = new LinkedHashMap<>();
            good.put("E-Mail", generatedEmail);
            good.put("Password", password);
            good.put("Expected", "success");
            rows.add(good);

            Map<String,String> bad = new LinkedHashMap<>();
            bad.put("E-Mail", "invalid.user@example.com");
            bad.put("Password", "wrongpass");
            bad.put("Expected", "error");
            rows.add(bad);

            ExcelWriter.writeLogs(path, rows, "LoginData");
        }

        ExcelUtils excel = new ExcelUtils(path);
        List<Map<String,String>> rows = excel.readSheetAsMap("LoginData");
        LoginPage lp = new LoginPage(driver);

        for (Map<String,String> r : rows) {
            System.out.println("\n[LoginTest] ========== TEST CASE: " + r.get("E-Mail") + " (Expected: " + r.get("Expected") + ") ==========");
            lp.open();
            lp.login(r.get("E-Mail"), r.get("Password"));
            
            // Pequeña pausa para que la página responda
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            
            if ("success".equalsIgnoreCase(r.get("Expected"))) {
                System.out.println("[LoginTest] Esperando login exitoso...");
                boolean loggedIn = lp.isLoggedIn();
                Assert.assertTrue(loggedIn, "Login debería ser exitoso para: " + r.get("E-Mail"));
                System.out.println("[LoginTest] ✓ Login exitoso validado para: " + r.get("E-Mail"));
            } else {
                System.out.println("[LoginTest] Esperando error de login...");
                boolean errorShown = lp.isLoginErrorDisplayed();
                String errorMsg = lp.getLoginErrorText();
                Assert.assertTrue(errorShown, "Debería mostrarse error para: " + r.get("E-Mail") + ". Mensaje: " + errorMsg);
                System.out.println("[LoginTest] ✓ Error de login validado para: " + r.get("E-Mail"));
                System.out.println("[LoginTest] Mensaje de error: " + errorMsg);
            }
        }
        excel.close();
    }
}
