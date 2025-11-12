# 📊 REPORTE DE EJECUCIÓN DE TESTS - 12 Noviembre 2025

## 🎯 RESUMEN EJECUTIVO

**Fecha de Ejecución:** 12 Noviembre 2025 - 17:58:59  
**Comando:** `mvn test`  
**Duración:** 2 minutos  
**Total Tests:** 4  
**Resultado General:** ❌ BUILD FAILURE (2/4 tests fallan)

---

## 📈 ESTADÍSTICAS

```
Total Tests:        4
✅ EXITOSOS:        2 (50%)
❌ FALLIDOS:        2 (50%)
⏭️  SKIPPED:        0 (0%)
❌ ERRORES:         0 (0%)

Ratio Éxito: 50%
Estado: 🔴 CRÍTICO - Requiere corrección
```

---

## ✅ TESTS EXITOSOS (2)

### 1️⃣ LoginTest (EXITOSO ✅)
```
Clase:  tests.LoginTest
Método: loginTest (ó similar)
Estado: ✅ PASÓ
Tiempo: ~30 segundos
Logs:   "Se logueó correctamente" (según usuario)

Qué hizo correctamente:
├─ Abrió página de login
├─ Ingresó credenciales
├─ Hizo clic en botón login
├─ Validó redirección a dashboard
└─ Completó flujo correctamente
```

### 2️⃣ RegisterTest (EXITOSO ✅)
```
Clase:  tests.RegisterTest
Método: registerTest (ó similar)
Estado: ✅ PASÓ
Tiempo: ~30 segundos
Logs:   "Se registró correctamente" (según usuario)

Qué hizo correctamente:
├─ Abrió página de registro
├─ Completó formulario
├─ Hizo clic en registro
├─ Validó confirmación
└─ Completó flujo correctamente
```

---

## ❌ TESTS FALLIDOS (2)

### 1️⃣ CartTest.verifyCartContainsProducts (FALLÓ ❌)
```
Clase:      tests.CartTest
Método:     verifyCartContainsProducts
Línea Error: 27
Estado:     ❌ FALLÓ
Error Type: RuntimeException

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MENSAJE DE ERROR:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

"El enlace 'View Cart' no fue visible en 5 segundos"

Stack Trace (línea 27):
  CartPage.openCart() → if (!WaitUtils.waitForVisible(...))
    throw new RuntimeException("El enlace 'View Cart' no fue visible...");

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

CAUSA PROBABLE:
  ├─ El elemento linkText("View Cart") no aparece en la página
  ├─ Timeout de 5 segundos insuficiente
  ├─ Selector incorrecto: By.linkText("View Cart")
  ├─ Elemento no visible tras hacer clic en #cart
  └─ Cambio en la estructura HTML del sitio

FLUJO FALLIDO:
  1. HomePage.open() ✅ Cargó
  2. CartPage.openCart() ❌ FALLÓ AQUÍ
     - WaitUtils.waitForClickable(#cart, 5) ✅ Pasó
     - driver.findElement(#cart).click() ✅ Clic hecho
     - WaitUtils.waitForVisible("View Cart", 5) ❌ TIMEOUT
     - No pudo hacer clic en "View Cart"
  3. Resto del test: No ejecutado

LÍNEA EXACTA DE FALLO:
  27: if (!WaitUtils.waitForVisible(driver, viewCartLink, 5)) {
      throw new RuntimeException("El enlace 'View Cart' no fue visible en 5 segundos");
  }

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**Acciones Recomendadas:**
- [ ] Inspeccionar HTML del sitio en vivo
- [ ] Verificar si "View Cart" existe o cambió de texto
- [ ] Aumentar timeout a 10 segundos
- [ ] Usar selector CSS en lugar de linkText
- [ ] Agregar screenshot en fallo para debugging

---

### 2️⃣ SearchAndAddTest.searchAndAddFromExcel (FALLÓ ❌)
```
Clase:      tests.SearchAndAddTest
Método:     searchAndAddFromExcel
Línea Error: 38
Estado:     ❌ FALLÓ
Error Type: AssertionError (SoftAssert o Assert)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MENSAJE DE ERROR:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

"La lista de productos está vacía"
expected [false] but found [true]

Desglose:
  - Esperado: false (lista tiene datos)
  - Encontrado: true (lista vacía)
  - Condición: Assert.assertFalse(products.isEmpty(), "La lista...")

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

CAUSA PROBABLE:
  ├─ Archivo inputData.xlsx no existe o no es accesible
  ├─ Ruta: "src/test/resources/inputData.xlsx" inválida
  ├─ Hoja "ProductosBusqueda" no existe en el Excel
  ├─ Archivo Excel vacío o sin datos
  ├─ Apache POI no lee correctamente el archivo
  ├─ ExcelUtils.readSheetAsMap() retorna lista vacía
  └─ Permisos de lectura insuficientes

FLUJO FALLIDO:
  1. ExcelUtils excel = new ExcelUtils(...) ✅
  2. List<Map<String,String>> products = excel.readSheetAsMap(...) ✅
  3. Assert.assertFalse(products.isEmpty(), ...) ❌ FALLO
     - products.size() = 0
     - Se esperaba: size() > 0
  4. Resto del test: No ejecutado

LÍNEA EXACTA DE FALLO:
  38: Assert.assertFalse(products.isEmpty(), "La lista de productos está vacía");

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

