package nileuniversity.masters.project.filemanager.utils;

import com.softobt.core.exceptions.models.RestServiceException;
import com.softobt.core.logger.services.LoggerService;

import java.security.MessageDigest;

/**
 * @author aobeitor
 * @since 8/10/20
 */
public class HashingUtil {
    public static String applySHA256(String input)throws RestServiceException{
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return hasher(hash);
        }
        catch(Exception e) {
            LoggerService.severe(e);
            throw new RestServiceException("Failed to encode input.");
        }
    }

    public static String applySHA512(String input)throws RestServiceException{
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return hasher(hash);
        }
        catch(Exception e) {
            LoggerService.severe(e);
            throw new RestServiceException("Failed to encode input.");
        }
    }

    private static String hasher(byte[] hash){
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
