package Functions;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Hash_Lib
{

    //SHA 256 start
    public static String SHA256(String text)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            String result = new String(messageDigest.digest());
            return result;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    //SHA 256 end


    //SHA 256 start
    public static String SHA256(File file)
    {
        try
        {
            FileInputStream fis=new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(fis.readAllBytes());
            String result = new String(messageDigest.digest());
            return result;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    //SHA 256 end


    //MD5 start
    public static String MD5(String text)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            String result = new String(messageDigest.digest());
            return result;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    //MD5 end


    //MD5 start
    public static String MD5(File file)
    {
        try
        {
            FileInputStream fis=new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(fis.readAllBytes());
            String result = new String(messageDigest.digest());
            return result;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    //MD5 end

}