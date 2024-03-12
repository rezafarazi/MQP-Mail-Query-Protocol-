package Http;

import Http.Models.ResponseModel;
import org.json.JSONObject;

public class HttpHandlerController
{

    //Get index start
    public ResponseModel Index()
    {
        return new ResponseModel("200","text/json","{\"name\":\"reza\",\"lastname\":\"farazi\"}");
    }
    //Get index end


    //Get index start
    public ResponseModel PostTest(JSONObject parametrs)
    {
        System.out.println("Fun is done");
        return new ResponseModel("200","text/html","hello "+parametrs.get("name"));
    }
    //Get index end


}
