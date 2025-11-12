package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utils.ExcelUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RegisterTest extends BaseTest {

    @Test
    public void registerFromExcel() throws IOException {
        ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
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
