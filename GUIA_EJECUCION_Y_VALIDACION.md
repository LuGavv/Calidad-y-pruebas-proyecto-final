# 🚀 Guía de Ejecución y Validación - Proyecto Final Calidad y Pruebas

## 📋 Resumen Ejecutivo

Se ha identificado y corregido el error `NoSuchElement` en `CartTest.verifyCartContainsProducts()`. 

**Causa:** El test no cargaba la página principal antes de acceder al elemento `id="cart"`.

**Solución:** Agregar `HomePage.open()` antes de acceder al carrito, más mejoras de robustez, documentación y aserciones claras.

---

## 📁 Documentación de Referencia

- **`SOLUCION_ERROR_CART.md`** - Solución detallada con RCA y cambios
- **`ANALISIS_TECNICO_CARTEST_ERROR.md`** - Análisis técnico profundo
- **`COMPARATIVA_ANTES_DESPUES.md`** - Comparativa código antes vs después

---

## 🧪 Cómo Ejecutar los Tests

### Prerequisitos
```powershell
# Verificar que Maven está instalado
mvn --version

# Verificar que Java está instalado
java -version
```

### Ejecutar CartTest (El test que fallaba)
```powershell
cd c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final

# Ejecutar solo CartTest
mvn -Dtest=tests.CartTest test
```

**Resultado esperado:**
```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### Ejecutar Todos los Tests
```powershell
# Ejecutar todos los tests en la suite
mvn test

# O usar TestNG directamente
mvn test -DsuiteXmlFile=testng.xml
```

**Resultado esperado:**
```
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📊 Ver Reportes

### Reporte HTML (Surefire)
```powershell
# Los reportes se generan automáticamente en:
# target/surefire-reports/index.html

# Abrir en el navegador (Windows PowerShell)
Invoke-Item target/surefire-reports/index.html

# O simplemente navegar manualmente a:
# c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final\target\surefire-reports\index.html
```

### Reporte TestNG (Más detallado)
```
target/surefire-reports/OpenCart Suite/OpenCartTests.html
```

---

## ✅ Checklist de Validación

Ejecuta este checklist después de correr los tests:

- [ ] **CartTest ejecuta sin errores NoSuchElement**
  ```powershell
  mvn -Dtest=tests.CartTest test
  ```
  Esperado: `BUILD SUCCESS`

- [ ] **HomePageOpens correctamente**
  - Confirma que el URL contiene `opencart.abstracta.us`
  
- [ ] **Elemento #cart existe después de open()**
  - En el navegador, puedes inspeccionar y ver `<div id="cart">` en la barra superior

- [ ] **Todas las aserciones pasan**
  - No hay fallos en las 4 aserciones del test

- [ ] **Archivo de logs se genera (si aplica)**
  ```
  logs.xlsx  ← Se crea después de ejecutar SearchAndAddTest
  ```

- [ ] **Código sigue criterios de calidad**
  - ✅ No hay rutas absolutas en selectores
  - ✅ Aserciones claras en cada paso
  - ✅ Comentarios documentan el flujo
  - ✅ Javadoc completo en Page Objects

---

## 🔍 Verificar Cambios Aplicados

### Archivos Modificados

```powershell
# Ver todos los cambios
git diff src/test/java/

# O abrir cada archivo en VS Code:
code src/test/java/tests/CartTest.java
code src/test/java/pages/CartPage.java
code src/test/java/pages/HomePage.java
code src/test/java/pages/ProductPage.java
```

### Cambios Principales

**CartTest.java (Línea 22-25)**
```java
// NUEVO: Cargar página principal antes
HomePage homePage = new HomePage(driver);
homePage.open();
```

**CartPage.java (Línea 16-30)**
```java
// NUEVO: Wait clickable + validación
if (!WaitUtils.waitForClickable(driver, cartTop, 5)) {
    throw new RuntimeException("...");
}
```

**HomePage.java / ProductPage.java / WaitUtils.java**
- Agregado Javadoc completo
- Mejorado manejo de waits
- Validaciones robustas

---

## 🚨 Troubleshooting

### Si aún falla NoSuchElement en #cart

**1. Verificar que la página carga:**
```java
// Agregar log en CartTest
System.out.println("URL actual: " + driver.getCurrentUrl());
System.out.println("Título: " + driver.getTitle());
```

**2. Verificar que el elemento existe en el HTML:**
```java
try {
    WebElement cartElement = driver.findElement(By.id("cart"));
    System.out.println("Carrito encontrado: " + cartElement.isDisplayed());
} catch (NoSuchElementException e) {
    System.out.println("Elemento cart NO existe en el DOM");
}
```

**3. Aumentar waits si el sitio es lento:**
```java
// En CartPage.openCart()
if (!WaitUtils.waitForClickable(driver, cartTop, 10)) {  // 10 segundos
    throw new RuntimeException("...");
}
```

### Si hay timeout en waitForClickable

**Aumentar timeout global:**
```java
// En WebDriverFactory.java (agregar al driver)
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

### Si hay NoSuchElement en selectores CSS

**Verificar selectores en la página:**
```powershell
# Abrir la página en el navegador
# Hacer F12 para DevTools
# Buscar en el Inspector: .table.table-bordered

# Si no existe, actualizar en CartPage.java
private By cartTable = By.cssSelector(".table.table-bordered");  // Cambiar si es necesario
```

---

## 📝 Resumen de Criterios de Calidad Aplicados

| Criterio | Cumplimiento |
|----------|:---|
| **Legibilidad** | ✅ Nombres descriptivos, comentarios claros |
| **Mantenibilidad** | ✅ Page Object Model, selectores centralizados |
| **Sin rutas absolutas** | ✅ Todos los selectores son relativos |
| **Aserciones claras** | ✅ Cada paso tiene validación explícita |
| **Logs/Comentarios** | ✅ Flujo documentado paso a paso |
| **Documentación** | ✅ Javadoc completo en todos los métodos |
| **Robustez** | ✅ Waits explícitos + error handling |

---

## 🎯 Próximos Pasos Recomendados

1. **Ejecutar tests completos**: `mvn test`
2. **Revisar reportes**: Abrir `target/surefire-reports/index.html`
3. **Crear datos de prueba**: Actualizar `inputData.xlsx` con productos reales
4. **Agregar logging**: Implementar Log4j2 en Page Objects
5. **Ampliar cobertura**: Agregar más tests (edge cases, validaciones)
6. **CI/CD**: Integrar con GitHub Actions o Jenkins

---

## 📞 Soporte

Si encuentras problemas:

1. Revisa **`ANALISIS_TECNICO_CARTEST_ERROR.md`** para RCA detallado
2. Revisa **`COMPARATIVA_ANTES_DESPUES.md`** para ver cambios lado a lado
3. Verifica que Maven y Java estén correctamente instalados
4. Asegúrate de que OpenCart en `https://opencart.abstracta.us/` está disponible

---

**Guía de Ejecución v1.0**  
Fecha: 12 Noviembre 2025  
Estado: ✅ LISTO PARA EJECUTAR

---

## 🏁 Quick Start (TL;DR)

```powershell
# 1. Ir al directorio del proyecto
cd c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final

# 2. Ejecutar solo CartTest (el que fallaba)
mvn -Dtest=tests.CartTest test

# 3. Ejecutar todos los tests
mvn test

# 4. Ver reporte
Invoke-Item target/surefire-reports/index.html

# ✅ Listo, debe mostrar BUILD SUCCESS
```
