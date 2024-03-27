package Http.File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Random;

public class HTTPFiles
{

    //Get create file from upload stream start
    public static void CreateFile(byte []value,String FileExtention)
    {
        try
        {
            Random rand=new Random();
            File UploadedFile=new File("C:\\Users\\Rezafta\\Desktop\\"+rand.nextInt(1000000)+"."+FileExtention);
            if(UploadedFile.createNewFile())
            {
                FileOutputStream fos = new FileOutputStream(UploadedFile);
                fos.write(value);

                System.out.println("File uploaded");
            }
        }
        catch (Exception e)
        {
            System.out.println("Http File Error "+e.getMessage());
        }
    }
    //Get create file from upload stream end

}
