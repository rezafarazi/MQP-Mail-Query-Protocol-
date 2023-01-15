import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class App
{

    //Global server socket variable
    public static ServerSocket S_Socket;

    //Run port number
    public static int Port=15615;


    //main function start
    public static void main(String []args)
    {

        //Get Run MQP
        try
        {
            //Init new server socket
            S_Socket = new ServerSocket(Port);

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
    //main function end

}
