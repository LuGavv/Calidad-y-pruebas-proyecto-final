# Proyecto Final: Calidad y Pruebas de Automatización Web
### Desarrollado por
- Daniel Felipe Patiño Triviño
- Dhens Haward Becerra Restrepo
- Luna Maria Gaviria Andrade
- Valentina Sarria Carreño


## Descripción General del Proyecto

Este proyecto es un conjunto integral de pruebas de automatización web desarrolladas con tecnología Selenium WebDriver y TestNG, diseñado para validar la funcionalidad completa de una plataforma de comercio electrónico basada en OpenCart. El proyecto implementa un framework robusto y escalable que utiliza patrones de diseño modernos para la automatización de pruebas funcionales.

El objetivo principal de este proyecto es demostrar la aplicación de buenas prácticas en automatización de pruebas de software, incluyendo el uso de Page Object Model, manejo de datos externos mediante archivos Excel, generación de reportes detallados, y captura de evidencia en caso de fallos.

### Tecnologías y Dependencias Principales

El proyecto utiliza las siguientes tecnologías y versiones:

- Java 17 como lenguaje de programación principal, aprovechando las características modernas del lenguaje
- Selenium WebDriver versión 4.25.0 para la automatización de interacciones con navegadores web
- TestNG versión 7.10.2 como framework de testing que proporciona estructura y anotaciones para las pruebas
- Apache POI versión 4.1.2 para la lectura y escritura de archivos Excel, permitiendo la gestión de datos de prueba
- Maven como gestor de dependencias y herramienta de compilación del proyecto
- WebDriverManager versión 5.9.2 para la gestión automática de drivers del navegador, eliminando la necesidad de descargas manuales
- JUnit 4.13.2 como framework complementario de testing
- AssertJ 3.24.2 para afirmaciones más legibles y expresivas

---

## Pruebas Implementadas

El proyecto contiene cuatro pruebas de automatización principales, cada una validando un aspecto crítico de la plataforma de comercio electrónico. A continuación se describe cada prueba en detalle.

### PRUEBA 1: Login Válido con Credenciales Correctas (LoginWithExcelTest)

Ubicación en el proyecto: `src/test/java/tests/LoginWithExcelTest.java`

**Objetivo General:** Esta prueba valida el flujo de autenticación de un usuario registrado, asegurando que la plataforma puede autenticar correctamente credenciales válidas y proporcionar acceso a la aplicación.

**Descripción Detallada del Flujo de Ejecución:**

El flujo completo de esta prueba se divide en cuatro pasos principales:

Paso 1 - Lectura de Datos desde Excel: La prueba comienza leyendo las credenciales de un usuario registrado desde el archivo Excel `inputData.xlsx`, específicamente de la hoja denominada `UsuariosRegistro`. Este archivo contiene los datos de prueba en formato tabular con columnas para Email y Password. La prueba utiliza la clase `ExcelUtils` para realizar esta lectura de manera programática, asegurando que los datos se cargan correctamente y que la hoja no está vacía.

Paso 2 - Apertura de la Página de Login: Una vez obtenidas las credenciales, la prueba crea una instancia de la clase `LoginPage` utilizando el WebDriver inyectado desde la clase base `BaseTest`. Luego invoca el método `open()` para navegar a la página de login de la aplicación. La prueba valida que la URL contiene la palabra clave "login" para asegurar que se navegó correctamente a la página esperada.

Paso 3 - Ingreso de Credenciales y Autenticación: La prueba llama al método `login()` de la página de login, pasando el email y la contraseña leídos del Excel. Esto simula la interacción del usuario con el formulario de login, completando los campos de email y contraseña, y haciendo clic en el botón de envío.

Paso 4 - Validación del Acceso Exitoso: Finalmente, la prueba verifica que el login fue exitoso confirmando que:
- La URL actual ya no contiene la palabra "login", indicando una navegación fuera de la página de autenticación
- El usuario fue redirigido a la página principal o a su dashboard personal
- No hay mensajes de error en la página

**Datos de Prueba Utilizados:**

Los datos se obtienen dinámicamente del archivo Excel:
- Campo Email: Dirección de correo electrónico del usuario registrado
- Campo Password: Contraseña asociada al usuario

Estos datos se leen de la primera fila de la hoja `UsuariosRegistro`.

**Validaciones Específicas:**

- Validación 1: El archivo Excel existe y contiene datos en la hoja `UsuariosRegistro`
- Validación 2: La página de login se abre correctamente (URL contiene "login")
- Validación 3: Después del login, la URL cambió y ya no contiene "login"
- Validación 4: El usuario fue autenticado correctamente y puede acceder a la aplicación

