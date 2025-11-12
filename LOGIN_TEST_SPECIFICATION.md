# Especificación de Pruebas - Módulo de Inicio de Sesión (Login)

## Descripción General
Este documento especifica los requisitos de prueba para el módulo de inicio de sesión de la aplicación OpenCart. El módulo valida la autenticación de usuarios, el manejo correcto de credenciales inválidas y la correcta presentación de mensajes de error.

---

## Requisitos de Prueba

### REQ-1: Lectura de Combinaciones de Email y Contraseña desde Excel
**Objetivo:** Leer múltiples combinaciones de credenciales desde un archivo Excel para ejecutar pruebas parametrizadas.

**Criterios de Aceptación:**
- ✅ El sistema debe leer datos desde el archivo `src/test/resources/inputData.xlsx`
- ✅ La hoja debe llamarse `LoginData`
- ✅ Las columnas deben incluir: `E-Mail`, `Password`, `Expected`, `Descripcion`
- ✅ El sistema debe crear automáticamente datos de prueba si el archivo/hoja no existe
- ✅ Los datos se crean dinámicamente registrando una cuenta válida
- ✅ Se incluyen casos de éxito y error

**Implementación:**
- Archivo: `src/test/java/tests/LoginTest.java`
- Método: `loginFromExcel()`
- Clase de utilidad: `ExcelUtils` y `ExcelWriter`

---

### REQ-2: Validación de Inicio de Sesión Exitoso
**Objetivo:** Validar que el login sea exitoso cuando se proporcionan credenciales válidas.

**Criterios de Aceptación:**
- ✅ El usuario debe ser redirigido a la página de cuenta (`/account` o `/dashboard`)
- ✅ Debe aparecer el link "Logout" en la página (indicador de sesión activa)
- ✅ Debe aparecer el link "My Account" en la página
- ✅ Debe aparecer un header con "My Account" o similar
- ✅ NO debe haber mensajes de error visibles

**Casos de Prueba:**

| Caso | Email | Contraseña | Resultado Esperado | Descripción |
|------|-------|------------|-------------------|-------------|
| 1 | [Email válido dinámico] | Password123 | ✓ Éxito | Login exitoso con credenciales válidas |

**Implementación:**
- Archivo: `src/test/java/pages/LoginPage.java`
- Método: `isLoggedIn()`

**Validaciones internas:**
```
Check 1: ¿Logout link visible? → Éxito
Check 2: ¿My Account link visible? → Éxito
Check 3: ¿Header contiene "My Account"? → Éxito
Check 4: ¿URL cambió a /account o /dashboard? → Éxito
Cualquiera de estas validaciones indica usuario autenticado
```

---

### REQ-3: Manejo de Errores con Credenciales Inválidas
**Objetivo:** Validar que el sistema maneja correctamente credenciales inválidas y muestra mensajes de error apropiados.

**Criterios de Aceptación:**
- ✅ El login debe fallar
- ✅ Debe mostrarse un mensaje de error visible
- ✅ El usuario NO debe ser redirigido (permanece en `/login`)
- ✅ El mensaje de error debe ser claro y descriptivo
- ✅ El formulario de login debe permanecer visible para reintentos

**Casos de Prueba:**

| Caso | Email | Contraseña | Resultado Esperado | Descripción |
|------|-------|------------|-------------------|-------------|
| 2 | invalid.user@example.com | wrongpass | ✗ Error | Login fallido - email y contraseña inválidos |
| 3 | [Email válido] | WrongPassword123 | ✗ Error | Login fallido - contraseña incorrecta |

**Implementación:**
- Archivo: `src/test/java/pages/LoginPage.java`
- Método: `isLoginErrorDisplayed()`
- Método: `getLoginErrorText()`

**Validaciones internas:**
```
Check 1: ¿Alert danger (.alert-danger) visible? → Error detectado
Check 2: ¿URL aún en /login? → Error detectado
Check 3: ¿Hay div con clase alert/warning/error? → Error detectado
Cualquiera de estas validaciones indica error de login
```

---

## Flujo de Ejecución de Pruebas

