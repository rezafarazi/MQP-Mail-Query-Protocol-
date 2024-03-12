import Conf.Config;
import Functions.TextEncript;
import Http.HttpServer;
import Models.mail_tbl;
import Models.users_tbl;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import org.json.JSONObject;
import MQPSocket.MQPSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class App
{
    //main function start
    public static void main(String []args)
    {

        //Get read properties values
        new Config();

        //Get initilize encription keys
        new TextEncript();

        //Get start http server
        new HttpServer();

        //Get run MQP socket
        new MQPSocket();

    }
    //main function end

}
