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


        //Get encript start
        String strKey=key;
        String strClearText=text;
        String strData;
        try
        {
            SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
            Cipher cipher= Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(strClearText.getBytes());
            strData=new String(encrypted);

            return strData;
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
        //Get encript end


        return "";
    }
    //Get encript text function end


    //Get decript text function start
    public static String TextDecript(String ip,String text)
    {
        //Get check ip key start
        String key="";
        for(int i=0;i<JA.length();i++)
        {
            if(JA.getJSONObject(i).getString("ip").equals(""))
            {
                key=JA.getJSONObject(i).getString("key");
            }
        }
        //Get check ip key end


        //Get decrypt string start
        String strData;
        try
        {
            SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted=cipher.doFinal(text.getBytes());
            strData=new String(decrypted);

            return strData;
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
        //Get decrypt string end


        return "";
    }
    //Get decript text function end


}
