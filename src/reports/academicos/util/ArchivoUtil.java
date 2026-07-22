package reports.academicos.util;

import java.io.*;
import java.util.*;

public class ArchivoUtil {
    
    public static void guardarArchivo(String nombreArchivo, List<String> lineas) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdir();
                System.out.println("Carpeta 'datos' creada automáticamente");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("datos/" + nombreArchivo))) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(" Error al guardar " + nombreArchivo);
        }
    }
    
    public static List<String> leerArchivo(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File archivo = new File("datos/" + nombreArchivo);
        
        if (!archivo.exists()) {
            return lineas;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println(" Error al leer " + nombreArchivo);
        }
        return lineas;
    }
}