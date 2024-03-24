package Http;

import Conf.Config;
import Functions.Hash;
import Functions.TextEncript;
import Http.Models.ResponseModel;
import Http.Models.UserAuthModel;
import MQPSocket.MQPSocket;
import Models.mail_tbl;
import Models.users_tbl;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import org.json.JSONArray;
import org.json.JSONObject;

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
        return new ResponseModel("200","text/json","{\"name\":\"reza\",\"lastname\":\"farazi\"}");
    }
    //Get index end


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
                return new ResponseModel("500","text/html","{\"message\":\"server internal 1 error\"}");
            }
        }
        else
        {
            try
            {
                //Get send mail
                int MailId=MQPSocket.SendMQPMail(
                        parametrs.get("address").toString().split("@")[1],
                        parametrs.get("address").toString(),
                        usr.getUsername()+"@"+Config.DomainAddress,
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString()
                );

                new Mail_Service().InsertnewMailById(
                        MailId,
                        parametrs.get("title").toString(),
                        parametrs.get("content").toString(),
                        new Users_Service().GetUserByUsername(usr.getUsername()),
                        usr.getUsername() + "@" + Config.DomainAddress,
                        parametrs.get("address").toString(),
                        Config.DomainAddress
                );
            }
            catch (Exception e)
            {
                return new ResponseModel("500","text/html","{\"message\":\"server internal 2 error\"}");
            }
        }

        return new ResponseModel("200","text/html","{\"status\":\"mail sended\"}");
    }
    //Send mail end


}
