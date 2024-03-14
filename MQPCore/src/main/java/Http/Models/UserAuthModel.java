package Http.Models;

import Functions.TextEncript;
import org.json.JSONObject;

public class UserAuthModel
{

    int id;
    String name;
    String family;
    String email;
    String username;
    String date;
    String phone;

    public UserAuthModel(int id, String name, String family, String email, String username, String date, String phone)
    {
        this.id = id;
        this.name = name;
        this.family = family;
        this.email = email;
        this.username = username;
        this.date = date;
        this.phone = phone;
    }

    public UserAuthModel(String json)
    {
        JSONObject js_user=new JSONObject(TextEncript.TextDecript(json));

        this.id = Integer.parseInt(js_user.get("id").toString());
        this.name = js_user.get("name").toString();
        this.family = js_user.get("family").toString();
        this.email = js_user.get("email").toString();
        this.username = js_user.get("username").toString();
        this.date = js_user.get("date").toString();
        this.phone = js_user.get("phone").toString();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFamily()
    {
        return family;
    }

    public void setFamily(String family)
    {
        this.family = family;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

}