**Prioridad de Ejecución:** 1 (Esta prueba se ejecuta primero en la suite)

**Mensajes de Consola:** La prueba imprime mensajes detallados en cada paso para facilitar el debugging y el seguimiento de la ejecución.

---

### PRUEBA 2: Registro de Nuevo Usuario (RegisterTest)

Ubicación en el proyecto: `src/test/java/tests/RegisterTest.java`

**Objetivo General:** Esta prueba valida el flujo completo de registro de un nuevo usuario en la plataforma, asegurando que se pueden crear cuentas nuevas correctamente y que todos los campos del formulario se procesan adecuadamente.

**Descripción Detallada del Flujo de Ejecución:**

El flujo de esta prueba es más complejo y se divide en los siguientes pasos:

Paso 1 - Verificación y Creación del Archivo Excel si es Necesario: La prueba comienza verificando si el archivo `src/test/resources/inputData.xlsx` existe en el sistema de archivos. Si el archivo no existe, la prueba lo crea automáticamente con datos de registro predefinidos en la hoja `UsuariosRegistro`. Esta característica permite que la prueba sea autosuficiente y no dependa de un archivo externo preexistente.

Los datos que se escriben en el Excel si no existe son:
- Nombre (First Name): Daniel
- Apellido (Last Name): Patino
- Email: Daniel.Patino+[timestamp]@example.com
- Teléfono (Telephone): 3175600619
- Contraseña (Password): Misterio2571!

El uso de timestamp en el email (generado con System.currentTimeMillis()) garantiza que cada ejecución tenga un email único, evitando conflictos por usuarios duplicados en la plataforma.

Paso 2 - Lectura de Datos del Excel: Una vez garantizado que el archivo existe, la prueba utiliza la clase `ExcelUtils` para leer todos los usuarios de la hoja `UsuariosRegistro`. Estos datos se almacenan en una lista de mapas (List<Map<String, String>>), donde cada mapa representa una fila del Excel con sus columnas.

Paso 3 - Iteración por Cada Usuario: La prueba itera a través de cada usuario en la lista leída del Excel. Para cada usuario, realiza el siguiente ciclo de acciones.

Paso 4 - Apertura de la Página de Registro: Para cada usuario, la prueba abre la página de registro llamando a `rp.open()`. Esto navega a la URL de registro de la aplicación.

Paso 5 - Llenado del Formulario de Registro: La prueba llama al método `register()` de la clase `RegisterPage`, pasando los datos del usuario:
- Nombre (First Name)
- Apellido (Last Name)
- Email (E-Mail)
- Teléfono (Telephone)
- Contraseña (Password)

Este método simula las acciones de llenar cada campo del formulario, completar validaciones requeridas y hacer clic en el botón de envío.

Paso 6 - Validación del Registro Exitoso: Después de enviar el formulario, la prueba valida que el registro fue exitoso llamando a `rp.isSuccess()`. Este método verifica la presencia de un mensaje de éxito o confirma que no hay mensajes de error.

Paso 7 - Cierre del Recurso Excel: Finalmente, se cierra el archivo Excel después de procesar todos los usuarios.

**Datos de Prueba Utilizados:**

Los datos provienen del archivo Excel `inputData.xlsx`, hoja `UsuariosRegistro`:
- First Name: Nombre del usuario
- Last Name: Apellido del usuario
- E-Mail: Correo electrónico único (con timestamp para unicidad)
- Telephone: Número de teléfono
- Password: Contraseña de acceso

**Validaciones Específicas:**

- Validación 1: El archivo Excel se crea si no existe
- Validación 2: Los datos se leen correctamente del Excel
- Validación 3: El formulario de registro se completa con todos los datos
- Validación 4: El registro se procesa sin errores
- Validación 5: Se muestra un mensaje de éxito después del registro
- Validación 6: El nuevo usuario puede acceder a la plataforma

**Características Especiales:**

Esta prueba implementa un patrón de "data-driven testing", donde los mismos pasos se repiten para múltiples conjuntos de datos. Esto permite validar múltiples escenarios de registro con diferentes usuarios simplemente agregando más filas al Excel.

---

### PRUEBA 3: Búsqueda de Productos y Agregación al Carrito (SearchAndAddTest)

Ubicación en el proyecto: `src/test/java/tests/SearchAndAddTest.java`

