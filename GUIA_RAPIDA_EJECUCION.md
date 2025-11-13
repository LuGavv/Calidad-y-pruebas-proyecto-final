# ⚡ GUÍA RÁPIDA: EJECUCIÓN INMEDIATA

**Proyecto:** Búsqueda, Agregación y Verificación en Carrito  
**Tiempo de Ejecución:** ~5 minutos  
**Status:** ✅ Listo para usar

---

## 🚀 EN 3 PASOS

### PASO 1: Preparar Datos Excel (2 minutos)

#### Abrir archivo
```
Ubicación: c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final\
           src\test\resources\inputData.xlsx
```

#### Agregar estos datos

**Hoja:** ProductosBusqueda

| Categoria | SubCategoria | Producto | Cantidad |
|-----------|--------------|----------|----------|
| Software | Office | MacBook | 1 |
| Software | Databases | Microsoft SQL Server | 1 |
| Phones & PDAs | Phones | iPhone | 2 |

#### Guardar
`Ctrl+S`

---

### PASO 2: Ejecutar Tests (2 minutos)

#### Abrir Terminal en Proyecto

```powershell
cd c:\Users\HP\Desktop\Calidad-y-pruebas-proyecto-final
```

#### Ejecutar Maven

```bash
mvn clean test
```

#### O solo los tests de carrito

```bash
mvn test -Dtest=SearchAndAddTest,CartTest
```

---

### PASO 3: Validar Resultados (1 minuto)

#### ✅ Éxito

Deberías ver:
```
SearchAndAddTest: PASS ✓
CartTest: PASS ✓
logs.xlsx: Creado ✓
```

#### ❌ Si falla

Ver sección "Troubleshooting" abajo

---

## 📂 ARCHIVOS GENERADOS

Después de ejecutar, tendrás:

```
proyecto-root/
├── logs.xlsx                  ← Resultados de búsqueda y agregación
├── target/
│   └── surefire-reports/      ← Reportes de test
│       ├── SearchAddTest.xml
│       └── CartTest.xml
└── src/test/resources/
    └── inputData.xlsx         ← Datos que ingresaste
```

---

## 📊 QUÉ HACE CADA TEST

### SearchAndAddTest

```
1. Lee productos de inputData.xlsx
2. Para cada producto:
   ├─ Busca en OpenCart
   ├─ Verifica en resultados
   ├─ Abre producto
   ├─ Establece cantidad
   ├─ Agrega al carrito
   ├─ Valida éxito
   └─ Registra en logs.xlsx
```

**Resultado:** 3 productos agregados al carrito

---

### CartTest

```
1. Abre página principal
2. Abre carrito desde menú
3. Verifica que MacBook esté en carrito
4. Valida que cantidad >= 1
5. ✅ PASS
```

**Resultado:** Confirmación que productos están en carrito

---

## 🐛 TROUBLESHOOTING

### Error 1: "La lista de productos está vacía"

**Causa:** inputData.xlsx sin datos

**Solución:**
1. Abre `src/test/resources/inputData.xlsx`
2. Verifica que tiene datos en hoja "ProductosBusqueda"
3. Fila 1 = Headers
4. Fila 2+ = Productos
5. Guarda

---

### Error 2: "Producto no visible"

**Causa:** Nombre del producto no coincide con OpenCart

**Solución:**
1. Visita https://opencart.abstracta.us/
2. Busca manualmente los productos
3. Copia nombres exactos
4. Actualiza Excel

**Nombres que funcionan:**
- "MacBook" ✓
- "iPhone" ✓
- "Microsoft SQL Server" ✓

---

### Error 3: "El botón del carrito no fue clickable"

**Causa:** Timeout insuficiente o elemento no visible

**Solución:**
1. Abrir `src/test/java/pages/CartPage.java`
2. Línea 27: Cambiar `10` a `15` en:
   ```java
   WaitUtils.waitForVisible(driver, viewCartLink, 15);
   ```
3. Guardar y ejecutar de nuevo

---

### Error 4: "La tabla del carrito no fue visible"

**Causa:** Página no cargó correctamente

**Solución:**
1. Verificar conexión a https://opencart.abstracta.us/
2. Ejecutar nuevamente:
   ```bash
   mvn clean test
   ```

---

## 📊 ESTRUCTURA DE CARPETAS

