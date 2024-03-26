package Functions;

import Conf.Config;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TextEncript
{

    //Global static variables
    public static JSONArray JA;


    //Get initlize class function start
    public TextEncript()
    {
        try
        {
            File JsonFile=new File("EncriptList.json");
            BufferedReader BF=new BufferedReader(new FileReader(JsonFile));
            String AllText="",AppendText;
            while((AppendText=BF.readLine())!=null)
            {
                AllText+=AppendText.toString()+"\n";
            }
            BF.close();

            //Get view json
            GetViewJson(AllText);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
    //Get initlize class function end


    //Get view json start
    public void GetViewJson(String json)
    {
        JA=new JSONArray(json);
    }
    //Get view json end


    //Get encript text function start
    public static String TextEncript(String ip,String text)
    {
        if(Config.Encription)
        {
            //Get check ip key start
            String key = "";

            for (int i = 0; i < JA.length(); i++)
            {
                if (JA.getJSONObject(i).getString("ip").equals(ip))
                {
                    key = JA.getJSONObject(i).getString("key");
                    break;
                }
            }
            //Get check ip key end

            //Get encript start
            String strKey = key;
            try
            {
                SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
                String result=Base64.getEncoder().encodeToString(encryptedBytes);
                return result;
            }
            catch (Exception e)
            {
                System.out.println("Error : " + e.getMessage());
            }
            //Get encript end

            return "";
        }
        else
        {
            return text;
        }
    }
    //Get encript text function end


    //Get decript text function start
    public static String TextDecript(String ip,String text)
    {
        if(Config.Encription)
        {
            //Get check ip key start
            String key="";
            for(int i=0;i<JA.length();i++)
            {
                if(JA.getJSONObject(i).getString("ip").equals(ip))
                {
                    key=JA.getJSONObject(i).getString("key");
                }
            }
            //Get check ip key end


            //Get decrypt string start
            try
            {
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] decodedBytes = Base64.getDecoder().decode(text);
                byte[] decryptedBytes = cipher.doFinal(decodedBytes);
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            }
            catch (Exception e)
            {
                System.out.println("Error : "+e.getMessage());
            }
            //Get decrypt string end

            return "";
        }
        else
        {
            return text;
        }
    }
    //Get decript text function end


    //Get encript text function start
    public static String TextEncript(String text)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(Config.JEncrptKey.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    //Get encript text function end


    //Get decript text function start
    public static String TextDecript(String text)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(Config.JEncrptKey.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    //Get decript text function end


}