**Objetivo General:** Esta prueba valida el flujo de navegación del cliente final en la plataforma, simulando búsquedas de productos y la adición de artículos al carrito de compras. Es una de las pruebas más complejas porque involucra múltiples páginas y validaciones.

**Descripción Detallada del Flujo de Ejecución:**

El flujo de esta prueba es iterativo y se puede dividir en los siguientes pasos principales:

Paso 1 - Lectura de Lista de Productos desde Excel: La prueba comienza leyendo una lista de productos desde el archivo Excel `inputData.xlsx`, específicamente de la hoja `ProductosBusqueda`. Estos datos incluyen información sobre los productos que se desean buscar y agregar al carrito. Los datos se almacenan en una lista de mapas para posterior procesamiento.

Paso 2 - Validación de Disponibilidad de Datos: Se verifica que la lista de productos no esté vacía. Si la lista está vacía, la prueba imprime un mensaje informativo indicando que no hay datos de productos para procesar y termina de manera controlada sin fallar. Esto es importante porque permite que la prueba sea flexible ante la ausencia de datos.

Paso 3 - Inicialización de Page Objects: La prueba inicializa las clases de página necesarias:
- HomePage: Para acceder a la página principal y realizar búsquedas
- ProductPage: Para interactuar con los detalles del producto
- CartPage: Para validar que el producto se agregó al carrito

Paso 4 - Iteración por Cada Producto: Para cada producto en la lista del Excel, la prueba realiza la siguiente secuencia:

Sub-paso 4a - Abrir la Página Principal: Se abre la página principal de la plataforma.

Sub-paso 4b - Realizar Búsqueda del Producto: Se utiliza la funcionalidad de búsqueda de la página principal para buscar el producto por su nombre. El nombre del producto se obtiene de la columna "Producto" del Excel.

Sub-paso 4c - Validar Visibilidad del Producto: Se verifica que el producto aparece en los resultados de búsqueda. Si el producto no es visible, la prueba falla en este punto.

Sub-paso 4d - Abrir la Página de Detalles del Producto: Se hace clic en el primer producto de los resultados para abrir su página de detalles.

Sub-paso 4e - Establecer la Cantidad Deseada: Se utiliza el campo de cantidad para establecer cuántos productos se desean agregar. Este valor proviene de la columna "Cantidad" del Excel, con un valor por defecto de 1 si no se especifica.

Sub-paso 4f - Agregar al Carrito: Se hace clic en el botón para agregar el producto al carrito.

Sub-paso 4g - Validar Agregación Exitosa: Se verifica que el producto se agregó correctamente al carrito. La clase ProductPage proporciona el método `isAddedSuccessfully()` para esta validación.

Paso 5 - Registro de Logs en Excel: Después de procesar cada producto, la prueba registra los resultados en un archivo de logs. Para cada producto, se crea un registro que incluye:
- Categoría del producto
- Subcategoría del producto
- Nombre del producto
- Cantidad agregada
- Estado de éxito (verdadero/falso)
- Timestamp de la operación

Paso 6 - Escritura de Logs Finales: Al terminar el procesamiento de todos los productos, la prueba escribe todos los logs registrados en un archivo Excel llamado `logs.xlsx` en la hoja `AddedProducts`.

**Datos de Prueba Utilizados:**

Los datos se leen del archivo Excel `inputData.xlsx`, hoja `ProductosBusqueda`:
- Categoria: Categoría a la que pertenece el producto
- SubCategoria: Subcategoría más específica del producto
- Producto: Nombre del producto a buscar
- Cantidad: Cantidad de unidades a agregar

**Validaciones Específicas:**

- Validación 1: El archivo Excel de entrada existe
- Validación 2: La hoja `ProductosBusqueda` contiene datos
- Validación 3: Para cada producto:
  - El producto se encuentra en la búsqueda
  - El producto es visible en los resultados
  - Se puede abrir la página de detalles del producto
  - Se puede establecer la cantidad correctamente
  - El producto se agrega al carrito exitosamente
  - Se muestra el mensaje de éxito

**Salida Generada:**

La prueba genera un archivo `logs.xlsx` con los resultados detallados de cada producto agregado, incluyendo timestamps para auditoría.

**Características de Robustez:**

Esta prueba implementa manejo de errores robusto. Si un producto no se encuentra o no se puede agregar, la prueba registra el fallo en los logs pero continúa con los siguientes productos, permitiendo completar la prueba incluso si hay fallos parciales.

---

### PRUEBA 4: Validación de Contenido del Carrito (CartTest)

