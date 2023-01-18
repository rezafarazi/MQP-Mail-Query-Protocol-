package Functions;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class File_Hash_Lib
{

    //Get hash file start
    public static String HashFile(File file)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(file.toPath())) {
                try (DigestInputStream dis = new DigestInputStream(is, md)) {
                    /* Read decorated stream (dis) to EOF as normal... */
                }
            }
            byte[] digest = md.digest();
            return new String(digest.toString());
        }
        catch (Exception e)
        {
            System.out.println("Error Hash : "+e.getMessage());
            return "";
        }

    }
    //Get hash file end

}
