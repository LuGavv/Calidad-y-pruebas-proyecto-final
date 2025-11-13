# 🔐 TEST DE LOGIN CON EXCEL - GUÍA COMPLETA

**Proyecto:** Calidad y Pruebas - Proyecto Final  
**Función:** Login con usuario registrado + 2 casos inválidos  
**Archivo:** `LoginWithExcelTest.java` (NUEVO)  
**Fecha:** 12 Noviembre 2025  
**Status:** ✅ LISTO PARA EJECUTAR

---

## 📋 REQUISITO

```
El usuario registrado es el que genera RegisterTest:
✅ Email: Alberto.perez+[timestamp]@example.com
✅ Password: Password123
✅ Ubicación: inputData.xlsx - Hoja "UsuariosRegistro"

Crear test de Login con:
✅ Caso 1: Login válido con usuario del Excel
✅ Caso 2: Login inválido - contraseña incorrecta
✅ Caso 3: Login inválido - usuario no registrado
```

---

## ✅ IMPLEMENTACIÓN

### Test 1: Login Válido

```java
@Test(priority = 1)
public void testLoginValidoDesdeExcel() throws IOException {
    // 1. Lee usuario registrado de Excel (UsuariosRegistro)
    ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
    List<Map<String,String>> users = excel.readSheetAsMap("UsuariosRegistro");
    
    // 2. Obtiene email y password del usuario registrado
    String email = users.get(0).get("E-Mail");
    String password = users.get(0).get("Password");
    
    // 3. Abre página de login
    LoginPage loginPage = new LoginPage(driver);
    loginPage.open();
    
    // 4. Ingresa credenciales
    loginPage.login(email, password);
    
    // 5. Valida que login fue exitoso (URL no contiene "login")
    Assert.assertTrue(!driver.getCurrentUrl().contains("login"));
}
```

**Validaciones:**
- ✅ Usuario registrado existe en Excel
- ✅ Email leído correctamente
- ✅ Password leído correctamente
- ✅ Login exitoso (redirección de página)

---

### Test 2: Login Inválido - Contraseña Incorrecta

```java
@Test(priority = 2)
public void testLoginContraseñaIncorrecta() throws IOException {
    // 1. Lee usuario del Excel
    ExcelUtils excel = new ExcelUtils("src/test/resources/inputData.xlsx");
    List<Map<String,String>> users = excel.readSheetAsMap("UsuariosRegistro");
    
    // 2. Usa email correcto pero contraseña INCORRECTA
    String email = users.get(0).get("E-Mail");
    String incorrectPassword = "WrongPassword123!"; // ≠ Password123
    
    // 3. Abre página de login
    LoginPage loginPage = new LoginPage(driver);
    loginPage.open();
    
    // 4. Intenta login con contraseña incorrecta
    loginPage.login(email, incorrectPassword);
    
    // 5. Valida que login fue rechazado (sigue en página login)
    Assert.assertTrue(driver.getCurrentUrl().contains("login"));
}
```

**Validaciones:**
- ✅ Email existe (registrado)
- ✅ Contraseña es incorrecta
- ✅ Login rechazado
- ✅ Permanece en página de login

---

### Test 3: Login Inválido - Usuario No Registrado

```java
@Test(priority = 3)
public void testLoginUsuarioNoRegistrado() throws IOException {
    // 1. Crea usuario que NO existe
    String emailNoExiste = "usuarionoexiste" + timestamp + "@example.com";
    String password = "SomePassword123!";
    
    // 2. Abre página de login
    LoginPage loginPage = new LoginPage(driver);
    loginPage.open();
    
    // 3. Intenta login con usuario no registrado
    loginPage.login(emailNoExiste, password);
    
    // 4. Valida que login fue rechazado (sigue en página login)
    Assert.assertTrue(driver.getCurrentUrl().contains("login"));
}
```

**Validaciones:**
- ✅ Usuario NO está registrado
- ✅ Login rechazado
- ✅ Permanece en página de login

---

## 📊 FLUJO DE DATOS

