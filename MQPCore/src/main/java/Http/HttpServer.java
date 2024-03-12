package Http;

import Conf.Config;
import Http.Models.ResponseModel;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
                System.out.println("New Request from "+request.getRemoteSocketAddress().toString());

                //Get request and response data stream
                DataInputStream input=new DataInputStream(request.getInputStream());
                DataOutputStream output=new DataOutputStream(request.getOutputStream());

                //Get read request from user
                byte []request_text=new byte[4096];
                input.read(request_text);
                String request_value=new String(request_text);

//                //Get Log
//                System.out.println("Http reqeust start ************************************************** ");
//                System.out.println(request_value);
//                System.out.println("Http reqeust end ************************************************** ");

                //Get handle request
                ResponseModel response=GetHandleRequest(request_value);

                //get response
                output.write(("HTTP/1.1 "+response.getStatusCode()+"\nContent-type:"+response.getStatusCode()+"\n\n"+response.getContent()).getBytes());

                //Get close all socket and streams
                input.close();
                output.close();
                request.close();

            }

        }
        catch (Exception e)
        {
            //Get error log
            System.out.println("Http Sever Error : "+e.getMessage());
        }
    }
    //Get begin server socket end


    //Request handler function start
    public ResponseModel GetHandleRequest(String HttpRequest)
    {
        String []requests=HttpRequest.split("\n");

        //First line splite
        String []FirstLine=requests[0].split(" ");

        System.out.println(FirstLine[0]);

        if(FirstLine[0].equals("POST"))
        {
            System.out.println("Post request");
            return GetHandlePostMethod(requests);
        }
        else if(FirstLine[0].equals("GET"))
        {
            System.out.println("Get request");
            return GetHandleGetMethod(requests);
        }
        else
        {
            return new ResponseModel("404","text/html","Not found   ");
        }

    }
    //Request handler function end


    //Get handle Get request function start
    public ResponseModel GetHandleGetMethod(String []requests)
    {
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //Get Routes
        switch (request_path)
        {
            case "/":
                response=new HttpHandlerController().Index();
            break;
            default:
                response=new ResponseModel("404","text/html","Not found");
            break;
        }

        return response;
    }
    //Get handle Get request function end


    //Get handle Post request function start
    public ResponseModel GetHandlePostMethod(String []requests)
    {
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //parametrs
        JSONObject parametrs_json=new JSONObject();
        String all_parametrs=request_path.toString().split("\\?")[1];
        String []parametrs=all_parametrs.split("&");

        for(int i=0;i<parametrs.length;i++)
        {
            String data[]=parametrs[i].split("=");
            parametrs_json.put(data[0],data[1]);
        }

        //Get Routes
        switch (request_path.split("\\?")[0])
        {
            case "/":
                response=new HttpHandlerController().Index();
                break;
            case "/SayHello":
                response=new HttpHandlerController().PostTest(parametrs_json);
                break;
            default:
                response=new ResponseModel("404","text/html","Not found");
                break;
        }

        return response;
    }
    //Get handle Post request function end

}
