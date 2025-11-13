@echo off
REM Script para crear inputData.xlsx desde CSV
REM Autor: GitHub Copilot
REM Fecha: 12 Noviembre 2025

setlocal enabledelayedexpansion

REM Rutas
set CSV_FILE=src\test\resources\inputData.csv
set XLSX_FILE=src\test\resources\inputData.xlsx

REM Verificar que CSV existe
if not exist "%CSV_FILE%" (
    echo ERROR: No se encuentra %CSV_FILE%
    exit /b 1
)

REM Usar PowerShell para convertir CSV a Excel
echo Creando Excel desde CSV...

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$csv = Import-Csv '%CSV_FILE%'; ^
    $xl = New-Object -ComObject Excel.Application; ^
    $xl.Visible = $false; ^
    $wb = $xl.Workbooks.Add(); ^
    $ws = $wb.Sheets.Item(1); ^
    $ws.Name = 'ProductosBusqueda'; ^
    $row = 1; ^
    foreach ($prop in $csv[0].PSObject.Properties.Name) { ^
        $ws.Cells.Item($row, [array]::IndexOf([object[]]$csv[0].PSObject.Properties.Name, $prop) + 1) = $prop; ^
    }; ^
    $row = 2; ^
    foreach ($item in $csv) { ^
        $col = 1; ^
        foreach ($prop in $item.PSObject.Properties.Name) { ^
            $ws.Cells.Item($row, $col) = $item.$prop; ^
            $col += 1; ^
        }; ^
        $row += 1; ^
    }; ^
    $wb.SaveAs('%XLSX_FILE%', 51); ^
    $wb.Close(); ^
    $xl.Quit(); ^
    Write-Host '✓ Archivo Excel creado: %XLSX_FILE%'"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Completado exitosamente!
    echo Archivo: %XLSX_FILE%
    echo.
) else (
    echo ERROR: No se pudo crear el archivo Excel
    exit /b 1
)

endlocal