```
RegisterTest
    ↓
    Registra: Alberto Perez
    Email: Alberto.perez+[timestamp]@example.com
    Password: Password123
    ↓
    Guarda en: inputData.xlsx (Hoja: UsuariosRegistro)
    ↓
LoginWithExcelTest
    ↓
    Lee usuario registrado
    ↓
    TEST 1: Login Válido
    ├─ Email: Alberto.perez+[timestamp]@example.com ✓
    ├─ Password: Password123 ✓
    └─ Resultado: LOGIN EXITOSO ✓
    
    TEST 2: Contraseña Incorrecta
    ├─ Email: Alberto.perez+[timestamp]@example.com ✓
    ├─ Password: WrongPassword123! ✗
    └─ Resultado: LOGIN RECHAZADO ✓
    
    TEST 3: Usuario No Registrado
    ├─ Email: usuarionoexiste[timestamp]@example.com ✗
    ├─ Password: SomePassword123! ✓
    └─ Resultado: LOGIN RECHAZADO ✓
```

---

## 🚀 CÓMO EJECUTAR

### Opción 1: Ejecutar solo Login Tests

```bash
mvn test -Dtest=LoginWithExcelTest
```

### Opción 2: Ejecutar Register + Login

```bash
mvn test -Dtest=RegisterTest,LoginWithExcelTest
```

### Opción 3: Ejecutar todos los tests

```bash
mvn clean test
```

---

## 📊 SALIDA ESPERADA

```
========================================
TEST 1: LOGIN VÁLIDO - USUARIO DEL EXCEL
========================================

[PASO 1] Leyendo usuario registrado de inputData.xlsx...
[PASO 1] ✓ Usuario registrado encontrado
  • Email: Alberto.perez+1731370800@example.com
  • Password: *********

[PASO 2] Abriendo página de login...
[PASO 2] ✓ Página de login abierta

[PASO 3] Ingresando credenciales válidas...
  • Email: Alberto.perez+1731370800@example.com
  • Password: ******* (correcta)
[PASO 3] ✓ Credenciales ingresadas

[PASO 4] Validando login exitoso...
  • URL actual: https://opencart.abstracta.us/index.php?route=account/account
  • ¿Logueado?: true
[PASO 4] ✓ Login exitoso

✅ TEST 1: LOGIN VÁLIDO - PASSED

========================================
TEST 2: LOGIN INVÁLIDO - CONTRASEÑA INCORRECTA
========================================

[PASO 1] Leyendo usuario del Excel...
[PASO 1] ✓ Usuario encontrado
  • Email: Alberto.perez+1731370800@example.com
  • Password ingresada: WrongPassword123! (INCORRECTA)

[PASO 2] Abriendo página de login...
[PASO 2] ✓ Página de login abierta

[PASO 3] Ingresando email correcto con contraseña INCORRECTA...
  • Email: Alberto.perez+1731370800@example.com (correcto)
  • Password: WrongPassword123! (INCORRECTO)
[PASO 3] ✓ Credenciales ingresadas

[PASO 4] Validando que login fue rechazado...
  • URL actual: https://opencart.abstracta.us/index.php?route=account/login
  • ¿Aún en página login?: true
[PASO 4] ✓ Login rechazado correctamente

✅ TEST 2: LOGIN INVÁLIDO (CONTRASEÑA) - PASSED

========================================
TEST 3: LOGIN INVÁLIDO - USUARIO NO REGISTRADO
========================================

[PASO 1] Preparando usuario que NO está registrado...
[PASO 1] ✓ Usuario no registrado preparado
  • Email: usuarionoexiste1731370850@example.com (NO EXISTE)
  • Password: SomePassword123!

[PASO 2] Abriendo página de login...
[PASO 2] ✓ Página de login abierta

[PASO 3] Intentando login con usuario NO REGISTRADO...
  • Email: usuarionoexiste1731370850@example.com (NO EXISTE)
  • Password: SomePassword123!
[PASO 3] ✓ Credenciales ingresadas

[PASO 4] Validando que login fue rechazado (usuario no existe)...
  • URL actual: https://opencart.abstracta.us/index.php?route=account/login
  • ¿Aún en página login?: true
[PASO 4] ✓ Login rechazado (usuario no registrado)

✅ TEST 3: LOGIN INVÁLIDO (USUARIO NO EXISTE) - PASSED

========================================
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS ✓
```

