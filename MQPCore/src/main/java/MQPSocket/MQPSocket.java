package MQPSocket;

import Conf.Config;
import Models.users_tbl;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MQPSocket
{

    //Static variables
    public static ServerSocket S_Socket;



    //Constractor start
    public MQPSocket()
    {

        //Get Run MQP
        try
        {
            //Init new server socket
            S_Socket = new ServerSocket(Config.Port);

            //Print server socket is started
            System.out.println("Socket is ready");

            while(true)
            {

                //Get accept request socket
                Socket client_socket = S_Socket.accept();
                System.out.println("New request");


                //Create a socket send and resvice instance
                DataInputStream DIS = new DataInputStream(client_socket.getInputStream());
                DataOutputStream DOS = new DataOutputStream(client_socket.getOutputStream());


                //Socket thread work with multi sockets
                //Socket thread start
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //Get read condition
                            byte []res=new byte[4094];
                            DIS.read(res);
                            String resvice=new String(res);
                            System.out.println(resvice);
                            JSONObject Data = new JSONObject(resvice);
                            String Condition=Data.get("Condition").toString();


                            //Get new message email exist
                            if(Condition.equals("NEWMAIL"))
                            {
                                NewMailCondition(client_socket,DIS,DOS,Data);
                            }
                            else if(Condition.equals("NEWFILES"))
                            {
                                NewFilesCondition(client_socket,DIS,DOS,Data);
                            }



                        }
                        catch (Exception e)
                        {
                            //Print error condition
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
                //Socket thread end

            }

        }
        catch (Exception e)
        {
            //Print error condition
            System.out.println(e.getMessage());
        }

    }
    //Constractor end




    //Get resivce new files start
    void NewFilesCondition(Socket socket,DataInputStream DIS,DataOutputStream DOS,JSONObject Data)
    {
        try
        {
            System.out.println("New Condition");
            users_tbl user = new Users_Service().GetUserByUsername(Data.get("TO").toString());
            new Mail_Service().InsertnewMail(Data.get("TITLE").toString(),
                    Data.get("CONTENT").toString(),
                    user ,
                    Data.get("FROM").toString()
            );
            System.out.println("Submit");
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
    //Get resivce new files end



    //Get new condition function start
    void NewMailCondition(Socket socket,DataInputStream DIS,DataOutputStream DOS,JSONObject Data)
    {
        try
        {
            System.out.println("New Condition");
            users_tbl user = new Users_Service().GetUserByUsername(Data.get("TO").toString());
            new Mail_Service().InsertnewMail(Data.get("TITLE").toString(),
                    Data.get("CONTENT").toString(),
                    user ,
                    Data.get("FROM").toString()
            );
            System.out.println("Submit");
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
    //Get new condition function end





}