Ubicación en el proyecto: `src/test/java/tests/CartTest.java`

**Objetivo General:** Esta prueba valida que los productos agregados anteriormente permanecen correctamente en el carrito de compras y que la información del carrito es consistente.

**Descripción Detallada del Flujo de Ejecución:**

El flujo de esta prueba es más simple pero crítico, ya que valida el estado final del carrito:

Paso 1 - Abrir la Página Principal: La prueba comienza abriendo la página principal de la plataforma. Se verifica que la página se cargó correctamente validando que el elemento `<title>` no es nulo.

Paso 2 - Acceder al Carrito de Compras: Desde la página principal, la prueba navega al carrito de compras. Esto usualmente implica hacer clic en un elemento de interfaz que muestra el carrito (por ejemplo, un icono de carrito en la barra de navegación).

Paso 3 - Validación de Navegación al Carrito: Se verifica que la navegación fue exitosa comprobando que la URL actual contiene la palabra clave "cart". Esto asegura que se está en la página correcta del carrito.

Paso 4 - Búsqueda del Producto Específico: La prueba busca un producto específico llamado "MacBook" en el carrito. Utiliza el método `isProductInCart()` de la clase CartPage para verificar la presencia del producto.

Paso 5 - Validación de Cantidad Mínima: Se valida que la cantidad del producto "MacBook" es mayor o igual a 1. Esto asegura que el producto no solo está presente, sino que tiene una cantidad válida de unidades.

**Datos de Prueba:**

El producto que se valida en esta prueba es fijo:
- Nombre del Producto: MacBook

Este producto debe haber sido agregado al carrito en pruebas anteriores (específicamente en la Prueba 3).

**Validaciones Específicas:**

- Validación 1: La página principal se carga correctamente
- Validación 2: Se puede acceder al carrito desde la página principal
- Validación 3: La URL contiene "cart" después de navegar
- Validación 4: El producto "MacBook" existe en el carrito
- Validación 5: La cantidad del producto es mayor o igual a 1

**Relación con Otras Pruebas:**

Esta prueba depende implícitamente de la Prueba 3 (SearchAndAddTest) para que el producto "MacBook" esté presente en el carrito. Si se ejecuta esta prueba de forma aislada sin ejecutar primero la Prueba 3, es posible que el carrito esté vacío y la prueba falle.

---

## Estructura Arquitectónica del Proyecto

El proyecto está organizado siguiendo una estructura clara que separa responsabilidades y facilita el mantenimiento:

