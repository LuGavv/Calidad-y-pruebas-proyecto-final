package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Utilidad para lectura de archivos CSV.
 * Lee datos de formato CSV y los retorna como List<Map<String,String>>.
 * 
 * Formato esperado:
 * Fila 1: Headers (separados por comas)
 * Fila 2+: Datos (separados por comas)
 */
public class CSVUtils {
    
    /**
     * Lee un archivo CSV y retorna como List<Map<String,String>>
     * @param filepath Ruta del archivo CSV
     * @return Lista de mapas (cada fila = un mapa)
     * @throws IOException Si no puede leer el archivo
     */
    public static List<Map<String,String>> readCSV(String filepath) throws IOException {
        List<Map<String,String>> result = new ArrayList<>();
        
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        
        if (lines.isEmpty()) {
            return result;
        }
        
        // Primera línea = headers
        String[] headers = parseCSVLine(lines.get(0));
        
        // Líneas siguientes = datos
        for (int i = 1; i < lines.size(); i++) {
            String[] values = parseCSVLine(lines.get(i));
            
            if (values.length != headers.length) {
                System.out.println("[WARNING] Fila " + (i + 1) + " tiene número diferente de columnas");
                continue;
            }
            
            Map<String,String> rowMap = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++) {
                rowMap.put(headers[j].trim(), values[j].trim());
            }
            result.add(rowMap);
        }
        
        return result;
    }
    
    /**
     * Parsea una línea CSV considerando comillas.
     * Maneja valores entre comillas que contienen comas.
     * @param line Línea a parsear
     * @return Array de valores
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}
