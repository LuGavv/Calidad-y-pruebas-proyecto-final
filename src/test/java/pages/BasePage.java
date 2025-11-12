package pages;

import org.openqa.selenium.WebDriver;

/**
 * Clase base para todos los Page Objects.
 * Proporciona acceso al WebDriver para todas las subclases.
 * Patrón: Page Object Model
 */
public class BasePage {
    protected WebDriver driver;
    
    public BasePage(WebDriver driver) { 
        this.driver = driver; 
    }
}

