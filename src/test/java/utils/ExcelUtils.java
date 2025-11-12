package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {
    private Workbook workbook;

    public ExcelUtils(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);
        fis.close();
    }

    public List<Map<String,String>> readSheetAsMap(String sheetName) {
        List<Map<String,String>> result = new ArrayList<>();
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return result;

        Iterator<Row> rows = sheet.iterator();
        Row header = rows.hasNext() ? rows.next() : null;
        if (header == null) return result;

        List<String> keys = new ArrayList<>();
        for (Cell c : header) keys.add(c.getStringCellValue());

        while (rows.hasNext()) {
            Row r = rows.next();
            Map<String,String> map = new LinkedHashMap<>();
            for (int i = 0; i < keys.size(); i++) {
                Cell cell = r.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                map.put(keys.get(i), cell.getStringCellValue());
            }
            result.add(map);
        }
        return result;
    }

    public void close() throws IOException {
        workbook.close();
    }
}
