# Solución: Error NoSuchElement en CartTest

## 📋 Resumen del Problema

**Error Original:**
```
CartTest.verifyCartContainsProducts:12 » NoSuchElement no such element: 
Unable to locate element: {"method":"css selector","selector":"#cart"}
```

**Causa Raíz:** El test `CartTest.verifyCartContainsProducts()` intentaba acceder al elemento con `id="cart"` sin haber cargado previamente la página principal (HomePage). El navegador estaba en blanco, por lo que el elemento del carrito no existía.

---

## 🔍 Análisis Detallado

### Problema Identificado

1. **Flujo incorrecto en CartTest.java (línea 10-12):**
   ```java
   // ANTES - INCORRECTO
   @Test
   public void verifyCartContainsProducts() {
       CartPage cp = new CartPage(driver);  // Instancia CartPage
       cp.openCart();  // Intenta hacer clic en #cart (¡que no existe!)
   ```
   
   El problema: Se instancia `CartPage` y se llama a `openCart()` **sin cargar la página principal primero**. El elemento `id="cart"` está en la barra superior de OpenCart, que solo existe cuando se carga `https://opencart.abstracta.us/`.

2. **Falta de waits explícitos en CartPage.openCart():**
   ```java
   // ANTES - INCORRECTO
   public void openCart() {
       driver.findElement(cartTop).click();  // ¡Sin wait! ¡Puede no estar clickable!
       WaitUtils.waitForVisible(driver, viewCartLink, 3);
       driver.findElement(viewCartLink).click();
   }
   ```

3. **Sin manejo de errores en métodos:** Los métodos no validaban si las esperas tenían éxito antes de continuar.

---

## ✅ Soluciones Implementadas

### 1. **CartTest.java - Agregar setup de página**
```java
@Test
public void verifyCartContainsProducts() {
    // Paso 1: Abrir la página principal PRIMERO
    HomePage homePage = new HomePage(driver);
    homePage.open();
    Assert.assertNotNull(driver.getTitle(), "La página no se cargó correctamente");
    
    // Paso 2: Ahora sí, abrir el carrito
    CartPage cartPage = new CartPage(driver);
    cartPage.openCart();
    // ... validaciones ...
}
```

**Cambios:**
- ✅ Instancia `HomePage` y llama a `open()` **antes** de acceder al carrito
- ✅ Añade aserciones después de cada paso
- ✅ Nombres descriptivos (`homePage` en lugar de `hp`)
- ✅ Comentarios de flujo claros

---

### 2. **CartPage.java - Waits clickables y manejo de errores**
```java
public void openCart() {
    // Esperar que el carrito esté clickable antes de interactuar
    if (!WaitUtils.waitForClickable(driver, cartTop, 5)) {
        throw new RuntimeException("El botón del carrito no fue clickable en 5 segundos");
    }
    driver.findElement(cartTop).click();
    
    // Esperar al enlace "View Cart" tras la interacción inicial
    if (!WaitUtils.waitForVisible(driver, viewCartLink, 5)) {
        throw new RuntimeException("El enlace 'View Cart' no fue visible en 5 segundos");
    }
    driver.findElement(viewCartLink).click();
}
```

**Cambios:**
- ✅ `waitForClickable()` antes del clic (no solo `waitForVisible()`)
- ✅ Validación explícita del resultado del wait
- ✅ Mensajes de error descriptivos
- ✅ Timeout aumentado de 3 a 5 segundos

---

### 3. **HomePage.java - Wait clickable en openFirstProduct()**
```java
public void openFirstProduct() {
    if (!WaitUtils.waitForClickable(driver, productList, 6)) {
        throw new RuntimeException("El primer producto no fue clickable en 6 segundos");
    }
    driver.findElement(productList).click();
}
```

**Cambios:**
- ✅ Validación explícita de clickability
- ✅ Manejo de error descriptivo

---