```
src/test/java/
├── tests/
│   ├── BaseTest.java
│   │   Clase abstracta base que proporciona configuración común a todas las pruebas
│   │   Inicializa el WebDriver antes de cada prueba
│   │   Limpia recursos después de cada prueba
│   │   Define las URLs base y configuraciones compartidas
│   │
│   ├── LoginWithExcelTest.java
│   │   Prueba 1: Login con credenciales válidas
│   │   Extiende BaseTest para heredar configuración
│   │
│   ├── RegisterTest.java
│   │   Prueba 2: Registro de nuevo usuario
│   │   Maneja creación automática de datos de prueba
│   │
│   ├── SearchAndAddTest.java
│   │   Prueba 3: Búsqueda y agregación al carrito
│   │   Procesa múltiples productos iterativamente
│   │
│   └── CartTest.java
│       Prueba 4: Validación del carrito
│       Verifica consistencia de datos en carrito
│
├── pages/
│   ├── BasePage.java
│   │   Clase base para todos los Page Objects
│   │   Proporciona métodos comunes como find(), click(), sendKeys()
│   │   Gestiona implícitamente los WebElements
│   │   Define el WebDriver para todas las páginas
│   │
│   ├── LoginPage.java
│   │   Page Object para la página de login
│   │   Métodos: open(), login()
│   │   Localizadores: emailField, passwordField, submitButton
│   │
│   ├── RegisterPage.java
│   │   Page Object para la página de registro
│   │   Métodos: open(), register(), isSuccess()
│   │   Localizadores: firstNameField, lastNameField, emailField, etc.
│   │
│   ├── HomePage.java
│   │   Page Object para la página principal
│   │   Métodos: open(), search(), openFirstProduct(), isProductVisible()
│   │   Localizadores: searchBox, searchButton, productsList
│   │
│   ├── ProductPage.java
│   │   Page Object para la página de detalles del producto
│   │   Métodos: setQuantity(), addToCart(), isAddedSuccessfully()
│   │   Localizadores: quantityField, addToCartButton, successMessage
│   │
│   └── CartPage.java
│       Page Object para la página del carrito
│       Métodos: openCart(), isProductInCart(), getQuantityForProduct()
│       Localizadores: cartItems, quantityColumn, removeButton
│
├── utils/
│   ├── WebDriverFactory.java
│   │   Factory class para crear instancias de WebDriver
│   │   Abstrae la lógica de inicialización del navegador
│   │   Soporta múltiples navegadores (Chrome, Firefox, etc.)
│   │   Configura opciones del navegador como descargas, notificaciones
│   │
│   ├── ExcelUtils.java
│   │   Utilidad para leer datos de archivos Excel
│   │   Métodos: readSheetAsMap(), readSheetAsList()
│   │   Gestiona cierres automáticos de recursos
│   │   Convierte filas del Excel en estructuras Java (Map, List)
│   │
│   ├── ExcelWriter.java
│   │   Utilidad para escribir datos en archivos Excel
│   │   Métodos: writeLogs(), writeData()
│   │   Crea nuevos libros o append a existentes
│   │   Formatea automáticamente las columnas
│   │
│   ├── CSVUtils.java
│   │   Utilidad para procesar archivos CSV
│   │   Métodos: readCSV(), writeCSV()
│   │   Alternativa a Excel para datos más simples
│   │
│   ├── WaitUtils.java
│   │   Utilidad para esperas explícitas
│   │   Métodos: waitForElement(), waitForElementClickable(), waitForCondition()
│   │   Encapsula la lógica de WebDriverWait
│   │   Maneja timeouts y excepciones
│   │
│   ├── TestListener.java
│   │   Implementa ITestListener de TestNG
│   │   Captura eventos del ciclo de vida de pruebas
│   │   Métodos: onTestSuccess(), onTestFailure(), onTestSkipped()
│   │   Genera reportes y captura pantallas en fallos
│   │
│   └── WaitUtils.java
│       Gestiona tiempos de espera para sincronización
│
└── resources/
    └── inputData.xlsx
        Archivo Excel con datos de entrada
        Hojas: UsuariosRegistro, ProductosBusqueda
        Se genera automáticamente si no existe
```

---

## Instalación y Configuración del Entorno

### Requisitos de Sistema Mínimos

Antes de ejecutar las pruebas, asegúrate de tener instalado:

Java Development Kit (JDK):
- Versión: Java 17 o superior
- Descargar desde: https://www.oracle.com/java/technologies/downloads/
- Verificación: Abrir terminal y ejecutar `java -version`

Maven:
- Versión: 3.8.1 o superior
- Descargar desde: https://maven.apache.org/download.cgi
- Verificación: Abrir terminal y ejecutar `mvn -version`

Navegador Web:
- Chrome o Chromium (el más comúnmente usado)
- Firefox (alternativa soportada)
- El driver se descarga automáticamente con WebDriverManager

Sistema Operativo:
- Windows 10 o superior
- macOS 10.13 o superior
- Linux (cualquier distribución moderna)

### Instalación Paso a Paso

1. Clonar o descargar el proyecto del repositorio
2. Abrir una terminal en la raíz del proyecto
3. Ejecutar `mvn clean install` para descargar todas las dependencias
4. Esperar a que se descarguen todas las librerías (primera ejecución puede tardar varios minutos)

---

## Ejecución de las Pruebas

### Ejecutar Todas las Pruebas

Para ejecutar la suite completa de pruebas:

```bash
mvn test
```

Este comando ejecutará todas las pruebas definidas en la suite de TestNG.

### Ejecutar Pruebas Individuales

Para ejecutar solo la Prueba 1 (Login):
```bash
mvn test -Dtest=LoginWithExcelTest
```

Para ejecutar solo la Prueba 2 (Registro):
```bash
mvn test -Dtest=RegisterTest
```

Para ejecutar solo la Prueba 3 (Búsqueda y carrito):
```bash
mvn test -Dtest=SearchAndAddTest
```

Para ejecutar solo la Prueba 4 (Validación carrito):
```bash
mvn test -Dtest=CartTest
```

### Ejecutar Pruebas Específicas usando Clase Base

Ejecutar solo pruebas de una clase específica:
```bash
mvn test -Dtest=LoginWithExcelTest#testLoginValidoDesdeExcel
```

### Ejecutar usando Configuración de TestNG

