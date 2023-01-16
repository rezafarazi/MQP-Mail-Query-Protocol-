package Models;

import javax.persistence.*;

@Entity
@Table(name = "users_tbl")

public class users_tbl
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String family;

    private String username;

    private String password;

    private String last_edit_date;

    private String login_token;

    private String fp_token;

    private String google_auth;

    private String email;

    private String phone;


    public users_tbl(String name, String family, String username, String password, String last_edit_date, String login_token, String fp_token, String google_auth, String email, String phone) {
        this.name = name;
        this.family = family;
        this.username = username;
        this.password = password;
        this.last_edit_date = last_edit_date;
        this.login_token = login_token;
        this.fp_token = fp_token;
        this.google_auth = google_auth;
        this.email = email;
        this.phone = phone;
    }

    public users_tbl(int id, String name, String family, String username, String password, String last_edit_date, String login_token, String fp_token, String google_auth, String email, String phone) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.username = username;
        this.password = password;
        this.last_edit_date = last_edit_date;
        this.login_token = login_token;
        this.fp_token = fp_token;
        this.google_auth = google_auth;
        this.email = email;
        this.phone = phone;
    }

    public users_tbl() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(String last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getFp_token() {
        return fp_token;
    }

    public void setFp_token(String fp_token) {
        this.fp_token = fp_token;
    }

    public String getGoogle_auth() {
        return google_auth;
    }

    public void setGoogle_auth(String google_auth) {
        this.google_auth = google_auth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
