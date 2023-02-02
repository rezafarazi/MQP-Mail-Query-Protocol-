package MQPSocket;

import Conf.Config;
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
                            JSONObject Data = new JSONObject(DIS.readUTF());
                            String Condition=Data.get("Condition").toString();


                            //Get new message email exist
                            if(Condition.equals("NEW"))
                            {
                                NewCondition(client_socket,DIS,DOS);
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



    //Get new condition function start
    void NewCondition(Socket socket,DataInputStream DIS,DataOutputStream DOS)
    {



    }
    //Get new condition function end





}