Usar el archivo de configuración XML de TestNG:
```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

### Opciones Avanzadas de Ejecución

Ejecutar pruebas en paralelo (útil para suites grandes):
```bash
mvn test -DthreadCount=4 -DuseUnlimitedThreads=true
```

Ejecutar pruebas con salida verbose (más detallada):
```bash
mvn test -X
```

Ejecutar pruebas y generar reportes:
```bash
mvn test surefire-report:report
```

---

## Resultados, Reportes y Artefactos

### Ubicación de Reportes Generados

Después de ejecutar las pruebas, se generan automáticamente varios artefactos en el directorio `target/`:

**Reportes HTML Interactivos:**
- `target/surefire-reports/index.html` - Página principal del reporte
- Este archivo proporciona una vista interactiva de todas las pruebas ejecutadas
- Muestra gráficos estadísticos de paso/fallo
- Permite expandir cada prueba para ver detalles

**Reportes XML:**
- `target/surefire-reports/testng-results.xml` - Resultados en formato XML estándar
- `target/surefire-reports/testng-failed.xml` - Solo pruebas que fallaron (si las hay)
- Estos archivos pueden procesarse con herramientas externas

**Reporte por Email:**
- `target/surefire-reports/emailable-report.html` - Reporte optimizado para email
- Formato autocontenido sin dependencias externas

**Reportes JUnit:**
- `target/surefire-reports/junitreports/` - Reportes en formato JUnit
- Compatible con herramientas CI/CD como Jenkins

**Capturas de Pantalla (Screenshots):**
- `reports/screenshots/` - Directorio donde se guardan capturas de pantalla
- Se generan automáticamente cuando una prueba falla
- Cada screenshot incluye timestamp en el nombre
- Útiles para debugging visual de fallos

**Archivos de Log:**
- `logs.xlsx` - Generado por SearchAndAddTest
- Contiene detalles de cada producto procesado
- Incluye información de tiempo y estado

### Análisis de Reportes

Para analizar los resultados de una ejecución:

1. Abrir el archivo `target/surefire-reports/index.html` en un navegador web
2. Observar el resumen general de pruebas pasadas/fallidas
3. Hacer clic en pruebas específicas para ver detalles
4. Revisar mensajes de error y stack traces en caso de fallos
5. Ver screenshots en `reports/screenshots/` para entender qué salió mal

---

## Administración de Datos de Prueba

### Archivo Excel Principal: inputData.xlsx

El archivo `src/test/resources/inputData.xlsx` es la fuente principal de datos de prueba. Contiene dos hojas principales:

**Hoja 1: UsuariosRegistro**

Esta hoja contiene información de usuarios para las pruebas de login y registro:

| Columna | Tipo | Ejemplo | Descripción |
|---------|------|---------|-------------|
| First Name | Texto | Daniel | Nombre del usuario |
| Last Name | Texto | Patino | Apellido del usuario |
| E-Mail | Texto | daniel.patino@example.com | Correo electrónico único |
| Telephone | Texto | 3175600619 | Número telefónico |
| Password | Texto | Misterio2571! | Contraseña de acceso |

Notas importantes:
- El email debe ser único para cada fila
- La contraseña debe cumplir con los requisitos de la plataforma
- Se pueden agregar múltiples usuarios para ejecutar pruebas data-driven
- Si el archivo no existe, RegisterTest lo crea automáticamente

**Hoja 2: ProductosBusqueda**

Esta hoja contiene información de productos para la prueba de búsqueda y carrito:

| Columna | Tipo | Ejemplo | Descripción |
|---------|------|---------|-------------|
| Categoria | Texto | Electronics | Categoría principal del producto |
| SubCategoria | Texto | Laptops | Subcategoría más específica |
| Producto | Texto | MacBook | Nombre del producto a buscar |
| Cantidad | Número | 1 | Cantidad de unidades a agregar |

Notas importantes:
- El nombre en la columna "Producto" debe coincidir exactamente con el nombre en la plataforma
- La cantidad debe ser un número entero positivo
- Se pueden agregar múltiples productos para procesarlos en la misma ejecución

### Cómo Agregar Nuevos Datos de Prueba

Para agregar un nuevo usuario para pruebas:

1. Abrir el archivo `src/test/resources/inputData.xlsx` con Excel o LibreOffice Calc
2. Ir a la hoja "UsuariosRegistro"
3. Agregar una nueva fila con los datos del usuario
4. Guardar el archivo

Para agregar un nuevo producto para búsqueda:

1. Abrir el archivo `src/test/resources/inputData.xlsx`
2. Ir a la hoja "ProductosBusqueda"
3. Agregar una nueva fila con los datos del producto
4. El nombre exacto debe existir en la plataforma
5. Guardar el archivo

### Archivo de Logs Generado: logs.xlsx

La prueba SearchAndAddTest genera automáticamente un archivo `logs.xlsx` que contiene:

| Columna | Descripción |
|---------|-------------|
| Categoria | Categoría del producto agregado |
| SubCategoria | Subcategoría del producto |
| Producto | Nombre del producto |
| Cantidad | Cantidad de unidades agregadas |
| Added | Booleano (true/false) indicando si se agregó exitosamente |
| Timestamp | Marca de tiempo de cuando se agregó |

Este archivo es útil para auditoría y tracking de operaciones.

---

## Patrones de Diseño y Mejores Prácticas Implementados

### Page Object Model (POM)

El proyecto implementa el patrón Page Object Model, que es una best practice fundamental en automatización de pruebas. Este patrón proporciona varias ventajas:

Encapsulación de Localizadores: Cada página tiene su propia clase que contiene todos los localizadores (By objects) para los elementos de esa página. Los localizadores no están dispersos en las pruebas, sino centralizados en la clase correspondiente.

Métodos de Interacción: Para cada acción que puede realizarse en una página, existe un método correspondiente. Por ejemplo, en LoginPage existe un método `login(email, password)` que realiza todas las acciones necesarias para completar el login.

Mantenibilidad: Si la interfaz de usuario cambia, solo necesita actualizarse la clase Page Object correspondiente, no todas las pruebas.

Legibilidad: Las pruebas se leen como historias de negocios, no como detalles técnicos de automatización.

Ejemplo de implementación:

```java
// Page Object - LoginPage.java
public class LoginPage extends BasePage {
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By submitButton = By.xpath("//button[@type='submit']");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void open() {
        driver.get("https://example.com/login");
    }
    