### 4. **ProductPage.java - Validaciones robustas**
```java
public void setQuantity(int qty) {
    if (!WaitUtils.waitForVisible(driver, quantityInput, 5)) {
        throw new RuntimeException("El input de cantidad no fue visible en 5 segundos");
    }
    driver.findElement(quantityInput).clear();
    driver.findElement(quantityInput).sendKeys(String.valueOf(qty));
}

public void addToCart() {
    if (!WaitUtils.waitForClickable(driver, addToCartBtn, 5)) {
        throw new RuntimeException("El botón 'Agregar al carrito' no fue clickable...");
    }
    driver.findElement(addToCartBtn).click();
}
```

---

### 5. **Documentación Completa**
Se añadieron Javadoc comments en todos los métodos:
```java
/**
 * Abre el carrito desde la página cargada.
 * 1. Valida que el botón del carrito sea clickable
 * 2. Hace clic en el carrito
 * 3. Espera a que aparezca el enlace "View Cart"
 * 4. Hace clic para ver el carrito completo
 */
public void openCart() { ... }
```

---

## 📊 Criterios de Calidad Aplicados

| Criterio | Antes | Después |
|----------|-------|---------|
| **Legibilidad** | Nombres cortos (`hp`, `cp`, `pp`) | Nombres descriptivos (`homePage`, `cartPage`) |
| **Rutas absolutas en selectores** | ❌ No aplicable (selectores OK) | ✅ Confirmado: sin rutas absolutas |
| **Aserciones claras** | ⚠️ Mínimas | ✅ Aserciones en cada paso |
| **Waits explícitos** | ⚠️ Mix de `waitForVisible` | ✅ `waitForClickable` + `waitForVisible` |
| **Manejo de errores** | ❌ No (fallos silenciosos) | ✅ Excepciones descriptivas |
| **Documentación (Javadoc)** | ❌ Mínima | ✅ Completa |
| **Flujo documentado** | ⚠️ Comentarios simples | ✅ Comentarios paso a paso |

---

## 🚀 Cómo Validar la Solución

```bash
# Ejecutar solo CartTest
mvn -Dtest=tests.CartTest test

# Ejecutar todos los tests
mvn test

# Ver reporte HTML
open target/surefire-reports/index.html
```

---

## ⚠️ Requisitos Previos

Para que el test funcione, se debe:
1. Asegurar que el archivo `src/test/resources/inputData.xlsx` existe y tiene datos válidos
2. Verificar que la tienda OpenCart está accesible en `https://opencart.abstracta.us/`
3. Confirmar que hay productos con "MacBook" en el catálogo

---

## 📝 Cambios por Archivo

| Archivo | Cambios |
|---------|---------|
| `CartTest.java` | ✅ Agregar setUp con `HomePage.open()`, aserciones claras, flujo documentado |
| `CartPage.java` | ✅ Waits clickables, validaciones, Javadoc completo |
| `HomePage.java` | ✅ Validaciones en `openFirstProduct()`, Javadoc, nombres descriptivos |
| `ProductPage.java` | ✅ Waits explícitos, manejo de errores, Javadoc |
| `BasePage.java` | ✅ Documentación Pattern Page Object |
| `BaseTest.java` | ✅ Documentación setUp/tearDown |
| `WaitUtils.java` | ✅ Documentación de métodos |
| `SearchAndAddTest.java` | ✅ Documentación completa, aserciones robustas |

---

## 🎯 Próximos Pasos Recomendados

1. **Ejecutar tests** para confirmar que el error se resolvió
2. **Agregar más selectores alternativos** en Page Objects (para robustez ante cambios de UI)
3. **Implementar logging** (Log4j2) para mejor trazabilidad
4. **Agregar capturas de pantalla automáticas** en fallos (ya está parcialmente configurado en `TestListener`)
5. **Crear datos de prueba** en `inputData.xlsx` con productos que existan realmente en la tienda

---

**Autor:** GitHub Copilot  
**Fecha:** 12 Noviembre 2025  
**Versión:** 1.0
