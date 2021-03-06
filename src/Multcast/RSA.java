package Multcast;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

public class RSA {

    public PublicKey publicKey;
    public PrivateKey privateKey;

    public RSA() {
        KeyPair pair = getPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

//        System.out.println("PUBLIC_KEY");
//        System.out.println(publicKey);
//        
//        System.out.println("---");
//        System.out.println("PRIVATE_KEY");
//        System.out.println(privateKey);
//
//        System.out.println("\n---\n");
//
//        System.out.println("ENCRYPTED_MESSAGE");
//        byte[] encrypted = encrypt("Olá a todos!");
//        System.out.println(new String(encrypted));
//        
//        System.out.println("---");
//        System.out.println("DECRYPTED_MESSAGE");
//        byte[] decrypted = decrypt(encrypted);
//        System.out.println(new String(decrypted));
    }

    public byte[] encrypt(String message) {
        return encrypt(privateKey, message);
    }

    public byte[] decrypt(byte[] encrypted) {
        return decrypt(publicKey, encrypted);
    }

    /**
     * Encripta uma mensagem com a chave privada e retorna os Bytes.
     */
    public static byte[] encrypt(PrivateKey privateKey, String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Decripta uma mensagem com a chave publica e retorna os Bytes.
     */
    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Gera a instancia de pareamento para criar as chaves publicas e privadas.
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getPair() {
        try {
            final int keySize = 2048;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.genKeyPair();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return null;
    }

    public String publicKeyBase64() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static PublicKey publicKeyByBase64(String publicKB64) {
        byte[] pkBytes2 = Base64.getDecoder().decode(publicKB64);

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pkBytes2);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

}
