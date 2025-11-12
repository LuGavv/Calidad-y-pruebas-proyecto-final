package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utilidad para esperas explícitas con Selenium WebDriverWait.
 * Métodos para esperar elementos visibles, clickables y validar texto.
 * Todos los métodos retornan boolean para permitir validación sin excepciones.
 */
public class WaitUtils {

    /**
     * Espera hasta que un elemento sea visible en la página.
     * @param driver WebDriver activo
     * @param locator Localizador del elemento
     * @param seconds Segundos a esperar (máximo)
     * @return true si el elemento es visible, false si timeout
     */
    public static boolean waitForVisible(WebDriver driver, By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera hasta que un elemento sea clickable en la página.
     * @param driver WebDriver activo
     * @param locator Localizador del elemento
     * @param seconds Segundos a esperar (máximo)
     * @return true si el elemento es clickable, false si timeout
     */
    public static boolean waitForClickable(WebDriver driver, By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera hasta que un elemento contenga un texto específico.
     * @param driver WebDriver activo
     * @param locator Localizador del elemento
     * @param text Texto esperado
     * @param seconds Segundos a esperar (máximo)
     * @return true si el texto coincide, false si timeout
     */
    public static boolean waitForText(WebDriver driver, By locator, String text, int seconds) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.textToBe(locator, text));
        } catch (Exception e) {
            return false;
        }
    }
}