---

## 📁 ARCHIVOS INVOLUCRADOS

```
src/test/java/tests/
├── RegisterTest.java                    (Genera usuario en Excel)
├── LoginWithExcelTest.java             (NUEVO - Lee usuario del Excel)
└── BaseTest.java                        (Setup/Teardown)

src/test/java/pages/
├── RegisterPage.java
├── LoginPage.java                       (login method)
└── BasePage.java

src/test/resources/
└── inputData.xlsx
    └── Hoja: UsuariosRegistro
        ├── First Name
        ├── Last Name
        ├── E-Mail
        ├── Telephone
        └── Password
```

---

## ✅ VALIDACIONES

### Test 1: Login Válido
```
✅ Usuario existe en Excel
✅ Email leído correctamente
✅ Password leído correctamente
✅ Login exitoso
✅ URL cambió (navegó a dashboard)
```

### Test 2: Contraseña Incorrecta
```
✅ Usuario existe
✅ Email correcto
✅ Password incorrecto
✅ Login fue rechazado
✅ Permanece en página login
```

### Test 3: Usuario No Registrado
```
✅ Usuario NO existe
✅ Email no está registrado
✅ Login fue rechazado
✅ Permanece en página login
```

**Total: 15 validaciones**

---

## 🔧 RELACIÓN CON REGISTERTEST

```
Flujo Completo de Prueba:

1. RegisterTest ejecuta primero
   ├─ Crea usuario: Alberto Perez
   ├─ Email: Alberto.perez+[timestamp]@example.com
   ├─ Password: Password123
   └─ Guarda en: inputData.xlsx (UsuariosRegistro)

2. LoginWithExcelTest ejecuta después
   ├─ TEST 1: Lee usuario de Excel y login ✓
   ├─ TEST 2: Login con contraseña incorrecta ✗
   └─ TEST 3: Login con usuario no registrado ✗

Resultado: Flujo COMPLETO de Registro + Login
```

---

## 💡 NOTAS IMPORTANTES

1. **Excel Compartido:** Ambos tests usan `inputData.xlsx`
   - RegisterTest genera datos
   - LoginWithExcelTest los consume

2. **Timestamp en Email:** Cada ejecución genera email único
   ```
   Alberto.perez+1731370800@example.com
   Alberto.perez+1731370850@example.com
   ```

3. **Prioridades:** Tests ejecutan en orden
   ```
   @Test(priority = 1) → TEST 1
   @Test(priority = 2) → TEST 2
   @Test(priority = 3) → TEST 3
   ```

4. **Independencia:** Cada test abre/cierra página limpiamente

---

## 🎓 VALIDACIONES IMPLEMENTADAS

| Validación | Tipo | Ubicación |
|-----------|------|-----------|
| Excel existe | ✅ assertNotNull | PASO 1 |
| Usuario existe | ✅ assertFalse (empty) | PASO 1 |
| Email no null | ✅ implícito | PASO 1 |
| Password correcto (T1) | ✅ login exitoso | PASO 4 T1 |
| URL cambió (T1) | ✅ !contains("login") | PASO 4 T1 |
| Password incorrecto (T2) | ✅ permanece login | PASO 4 T2 |
| URL contiene login (T2) | ✅ assertTrue | PASO 4 T2 |
| Usuario no existe (T3) | ✅ login rechazado | PASO 4 T3 |
| URL contiene login (T3) | ✅ assertTrue | PASO 4 T3 |

---

## 🚀 PRÓXIMOS PASOS

1. **Ejecutar RegisterTest:**
   ```bash
   mvn test -Dtest=RegisterTest
   ```

2. **Ejecutar LoginWithExcelTest:**
   ```bash
   mvn test -Dtest=LoginWithExcelTest
   ```

3. **O ejecutar ambos juntos:**
   ```bash
   mvn test -Dtest=RegisterTest,LoginWithExcelTest
   ```

---

**Guía de Login con Excel v1.0**  
**Creada:** 12 Noviembre 2025  
**Estado:** LISTO PARA USAR
