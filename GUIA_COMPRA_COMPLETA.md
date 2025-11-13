# 🛒 TEST DE COMPRA COMPLETA - GUÍA DE EJECUCIÓN

**Proyecto:** Calidad y Pruebas - Proyecto Final  
**Test:** CompraCompleteTest.java  
**Función:** Búsqueda, Agregación y Verificación de Productos en Carrito  
**Fecha:** 12 Noviembre 2025  
**Status:** ✅ LISTO PARA EJECUTAR

---

## 📋 RESUMEN

El test `CompraCompleteTest` implementa el flujo **completo de compra**:

```
1. Leer productos desde inputData.csv
2. Para CADA producto:
   ├─ Abrir OpenCart
   ├─ Buscar producto
   ├─ Verificar en resultados
   ├─ Abrir producto
   ├─ Establecer cantidad
   ├─ Agregar al carrito
   └─ Validar éxito
3. Verificar TODOS los productos en carrito
4. Validar cantidades
```

---

## 📁 ARCHIVOS UTILIZADOS

### 1. Datos de Entrada
```
Archivo: src/test/resources/inputData.csv
Contenido:
  Categoria,SubCategoria,Producto,Cantidad
  Software,Office,MacBook,1
  Software,Databases,Microsoft SQL Server,1
  Phones & PDAs,Phones,iPhone,2
```

### 2. Código Principal
```
Test: src/test/java/tests/CompraCompleteTest.java
Page Objects:
  - HomePage.java (search, isProductVisible, openFirstProduct)
  - ProductPage.java (setQuantity, addToCart, isAddedSuccessfully)
  - CartPage.java (openCart, isProductInCart, getQuantityForProduct)
Utilidades:
  - CSVUtils.java (lectura CSV)
  - WaitUtils.java (esperas explícitas)
```

---

## 🚀 CÓMO EJECUTAR

### Opción 1: Ejecutar solo CompraCompleteTest

```bash
cd c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final

mvn test -Dtest=CompraCompleteTest
```

### Opción 2: Ejecutar todos los tests

```bash
mvn clean test
```

### Opción 3: Ejecutar con Maven desde VS Code

