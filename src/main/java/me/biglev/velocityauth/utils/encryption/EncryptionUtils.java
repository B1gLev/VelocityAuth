package me.biglev.velocityauth.utils.encryption;

import at.favre.lib.crypto.bcrypt.BCrypt;
import me.biglev.velocityauth.utils.settings.Manager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncryptionUtils {

    public static String hashPassword(String password, Type type){
        switch (type) {
            case SHA256:
                return sha256(password);
            case BCRYPT:
                return BCrypt.withDefaults().hashToString(Manager.getSettings().getSecurity().getBcryptRounds(), password.toCharArray());
        }
        return null;
    }

    public static boolean verifyPassword(String password, String hash, Type type) {
        switch (type) {
            case SHA256:
                return hashPassword(password, type).equalsIgnoreCase(hash);
            case BCRYPT:
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
                return result.verified;
        }
        return false;
    }

    private static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
