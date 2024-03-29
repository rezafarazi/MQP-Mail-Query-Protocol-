package Http;

import Conf.Config;
import Functions.TextEncript;
import Http.File.HTTPFiles;
import Http.Models.FileResponseModel;
import Http.Models.ResponseModel;
import Http.Models.UserAuthModel;
import com.sun.net.httpserver.Headers;
import org.json.HTTP;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.UUID;


public class HttpServer
{

    //Global variables
    ServerSocket HttpSocket;


    //Constrator function start
    public HttpServer()
    {

        //Get start http sever
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get start http
                BeginHttp();

            }
        }).start();

    }
    //Constrator function end

    //Get begin server socket start
    public void BeginHttp()
    {
        try
        {

            //Get initilze server socket
            HttpSocket = new ServerSocket(Config.HttpPort);

            //Get Log
            System.out.println("Http socket is ready on port "+Config.HttpPort);

            //Get wait for request
            while(true)
            {
                //Get new request
                Socket request=HttpSocket.accept();

                //Get Log
                //InetSocketAddress socketAddress = (InetSocketAddress) request.getRemoteSocketAddress();
                //System.out.println("Ip is "+socketAddress.getAddress().getHostAddress());

                //Get handle multi request
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //Get handle request
                        GetSocketHandller(request);

                    }
                }).start();

            }

        }
        catch (Exception e)
        {
            //Get error log
            System.out.println("Http Sever Error : "+e.getMessage());
        }
    }
    //Get begin server socket end


    public void GetSocketHandller(Socket request)
    {
        try
        {

            //Get Log
//        System.out.println("New Request from "+request.getRemoteSocketAddress().toString());

            //Get request and response data stream
            DataInputStream input = new DataInputStream(request.getInputStream());
            DataOutputStream output = new DataOutputStream(request.getOutputStream());

            //Get read request from user
            byte[] request_text = new byte[4096];
            input.read(request_text);
            String request_value = new String(request_text);

            //Get check request file
            if(request_value.contains("Content-Type: multipart/form-data;") && request_value.contains("POST /FileUpload"))
            {
                //Get check user is auth
                if(request_value.contains("Auth"))
                {

                    String Boundary = GetBoundary(request_value).replace("=","").replace("boundary","");
                    String FileExtention = GetFileExtention(request_value.split(Boundary)[2]).split("\\.")[1].replace("\"","");
                    int FileSize=GetFileSize(request_value);

//                    System.out.println("File size is "+FileSize);

                    //Get check file size
                    if(FileSize <= Config.FileSize)
                    {

                        byte file_all_result[]=new byte[Config.FileSize];
                        byte[] request_text_append = new byte[Config.FileSizeSpli];
                        ByteBuffer bf = ByteBuffer.wrap(file_all_result);
                        bf.put(request_text);

                        while(true)
                        {
                            try
                            {

                                int input_bytes=input.read(request_text_append);
                                bf.put(request_text_append);
                                if(input_bytes<Config.FileSizeSpli)
                                {
                                    break;
                                }

                            }
                            catch (Exception e)
                            {
                                System.out.println("Error loop "+e.getMessage());
                                break;
                            }
                        }

                        HTTPFiles.CreateFile(file_all_result,FileExtention);

                        //Get response
                        ResponseModel response=new ResponseModel("200","text/json","{\"message\":\"File Uploaded\"}");

                        //Http response
                        String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                                + "\nContent-type:" + response.getContentType()
                                + "\nAccess-Control-Allow-Origin : * "
                                + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                                + "\nAccess-Control-Allow-Credentials : true "
                                + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                                + "\n\n" + response.getContent();

                        //get response
                        output.write(HttpResponse.getBytes());

                    }
                    else
                    {
                        //Get response
                        ResponseModel response=new ResponseModel("500","text/json","{\"message\":\"File size is big\"}");

                        //Http response
                        String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                                + "\nContent-type:" + response.getContentType()
                                + "\nAccess-Control-Allow-Origin : * "
                                + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                                + "\nAccess-Control-Allow-Credentials : true "
                                + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                                + "\n\n" + response.getContent();

                        //get response
                        output.write(HttpResponse.getBytes());
                    }
                }
                else
                {
                    //Get response
                    ResponseModel response=new ResponseModel("403","text/json","{\"message\":\"You not authenticated\"}");

                    //Http response
                    String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                            + "\nContent-type:" + response.getContentType()
                            + "\nAccess-Control-Allow-Origin : * "
                            + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                            + "\nAccess-Control-Allow-Credentials : true "
                            + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                            + "\n\n" + response.getContent();

                    //get response
                    output.write(HttpResponse.getBytes());
                }
            }
            else if(request_value.contains("GET /File/"))
            {
                //Get file
                FileResponseModel response=new HttpHandlerController().GetFile(GetParametsByUrl("/File",request_value));

                File fileToSend = new File(response.getPath());

                //Http response
                String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                        + "\nContent-type:" + Files.probeContentType(fileToSend.toPath())
                        + "\n\n";

                //get response
                if(!response.getPath().trim().equals(""))
                {
                    output.write(HttpResponse.getBytes());
                    output.write(response.getContent());
                    output.flush();
                }

            }
            else
            {
                //Get handle request
                ResponseModel response = GetHandleRequest(request_value);

                //Http response
                String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                        + "\nContent-type:" + response.getContentType()
                        + "\nAccess-Control-Allow-Origin : * "
                        + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                        + "\nAccess-Control-Allow-Credentials : true "
                        + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                        + "\n\n" + response.getContent();

                //get response
                output.write(HttpResponse.getBytes());
            }

            //Get close all socket and streams
            input.close();
            output.close();
            request.close();

        }
        catch (Exception e)
        {
            System.out.println("Error on http request handller " + e.getMessage());
        }

    }


    //Request handler function start
    public ResponseModel GetHandleRequest(String HttpRequest)
    {
//        System.out.println(HttpRequest.trim());

        String []requests=HttpRequest.split("\n");

        //First line splite
        String []FirstLine=requests[0].split(" ");

//        System.out.println(FirstLine[0]);

        JSONObject Headers = GetHttpHeaders(HttpRequest);

        if(FirstLine[0].equals("POST") || FirstLine[0].equals("OPTIONS"))
        {
            System.out.println("Post request : "+requests[0]);
            AddLog("Post request : "+requests[0]);
            return GetHandlePostMethod(HttpRequest,requests,Headers);
        }
        else if(FirstLine[0].equals("GET"))
        {
            System.out.println("Get request : "+requests[0]);
            AddLog("Get request : "+requests[0]);
            return GetHandleGetMethod(HttpRequest,requests,Headers);
        }
        else
        {
            return new ResponseModel("404","text/json","{\"message\":\"not found\"}");
        }

    }
    //Request handler function end

    //Get all headers function start
    public JSONObject GetHttpHeaders(String HttpRequest)
    {
        JSONObject result=new JSONObject();

        //Get split all http request by "\n"
        String []split_request=HttpRequest.split("\n");

        for (int i=1;i<split_request.length;i++)
        {
            try
            {
//                System.out.println("Header " + split_request[i].split(":")[0] + "-" + split_request[i].split(":")[1]);
                result.put(split_request[i].split(":")[0].trim(),split_request[i].split(":")[1].trim());
            }
            catch (Exception e)
            {

            }
        }
//        System.out.println(result.toString());
        return result;
    }
    //Get all headers function end

    //Get handle Get request function start
    public ResponseModel GetHandleGetMethod(String request,String []requests,JSONObject Header)
    {
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //parametrs
        JSONObject parametrs_json=new JSONObject();
        if(request_path.toString().contains("?"))
        {
            String all_parametrs = request_path.toString().split("\\?")[1];
            String[] parametrs = all_parametrs.split("&");

            for (int i = 0; i < parametrs.length; i++) {
                String data[] = parametrs[i].split("=");
                parametrs_json.put(data[0], data[1]);
            }
        }

        //Get Routes
        switch (request_path)
        {
            case "/":
                response=new HttpHandlerController().Index();
            break;
            default:
                response=new ResponseModel("404","text/json","{\"message\":\"not found\"}");
            break;
        }

        return response;
    }
    //Get handle Get request function end


    //Get parametrs function start
    public String GetParametsByUrl(String Parametr,String URL)
    {
        URL=URL.split("\n")[0].split(" ")[1];
        System.out.println("URL is "+URL );
        String [] url_sections = URL.split("\n")[0].split(Parametr+"/");
        return url_sections[1];
    }
    //Get parametrs function end


    //Get handle Post request function start
    public ResponseModel GetHandlePostMethod(String request,String []requests,JSONObject Header)
    {
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //parametrs
        JSONObject parametrs_json=new JSONObject();
        if(request_path.toString().contains("?"))
        {
            String all_parametrs = request_path.toString().split("\\?")[1];
            String[] parametrs = all_parametrs.split("&");

            for (int i = 0; i < parametrs.length; i++) {
                String data[] = parametrs[i].split("=");
                parametrs_json.put(data[0], data[1]);
            }
        }


//        if(request.contains("&") || request.contains("\n\n"))
//        {
//            String[] parametrs = request.split("\n")[request.split("\n").length - 1].trim().toString().split("&");
//
//            for (int i = 0; i < parametrs.length; i++)
//            {
//                String data[] = parametrs[i].split("=");
//                parametrs_json.put(data[0], data[1]);
//            }
//        }

        //Get Routes
        switch (request_path.split("\\?")[0])
        {
            case "/":
                response=new HttpHandlerController().Index();
                break;
            case "/Login":
                response=new HttpHandlerController().Login(parametrs_json);
                break;
            case "/Signup":
                response=new HttpHandlerController().Signup(parametrs_json,Header);
                break;
            case "/CheckUser":
                response=new HttpHandlerController().CheckUserNetwork(parametrs_json,Header);
                break;
            case "/GetUser":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().GetUser(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/GetMailBox":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().GetAllUserMails(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/GetMailsOfUser":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().GetUserMails(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/SendMail":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().SendMail(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/UpdateMail":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().UpdateMail(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/DeleteMail":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().DeleteMail(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/SeenMail":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().SeenMail(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            case "/FileUpload":
                if(GetApiAuthCheck(Header))
                    response=new HttpHandlerController().FileUpload(parametrs_json,Header);
                else
                    response=new ResponseModel("403","text/json","{\"message\":\"Auth error\"}");
                break;
            default:
                response=new ResponseModel("404","text/json","{\"message\":\"not found\"}");
                break;
        }

        return response;
    }
    //Get handle Post request function end

    //Get auth check middleware function start
    public boolean GetApiAuthCheck(JSONObject Headers)
    {
        try
        {
            String AuthToken = Headers.get("Auth").toString();
            new UserAuthModel(AuthToken);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Auth Error");
        }

        return false;
    }
    //Get auth check middleware function end

    //Http log file function start
    public void AddLog(String request)
    {
        try
        {
            //Initilze log file
            File LogFile=new File(Config.HttpLogFileAddress);
            LogFile.createNewFile();

            //Write request log on file
            if(LogFile.canWrite())
            {
                PrintWriter Writer=new PrintWriter(new BufferedWriter(new FileWriter(LogFile,true)));
                Writer.append(request + "\n\r******************************************************************************************************************************\n\r");
                Writer.close();
            }

        }
        catch (Exception e)
        {
            System.out.println("Error on Logfile : "+e.getMessage());
        }
    }
    //Http log file function end


    //Get http content size function start
    private static int GetContentLength(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Length: ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return Integer.parseInt(requestHeaders.substring(start, end).trim());
    }
    //Get http content size function end


    //Get boundary function start
    private static String GetBoundary(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Type: multipart/form-data; ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return requestHeaders.substring(start, end).trim();
    }
    //Get boundary function end


    //Get file extention function start
    private static String GetFileExtention(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Type: multipart/form-data; ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return requestHeaders.substring(start, end).trim();
    }
    //Get file extention function end


    //Get file size function start
    private static int GetFileSize(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Length: ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return Integer.parseInt(requestHeaders.substring(start, end).trim());
    }
    //Get file size function end


    //Get remove lines of string funcition start
    private static String GetRemoveLines(String value,int from)
    {
        String result="";
        String []lines=value.split("\n");

        for (int i=from;i<lines.length;i++)
            result+=lines[i]+"\r\n";

        return result;
    }
    //Get remove lines of string funcition end


}