    public void login(String email, String password) {
        find(emailField).sendKeys(email);
        find(passwordField).sendKeys(password);
        click(submitButton);
    }
}

// Prueba - LoginWithExcelTest.java
public class LoginWithExcelTest extends BaseTest {
    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("user@example.com", "password");
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));
    }
}
```

### Base Test Class

La clase `BaseTest` proporciona configuración común a todas las pruebas:

Inicialización del WebDriver: Se inicializa en el método `@BeforeMethod`
Limpieza de Recursos: Se cierra el WebDriver en el método `@AfterMethod`
Configuración Común: URLs base, timeouts, opciones del navegador
Herencia: Todas las pruebas extienden BaseTest para heredar esta funcionalidad

### Utility Classes

Se han creado clases de utilidad reutilizables para tareas comunes:

**WebDriverFactory**: Factory pattern para crear instancias de WebDriver. Abstrae la lógica de inicialización del navegador.

**ExcelUtils y ExcelWriter**: Encapsulan la lógica de lectura y escritura de archivos Excel. Manejan automáticamente la apertura, lectura, escritura y cierre de recursos.

**WaitUtils**: Encapsulan la lógica de esperas explícitas de Selenium. Proporcionan métodos para esperar elementos, clickeabilidad, etc.

**TestListener**: Implementa listeners de TestNG para capturar eventos del ciclo de vida de pruebas.

### Data-Driven Testing

Las pruebas utilizan archivos Excel como fuente de datos, permitiendo ejecutar los mismos pasos de prueba con múltiples conjuntos de datos sin duplicar código.

### Manejo de Excepciones

El código implementa try-catch blocks apropiados para manejar excepciones esperadas e inesperadas, proporcionando mensajes de error descriptivos.

---

## Configuración Detallada

### Archivo pom.xml

El archivo `pom.xml` define todas las dependencias y configuración de Maven:

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

Propiedades del compilador:
- Fuente de compilación Java 17
- Target de compilación Java 17
- Encoding UTF-8 para caracteres especiales

Dependencias principales:
- Selenium WebDriver 4.25.0
- TestNG 7.10.2
- Apache POI 4.1.2
- WebDriverManager 5.9.2
- AssertJ 3.24.2

Plugin Surefire:
- Versión 3.0.0-M9
- Configurado para usar suite XML de TestNG
- Define variables del sistema como `screenshots.dir`

### Archivo testng.xml

El archivo `testng.xml` define la configuración de la suite de TestNG:

Organiza las pruebas en grupos lógicos
Define el orden de ejecución
Configura listeners para reportes
Define el nivel de paralelismo (si aplicable)

---

## Solución de Problemas y Troubleshooting

### Problema: "Chrome driver not found"

Causa: El driver de Chrome no está instalado o no es accesible.

Solución:
- WebDriverManager descarga automáticamente el driver en la primera ejecución
- Asegurarse de que Chrome está instalado en el sistema
- Si el problema persiste, ejecutar: `mvn clean install`
- Verificar que hay conexión a Internet (necesaria para descargar el driver)

### Problema: "Element not found exception"

Causa: El elemento no existe en la página o el localizador es incorrecto.

Soluciones:
- Aumentar los timeouts en WaitUtils.java
- Usar `WaitUtils.waitForElement()` en lugar de `driver.findElement()`
- Verificar que el localizador (By) es correcto usando herramientas de inspección del navegador
- Agregar esperas implícitas o explícitas adicionales

### Problema: "Excel file not found"

Causa: El archivo inputData.xlsx no existe en la ubicación esperada.

Solución:
- RegisterTest crea el archivo automáticamente en la primera ejecución
- Verificar que la ruta `src/test/resources/inputData.xlsx` es correcta
- Si el archivo se perdió, eliminarlo y ejecutar RegisterTest nuevamente
- Crear manualmente el archivo con las hojas UsuariosRegistro y ProductosBusqueda

### Problema: "Login failed - incorrect credentials"

Causa: Las credenciales en el Excel no son válidas en la plataforma.

Soluciones:
- Verificar que los datos en Excel son correctos
- Asegurarse de que el usuario está registrado en la plataforma
- Ejecutar la Prueba 2 (RegisterTest) primero para crear un usuario válido
- Usar las credenciales del usuario creado en Prueba 2 para Prueba 1

### Problema: "Product not found in search results"

Causa: El nombre del producto no coincide exactamente con el nombre en la plataforma.

Soluciones:
- Verificar el nombre exacto del producto en la plataforma
- Actualizar la columna "Producto" en el Excel para que coincida exactamente
- Usar búsqueda parcial en lugar de exacta si es posible
- Verificar que el producto existe y está disponible en la plataforma

### Problema: "Tests fail intermittently"

Causa: Problemas de sincronización o timings inconsistentes.

Soluciones:
- Aumentar los timeouts en WaitUtils.java
- Usar WebDriverWait en lugar de esperas implícitas
- Agregar esperas explícitas antes de interactiones críticas
- Verificar que la plataforma es estable
- Ejecutar pruebas en secuencia en lugar de paralelo

### Problema: "Maven build failure"

Causa: Problemas con dependencias o configuración de Maven.

Soluciones:
- Ejecutar `mvn clean` para limpiar caché
- Ejecutar `mvn install` para descargar dependencias nuevamente
- Verificar que Java 17 está instalado correctamente
- Verificar que Maven está en el PATH del sistema
- Eliminar el directorio `.m2/repository` para forzar descarga de nuevas dependencias

---

## Notas Técnicas y Consideraciones Importantes

### Independencia de Pruebas

Aunque las pruebas pueden tener dependencias implícitas (por ejemplo, Prueba 4 depende de que Prueba 3 agregue el producto), cada prueba está diseñada para ser ejecutable de manera independiente. Sin embargo, para una ejecución completa y consistente, es recomendable ejecutarlas en el orden especificado.

### Unicidad de Datos

Se utiliza System.currentTimeMillis() para garantizar la unicidad de emails en cada ejecución. Esto permite que las pruebas se ejecuten múltiples veces sin conflictos de usuarios duplicados.

### Gestión de Recursos

Todos los recursos (WebDriver, Excel files) se cierran apropiadamente en bloques finally o usando try-with-resources cuando sea posible.

### Logging y Debugging

Todas las pruebas incluyen mensajes detallados en System.out para facilitar el debugging. Para debugging más avanzado, revisar los logs de Selenium en la consola.

### Compatibilidad de Navegadores

El proyecto utiliza WebDriverManager que soporta múltiples navegadores. Para cambiar de Chrome a Firefox, modificar WebDriverFactory.java.

### Variables de Entorno

Las URLs base pueden parametrizarse usando variables de entorno para facilitar la ejecución en diferentes ambientes (desarrollo, staging, producción).

---

## Conclusión

Este proyecto demuestra la implementación de un framework de automatización de pruebas robusto, escalable y mantenible. Utiliza patrones de diseño establecidos, mejores prácticas de la industria, y proporciona una base sólida para expandir las pruebas futuras. La documentación detallada y los ejemplos claros facilitan el entendimiento y la reutilización del código por otros desarrolladores.