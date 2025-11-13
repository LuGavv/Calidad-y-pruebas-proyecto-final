# ⚡ INSTRUCCIONES FINALES: EJECUTAR COMPRA COMPLETA

**Proyecto:** Búsqueda, Agregación y Verificación de Productos  
**Status:** ✅ 100% LISTO  
**Fecha:** 12 Noviembre 2025

---

## 🎯 LO QUE SE IMPLEMENTÓ

### ✅ Test de Compra Completa
- Archivo: `CompraCompleteTest.java`
- Lee datos del CSV
- Busca cada producto
- Verifica en resultados
- Agrega al carrito
- Verifica en carrito final

### ✅ Datos de Entrada
- Archivo: `inputData.csv`
- 3 productos listos
- Estructura: Categoria, SubCategoria, Producto, Cantidad

### ✅ Utilidad CSV
- Archivo: `CSVUtils.java`
- Lee archivos CSV automáticamente
- Parsea correctamente

---

## 🚀 EJECUTAR EN 3 COMANDOS

### 1. Abrir Terminal

Abre VS Code Terminal:
```
Ctrl + ` (backtick)
```

### 2. Navegar al Proyecto

```bash
cd c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final
```

### 3. Ejecutar Test

```bash
mvn test -Dtest=CompraCompleteTest
```

---

## 📊 SALIDA ESPERADA

La consola mostrará:

```
========================================
INICIANDO TEST: COMPRA COMPLETA
========================================

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

─────────────────────────────────────
PRODUCTO #2: Microsoft SQL Server
...

─────────────────────────────────────
PRODUCTO #3: iPhone
...

========================================
FASE 1 COMPLETA: Todos los productos agregados
========================================

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

## ✅ SI VES ESTO = ÉXITO

```
BUILD SUCCESS

Tests run: 1, Failures: 0, Skipped: 0
```

---

## 🛠️ SI ALGO FALLA

### Error: "El CSV está vacío"

**Solución:**
1. Verifica que `src/test/resources/inputData.csv` existe
2. Tiene datos (vimos que tiene 3 productos)
3. Primera línea = headers
4. Resto = productos

### Error: "Producto no aparece en resultados"

**Solución:**
1. Abrir https://opencart.abstracta.us/
2. Buscar manualmente el producto
3. Verificar que el nombre en CSV es exacto
4. Nombres que funcionan: MacBook, iPhone, Microsoft SQL Server

### Error: Timeout o conexión

**Solución:**
1. Verificar conexión a Internet
2. Verificar que opencart.abstracta.us está disponible
3. Ejecutar de nuevo

---

## 📁 ESTRUCTURA DE ARCHIVOS

```
c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final\
│
├── src/test/java/tests/
│   └── CompraCompleteTest.java          ← TEST NUEVO
│
├── src/test/java/utils/
│   ├── CSVUtils.java                    ← NUEVA UTILIDAD
│   ├── WaitUtils.java
│   ├── ExcelUtils.java
│   └── ...
│
├── src/test/resources/
│   ├── inputData.csv                    ← DATOS (3 productos)
│   └── inputData.xlsx                   ← (Excel alternativo)
│
└── pom.xml                              ← Dependencias Maven
```

---

## 🎓 QUÉ HACE EL TEST

### FASE 1: Procesamiento de Productos

Para cada producto en CSV:
1. ✓ Abre OpenCart
2. ✓ Busca el producto
3. ✓ Verifica que aparece
4. ✓ Abre la página de producto
5. ✓ Establece la cantidad
6. ✓ Agrega al carrito
7. ✓ Valida que se agregó

### FASE 2: Verificación Final

1. ✓ Abre la página del carrito
2. ✓ Verifica que TODOS los productos están
3. ✓ Valida las cantidades

---

## 📊 VALIDACIONES

**Total: 9+ Assertions**

En cada producto:
- ✓ Búsqueda exitosa
- ✓ Visible en resultados
- ✓ Agregado al carrito

En carrito:
- ✓ Producto presente
- ✓ Cantidad correcta

---

## 🔧 PERSONALIZACIÓN

### Agregar Más Productos

Edita `src/test/resources/inputData.csv`:

```csv
Categoria,SubCategoria,Producto,Cantidad
Software,Office,MacBook,1
Software,Databases,Microsoft SQL Server,1
Phones & PDAs,Phones,iPhone,2
Audio,Headphones,AirPods,1              ← Agregar aquí
Tablets,iPad,Apple iPad,2               ← Agregar aquí
```

El test procesará todos automáticamente.

### Cambiar Cantidades

```csv
Producto,Cantidad
MacBook,1       → MacBook,5
iPhone,2        → iPhone,3
```

---

## 📝 ARCHIVOS IMPORTANTES

| Archivo | Función |
|---------|---------|
| `CompraCompleteTest.java` | Test principal (nuevo) |
| `CSVUtils.java` | Lee CSV (nuevo) |
| `inputData.csv` | Datos de productos (ya existe con datos) |
| `HomePage.java` | Búsqueda |
| `ProductPage.java` | Agregar carrito |
| `CartPage.java` | Verificar carrito |

---

## ✨ RESUMEN RÁPIDO

| Elemento | Status |
|----------|--------|
| Test creado | ✅ CompraCompleteTest.java |
| Utilidad CSV | ✅ CSVUtils.java |
| Datos | ✅ inputData.csv con 3 productos |
| Búsqueda | ✅ HomePage.search() |
| Verificación resultados | ✅ HomePage.isProductVisible() |
| Cantidad | ✅ ProductPage.setQuantity() |
| Agregar carrito | ✅ ProductPage.addToCart() |
| Verificar carrito | ✅ CartPage.isProductInCart() |
| Validaciones | ✅ 9+ Assertions |
| Documentación | ✅ 3 documentos |

**TODO LISTO ✅**

---

## 🚀 PRÓXIMOS PASOS

1. Abre Terminal (`Ctrl + ` `)
2. Ejecuta: `mvn test -Dtest=CompraCompleteTest`
3. Espera a que termine
4. Ver resultados en consola

**¡Eso es todo!**

---

**Instrucciones Finales v1.0**  
**Creadas:** 12 Noviembre 2025
