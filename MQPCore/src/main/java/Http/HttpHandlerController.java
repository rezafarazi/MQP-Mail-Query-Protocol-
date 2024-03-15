package Http;

import Functions.TextEncript;
import Http.Models.ResponseModel;
import Http.Models.UserAuthModel;
import Models.mail_tbl;
import Models.users_tbl;
import Services.Mail.Mail_Service;
import Services.Users.Users_Service;
import org.json.JSONArray;
import org.json.JSONObject;

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


    //Get user data from token start
    public ResponseModel GetUser(JSONObject parametrs,JSONObject Header)
    {
        try
        {
            UserAuthModel usr = new UserAuthModel(Header.get("Auth").toString());
            return new ResponseModel("200","text/html","hello "+usr.getUsername());
        }
        catch (Exception e)
        {
            //get usernot found
            return new ResponseModel("200","text/html","{\"message\":\"user not found\"}");
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


}