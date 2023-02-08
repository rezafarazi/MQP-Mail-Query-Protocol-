package Conf;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Properties;

public class Config
{
    public static String Root_Dir;

    //Run port number
    public static int Port;
    public static int FilePort;
    public static int SeenPort;

    public Config()
    {

        try
        {
            FileReader reader = new FileReader("Protocol.properties");
            Properties properties = new Properties();
            properties.load(reader);

            //Prot conf
            Port=Integer.parseInt(properties.getProperty("port").toString());

            //File prot conf
            FilePort=Integer.parseInt(properties.getProperty("file_port").toString());

            //Seen port conf
            SeenPort=Integer.parseInt(properties.getProperty("seen_delete_port").toString());

            //Root folder conf
            Root_Dir=properties.getProperty("rootdir").toString();

        }
        catch (Exception e)
        {
            //Print error
            System.out.println(e.getMessage());

            Port=15615;
        }

    }

}
