package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ExcelWriter {
    /**
     * Escribe/añade un conjunto de filas maps a un sheet dentro de filePath.
     * Si el archivo no existe lo crea y escribe encabezado usando las keys del primer map.
     * Si existe, hace append.
     */
    public static void writeLogs(String filePath, List<Map<String,String>> rows, String sheetName) throws IOException {
        File file = new File(filePath);
        XSSFWorkbook workbook;
        Sheet sheet;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

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
        // si sheet vacío y rows no vacío, escribir encabezado
        if (startRow == 0 && rows.size() > 0) {
            Row header = sheet.createRow(0);
            int c = 0;
            for (String key : rows.get(0).keySet()) {
                Cell cell = header.createCell(c++);
                cell.setCellValue(key);
            }
            startRow = 1;
        }

        // append las filas
        for (Map<String,String> map : rows) {
            Row row = sheet.createRow(startRow++);
            int c = 0;
            for (String v : map.values()) {
                Cell cell = row.createCell(c++);
                cell.setCellValue(v);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
