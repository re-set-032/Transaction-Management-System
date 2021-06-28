package sample;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class Cryptography {

        public static String generateHash(byte[] pass,String algo)
        {
            String hash = null;
            try{
                MessageDigest messageDigest = MessageDigest.getInstance(algo);
                messageDigest.update(pass);
                byte[] digestedPass = messageDigest.digest();
                hash = DatatypeConverter.printHexBinary(digestedPass);
            }
            catch (Exception e)
            {
                System.out.println("Error in hashing password");
                e.printStackTrace();
            }

            return hash;
        }

}
