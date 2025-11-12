package tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.WebDriverFactory;

/**
 * Clase base para todos los tests.
 * Proporciona setUp y tearDown del WebDriver.
 * setUp: Crea un nuevo ChromeDriver y lo maximiza
 * tearDown: Cierra el driver después de cada test
 */
public class BaseTest {
    protected WebDriver driver;

    /**
     * Inicialización antes de cada test.
     * 1. Crea una instancia de ChromeDriver
     * 2. Maximiza la ventana
     * 3. Pasa el driver al contexto del test para screenshots en fallos
     * @param method Método de test (usado para logs)
     * @param ctx Contexto del test (TestNG)
     */
    @BeforeMethod
    public void setUp(Method method, ITestContext ctx) {
        driver = WebDriverFactory.createChrome();
        driver.manage().window().maximize();
        // Pasar driver al contexto para que el listener lo use en screenshots
        ctx.setAttribute("driver", driver);
    }

    /**
     * Limpieza después de cada test.
     * Cierra el navegador y libera recursos.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
