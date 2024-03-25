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
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MQPSocket
{

    //Static variables
    public static ServerSocket S_Socket;
    public static ServerSocket Check_Socket;
    public static ServerSocket File_S_Socket;
    public static ServerSocket Seen_Delete_S_Socket;


    //Constractor start
    public MQPSocket()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Get start mail socket
                MailSocket();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CheckSocket();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get start file socket
                FileSocket();

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get start seen socket
                SeenSocket();

            }
        }).start();

    }
    //Constractor end

    //Get Mail Socket start
    void MailSocket()
    {
        //Get Run MQP
        try
        {
            //Init new server socket
            S_Socket = new ServerSocket(Config.Port);

            //Print server socket is started
            System.out.println("Socket is ready on "+Config.Port);


            while(true)
            {

                //Get accept request socket
                Socket client_socket = S_Socket.accept();
                System.out.println("New request");


                //Create a socket send and resvice instance
                DataInputStream DIS = new DataInputStream(client_socket.getInputStream());
                DataOutputStream DOS = new DataOutputStream(client_socket.getOutputStream());


                //Socket thread work with multi sockets
                //Mail Socket thread start
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {

                            //Get ready receive len
                            byte []inp_len=new byte[1024];
                            DIS.read(inp_len);
                            String count_len=new String(inp_len);
                            int Mesage_Len=Integer.parseInt(count_len.trim());
                            System.out.println("Ready to receive "+Mesage_Len+" Len");

                            //Get send ready
                            DOS.write("Ready".getBytes());

                            //Get read condition
                            byte []res=new byte[Mesage_Len];
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
                            else if(Condition.equals("UPDATEMAIL"))
                            {
                                UpdateMailCondition(client_socket,DIS,DOS,Data);
                            }

                            client_socket.close();

                        }
                        catch (Exception e)
                        {
                            //Print error condition
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
                //Mail Socket thread end


            }

        }
        catch (Exception e)
        {
            //Print error condition
            System.out.println("Error : Mail port : "+e.getMessage());
        }
    }
    //Get Mail Socket end

    //Get Check Socket start
    void CheckSocket()
    {
        try
        {
            //Init new server socket
            Check_Socket = new ServerSocket(Config.CheckPort);

            //Print server socket is started
            System.out.println("Socket is ready on "+Config.Port);

            while (true)
            {
                //Get accept request socket
                Socket client_socket = Check_Socket.accept();
                System.out.println("New Check request");

                //Create a socket send and resvice instance
                DataInputStream DIS = new DataInputStream(client_socket.getInputStream());
                DataOutputStream DOS = new DataOutputStream(client_socket.getOutputStream());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            //Get resicve a username
                            byte []usernamebyte=new byte[1024];
                            DIS.read(usernamebyte);
                            String res_value=new String(usernamebyte);
                            res_value=res_value.trim();

                            //Get convert resive value to json object
                            JSONObject inp=new JSONObject(res_value);

                            //result
                            JSONObject result=new JSONObject();

                            //Get check user
                            if(new Users_Service().CheckUserExist(inp.get("USERNAME").toString()))
                            {
                                users_tbl user=new Users_Service().GetUserByUsername(inp.get("USERNAME").toString());

                                result.put("USER",true);
                                result.put("USERNAME",user.getUsername());
                                result.put("NAME",user.getName());
                                result.put("FAMILY",user.getFamily());
                                result.put("EMAIL",user.getEmail());
                                result.put("PHONE",user.getPhone());
                            }
                            else
                            {
                                result.put("USER",false);
                            }

                            //send result
                            DOS.write(result.toString().getBytes());

                            //Close sockets
                            DIS.close();
                            DOS.close();
                            client_socket.close();

                        }
                        catch (Exception e)
                        {
                            //Print error condition
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();

            }

        }
        catch (Exception e)
        {
            //Print error condition
            System.out.println("Error : Check port "+e.getMessage());
        }
    }
    //Get Check Socket end

    //Get File Socket start
    void FileSocket()
    {
        //Get Run MQP
        try
        {
            //Init new server socket
            File_S_Socket = new ServerSocket(Config.FilePort);


            //Print server socket is started
            System.out.println("File Socket is ready on "+Config.FilePort);


            while(true)
            {

                //Get accept request socket
                Socket client_socket = File_S_Socket.accept();
                System.out.println("New File request");


                //Create a socket send and resvice instance
                DataInputStream DIS = new DataInputStream(client_socket.getInputStream());
                DataOutputStream DOS = new DataOutputStream(client_socket.getOutputStream());


                //Socket thread work with multi sockets
                //File Socket thread start
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //Get read condition
                            long size=DIS.readLong();
                            byte []file_name_byte=new byte[Config.FileSize];
                            DIS.read(file_name_byte);
                            String File_name = new String(file_name_byte);

                            File f=new File(Config.Root_Dir+"/"+File_name);
                            FileOutputStream FOS=new FileOutputStream(f);

                            byte []buffer=new byte[Config.FileSize];
                            int bytes=0;
                            while(size > 0 && (bytes=DIS.read(buffer,0,buffer.length))!=-1){
                                FOS.write(buffer,0,bytes);
                                size-=bytes;
                            }

                            client_socket.close();

                        }
                        catch (Exception e)
                        {
                            //Print error condition
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
                //File Socket thread end

            }

        }
        catch (Exception e)
        {
            //Print error condition
            System.out.println("Error : File port "+e.getMessage());
        }
    }
    //Get File Socket end

    //Get File Socket start
    void SeenSocket()
    {
        //Get Run MQP
        try
        {
            //Init new server socket
            Seen_Delete_S_Socket = new ServerSocket(Config.SeenPort);


            //Print server socket is started
            System.out.println("Seen Socket is ready on "+Config.SeenPort);


            while(true)
            {

                //Get accept request socket
                Socket client_socket = Seen_Delete_S_Socket.accept();
                System.out.println("New Seen request");

                //Create a socket send and resvice instance
                DataInputStream DIS = new DataInputStream(client_socket.getInputStream());
                DataOutputStream DOS = new DataOutputStream(client_socket.getOutputStream());

                //Socket thread work with multi sockets
                //File Socket thread start
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //Get read condition
                            byte []res=new byte[4096];
                            DIS.read(res);
                            String resvice=new String(res);
                            //System.out.println("Resived value "+resvice.trim());
                            JSONObject Data = new JSONObject(resvice.trim());
                            int MailId=Data.getInt("MailId");

                            //Result
                            JSONObject result=new JSONObject();

                            if(Data.get("Condition").toString().equals("SEEN"))
                            {
                                //Get mail
                                mail_tbl Mail=new Mail_Service().GetMailById(MailId);

                                System.out.println("IP is "+client_socket.getInetAddress().toString().replace("/",""));

                                //Check from server ip address
                                if(Mail.getFrom_Ip().equals(client_socket.getInetAddress().toString().replace("/","")))
                                {
                                    if (new Mail_Service().SeenMail(MailId))
                                    {
                                        result.put("Status", "OK");
                                        //System.out.println("Delete ok");
                                    }
                                    else
                                    {
                                        result.put("Status", "Bad Id");
                                        //System.out.println("Bad Id");
                                    }
                                }
                                else
                                {
                                    result.put("Status", "Bad Id");
                                }
                            }
                            else if(Data.get("Condition").toString().equals("DELETE"))
                            {
                                //Get mail
                                mail_tbl Mail=new Mail_Service().GetMailById(MailId);

                                System.out.println("IP is "+client_socket.getInetAddress().toString().replace("/",""));

                                //Check from server ip address
                                if(Mail.getFrom_Ip().equals(client_socket.getInetAddress().toString().replace("/","")))
                                {
                                    if (new Mail_Service().DeleteMail(MailId))
                                    {
                                        result.put("Status", "OK");
                                        //System.out.println("Delete ok");
                                    }
                                    else
                                    {
                                        result.put("Status", "Bad Id");
                                        //System.out.println("Bad Id");
                                    }
                                }
                                else
                                {
                                    result.put("Status", "Bad Id");
                                }
                            }

                            //Get send result
                            DOS.write(result.toString().getBytes());

                            //Close socket
                            DIS.close();
                            DOS.close();
                            client_socket.close();

                        }
                        catch (Exception e)
                        {
                            //Print error condition
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
                //File Socket thread end

            }

        }
        catch (Exception e)
        {
            //Print error condition
            System.out.println("Error : Seen port "+e.getMessage());
        }
    }
    //Get File Socket end

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
            users_tbl user = new Users_Service().GetUserByUsername(Data.get("TO").toString().split("@")[0]);
            mail_tbl mail = new Mail_Service().InsertnewMail(Data.get("TITLE").toString(),
                    Data.get("CONTENT").toString(),
                    user ,
                    Data.get("FROM").toString(),
                    Data.get("TO").toString(),
                    socket.getInetAddress().toString().replace("/","")
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

    //Get update condition function start
    void UpdateMailCondition(Socket socket,DataInputStream DIS,DataOutputStream DOS,JSONObject Data)
    {
        try
        {
            System.out.println("Update Mail Condition");
            users_tbl user = new Users_Service().GetUserByUsername(Data.get("TO").toString());
            mail_tbl mail = new Mail_Service().UpdatenewMail(
                    Data.getInt("MailId"),
                    Data.get("TITLE").toString(),
                    Data.get("CONTENT").toString(),
                    user ,
                    Data.get("FROM").toString(),
                    socket.getInetAddress().toString()
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
    //Get update condition function end
    
    
    
    
    
    /*******************************************************************************************************************************************/
    
    
    
    //Send new mail function start
    public static String SendMQPMail(String address,String To,String From,String Title,String Content)
    {

        try
        {
            //New mail exmaple format
            //{"Condition":"NEWMAIL","TO":"Rezafta","FROM":"Rezaftaturk","CONTENT":"Cont","TITLE":"ONVAN"}

            //Get initlitze mqp socket
            Socket SendSocket = new Socket(address, Config.Port);
//            InetSocketAddress socketAddress = (InetSocketAddress) SendSocket.getRemoteSocketAddress();
//            String ServerIP=socketAddress.getAddress().getHostAddress();
            String ServerIP=SendSocket.getInetAddress().toString().replace("/","");
            System.out.println("Connected to "+address);

            //Get initlitze stream on socket
            DataInputStream DIS=new DataInputStream(SendSocket.getInputStream());
            DataOutputStream DOS=new DataOutputStream(SendSocket.getOutputStream());

            JSONObject SendValue=new JSONObject();
            SendValue.put("Condition","NEWMAIL");
            SendValue.put("TO",To);
            SendValue.put("FROM",From);
            SendValue.put("TITLE",Title);
            SendValue.put("CONTENT",Content);

            //Get send string length
            DOS.write((SendValue.toString().trim().length()+"").getBytes());

            //Get check
            byte check[]=new byte[20];
            DIS.read(check);
            System.out.println("Condition is "+(new String(check)).trim());

            //Send mail to server
            DOS.write(SendValue.toString().getBytes());

            //Get mail condition
            byte []readvalue=new byte[120];
            DIS.read(readvalue);
            String result=new String(readvalue);

            JSONObject server_result=new JSONObject(result.trim());

            //Close socket
            DIS.close();
            DOS.close();
            SendSocket.close();

            return Integer.parseInt(server_result.get("MailId").toString())+"-"+ServerIP;

        }
        catch (Exception e)
        {
            System.out.println("Error : Send socket -> "+e.getMessage());
        }

        return "-";

    }
    //Send new mail function end

    //Get Check Socket start
    public static ArrayList<Object> CheckUser(String address)
    {
        ArrayList result=new ArrayList();

        try
        {
            //Get initlitze mqp socket
            Socket SendSocket = new Socket(address.split("@")[1], Config.Port);
            System.out.println("Connected to "+address.split("@")[1]);

            //Get initlitze stream on socket
            DataInputStream DIS=new DataInputStream(SendSocket.getInputStream());
            DataOutputStream DOS=new DataOutputStream(SendSocket.getOutputStream());

            //Create send value
            JSONObject send_value=new JSONObject();
            send_value.put("USERNAME",address.split("@")[0]);

            //Send
            DOS.write(send_value.toString().getBytes());

            //GetCheck
            byte GetCheckBytes[]=new byte[1024];
            String GetCheck=new String(GetCheckBytes);
            GetCheck=GetCheck.trim();
            JSONObject CheckValue=new JSONObject(GetCheck);

            //Get check user exist
            if(CheckValue.getBoolean("USER"))
            {
                result.add(true);
                result.add(CheckValue.toString());
            }
            else
            {
                result.add(false);
            }

            //Close socket
            DIS.close();
            DOS.close();
            SendSocket.close();

            return result;

        }
        catch (Exception e)
        {
            System.out.println("Error : Send socket -> "+e.getMessage());
        }

        result.add(false);
        return result;

    }
    //Get Check Socket ene
    
    //Get delete mail start
    public static boolean DeleteMail(String address,String mail_id)
    {
        try
        {
            //Get initlitze mqp socket
            Socket SendSocket = new Socket(address.split("@")[1], Config.SeenPort);
            System.out.println("Connected to "+address.split("@")[1]);

            //result
            boolean result=false;

            //Get initlitze stream on socket
            DataInputStream DIS=new DataInputStream(SendSocket.getInputStream());
            DataOutputStream DOS=new DataOutputStream(SendSocket.getOutputStream());

            //Create send value
            JSONObject send_value=new JSONObject();
            send_value.put("Condition","DELETE");
            send_value.put("MailId",mail_id);

            //Send
            DOS.write(send_value.toString().getBytes());

            //GetCheck
            byte GetCheckBytes[]=new byte[1024];
            DIS.read(GetCheckBytes);
            String GetCheck=new String(GetCheckBytes);
            GetCheck=GetCheck.trim();
            System.out.println(GetCheck);
            JSONObject CheckValue=new JSONObject(GetCheck);

            if(CheckValue.get("Status").toString().equals("OK"))
            {
                result=true;
            }

            //Close socket
            DIS.close();
            DOS.close();
            SendSocket.close();

            return result;

        }
        catch (Exception e)
        {
            System.out.println("Error : Send socket -> "+e.getMessage());
        }

        return false;

    }
    //Get delete mail end

    //Get seen mail start
    public static boolean SeenMail(String address,String mail_id)
    {
        try
        {
            //Get initlitze mqp socket
            Socket SendSocket = new Socket(address.split("@")[1], Config.SeenPort);
            System.out.println("Connected to "+address.split("@")[1]);

            //result
            boolean result=false;

            //Get initlitze stream on socket
            DataInputStream DIS=new DataInputStream(SendSocket.getInputStream());
            DataOutputStream DOS=new DataOutputStream(SendSocket.getOutputStream());

            //Create send value
            JSONObject send_value=new JSONObject();
            send_value.put("Condition","SEEN");
            send_value.put("MailId",mail_id);

            //Send
            DOS.write(send_value.toString().getBytes());

            //GetCheck
            byte GetCheckBytes[]=new byte[1024];
            DIS.read(GetCheckBytes);
            String GetCheck=new String(GetCheckBytes);
            GetCheck=GetCheck.trim();
            System.out.println(GetCheck);
            JSONObject CheckValue=new JSONObject(GetCheck);

            if(CheckValue.get("Status").toString().equals("OK"))
            {
                result=true;
            }

            //Close socket
            DIS.close();
            DOS.close();
            SendSocket.close();

            return result;

        }
        catch (Exception e)
        {
            System.out.println("Error : Send socket -> "+e.getMessage());
        }

        return false;

    }
    //Get seen mail end
    

}
