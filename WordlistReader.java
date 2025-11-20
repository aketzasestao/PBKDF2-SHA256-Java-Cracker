import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordlistReader {
    
    public static List<String> readWordlist(String filepath) {
        List<String> wordlist = new ArrayList<>();
        
        // Verificar que el archivo existe
        File file = new File(filepath);
        if (!file.exists()) {
            System.err.println("[-] Error: El archivo '" + filepath + "' no existe");
            return null;
        }
        
        // Verificar que es un archivo (no carpeta)
        if (!file.isFile()) {
            System.err.println("[-] Error: '" + filepath + "' no es un archivo");
            return null;
        }
        
        // Verificar que se puede leer
        if (!file.canRead()) {
            System.err.println("[-] Error: No tienes permisos para leer '" + filepath + "'");
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int i=0;
            while ((line = reader.readLine()) != null) {
                    wordlist.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("[-] Error al leer el archivo: " + e.getMessage());
            return null;
        }
        
        return wordlist;
    }
    
    public static long countLines(String filepath) {
        File file = new File(filepath);
        
        // Validaciones previas
        if (!file.exists()) {
            System.err.println("[-] Error: El archivo '" + filepath + "' no existe");
            return -1;
        }
        
        if (!file.isFile()) {
            System.err.println("[-] Error: '" + filepath + "' no es un archivo");
            return -1;
        }
        
        if (!file.canRead()) {
            System.err.println("[-] Error: No tienes permisos para leer '" + filepath + "'");
            return -1;
        }
        
        long lineCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            System.err.println("[-] Error al contar líneas: " + e.getMessage());
            return -1;
        }
        
        return lineCount;
    }
    
    public static void printFileInfo(String filepath) {
        File file = new File(filepath);
        
        if (!file.exists()) {
            System.err.println("[-] Error: El archivo no existe");
            return;
        }
        
        System.out.println("[+] Información del archivo:");
        System.out.println("    Ruta: " + file.getAbsolutePath());
        System.out.println("    Tamaño: " + (file.length() / 1024) + " KB");
        System.out.println("    Última modificación: " + new Date(file.lastModified()));
        
        long lines = countLines(filepath);
        if (lines > 0) {
            System.out.println("    Número de líneas: " + lines);
        }
    }
}