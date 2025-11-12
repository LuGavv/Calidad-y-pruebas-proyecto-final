# ⚠️ DATOS DE PRUEBA FALTANTES - ACCIÓN REQUERIDA

## Problema Identificado

El test `SearchAndAddTest` falló porque:

```
AssertionError: La lista de productos está vacía (línea 38)
```

**Causa:** El archivo `src/test/resources/inputData.xlsx` no existe o está vacío.

---

## 🛠️ Solución: Crear Archivo Excel

### Opción 1: Crear Manualmente (Recomendado)

**Pasos:**

1. Abre Excel, LibreOffice Calc o Google Sheets
2. Crea una nueva hoja de cálculo
3. Crea las hojas con los siguientes nombres:
   - `ProductosBusqueda`
   - `LoginData` (opcional)

4. En la hoja `ProductosBusqueda`, agrega:

```
┌───────────┬──────────────┬──────────┬──────────┐
│ Categoria │ SubCategoria │ Producto │ Cantidad │
├───────────┼──────────────┼──────────┼──────────┤
│ Electr.   │ Comp.        │ MacBook  │ 1        │
│ Electr.   │ Tablet       │ iPad     │ 2        │
│ Ropa      │ Hombre       │ T-Shirt  │ 1        │
└───────────┴──────────────┴──────────┴──────────┘
```

5. Guarda como:
   - **Nombre:** `inputData.xlsx`
   - **Ubicación:** `src/test/resources/`
   - **Formato:** Excel 2007+ (.xlsx)

### Opción 2: Usar Python (Script)

```python
import openpyxl
from openpyxl.utils import get_column_letter

# Crear workbook
wb = openpyxl.Workbook()
ws = wb.active
ws.title = "ProductosBusqueda"

# Headers
headers = ["Categoria", "SubCategoria", "Producto", "Cantidad"]
ws.append(headers)

# Datos
datos = [
    ["Electronica", "Computadoras", "MacBook", 1],
    ["Electronica", "Tablets", "iPad", 2],
    ["Ropa", "Hombre", "T-Shirt", 1],
]

for fila in datos:
    ws.append(fila)

# Guardar
wb.save("src/test/resources/inputData.xlsx")
print("✅ Archivo creado: src/test/resources/inputData.xlsx")
```

**Ejecutar:**
```bash
python crear_excel.py
```

### Opción 3: Usar PowerShell (Si Excel está instalado)

```powershell
# Crear directorio si no existe
if (!(Test-Path "src/test/resources")) {
    New-Item -ItemType Directory -Path "src/test/resources" -Force
}

# Crear Excel
$excel = New-Object -ComObject Excel.Application
$workbook = $excel.Workbooks.Add()
$sheet = $workbook.Sheets.Item(1)
$sheet.Name = "ProductosBusqueda"

# Headers
$sheet.Cells.Item(1, 1) = "Categoria"
$sheet.Cells.Item(1, 2) = "SubCategoria"
$sheet.Cells.Item(1, 3) = "Producto"
$sheet.Cells.Item(1, 4) = "Cantidad"

# Datos
$datos = @(
    @("Electronica", "Computadoras", "MacBook", 1),
    @("Electronica", "Tablets", "iPad", 2),
    @("Ropa", "Hombre", "T-Shirt", 1)
)

$row = 2
foreach ($data in $datos) {
    $sheet.Cells.Item($row, 1) = $data[0]
    $sheet.Cells.Item($row, 2) = $data[1]
    $sheet.Cells.Item($row, 3) = $data[2]
    $sheet.Cells.Item($row, 4) = $data[3]
    $row++
}

# Guardar
$workbook.SaveAs("$(Get-Location)\src\test\resources\inputData.xlsx")
$excel.Quit()

Write-Host "✅ Archivo creado exitosamente"
```

---

## 📋 ESTRUCTURA ESPERADA DEL ARCHIVO

```
Archivo: inputData.xlsx
├─ Hoja 1: ProductosBusqueda
│  ├─ Columna A: Categoria
│  ├─ Columna B: SubCategoria
│  ├─ Columna C: Producto
│  └─ Columna D: Cantidad
│
└─ Datos mínimos: 1 fila (header + 1 producto)
```

---

## ✅ VERIFICACIÓN

Después de crear el archivo, verifica:

```powershell
# 1. Verificar que la carpeta existe
Test-Path src/test/resources/

# 2. Verificar que el archivo existe
Test-Path src/test/resources/inputData.xlsx

# 3. Verificar contenido (si usas PowerShell)
Get-ChildItem src/test/resources/ -Filter "*.xlsx"

# 4. Ejecutar test
mvn -Dtest=tests.SearchAndAddTest test
```

**Esperado:** ✅ Test pasa sin "lista vacía"

---

## 🚀 SIGUIENTE PASO

1. **Crea el archivo** `src/test/resources/inputData.xlsx`
2. **Agrega los datos** según la estructura arriba
3. **Ejecuta de nuevo:** `mvn test`
4. **Verifica:** SearchAndAddTest debe pasar ahora ✅

---

**Nota:** Este archivo es **CRÍTICO** para que SearchAndAddTest funcione. Sin datos, el test siempre fallará con "lista vacía".

