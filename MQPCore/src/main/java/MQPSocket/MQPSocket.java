package MQPSocket;

import Conf.Config;
import Models.files_tbl;
import Models.mail_files_tbl;
import Models.mail_tbl;
import Models.users_tbl;
import Services.FileManager.FileManager_Service;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
            System.out.println("New File Condition");

            JSONArray ALLFiles=new JSONArray(Data.get("FileList").toString());
            mail_tbl mail=new Mail_Service().GetMailById(Integer.parseInt(Data.get("MAILID").toString()));
            JSONArray want_files=new JSONArray();

            //Get check all share files hash exist in system start
            for(int i=0;i<ALLFiles.length();i++)
            {
                JSONObject FilesData=new JSONObject(ALLFiles.get(i).toString());
                boolean FileExist = new FileManager_Service().FileExist(FilesData.get("File_Hash").toString());
                if(FileExist)
                {
                    files_tbl files = new FileManager_Service().GetFileTbl(FilesData.get("File_Hash").toString());

                    //Check exist file in storage
                    boolean check_exist_file=new File(files.getAddress()).exists();

                    if(check_exist_file)
                    {
                        mail_files_tbl mailfile = new mail_files_tbl(
                                files,
                                mail,
                                1
                        );
                    }
                    else
                    {
                        //If file not exist in storage download again
                        //want_files.put(new JSONObject("File_Hash",FilesData.get("File_Hash").toString()));
                        want_files.put(FilesData.get("File_Hash").toString());
                    }

                }
                else
                {
                    //If file not exist in database get download
                    want_files.put(FilesData.get("File_Hash").toString());
                }
            }
            //Get check all share files hash exist in system end

            JSONObject result=new JSONObject();
            result.put("File_Hash",want_files);
            DOS.write(result.toString().getBytes());

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
            System.out.println("New Mail Condition");
            users_tbl user = new Users_Service().GetUserByUsername(Data.get("TO").toString());
            mail_tbl mail = new Mail_Service().InsertnewMail(Data.get("TITLE").toString(),
                    Data.get("CONTENT").toString(),
                    user ,
                    Data.get("FROM").toString()
            );

            //Send mail id start
            JSONObject JO=new JSONObject();
            JO.put("Status","Success");
            JO.put("MailId",mail.getId());
            DOS.write(JO.toString().getBytes());
            //Send mail id end

            System.out.println("Submit");
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
    //Get new condition function end





}
