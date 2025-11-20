import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class PBKDF2Verifier {

    public static boolean verifyPassword(String password, String salt, 
                                         int iterations, String targetHash) {
        try {
            // Especificar el algoritmo y parámetros
            KeySpec spec = new PBEKeySpec(
                password.toCharArray(),  // contraseña
                salt.getBytes(),         // salt
                iterations,              // iteraciones
                256                      // longitud en bits (32 bytes)
            );
            
            // Crear la fábrica con SHA256
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            
            // Generar el hash
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // Convertir a hexadecimal para comparar
            String generatedHash = bytesToHex(hash);
            
            return generatedHash.equalsIgnoreCase(targetHash);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}