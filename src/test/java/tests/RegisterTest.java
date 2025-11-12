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

import pages.RegisterPage;
import utils.ExcelUtils;
import utils.ExcelWriter;

public class RegisterTest extends BaseTest {

    @Test
    public void registerFromExcel() throws IOException {
        String path = "src/test/resources/inputData.xlsx";

        if (!Files.exists(Paths.get(path))) {
            List<Map<String,String>> rows = new ArrayList<>();
            Map<String,String> u = new LinkedHashMap<>();
            u.put("First Name", "Alberto");
            u.put("Last Name", "Perez");
            u.put("E-Mail", "Alberto.perez+" + System.currentTimeMillis() + "@example.com");
            u.put("Telephone", "5551234567");
            u.put("Password", "Password123");
            rows.add(u);
            ExcelWriter.writeLogs(path, rows, "UsuariosRegistro");
        }

        ExcelUtils excel = new ExcelUtils(path);
        List<Map<String,String>> users = excel.readSheetAsMap("UsuariosRegistro");
        RegisterPage rp = new RegisterPage(driver);

        for (Map<String,String> u : users) {
            rp.open();
            rp.register(u.get("First Name"), u.get("Last Name"), u.get("E-Mail"),
                    u.get("Telephone"), u.get("Password"));
            Assert.assertTrue(rp.isSuccess(), "Registro falló para: " + u.get("E-Mail"));
        }
        excel.close();
    }
}
