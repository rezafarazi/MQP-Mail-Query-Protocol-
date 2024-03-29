package Conf;

import java.io.FileReader;
import java.util.Properties;

public class Config
{

    //Global variables
    public static String DomainAddress;
    public static String Root_Dir;
    public static String JEncrptKey;
    public static String HttpLogFileAddress;

    //Run port number
    public static int Port;
    public static int CheckPort;
    public static int FilePort;
    public static int HttpPort;
    public static int SeenPort;
    public static int FileSize;
    public static int FileSizeSpli;

    //Check enciption
    public static boolean Encription;

    public Config()
    {

        try
        {
            FileReader reader = new FileReader("Protocol.properties");
            Properties properties = new Properties();
            properties.load(reader);

            //Domain address conf
            DomainAddress=properties.getProperty("domain_address").toString();

            //Port conf
            Port=Integer.parseInt(properties.getProperty("port").toString());

            //Check port conf
            CheckPort=Integer.parseInt(properties.getProperty("check_port").toString());

            //File prot conf
            FilePort=Integer.parseInt(properties.getProperty("file_port").toString());

            //File size conf
            FileSize=Integer.parseInt(properties.getProperty("file_size_lim").toString());

            //File spli array size conf
            FileSizeSpli=Integer.parseInt(properties.getProperty("file_spli_size").toString());

            //Seen port conf
            SeenPort=Integer.parseInt(properties.getProperty("seen_delete_port").toString());

            //Http request response server port
            HttpPort=Integer.parseInt(properties.getProperty("http_port").toString());

            //Root folder conf
            Root_Dir=properties.getProperty("rootdir").toString();

            //Get http log file address
            HttpLogFileAddress=properties.getProperty("http_log_file_address").toString();

            //Json encript key
            JEncrptKey=properties.getProperty("http_user_encript_key").toString();

            //Encription transferdata
            Encription=Boolean.parseBoolean(properties.getProperty("encription").toString());

        }
        catch (Exception e)
        {
            //Print error
            System.out.println(e.getMessage());

            Port=15615;
        }

    }

}