```
proyecto/
│
├── src/
│   ├── main/java/
│   │   └── org/example/
│   │       └── App.java
│   │
│   └── test/
│       ├── java/
│       │   ├── listeners/
│       │   │   └── TestListener.java
│       │   │
│       │   ├── pages/
│       │   │   ├── BasePage.java
│       │   │   ├── HomePage.java
│       │   │   ├── ProductPage.java
│       │   │   ├── CartPage.java
│       │   │   ├── LoginPage.java
│       │   │   └── RegisterPage.java
│       │   │
│       │   ├── tests/
│       │   │   ├── BaseTest.java
│       │   │   ├── SearchAndAddTest.java    ← PRINCIPAL
│       │   │   ├── CartTest.java            ← PRINCIPAL
│       │   │   ├── LoginTest.java
│       │   │   └── RegisterTest.java
│       │   │
│       │   └── utils/
│       │       ├── WaitUtils.java
│       │       ├── ExcelUtils.java
│       │       ├── ExcelWriter.java
│       │       └── WebDriverFactory.java
│       │
│       └── resources/
│           └── inputData.xlsx               ← TUS DATOS
│
├── pom.xml                                  ← Dependencias
├── testng.xml                               ← Configuración tests
└── logs.xlsx                                ← Generado (resultados)
```

---

## 🎯 PUNTOS CLAVE

✅ **POM:** Patrón Page Object implementado
✅ **Esperas:** Explícitas con validaciones
✅ **Assertions:** HardAssert en cada paso
✅ **Excel:** Lectura y escritura automática
✅ **Ciclo:** FOR completo por cada producto
✅ **Verificación:** CartTest valida presencia

---

## 📝 CAMBIOS PERSONALIZABLES

### Aumentar Timeouts

**Archivo:** `src/test/java/utils/WaitUtils.java`

```java
// Cambiar segundos:
public static boolean waitForVisible(WebDriver driver, By locator, int seconds) {
    // seconds = 5, 6, 10, etc.
}
```

### Agregar Más Productos

**Archivo:** `src/test/resources/inputData.xlsx`

Simplemente agregar filas:
```
Fila N: Categoria | SubCategoria | Producto | Cantidad
```

### Cambiar URL Base

**Archivo:** `src/test/java/pages/HomePage.java` (línea 21)

```java
public void open() {
    driver.get("https://opencart.abstracta.us/");  // ← AQUÍ
}
```

---

## 🎓 CONCEPTOS APRENDIDOS

| Concepto | Ubicación | Ejemplo |
|----------|-----------|---------|
| Page Object Model | pages/ | HomePage.java |
| Explicit Waits | WaitUtils.java | waitForClickable() |
| Excel I/O | ExcelUtils.java | readSheetAsMap() |
| Assertions | tests/ | Assert.assertTrue() |
| Ciclos FOR | SearchAndAddTest | for (Map product...) |
| Selectors CSS | pages/ | By.cssSelector() |
| RuntimeException | pages/ | throw new RuntimeException() |

---

## ✨ SIGUIENTES PASOS (Opcional)

1. **Agregar SoftAssert:** Validaciones múltiples sin fallar
2. **Agregar Logging:** Log4j2 para debugging
3. **Ampliar Tests:** Más casos de uso
4. **CI/CD:** Jenkins/GitHub Actions

---

## 🆘 CONTACTO/AYUDA

Si algo no funciona:

1. **Verificar Excel:** ¿Tiene datos en ProductosBusqueda?
2. **Verificar Timeout:** ¿Aumentó a 15 segundos?
3. **Verificar Conexión:** ¿Puede abrir opencart.abstracta.us?
4. **Ver Logs:** `target/surefire-reports/`

---

## ✅ CHECKLIST

Antes de ejecutar:
```
☐ inputData.xlsx tiene datos
☐ Hoja se llama "ProductosBusqueda"
☐ Headers: Categoria, SubCategoria, Producto, Cantidad
☐ Fila 2 en adelante: Productos
☐ Maven instalado y en PATH
☐ Java 8+ instalado
```

Después de ejecutar:
```
☐ SearchAndAddTest PASS ✓
☐ CartTest PASS ✓
☐ logs.xlsx creado ✓
☐ Reportes en target/surefire-reports/ ✓
```

---

**Guía Rápida v1.0**  
**Creada:** 12 Noviembre 2025  
**Última revisión:** 12 Noviembre 2025

Cualquier duda: Ver documentos en carpeta raíz del proyecto
