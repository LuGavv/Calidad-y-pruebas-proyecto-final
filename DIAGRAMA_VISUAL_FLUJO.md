# 🔄 Diagrama Visual: Flujo del Test Antes vs Después

## ❌ ANTES (Incorrecto) - Causa Error

```
┌─────────────────────────────────────────────────────────────┐
│ CartTest.verifyCartContainsProducts()                       │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
            ┌──────────────────────────────┐
            │ 1. new CartPage(driver)      │
            │    - WebDriver vacío         │
            │    - No hay URL cargada      │
            └──────────────────────────────┘
                           │
                           ▼
            ┌──────────────────────────────┐
            │ 2. cartPage.openCart()       │
            │    - Busca id="cart"         │
            │    - ❌ ELEMENTO NO EXISTE   │
            └──────────────────────────────┘
                           │
                           ▼
                    ❌ NoSuchElement Exception
              "Unable to locate element: #cart"
              
        ❌ TEST FALLA - BUILD FAILURE
```

---

## ✅ DESPUÉS (Correcto) - Funciona Correctamente

```
┌────────────────────────────────────────────────────────────────┐
│ CartTest.verifyCartContainsProducts()                          │
└────────────────────────────────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 1: Abrir la página principal     │
        │ ✅ new HomePage(driver)               │
        │ ✅ homePage.open()                    │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ Cargar: https://opencart.abstracta.us │
        │ ✅ Título de página cargado          │
        │ ✅ Elemento id="cart" AHORA EXISTE   │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 2: Aceptar que página está lista │
        │ ✅ Assert.assertNotNull(title)        │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 3: Ahora interactuar con carrito │
        │ ✅ new CartPage(driver)               │
        │ ✅ cartPage.openCart()                │
        │    - Espera clickable (5 seg)         │
        │    - Hace clic en id="cart"           │
        │    - Espera "View Cart" visible       │
        │    - Hace clic en "View Cart"         │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 4: Validar navegación            │
        │ ✅ Assert URL contiene "cart"         │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 5: Verificar producto en carrito │
        │ ✅ cartPage.isProductInCart("MacBook")│
        │ ✅ Assert.assertTrue(productExists)   │
        └───────────────────────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │ PASO 6: Verificar cantidad            │
        │ ✅ cartPage.getQuantityForProduct()   │
        │ ✅ Assert.assertTrue(qty >= 1)        │
        └───────────────────────────────────────┘
                            │
                            ▼
                  ✅ BUILD SUCCESS
              Todos los tests pasan
```

---

## 📊 Comparación de Estados

### Estado del WebDriver

#### ❌ ANTES
```
WebDriver State
│
├─ URL: (vacía - sin cargar)
├─ DOM Elements: (mínimos)
├─ id="cart": ❌ NO EXISTE
└─ Resultado: NoSuchElement Error
```

#### ✅ DESPUÉS
```
WebDriver State
│
├─ URL: https://opencart.abstracta.us/
├─ DOM Elements: (todos cargados)
├─ id="cart": ✅ EXISTE (en barra superior)
├─ Selectores: ✅ TODOS ACCESIBLES
└─ Resultado: Test Pasa Correctamente
```

---

## 🔀 Flujo de Wait - Mejora Implementada

### ❌ ANTES: Sin Validación
```
WaitUtils.waitForVisible(...)   ← Sin verificar resultado
    │
    └─→ Retorna boolean → Se ignora
    
driver.findElement(...).click()  ← Puede fallar aquí
```

### ✅ DESPUÉS: Con Validación
```
if (!WaitUtils.waitForVisible(...)) {
    throw new RuntimeException("Elemento no visible")
}
    │
    └─→ Si falla: excepción clara
    └─→ Si pasa: continuar seguro

driver.findElement(...).click()  ← Seguro de proceder
```

---

## 📱 Árbol de Decisión: CartTest

