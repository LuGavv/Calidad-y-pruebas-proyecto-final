package tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.WebDriverFactory;


public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp(Method method, ITestContext ctx) {
        driver = WebDriverFactory.createChrome();
        driver.manage().window().maximize();
        // pasar driver al contexto para que el listener lo use
        ctx.setAttribute("driver", driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