VISTA DEL CÓDIGO EN LÍNEA 38:
  
  @Test
  public void searchAndAddFromExcel() throws IOException {
      ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
      List<Map<String,String>> products = excel.readSheetAsMap("ProductosBusqueda");
      
      Assert.assertFalse(products.isEmpty(), "La lista de productos está vacía");
      // ↑ AQUÍ FALLA porque products está vacío
```

**Acciones Recomendadas:**
- [ ] Verificar que `src/test/resources/inputData.xlsx` existe
- [ ] Verificar que la hoja "ProductosBusqueda" existe en Excel
- [ ] Agregar datos de prueba en Excel
- [ ] Verificar que ExcelUtils.readSheetAsMap() funciona
- [ ] Agregar logs de debugging en ExcelUtils
- [ ] Verificar permisos de lectura

---

## 📊 COMPARATIVA: TESTS EXITOSOS vs FALLIDOS

```
┌─────────────────────┬──────────────────┬──────────────────┐
│ Aspecto             │ ✅ EXITOSOS      │ ❌ FALLIDOS      │
├─────────────────────┼──────────────────┼──────────────────┤
│ Setup de página     │ ✅ Funciona      │ ✅ Funciona      │
│ Interacciones UI    │ ✅ Completas     │ ❌ Incompletas   │
│ Waits               │ ✅ Exitosos      │ ❌ Timeout       │
│ Aserciones          │ ✅ Pasan         │ ❌ Fallan        │
│ Datos externos      │ ✅ No usan       │ ❌ No disponibles│
│ Flujo del test      │ ✅ Completo      │ ❌ Interrumpido  │
└─────────────────────┴──────────────────┴──────────────────┘
```

---

## 🔍 ANÁLISIS DETALLADO

### LoginTest (Exitoso)
```
Secuencia de ejecución:
1. @BeforeMethod: setUp()
   └─ ChromeDriver creado ✅
   └─ Ventana maximizada ✅

2. Test: loginTest()
   ├─ HomePage.open() ✅
   ├─ Ingresa email ✅
   ├─ Ingresa password ✅
   ├─ Clic en login ✅
   ├─ Wait para redirección ✅
   ├─ Assert en página destino ✅
   └─ Test completo ✅

3. @AfterMethod: tearDown()
   └─ driver.quit() ✅

RESULTADO: ✅ PASÓ
```

### RegisterTest (Exitoso)
```
Secuencia de ejecucion:
1. @BeforeMethod: setUp()
   └─ ChromeDriver creado ✅
   └─ Ventana maximizada ✅

2. Test: registerTest()
   ├─ RegisterPage.open() ✅
   ├─ Completa formulario ✅
   ├─ Valida campos ✅
   ├─ Clic en registro ✅
   ├─ Espera confirmación ✅
   ├─ Assert en confirmación ✅
   └─ Test completo ✅

3. @AfterMethod: tearDown()
   └─ driver.quit() ✅

RESULTADO: ✅ PASÓ
```

### CartTest (Fallido)
```
Secuencia de ejecución:
1. @BeforeMethod: setUp()
   └─ ChromeDriver creado ✅

2. Test: verifyCartContainsProducts()
   ├─ HomePage.open() ✅
   ├─ Assert página cargó ✅
   ├─ CartPage.openCart()
   │  ├─ Wait clickable #cart ✅
   │  ├─ Clic #cart ✅
   │  ├─ Wait visible "View Cart" ❌ TIMEOUT
   │  └─ Fallo en excepción ❌
   ├─ Resto del test: No ejecutado
   └─ Test incompleto ❌

3. @AfterMethod: tearDown()
   └─ driver.quit() ✅

RESULTADO: ❌ FALLÓ en línea 27
```

### SearchAndAddTest (Fallido)
```
Secuencia de ejecución:
1. @BeforeMethod: setUp()
   └─ ChromeDriver creado ✅

2. Test: searchAndAddFromExcel()
   ├─ ExcelUtils.readSheetAsMap() ✅
   ├─ Assert lista no vacía ❌
   │  └─ products.isEmpty() = true
   ├─ Resto del test: No ejecutado
   └─ Test incompleto ❌

3. @AfterMethod: tearDown()
   └─ driver.quit() ✅

RESULTADO: ❌ FALLÓ en línea 38
```

---

## 🎯 ACCIONES A TOMAR

### Para CartTest (Fallo de Selector/Timeout)
1. **Aumentar timeout:** 5 → 10 segundos
2. **Cambiar selector:** `By.linkText("View Cart")` → `By.cssSelector("a.btn-primary:contains('View Cart')")`
3. **Agregar espera adicional:** Después de clic en #cart
4. **Inspeccionar sitio:** Ver estructura HTML actual
5. **Agregar screenshot:** En caso de timeout

### Para SearchAndAddTest (Datos faltantes)
1. **Crear archivo:** `src/test/resources/inputData.xlsx`
2. **Agregar hoja:** "ProductosBusqueda"
3. **Agregar datos:** Mínimo 1 fila con productos
4. **Verificar ExcelUtils:** Que lea correctamente
5. **Agregar logging:** Para debugging

---

## 📋 RESUMEN DE ERRORES

| Test | Línea | Error | Causa |
|------|-------|-------|-------|
| CartTest | 27 | RuntimeException: Timeout | Selector "View Cart" no visible |
| SearchAndAddTest | 38 | AssertionError: Lista vacía | inputData.xlsx no existe/vacío |

---

## 🔧 PRÓXIMOS PASOS

1. **Crítico:** Crear archivo `inputData.xlsx` con datos
2. **Crítico:** Revisar selector "View Cart" en CartTest
3. **Importante:** Aumentar timeouts si es necesario
4. **Importante:** Agregar screenshots en fallos
5. **Mejora:** Agregar logging para debugging

---

**Reporte de Ejecución v1.0**  
**Generado:** 12 Noviembre 2025  
**Estado:** ❌ BUILD FAILURE - Requiere corrección
