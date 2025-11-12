package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    /**
     * Escribe/añade un conjunto de filas maps a un sheet dentro de filePath.
     * Si el archivo no existe lo crea y escribe encabezado usando las keys del primer map.
     * Si existe, hace append.
     * 
     * Utiliza solo POI, sin dependencias externas (compatible con todas las versiones).
     */
    public static void writeLogs(String filePath, List<Map<String,String>> rows, String sheetName) throws IOException {
        File file = new File(filePath);
        XSSFWorkbook workbook;
        Sheet sheet;

        // Crear directorio si no existe
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Cargar workbook existente o crear uno nuevo
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            }
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) sheet = workbook.createSheet(sheetName);
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet(sheetName);
        }

        int startRow = sheet.getPhysicalNumberOfRows();
        
        // Si sheet vacío y rows no vacío, escribir encabezado
        if (startRow == 0 && rows.size() > 0) {
            Row header = sheet.createRow(0);
            int c = 0;
            for (String key : rows.get(0).keySet()) {
                Cell cell = header.createCell(c++);
                cell.setCellValue(key);
            }
            startRow = 1;
        }

        // Append las filas de datos
        for (Map<String,String> map : rows) {
            Row row = sheet.createRow(startRow++);
            int c = 0;
            for (String v : map.values()) {
                Cell cell = row.createCell(c++);
                cell.setCellValue(v);
            }
        }

        // Guardar a disco
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
