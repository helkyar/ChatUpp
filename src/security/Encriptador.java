/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 
 * La clase Encriptador se encarga de asegurar las contraseñas introducidas 
 * por los usuarios y de comprobar si las contraseñas introducidas son iguales 
 * a las contraseñas encriptadas en la base de datos.
 * 
 * private static String generateStorngPasswordHash(String contrasenya)
 * -> dada una contraseña, devuelve su versión encriptada
 * 
 * public static boolean validadorContrasenya(String password, String storedPassword) 
 * -> dada una contraseña plana y una contraseña encriptada, devuelve si son
 * iguales.
 * 
 * @author Houssam Amrouch, Cristian Echauri
 */
public class Encriptador {
    private String password;
    private String hashedPassword;
    
    private static boolean devmode = false;
    
    // 1.1.-Metodo de encriptacion 
    public Encriptador(String contrasenyaOriginal){
        
        this.password = contrasenyaOriginal;
        //Metodo (GeneradorSeguroContrasenyaHash) de encriptacion "SHA1PRNG"
        try {
            this.hashedPassword = generateStorngPasswordHash(contrasenyaOriginal);
            if(devmode) System.out.println("generatedSecuredPasswordHash = " + this.hashedPassword);
        } catch ( Exception ex){
            //NoSuchAlgorithmException || InvalidKeySpecException
            this.hashedPassword = "";
            if(devmode) System.out.println(ex.getMessage());
        }
        
    }
    
    //1.2.-metodo de encriptacion
    private static String generateStorngPasswordHash(String contrasenya) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = contrasenya.toCharArray();
        byte[] salt = getSalt();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
    
    //1.3.-algorimo de encriptacion
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    //1.4.-ni puta idea
    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    //2.2.-Validacion de la contraseña
    public static boolean validadorContrasenya(String password, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    //2.3.-ni puta idea
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    
    
}
