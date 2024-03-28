package Http;

import Conf.Config;
import Functions.Hash;
import Functions.TextEncript;
import Http.Models.FileResponseModel;
import Http.Models.ResponseModel;
import Http.Models.UserAuthModel;
import MQPSocket.MQPSocket;
import Models.mail_tbl;
import Models.users_tbl;
import Services.FileManager.FileManager_Service;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpHandlerController
{

    //Get index start
    public ResponseModel Index()
    {
        return new ResponseModel("200","text/json","{\"message\":\"hello world\"}");
    }
    //Get index end


    //Get file by hash address start
    public FileResponseModel GetFile(String FileHash)
    {
        try
        {
            File file = new FileManager_Service().GetFile(FileHash);
            System.out.println("Adddress is "+file.getPath());
            if (file.exists())
            {
                System.out.println("File exist");
                byte[] FileBytes = Files.readAllBytes(file.toPath());
                return new FileResponseModel("200", "image/jpeg", FileBytes,file.getPath());
            }
            else
            {
                System.out.println("File not exist");
                return new FileResponseModel("404", "text/json", ("{\"message\":\"can not found file on server\"}").getBytes(),"");
            }
        }
        catch (Exception e)
        {
            System.out.println("File error");
            return new FileResponseModel("500", "text/json", ("{\"message\":\"Internal server error\"}").getBytes(),"");
        }
    }
    //Get file by hash address end


    //Get login start
    public ResponseModel Login(JSONObject parametrs)
    {
        try
        {
            //Get userdata
            users_tbl user = new Users_Service().Login(parametrs.get("username").toString(), parametrs.get("password").toString());

            //Get result
            JSONObject result=new JSONObject();
            result.put("id",user.getId());
            result.put("name",user.getName());
            result.put("family",user.getFamily());
            result.put("username",user.getUsername());
            result.put("date",user.getLast_edit_date());
            result.put("email",user.getEmail());
            result.put("phone",user.getPhone());
            result.put("token",TextEncript.TextEncript(result.toString()));

            return new ResponseModel("200","text/html",result.toString());
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("200","text/html","{\"message\":\"user not found\"}");
        }
    }
    //Get login end


    //Get signup new user start
    public ResponseModel Signup(JSONObject parametrs,JSONObject Header)
    {
        try
        {
            if(!new Users_Service().CheckUserExist(parametrs.get("username").toString()))
            {
                //Add to database
                boolean new_user=new Users_Service().insert(
                        parametrs.get("name").toString(),
                        parametrs.get("family").toString(),
                        parametrs.get("username").toString(),
                        parametrs.get("password").toString(),
                        parametrs.get("email").toString(),
                        parametrs.get("phone").toString()
                );

                if(new_user)
                {
                    return new ResponseModel("200", "text/html", "{\"message\":\"user created\"}");
                }
                else
                {
                    return new ResponseModel("200", "text/html", "{\"message\":\"user exist\"}");
                }
            }
            else
            {
                return new ResponseModel("403","text/html","{\"message\":\"user exist\"}");
            }
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("403","text/html","{\"message\":\"user not found\"}");
        }
    }
    //Get signup new user end


    //Get check user exist in network start
    public ResponseModel CheckUserNetwork(JSONObject parametrs,JSONObject Header)
    {
        //Get check server
        if(parametrs.get("Address").toString().contains("@"+Config.DomainAddress))
        {
            if(new Users_Service().CheckUserExist(parametrs.get("Address").toString().split("@")[0]))
            {
                try
                {
                    //Get user data
                    users_tbl user = new Users_Service().GetUserByUsername(parametrs.get("Address").toString().split("@")[0]);

                    //Get result variavle
                    JSONObject result=new JSONObject();

                    result.put("USERNAME",user.getUsername());
                    result.put("NAME",user.getName());
                    result.put("FAMILY",user.getFamily());
                    result.put("EMAIL",user.getEmail());
                    result.put("PHONE",user.getPhone());

                    return new ResponseModel("200","text/html",result.toString());
                }
                catch (Exception e)
                {
                    return new ResponseModel("500","text/html","{\"message\":\"Internal server error\"}");
                }
            }
            else
            {
                return new ResponseModel("403","text/html","{\"message\":\"User not exist\"}");
            }
        }
        else
        {
            ArrayList UserCheck=MQPSocket.CheckUser(parametrs.get("Address").toString().split("@")[0]);

            if(Boolean.parseBoolean(UserCheck.get(0).toString()))
            {
                return new ResponseModel("200","text/html",UserCheck.get(0).toString());
            }
            else
            {
                return new ResponseModel("403","text/html","{\"message\":\"User not exist\"}");
            }
        }
    }
    //Get check user exist in network end


    //Get user data from token start
    public ResponseModel GetUser(JSONObject parametrs,JSONObject Header)
    {
        try
        {
            UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());
            return new ResponseModel("200","text/html",usr.toString());
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("403","text/html","{\"message\":\"user not found\"}");
        }
    }
    //Get user data from token end


    //Get all user mails start
    public ResponseModel GetAllUserMails(JSONObject parametrs,JSONObject Header)
    {
        try
        {
            UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());
            List<mail_tbl> Mails = new Mail_Service().GetMailsByUserName(usr.getUsername());
            JSONArray jsonarray_result=new JSONArray(Mails);
            return new ResponseModel("200","text/html",jsonarray_result.toString());
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("403","text/html","{\"message\":\"user not access\"}");
        }
    }
    //Get all user mails end


    //Get all user mails start
    public ResponseModel GetUserMails(JSONObject parametrs,JSONObject Header)
    {
        try
        {
            UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());
            List<mail_tbl> Mails = new Mail_Service().GetUserAndOtherUserMails(usr.getUsername(),parametrs.get("username").toString());
            JSONArray jsonarray_result=new JSONArray(Mails);
            return new ResponseModel("200","text/html",jsonarray_result.toString());
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("403","text/html","{\"message\":\"user not access\"}");
        }
    }
    //Get all user mails end


    //Send mail start
    public ResponseModel SendMail(JSONObject parametrs,JSONObject Header)
    {

        //Get userdata
        UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());

        //Get address check
        if(parametrs.get("address").toString().contains("@"+Config.DomainAddress))
        {
            try
            {
                new Mail_Service().InsertnewMail(
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString(),
                        new Users_Service().GetUserByUsername(usr.getUsername()),
                        usr.getUsername()+"@"+Config.DomainAddress,
                        parametrs.get("address").toString(),
                        Config.DomainAddress
                );
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }
        else
        {
            try
            {
                //Get send mail
                String MailDitales[]=MQPSocket.SendMQPMail(
                        parametrs.get("address").toString().split("@")[1],
                        parametrs.get("address").toString(),
                        usr.getUsername()+"@"+Config.DomainAddress,
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString()
                ).split("-");

                new Mail_Service().InsertnewMailById(
                        Integer.parseInt(MailDitales[0]),
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString(),
                        new Users_Service().GetUserByUsername(usr.getUsername()),
                        usr.getUsername() + "@" + Config.DomainAddress,
                        parametrs.get("address").toString(),
                        MailDitales[1]
                );
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }

        return new ResponseModel("200","text/html","{\"status\":\"mail sended\"}");
    }
    //Send mail end


    //Update mail start
    public ResponseModel UpdateMail(JSONObject parametrs,JSONObject Header)
    {

        //Get userdata
        UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());

        //Get address check
        if(parametrs.get("address").toString().contains("@"+Config.DomainAddress))
        {
            try
            {
                new Mail_Service().UpdatenewMail(
                        Integer.parseInt(parametrs.get("mail_id").toString()),
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString(),
                        new Users_Service().GetUserByUsername(usr.getUsername()),
                        usr.getUsername()+"@"+Config.DomainAddress,
                        parametrs.get("address").toString(),
                        Config.DomainAddress
                );
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }
        else
        {
            try
            {
                //Get send mail
                String MailDitales[]=MQPSocket.UpdateMQPMail(
                        Integer.parseInt(parametrs.get("mail_id").toString()),
                        parametrs.get("address").toString().split("@")[1],
                        parametrs.get("address").toString(),
                        usr.getUsername()+"@"+Config.DomainAddress,
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString()
                ).split("-");

                new Mail_Service().UpdatenewMail(
                        Integer.parseInt(MailDitales[0]),
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString(),
                        new Users_Service().GetUserByUsername(usr.getUsername()),
                        usr.getUsername() + "@" + Config.DomainAddress,
                        parametrs.get("address").toString(),
                        MailDitales[1]
                );
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }

        return new ResponseModel("200","text/html","{\"status\":\"mail updated\"}");
    }
    //Update mail end


    //Delete mail start
    public ResponseModel DeleteMail(JSONObject parametrs,JSONObject Header)
    {

        //Get userdata
        UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());

        //Get address check
        if(parametrs.get("address").toString().contains("@"+Config.DomainAddress))
        {
            try
            {
                //Get check user mail by username
                mail_tbl mail=new Mail_Service().GetMailById(Integer.parseInt(parametrs.get("MAILID").toString()));
                if(mail.getTo_user().equals(usr.getUsername()+"@"+Config.DomainAddress) || mail.getFrom_user().equals(usr.getUsername()+"@"+Config.DomainAddress))
                {
                    //Get remove mail
                    new Mail_Service().DeleteMail(Integer.parseInt(parametrs.get("MAILID").toString()));
                }
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }
        else
        {
            try
            {
                //Get mail
                mail_tbl mail = new Mail_Service().GetMailById(Integer.parseInt(parametrs.get("MAILID").toString()));

                if(MQPSocket.DeleteMail(parametrs.get("address").toString(),mail.getSend_mail_id()+""))
                {
                    new Mail_Service().DeleteMail(Integer.parseInt(mail.getId()+"") );
                }
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }

        return new ResponseModel("200","text/html","{\"status\":\"mail removed\"}");
    }
    //Delete mail end


    //Seen mail start
    public ResponseModel SeenMail(JSONObject parametrs,JSONObject Header)
    {

        //Get userdata
        UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());

        //Get address check
        if(parametrs.get("address").toString().contains("@"+Config.DomainAddress))
        {
            try
            {
                //Get check user mail by username
                mail_tbl mail=new Mail_Service().GetMailById(Integer.parseInt(parametrs.get("MAILID").toString()));
                if(mail.getTo_user().equals(usr.getUsername()+"@"+Config.DomainAddress) || mail.getFrom_user().equals(usr.getUsername()+"@"+Config.DomainAddress))
                {
                    //Get remove mail
                    new Mail_Service().SeenMail(Integer.parseInt(parametrs.get("MAILID").toString()));
                }
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }
        else
        {
            try
            {
                //Get mail
                mail_tbl mail = new Mail_Service().GetMailById(Integer.parseInt(parametrs.get("MAILID").toString()));

                if(MQPSocket.SeenMail(parametrs.get("address").toString(),mail.getSend_mail_id()+""))
                {
                    new Mail_Service().SeenMail(Integer.parseInt(mail.getId()+"") );
                }
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal error\"}");
            }
        }

        return new ResponseModel("200","text/html","{\"status\":\"mail is seen done\"}");
    }
    //Seen mail end


    //FileUpload start
    public ResponseModel FileUpload(JSONObject parametrs,JSONObject Header)
    {

        return new ResponseModel("200","text/html","{\"status\":\"hello\"}");
    }
    //FileUpload end


}
