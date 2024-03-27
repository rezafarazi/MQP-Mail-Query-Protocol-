package Http.File;

import Conf.Config;

import java.io.*;
import java.util.Random;

public class HTTPFiles
{

    //Get create file from upload stream start
    public static void CreateFile(byte []value,String FileExtention)
    {
        try
        {
            //Get remove headers
            byte []LastFile=RemoveFileHeader(value);

            Random rand=new Random();
            String Filename=rand.nextInt(1000000)+"";
            File UploadedFile=new File(Config.Root_Dir +"\\"+Filename+"."+FileExtention);
            if(UploadedFile.createNewFile())
            {
                FileOutputStream fos = new FileOutputStream(UploadedFile);
                fos.write(LastFile);
                fos.close();
                System.out.println("File uploaded");
            }
        }
        catch (Exception e)
        {
            System.out.println("Http File Error "+e.getMessage());
        }
    }
    //Get create file from upload stream end


    //Get remove http header start
    public static byte[] RemoveFileHeader(byte []value)
    {
        String all_bytes = new String(value);

        int removed_char=all_bytes.lastIndexOf("Content-Type:")+28;
        for(int i=0;i<1500;i++)
        {
            if(value[i] == -1)
            {
                removed_char=i;
                break;
            }

//            System.out.println("b "+i+" is "+value[i]);
        }

        byte []LastFile=new byte[value.length - removed_char];
//        System.out.println("Removed "+removed_char);
        for(int i=removed_char;i<value.length;i++)
        {
            LastFile[i-removed_char]=value[i];
        }

        return LastFile;
    }
    //Get remove http header end


}