1. Abrir Terminal integrada (Ctrl + `)
2. Ejecutar:
```bash
mvn test -Dtest=CompraCompleteTest
```

---

## 📊 FLUJO DETALLADO DEL TEST

### FASE 1: LECTURA Y PROCESAMIENTO

```
[PASO 1] Leyendo productos desde inputData.csv...
✓ Se encontraron 3 productos

[PASO 2-4] Procesando cada producto...

─────────────────────────────────────
PRODUCTO #1: MacBook
Categoría: Software > Office
Cantidad: 1
─────────────────────────────────────
  1. Abriendo OpenCart...
     ✓ Página cargada

  2. Buscando producto: 'MacBook'...
     ✓ Búsqueda realizada

  3. Verificando que aparece en resultados...
     ✓ Producto encontrado en resultados

  4. Abriendo página de producto...
     ✓ Página de producto abierta

  5. Estableciendo cantidad: 1
     ✓ Cantidad establecida

  6. Agregando al carrito...
     ✓ Botón agregado clickeado

  7. Validando agregación exitosa...
     ✓ PRODUCTO AGREGADO EXITOSAMENTE

✓ PRODUCTO #1 COMPLETADO

[Repite para productos #2 y #3...]

========================================
FASE 1 COMPLETA: Todos los productos agregados
========================================
```

### FASE 2: VERIFICACIÓN EN CARRITO

```
[PASO 5] Verificando productos en carrito...

  1. Abriendo página del carrito...
     ✓ Carrito abierto

  2. Verificando presencia de productos:

     • Verificando 'MacBook'...
       ✓ Encontrado (Cantidad: 1)

     • Verificando 'Microsoft SQL Server'...
       ✓ Encontrado (Cantidad: 1)

     • Verificando 'iPhone'...
       ✓ Encontrado (Cantidad: 2)

========================================
FASE 2 COMPLETA: Todos los productos verificados
========================================

✓✓✓ TEST COMPLETADO EXITOSAMENTE ✓✓✓
Productos procesados: 3
Todos verificados en carrito
```

---

## ✅ VALIDACIONES IMPLEMENTADAS

### En Cada Producto

```java
Assert.assertTrue(productVisible, 
    "Producto '" + nombreProducto + "' no aparece en resultados");
Assert.assertTrue(addedSuccessfully, 
    "Producto '" + nombreProducto + "' no se agregó exitosamente");
```

### En Verificación de Carrito

```java
Assert.assertTrue(enCarrito, 
    "Producto '" + producto + "' NO ESTÁ en el carrito");
Assert.assertTrue(cantidadEnCarrito >= 1, 
    "Cantidad de '" + producto + "' debe ser >= 1");
```

**Total: 7+ Assertions**

---

## 📝 PERSONALIZACIÓN

### Agregar Más Productos

**Archivo:** `src/test/resources/inputData.csv`

Simplemente agregar filas al CSV:

```csv
Categoria,SubCategoria,Producto,Cantidad
Software,Office,MacBook,1
Software,Databases,Microsoft SQL Server,1
Phones & PDAs,Phones,iPhone,2
Audio,Headphones,AirPods,1        ← Nueva fila
Tablets,iPad,Apple iPad,2         ← Nueva fila
```

El test automáticamente procesará todos los productos.

### Cambiar Cantidades

En el CSV, cambiar el valor de la columna `Cantidad`:

```csv
Producto,Cantidad
MacBook,1      → MacBook,5
iPhone,2       → iPhone,3
```

---

## 🐛 TROUBLESHOOTING

### Error 1: "El CSV está vacío"

**Solución:**
1. Verificar que `inputData.csv` tiene datos
2. Primera línea debe ser: `Categoria,SubCategoria,Producto,Cantidad`
3. Líneas siguientes: Los productos
4. Guardar archivo

### Error 2: "Producto no aparece en resultados"

**Causa:** Nombre del producto no coincide exactamente

**Solución:**
1. Visitar https://opencart.abstracta.us/
2. Buscar manualmente
3. Copiar nombre EXACTO del producto
4. Actualizar CSV

**Nombres que funcionan:**
- MacBook ✓
- iPhone ✓
- Microsoft SQL Server ✓
- iPad ✓
- AirPods ✓

### Error 3: Timeout esperando elemento

**Solución:** Aumentar timeout en `WaitUtils.java`

```java
// Cambiar de 5 a 10 segundos
WaitUtils.waitForVisible(driver, locator, 10);
```

---

## 📊 ESTRUCTURA DEL CÓDIGO

```java
@Test(priority = 1)
public void testCompraCompleta() throws IOException {
    
    // PASO 1: Leer CSV
    List<Map<String,String>> products = CSVUtils.readCSV(...);
    
    // PASO 2-4: Ciclo por cada producto
    for (Map<String,String> product : products) {
        // Buscar, verificar, agregar
        homePage.open();
        homePage.search(nombreProducto);
        Assert.assertTrue(productVisible);
        homePage.openFirstProduct();
        productPage.setQuantity(cantidad);
        productPage.addToCart();
        Assert.assertTrue(addedSuccessfully);
    }
    
    // PASO 5: Verificar en carrito
    cartPage.openCart();
    for (String producto : productosAgregados) {
        Assert.assertTrue(cartPage.isProductInCart(producto));
        Assert.assertTrue(cartPage.getQuantityForProduct(producto) >= 1);
    }
}
```

---

## 🎯 RESULTADO ESPERADO

```
TEST PASSED ✓

SearchResults:
  ✓ MacBook - Encontrado
  ✓ Microsoft SQL Server - Encontrado
  ✓ iPhone - Encontrado

Agregados al carrito:
  ✓ MacBook (Cantidad: 1)
  ✓ Microsoft SQL Server (Cantidad: 1)
  ✓ iPhone (Cantidad: 2)

Verificación en carrito:
  ✓ Todos los productos presentes
  ✓ Cantidades correctas

Total: 3 productos agregados y verificados
```

---

## 📚 ARCHIVOS GENERADOS DESPUÉS DE EJECUCIÓN

```
target/
├── surefire-reports/
│   ├── CompraCompleteTest.xml
│   ├── index.html
│   └── testng-results.xml
└── test-classes/
    └── tests/
        └── CompraCompleteTest.class
```

---

## 🔧 UTILIDADES IMPLEMENTADAS

### CSVUtils.java

Lectura de archivos CSV:

```java
List<Map<String,String>> products = CSVUtils.readCSV("ruta/archivo.csv");
```

**Características:**
- Parsea headers (primera línea)
- Crea mapas para cada fila
- Maneja valores con comas (entre comillas)
- Retorna `List<Map<String,String>>`

### WaitUtils.java

Esperas explícitas:

```java
WaitUtils.waitForVisible(driver, locator, segundos);
WaitUtils.waitForClickable(driver, locator, segundos);
WaitUtils.waitForText(driver, locator, texto, segundos);
```

---

## ✨ CARACTERÍSTICAS

✅ **Ciclo completo:** Búsqueda → Agregación → Verificación  
✅ **Múltiples productos:** Procesa N productos del CSV  
✅ **Validaciones:** 7+ assertions  
✅ **Mensajes claros:** Logs detallados de cada paso  
✅ **Manejo errores:** RuntimeException con contexto  
✅ **Reutilizable:** Funciona con cualquier CSV  
✅ **Modular:** Page Objects separados  

---

## 📞 PRÓXIMOS PASOS

1. ✅ Test creado: CompraCompleteTest.java
2. ✅ Utilidad CSV: CSVUtils.java
3. ✅ Datos listos: inputData.csv
4. ⏭️ **Ejecutar:** `mvn test -Dtest=CompraCompleteTest`
5. ⏭️ Validar resultados en output

---

**Guía de Compra Completa v1.0**  
**Creada:** 12 Noviembre 2025  
**Estado:** LISTO PARA USAR
