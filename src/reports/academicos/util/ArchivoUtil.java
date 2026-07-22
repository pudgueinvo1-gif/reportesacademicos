package reports.academicos.util;

import java.io.*;
import java.util.*;

public class ArchivoUtil {
    
    public static void guardarArchivo(String nombreArchivo, String[] lineas) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdir();
                System.out.println("Carpeta 'datos' creada automaticamente");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("datos/" + nombreArchivo))) {
                for (String linea : lineas) {
                    if (linea != null && !linea.trim().isEmpty()) {
                        writer.write(linea);
                        writer.newLine();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo no encontrado - " + nombreArchivo);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR: Error al escribir el archivo - " + nombreArchivo);
            e.printStackTrace();
        } catch (SecurityException e) {
            System.err.println("ERROR: Permiso denegado - " + nombreArchivo);
            e.printStackTrace();
        }
    }
    
    public static String[] leerArchivo(String nombreArchivo) {
        List<String> lineasList = new ArrayList<>();
        File archivo = new File("datos/" + nombreArchivo);
        
        if (!archivo.exists()) {
            return new String[0];
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineasList.add(linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo no encontrado - " + nombreArchivo);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR: Error al leer el archivo - " + nombreArchivo);
            e.printStackTrace();
        }
        return lineasList.toArray(new String[0]);
    }
}