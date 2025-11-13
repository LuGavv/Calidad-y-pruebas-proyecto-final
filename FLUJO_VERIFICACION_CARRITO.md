# 📋 FLUJO: VERIFICACIÓN DE PRODUCTOS EN EL CARRITO

**Proyecto:** Calidad y Pruebas - Proyecto Final  
**Componentes:** SearchAndAddTest → CartPage → Verificación  
**Estado:** ✅ IMPLEMENTADO  
**Última actualización:** 12 Noviembre 2025

---

## 📊 TABLA DE CONTENIDOS

1. [Visión General](#visión-general)
2. [Arquitectura del Flujo](#arquitectura)
3. [Flujo Paso a Paso](#flujo-pasos)
4. [Métodos Utilizados](#métodos)
5. [Localizadores CSS/XPath](#localizadores)
6. [Manejo de Esperas](#esperas)
7. [Validaciones (Assertions)](#validaciones)
8. [Casos de Uso](#casos-uso)
9. [Datos de Prueba (Excel)](#datos-excel)
10. [Debugging y Troubleshooting](#debugging)

---

## 🎯 VISIÓN GENERAL {#visión-general}

El flujo de verificación de productos en el carrito implementa el siguiente ciclo:

```
Excel (inputData.xlsx)
        ↓
    Lectura de Productos
        ↓
SearchAndAddTest (Ciclo FOR)
        ↓
    1. Abrir HomePage
    2. Buscar Producto (search())
    3. Verificar Visibilidad (isProductVisible())
    4. Abrir Producto (openFirstProduct())
    5. Establecer Cantidad (setQuantity())
    6. Agregar al Carrito (addToCart())
    7. Validar Adición (isAddedSuccessfully())
        ↓
CartTest (Verificación Final)
        ↓
    1. Abrir HomePage
    2. Abrir Carrito (openCart())
    3. Verificar Producto en Carrito (isProductInCart())
    4. Validar Cantidad (getQuantityForProduct())
        ↓
    ✅ ASSERTION PASS
```

---

## 🏗️ ARQUITECTURA DEL FLUJO {#arquitectura}

### Componentes

```
┌─────────────────────────────────────────────────────┐
│              SearchAndAddTest.java                  │
│  (Lee Excel, busca productos, los agrega al carro) │
└──────────────┬──────────────────────────────────────┘
               │
    ┌──────────┴──────────┐
    ↓                     ↓
┌─────────────┐    ┌──────────────┐
│ HomePage    │    │ ProductPage  │
│ · open()    │    │ · setQty()   │
│ · search()  │    │ · addCart()  │
│ · isVis()   │    │ · isSuccess()│
└─────────────┘    └──────────────┘
    ↓                     ↑
    └─────────────────────┘
               ↓
    Producto Agregado al Carrito
               ↓
┌─────────────────────────────────────────────────────┐
│              CartTest.java                          │
│  (Verifica que productos estén en el carrito)      │
└──────────────┬──────────────────────────────────────┘
               │
        ┌──────┴──────┐
        ↓             ↓
   HomePage      CartPage
   · open()      · openCart()
                 · isProductInCart()
                 · getQuantityForProduct()
               ↓
    ✅ Validaciones Completadas
```

---

## 🔄 FLUJO PASO A PASO {#flujo-pasos}

### FASE 1: LECTURA DE DATOS (SearchAndAddTest)

```java
// 1. Abrir archivo Excel
ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");

// 2. Leer hoja "ProductosBusqueda"
List<Map<String,String>> products = excel.readSheetAsMap("ProductosBusqueda");

// 3. Validaciones iniciales
Assert.assertNotNull(products, "Error al leer Excel");
if (products.isEmpty()) return; // Manejo de Excel vacío
```

**Estructura de Datos Esperada:**

| Categoria | SubCategoria | Producto | Cantidad |
|-----------|--------------|----------|----------|
| Software | Office | MacBook | 1 |
| Software | Databases | Microsoft SQL Server | 1 |
| Phones & PDAs | Phones | iPhone | 2 |

---

### FASE 2: CICLO DE PROCESAMIENTO (SearchAndAddTest)

```java
for (Map<String,String> product : products) {
    String productName = product.get("Producto");
    int quantity = Integer.parseInt(product.get("Cantidad"));
    
    // PASO 1: Abrir página principal
    homePage.open();
    
    // PASO 2: Buscar producto
    homePage.search(productName);
    
    // PASO 3: Verificar que aparece en resultados
    boolean productVisible = homePage.isProductVisible(productName);
    Assert.assertTrue(productVisible, "Producto no visible: " + productName);
    
    // PASO 4: Abrir producto
    homePage.openFirstProduct();
    
    // PASO 5: Establecer cantidad
    productPage.setQuantity(quantity);
    
    // PASO 6: Agregar al carrito
    productPage.addToCart();
    
    // PASO 7: Validar agregación exitosa
    boolean addedSuccessfully = productPage.isAddedSuccessfully();
    Assert.assertTrue(addedSuccessfully, "Producto no agregado: " + productName);
    
    // PASO 8: Registrar resultado en log
    logRow.put("Producto", productName);
    logRow.put("Cantidad", String.valueOf(quantity));
    logRow.put("Added", String.valueOf(addedSuccessfully));
}
```

---

### FASE 3: VERIFICACIÓN EN CARRITO (CartTest)

```java
// PASO 1: Abrir página principal
HomePage homePage = new HomePage(driver);
homePage.open();

// PASO 2: Abrir carrito
CartPage cartPage = new CartPage(driver);
cartPage.openCart();

// PASO 3: Verificar que producto está en carrito
boolean productExists = cartPage.isProductInCart("MacBook");
Assert.assertTrue(productExists, "MacBook no está en el carrito");

// PASO 4: Validar cantidad
int quantity = cartPage.getQuantityForProduct("MacBook");
Assert.assertTrue(quantity >= 1, "Cantidad debe ser >= 1, pero es: " + quantity);
```

---

## 🔧 MÉTODOS UTILIZADOS {#métodos}

### HomePage Methods

```java
/**
 * 1. open() - Abre la página principal
 * URL: https://opencart.abstracta.us/
 */
public void open() {
    driver.get("https://opencart.abstracta.us/");
}

/**
 * 2. search(String term) - Busca un producto
 * Pasos:
 *   - Limpia input de búsqueda
 *   - Escribe término
 *   - Hace clic en buscar
 */
public void search(String term) {
    driver.findElement(searchInput).clear();
    driver.findElement(searchInput).sendKeys(term);
    driver.findElement(searchBtn).click();
}

/**
 * 3. isProductVisible(String name) - Verifica si producto aparece
 * Retorna: boolean (true/false)
 */
public boolean isProductVisible(String expectedProductName) {
    try {
        WaitUtils.waitForVisible(driver, productList, 6);
        String name = driver.findElement(productList).getText();
        return name.toLowerCase().contains(expectedProductName.toLowerCase());
    } catch (Exception e) {
        return false;
    }
}

/**
 * 4. openFirstProduct() - Abre el primer producto en resultados
 * Lanza: RuntimeException si no es clickable
 */
public void openFirstProduct() {
    if (!WaitUtils.waitForClickable(driver, productList, 6)) {
        throw new RuntimeException("El primer producto no fue clickable en 6 segundos");
    }
    driver.findElement(productList).click();
}
```

### ProductPage Methods

```java
/**
 * 1. setQuantity(int qty) - Establece cantidad de producto
 * Pasos:
 *   - Espera que input sea visible
 *   - Limpia contenido
 *   - Escribe nueva cantidad
 */
public void setQuantity(int qty) {
    if (!WaitUtils.waitForVisible(driver, quantityInput, 5)) {
        throw new RuntimeException("Input de cantidad no visible en 5 segundos");
    }
    driver.findElement(quantityInput).clear();
    driver.findElement(quantityInput).sendKeys(String.valueOf(qty));
}

/**
 * 2. addToCart() - Agrega producto al carrito
 * Espera que botón sea clickable antes de hacer clic
 */
public void addToCart() {
    if (!WaitUtils.waitForClickable(driver, addToCartBtn, 5)) {
        throw new RuntimeException("Botón 'Agregar al carrito' no clickable");
    }
    driver.findElement(addToCartBtn).click();
}

/**
 * 3. isAddedSuccessfully() - Verifica si se agregó exitosamente
 * Retorna: boolean (true si alerta de éxito visible)
 */
public boolean isAddedSuccessfully() {
    return WaitUtils.waitForVisible(driver, successAlert, 6);
}
```

### CartPage Methods

```java
/**
 * 1. openCart() - Abre el carrito desde página principal
 * Pasos:
 *   - Hace clic en botón carrito (#cart)
 *   - Espera enlace "View Cart"
 *   - Hace clic en "View Cart"
 */
public void openCart() {
    if (!WaitUtils.waitForClickable(driver, cartTop, 5)) {
        throw new RuntimeException("Botón carrito no clickable en 5 segundos");
    }
    driver.findElement(cartTop).click();
    
    boolean viewCartVisible = WaitUtils.waitForVisible(driver, viewCartLink, 10);
    if (!viewCartVisible) {
        viewCartVisible = WaitUtils.waitForVisible(driver, viewCartLinkAlt, 10);
        if (!viewCartVisible) {
            throw new RuntimeException("Enlace 'View Cart' no visible en 20 segundos");
        }
        driver.findElement(viewCartLinkAlt).click();
    } else {
        driver.findElement(viewCartLink).click();
    }
}

/**
 * 2. isProductInCart(String name) - Verifica si producto está en carrito
 * Retorna: boolean (true si nombre aparece en tabla)
 */
public boolean isProductInCart(String productName) {
    if (!WaitUtils.waitForVisible(driver, cartTable, 5)) {
        throw new RuntimeException("Tabla carrito no visible en 5 segundos");
    }
    return driver.getPageSource().toLowerCase()
        .contains(productName.toLowerCase());
}

/**
 * 3. getQuantityForProduct(String name) - Obtiene cantidad en carrito
 * Retorna: int (cantidad del producto, 0 si no encontrado)
 */
public int getQuantityForProduct(String productName) {
    if (!WaitUtils.waitForVisible(driver, cartTable, 5)) {
        throw new RuntimeException("Tabla carrito no visible");
    }
    for (var row : driver.findElements(cartRows)) {
        String text = row.getText();
        if (text.toLowerCase().contains(productName.toLowerCase())) {
            String[] cells = text.split("\\r?\\n");
            for (String c : cells) {
                if (c.matches("\\d+")) {
                    return Integer.parseInt(c);
                }
            }
        }
    }
    return 0;
}
```

---

## 🎯 LOCALIZADORES CSS/XPATH {#localizadores}

### HomePage Locators

```java
private By searchInput = By.name("search");              // INPUT de búsqueda
private By searchBtn = By.cssSelector("button.btn.btn-default.btn-lg");  // BOTÓN buscar
private By productList = By.cssSelector(".product-thumb h4 a");          // ENLACE producto
```

**Explicación:**
- `searchInput`: Campo de texto con nombre "search"
- `searchBtn`: Botón grande con clases `btn btn-default btn-lg`
- `productList`: Enlaces en tarjetas de producto (`.product-thumb`)

---

### ProductPage Locators

```java
private By quantityInput = By.id("input-quantity");     // INPUT cantidad
private By addToCartBtn = By.id("button-cart");         // BOTÓN agregar carrito
private By successAlert = By.cssSelector(".alert.alert-success");  // ALERTA éxito
```

**Explicación:**
- `quantityInput`: Input con ID `input-quantity`
- `addToCartBtn`: Botón con ID `button-cart`
- `successAlert`: DIV con clases `alert alert-success`

---

### CartPage Locators

```java
private By cartTop = By.id("cart");                     // BOTÓN carrito (arriba)
private By viewCartLink = By.linkText("View Cart");     // ENLACE por texto
private By viewCartLinkAlt = By.cssSelector("a[href*='cart']");  // ENLACE alternativo
private By cartTable = By.cssSelector(".table.table-bordered");   // TABLA carrito
private By cartRows = By.cssSelector(".table.table-bordered tbody tr");  // FILAS tabla
```

**Explicación:**
- `cartTop`: Botón carrito en esquina superior derecha
- `viewCartLink`: Enlace "View Cart" en dropdown (linkText)
- `viewCartLinkAlt`: Fallback CSS para enlace View Cart
- `cartTable`: Tabla principal del carrito
- `cartRows`: Filas de productos en tabla

---

## ⏱️ MANEJO DE ESPERAS {#esperas}

### Tipos de Esperas Implementadas

#### 1. **Explicit Wait - Visible**
```java
WaitUtils.waitForVisible(driver, By locator, int seconds)
```
- Espera hasta que elemento sea **visible en el DOM**
- Timeout: 5-10 segundos
- Retorna: `boolean` (true si visible, false si timeout)
- Usado en: Búsqueda, validación de tabla

#### 2. **Explicit Wait - Clickable**
```java
WaitUtils.waitForClickable(driver, By locator, int seconds)
```
- Espera hasta que elemento sea **clickable (visible + enabled)**
- Timeout: 5-6 segundos
- Retorna: `boolean` (true si clickable, false si timeout)
- Usado en: Clicks de botones, enlaces

#### 3. **Explicit Wait - Text**
```java
WaitUtils.waitForText(driver, By locator, String text, int seconds)
```
- Espera hasta que elemento contenga **texto específico**
- Timeout: variable
- Retorna: `boolean` (true si texto coincide, false si timeout)
- Usado en: Validaciones de texto

### Configuración de Timeouts

```
Búsqueda de Productos:    6 segundos
Cantidad/Input:           5 segundos
Botón Agregar Carrito:    5 segundos
Alerta de Éxito:          6 segundos
Botón Carrito:            5 segundos
Enlace View Cart:         10 segundos (20 con fallback)
Tabla Carrito:            5 segundos
```

---

## ✅ VALIDACIONES (ASSERTIONS) {#validaciones}

### SearchAndAddTest Assertions

```java
// 1. Validar lectura de Excel
Assert.assertNotNull(products, "No se pudieron leer los productos del Excel");

// 2. Validar que página cargó
Assert.assertNotNull(productName, "El nombre del producto no puede ser nulo");

// 3. Validar producto visible
Assert.assertTrue(productVisible, 
    "Producto no visible: " + productName);

// 4. Validar agregación exitosa
Assert.assertTrue(addedSuccessfully, 
    "Producto no agregado exitosamente: " + productName);
```

### CartTest Assertions

```java
// 1. Validar carga de página principal
Assert.assertNotNull(driver.getTitle(), 
    "La página no se cargó correctamente");

// 2. Validar navegación a carrito
Assert.assertTrue(driver.getCurrentUrl().contains("cart"), 
    "No se navegó a la página del carrito");

// 3. Validar producto en carrito
Assert.assertTrue(productExists, 
    "MacBook no está en el carrito");

// 4. Validar cantidad
Assert.assertTrue(quantity >= 1, 
    "Cantidad de MacBook debe ser >= 1, pero es: " + quantity);
```

---

## 💼 CASOS DE USO {#casos-uso}

### Caso 1: Agregar UN Producto desde Excel

**Entrada:**
```
Categoria: Software
SubCategoria: Office
Producto: MacBook
Cantidad: 1
```

**Ejecución:**
```
1. SearchAndAddTest lee producto
2. Abre HomePage
3. Busca "MacBook"
4. Verifica que aparece
5. Abre primer resultado
6. Establece cantidad = 1
7. Hace clic "Add to Cart"
8. Valida alerta de éxito
9. Registra en Excel (logs.xlsx)
```

**Validaciones:**
```
✓ Producto es visible en búsqueda
✓ Carrito suma +1 producto
✓ Cantidad en carrito es 1
✓ CartTest verifica presencia
```

---

### Caso 2: Agregar MÚLTIPLES Productos (Ciclo)

**Entrada (Excel):**
```
Fila 1: MacBook, 1
Fila 2: Microsoft SQL Server, 1
Fila 3: iPhone, 2
```

**Ejecución:**
```
ITERACIÓN 1:
  - Buscar MacBook
  - Cantidad: 1
  - Agregar al carrito
  - ✓ Éxito

ITERACIÓN 2:
  - Buscar Microsoft SQL Server
  - Cantidad: 1
  - Agregar al carrito
  - ✓ Éxito

ITERACIÓN 3:
  - Buscar iPhone
  - Cantidad: 2
  - Agregar al carrito
  - ✓ Éxito
```

**Estado Final del Carrito:**
```
Carrito contiene:
  - MacBook (Qty: 1)
  - Microsoft SQL Server (Qty: 1)
  - iPhone (Qty: 2)
Total: 3 productos, 4 unidades
```

**Validaciones:**
```
✓ CartTest verifica MacBook presente
✓ CartTest verifica cantidad MacBook ≥ 1
✓ logs.xlsx contiene 3 filas de resultados
✓ Todas las filas muestran "Added: true"
```

---

## 📊 DATOS DE PRUEBA (EXCEL) {#datos-excel}

### Estructura Esperada: inputData.xlsx

**Hoja:** ProductosBusqueda

**Columnas:**
1. `Categoria` - Categoría del producto
2. `SubCategoria` - Subcategoría
3. `Producto` - Nombre exacto a buscar
4. `Cantidad` - Cantidad a agregar

**Ejemplo de Datos:**

| Categoria | SubCategoria | Producto | Cantidad |
|-----------|--------------|----------|----------|
| Software | Office | MacBook | 1 |
| Software | Databases | Microsoft SQL Server | 1 |
| Phones & PDAs | Phones | iPhone | 2 |

### Requisitos de Datos

```
✓ Mínimo 1 fila de datos (además de headers)
✓ Nombres de productos EXACTOS (matchear con sitio)
✓ Cantidades como números (1, 2, 3, etc.)
✓ Caracteres especiales permitidos (& en "Phones & PDAs")
```

### Ubicación del Archivo

```
c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final\
    src\test\resources\
        inputData.xlsx
```

---

## 🐛 DEBUGGING Y TROUBLESHOOTING {#debugging}

### Problema 1: "La lista de productos está vacía"

**Síntoma:**
```
[WARNING] La lista de productos está vacía. 
Test omitido por falta de datos de entrada.
```

**Causa:** `inputData.xlsx` no existe o no tiene datos

**Solución:**
```
1. Verificar que inputData.xlsx existe en src/test/resources/
2. Abrir con Excel y validar:
   - Hoja "ProductosBusqueda" existe
   - Tiene headers en fila 1
   - Tiene datos en fila 2+
3. Si está vacío, agregar productos
```

---

### Problema 2: "Producto no visible"

**Síntoma:**
```
AssertionError: Producto no visible: MacBook
```

**Causas Posibles:**
```
1. Nombre exacto del producto no coincide
2. Búsqueda no retorna resultados
3. Timeout insuficiente (6 segundos)
4. Selector CSS cambió (.product-thumb)
```

**Soluciones:**
```
1. Validar nombre en OpenCart real
2. Buscar manualmente en sitio
3. Aumentar timeout en WaitUtils
4. Inspeccionar HTML (F12) para selector correcto
```

---

### Problema 3: "Botón carrito no clickable"

**Síntoma:**
```
RuntimeException: El botón del carrito no fue clickable en 5 segundos
```

**Causas Posibles:**
```
1. Elemento #cart no visible en página
2. Elemento oculto por CSS (display: none)
3. Timeout insuficiente
4. ID cambió
```

**Soluciones:**
```
1. Aumentar timeout a 10 segundos
2. Inspeccionar HTML: document.getElementById("cart")
3. Cambiar By.id("cart") si ID cambió
4. Verificar que HomePage.open() cargó página
```

---

### Problema 4: "Enlace 'View Cart' no visible"

**Síntoma:**
```
RuntimeException: El enlace 'View Cart' no fue visible en 20 segundos
```

**Causas Posibles:**
```
1. Selector linkText("View Cart") no existe
2. Texto exacto es diferente (ej: "view cart" minúsculas)
3. Elemento en iframe
4. JavaScript async cargando tarde
```

**Soluciones:**
```
1. Usar selector alternativo: By.cssSelector("a[href*='cart']")
2. Aumentar timeout a 15 segundos
3. Inspeccionar HTML para texto exacto
4. Verificar que primer click en #cart funcionó
```

---

### Problema 5: "Producto no está en el carrito"

**Síntoma:**
```
AssertionError: MacBook no está en el carrito
```

**Causas Posibles:**
```
1. Producto no se agregó realmente
2. Carrito se limpió entre tests
3. isProductInCart() busca en lugar equivocado
4. Nombre del producto en carrito diferente
```

**Soluciones:**
```
1. Ejecutar SearchAndAddTest antes de CartTest
2. En testng.xml, definir orden: SearchAndAddTest antes de CartTest
3. Validar que isProductInCart() busca en .getPageSource()
4. Hacer screenshot del carrito para verificar nombre
```

---

## 📋 CHECKLIST DE VALIDACIÓN

```
Antes de ejecutar pruebas:
☐ inputData.xlsx existe en src/test/resources/
☐ inputData.xlsx contiene hoja "ProductosBusqueda"
☐ Hoja tiene headers: Categoria, SubCategoria, Producto, Cantidad
☐ Hoja tiene ≥1 fila de datos
☐ Nombres de productos coinciden con sitio OpenCart
☐ Cantidades son números válidos (1, 2, 3...)

Después de ejecutar pruebas:
☐ SearchAndAddTest: 2/2 assertions pasaron
☐ CartTest: 4/4 assertions pasaron
☐ logs.xlsx se creó en proyecto root
☐ logs.xlsx contiene filas de "AddedProducts"
☐ Cada fila tiene "Added: true"
```

---

## ✨ RESUMEN

El flujo de verificación de productos en el carrito:

1. ✅ **Lee datos de Excel** (ProductosBusqueda)
2. ✅ **Itera cada producto** (FOR loop)
3. ✅ **Busca en OpenCart** (search())
4. ✅ **Verifica en resultados** (isProductVisible())
5. ✅ **Agrega al carrito** (addToCart())
6. ✅ **Valida agregación** (isAddedSuccessfully())
7. ✅ **Verifica en carrito** (isProductInCart())
8. ✅ **Obtiene cantidad** (getQuantityForProduct())
9. ✅ **Registra resultados** (ExcelWriter)

**Todas las validaciones con Assertions (HardAssert)**

---

**Documento de Flujo v1.0**  
**Creado:** 12 Noviembre 2025  
**Para:** Entrega Final del Proyecto