```
START: verifyCartContainsProducts()
    │
    ├─ ¿Está cargada la página principal?
    │  ├─ ❌ NO (ANTES)
    │  │  └─ Resultado: NoSuchElement en #cart ❌
    │  │
    │  └─ ✅ SÍ (DESPUÉS)
    │     └─ ¿Existe elemento id="cart"?
    │        ├─ ❌ NO
    │        │  └─ Resultado: NoSuchElement ❌
    │        │
    │        └─ ✅ SÍ
    │           └─ ¿Es clickable?
    │              ├─ ❌ NO (timeout)
    │              │  └─ Resultado: RuntimeException ❌
    │              │
    │              └─ ✅ SÍ
    │                 └─ Clic en #cart
    │                    └─ ¿Aparece "View Cart"?
    │                       ├─ ❌ NO (timeout)
    │                       │  └─ Resultado: RuntimeException ❌
    │                       │
    │                       └─ ✅ SÍ
    │                          └─ Clic en "View Cart"
    │                             └─ ¿URL contiene "cart"?
    │                                ├─ ❌ NO
    │                                │  └─ Resultado: AssertionError ❌
    │                                │
    │                                └─ ✅ SÍ
    │                                   └─ ¿"MacBook" en carrito?
    │                                      ├─ ❌ NO
    │                                      │  └─ Resultado: AssertionError ❌
    │                                      │
    │                                      └─ ✅ SÍ
    │                                         └─ ¿Cantidad >= 1?
    │                                            ├─ ❌ NO
    │                                            │  └─ Resultado: AssertionError ❌
    │                                            │
    │                                            └─ ✅ SÍ
    │                                               └─ ✅ TEST PASSA ✅
    │
END
```

---

## 🏗️ Arquitectura: Page Object Pattern

### ❌ ANTES: Débil
```
CartTest
    │
    └─→ CartPage
            │
            ├─ openCart() [sin waits]
            ├─ isProductInCart()
            └─ getQuantityForProduct()
        
Problema: No hay setup de página previa
```

### ✅ DESPUÉS: Robusto
```
CartTest
    │
    ├─→ HomePage
    │   └─ open() [carga URL primero]
    │
    ├─ Assert.assertNotNull(title) [validación]
    │
    └─→ CartPage
        │
        ├─ openCart() 
        │  ├─ waitForClickable() [validado]
        │  ├─ click()
        │  ├─ waitForVisible() [validado]
        │  └─ click()
        │
        ├─ isProductInCart()
        │  ├─ waitForVisible() [validado]
        │  └─ search
        │
        └─ getQuantityForProduct()
           ├─ waitForVisible() [validado]
           └─ parse quantity

Mejora: Cada paso validado, flujo claro
```

---

## ⏱️ Timeline de Ejecución

### ❌ ANTES
```
0:00  → new CartTest
0:01  → @BeforeMethod (setUp)
         - ChromeDriver iniciado
         - Ventana maximizada
0:02  → cartPage.openCart()
0:03  → ❌ NoSuchElement Exception
0:04  → @AfterMethod (tearDown)
        - Driver cerrado
────────────────────────────────
Total: 4 segundos (FALLA)
```

### ✅ DESPUÉS
```
0:00  → new CartTest
0:01  → @BeforeMethod (setUp)
         - ChromeDriver iniciado
         - Ventana maximizada
0:02  → HomePage.open()
         - Navega a URL
         - Espera carga (waits)
0:05  → Assert.assertNotNull(title) ✅
0:06  → CartPage.openCart()
         - Espera clickable
         - Clic
         - Espera visible
         - Clic
0:12  → Assert URL contains "cart" ✅
0:13  → Assert MacBook en carrito ✅
0:14  → Assert cantidad >= 1 ✅
0:15  → @AfterMethod (tearDown)
         - Driver cerrado
────────────────────────────────
Total: 15 segundos (PASA ✅)
```

---

## 📈 Matriz de Cambio de Riesgo

```
        ANTES      DESPUÉS
        ─────      ──────

Waits:  ❌❌❌      ✅✅✅
URL:    ❌❌❌      ✅✅✅
Docs:   ❌         ✅✅✅
Errors: ❌         ✅✅✅
Asserts:❌❌       ✅✅✅

Riesgo: 🔴🔴🔴    🟢🟢
        CRÍTICO    BAJO
```

---

## 🎯 Resumen Visual

```
┌─────────────────────────────────────────────────────────────┐
│ CAMBIO PRINCIPAL: Agregar HomePage.open()                   │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ANTES                    DESPUÉS                            │
│  ──────────────────────   ────────────────────              │
│                                                              │
│  CartPage cartPage        HomePage homePage                 │
│      ↓                    CartPage cartPage                 │
│  openCart()                   ↓                             │
│      ↓                    homePage.open()                   │
│  ❌ NoSuchElement             ↓                             │
│                           Assert page loaded                │
│                               ↓                             │
│                           cartPage.openCart()               │
│                               ↓                             │
│                           ✅ Element Found                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧠 Mental Model: Qué Aprender

### Antes (Incorrecto)
```
Test → Element
(sin URL) → (no existe)
```

### Después (Correcto)
```
Test → Load URL → Page Ready → Element Exists → Test Can Continue
```

### La Lección
> **Los elementos web solo existen después de que se carga la página.**
> 
> No puedes interactuar con elementos si no has navegado a la página primero.

---

**Diagrama Visual v1.0**  
**Fecha:** 12 Noviembre 2025  
**Propósito:** Visualizar la solución