```
1. loginFromExcel() inicia
   ↓
2. Verifica si archivo inputData.xlsx existe
   ├─ NO existe → Crear datos de prueba
   │  ├─ Registrar nueva cuenta válida
   │  └─ Crear 3 casos en Excel: éxito + 2 errores
   └─ Existe → Leer datos existentes
   ↓
3. Para cada caso en Excel:
   ├─ Abrir página de login
   ├─ Rellenar email y contraseña
   ├─ Hacer click en botón Login
   ├─ Esperar respuesta de servidor (2 segundos)
   │
   ├─ Si resultado esperado = "success":
   │  └─ Validar: isLoggedIn() == true
   │     ├─ Check links Logout/My Account
   │     ├─ Check header My Account
   │     └─ Check cambio de URL
   │
   └─ Si resultado esperado = "error":
      └─ Validar: isLoginErrorDisplayed() == true
         ├─ Check alert danger
         ├─ Check URL aún en /login
         └─ Check elementos de error
   ↓
4. Registrar resultados y mensajes
```

---

## Archivos Involucrados

### Clase Principal de Pruebas
- **Archivo:** `src/test/java/tests/LoginTest.java`
- **Método:** `loginFromExcel()`
- **Responsabilidad:** Orquestar la ejecución de todos los casos de prueba

### Page Object - Página de Login
- **Archivo:** `src/test/java/pages/LoginPage.java`
- **Métodos principales:**
  - `open()` - Abre la página de login
  - `login(email, password)` - Realiza el login
  - `isLoggedIn()` - Valida autenticación exitosa
  - `isLoginErrorDisplayed()` - Valida error de login
  - `getLoginErrorText()` - Obtiene mensaje de error

### Utilidades de Excel
- **Archivo:** `src/test/java/utils/ExcelUtils.java`
- **Responsabilidad:** Lectura de datos desde Excel
- **Método:** `readSheetAsMap(sheetName)`

- **Archivo:** `src/test/java/utils/ExcelWriter.java`
- **Responsabilidad:** Escritura de datos en Excel
- **Método:** `writeLogs(filePath, rows, sheetName)`

### Utilidades de Wait
- **Archivo:** `src/test/java/utils/WaitUtils.java`
- **Responsabilidad:** Esperas explícitas y condicionales

### Datos de Prueba
- **Archivo:** `src/test/resources/inputData.xlsx`
- **Hoja:** `LoginData`
- **Columnas:** E-Mail, Password, Expected, Descripcion

---

## Mensajes de Diagnóstico

El código incluye logs detallados para facilitar diagnóstico:

```
[LoginTest] ========== INICIANDO PRUEBAS DE LOGIN DESDE EXCEL ==========
[LoginTest] Total casos a ejecutar: 3
[LoginTest] ========== CASO 1/3 ==========
[LoginTest] Email: test.user+1234567890@example.com
[LoginTest] Contraseña: ***
[LoginTest] Resultado esperado: success
[LoginTest] Descripción: Login exitoso con credenciales válidas

[LoginPage] Iniciando login...
[LoginPage] Email: test.user+1234567890@example.com
[LoginPage] ✓ Email rellenado
[LoginPage] ✓ Contraseña rellenada
[LoginPage] ✓ Botón de login clickeado

[LoginPage] Validando si el usuario está autenticado...
[LoginPage] URL actual: https://opencart.abstracta.us/index.php?route=account/dashboard
[LoginPage] ✓ ÉXITO - Link 'Logout' detectado → Usuario autenticado

[LoginTest] ✓ ÉXITO - Login validado correctamente
```

---

## Extensibilidad

Para agregar más casos de prueba, simplemente añade filas al Excel con:
- **E-Mail:** Dirección de correo
- **Password:** Contraseña
- **Expected:** `success` o `error`
- **Descripcion:** Descripción del caso

El sistema ejecutará automáticamente todos los casos.

---

## Conclusión

Este módulo de pruebas garantiza:
1. ✅ Lectura flexible de datos desde Excel
2. ✅ Validación robusta de login exitoso
3. ✅ Manejo correcto de credenciales inválidas
4. ✅ Logs detallados para diagnóstico
5. ✅ Fácil extensión con nuevos casos de prueba
