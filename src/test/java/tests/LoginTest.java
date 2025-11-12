package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import utils.ExcelUtils;

public class LoginTest extends BaseTest {

    @Test
    public void loginFromExcel() throws IOException {
        ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
        List<Map<String,String>> rows = excel.readSheetAsMap("LoginData");
        LoginPage lp = new LoginPage(driver);

        for (Map<String,String> r : rows) {
            lp.open();
            lp.login(r.get("E-Mail"), r.get("Password"));
            if ("success".equalsIgnoreCase(r.get("Expected"))) {
                Assert.assertTrue(lp.isLoggedIn(), "Login debería ser exitoso para: " + r.get("E-Mail"));
            } else {
                Assert.assertTrue(lp.isLoginErrorDisplayed(), "Debería mostrarse error para: " + r.get("E-Mail"));
            }
        }
        excel.close();
    }
}
