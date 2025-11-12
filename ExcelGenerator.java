import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Generador de archivo Excel de datos de prueba.
 * Ejecutar como: mvn exec:java -Dexec.mainClass="ExcelGenerator"
 */
public class ExcelGenerator {
    
    public static void main(String[] args) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("ProductosBusqueda");
            
            // Crear headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Categoria", "SubCategoria", "Producto", "Cantidad"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Crear datos de ejemplo
            String[][] data = {
                {"Software", "Office", "MacBook", "1"},
                {"Software", "Databases", "Microsoft SQL Server", "1"},
                {"Phones & PDAs", "Phones", "iPhone", "2"}
            };
            
            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data[i][j]);
                }
            }
            
            // Guardar archivo
            String filepath = "src/test/resources/inputData.xlsx";
            FileOutputStream fos = new FileOutputStream(filepath);
            workbook.write(fos);
            fos.close();
            workbook.close();
            
            System.out.println("✅ inputData.xlsx creado exitosamente");
            System.out.println("   Ubicación: " + filepath);
            System.out.println("   Filas: " + (data.length + 1) + " (incluyendo headers)");
            System.out.println("   Columnas: " + headers.length);
        } catch (IOException e) {
            System.err.println("❌ Error al crear Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
