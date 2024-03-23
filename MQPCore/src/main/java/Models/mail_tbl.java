package Models;

import javax.persistence.*;

@Entity
@Table(name = "mail_tbl")
public class mail_tbl
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private users_tbl users_id;

    private String datetime;

    private int delete_flag;

    private String from_user;

    private String to_user;

    private int seen;

    private String From_Ip;


    public mail_tbl() {
    }

    public mail_tbl(String title, String content, users_tbl user_id, String datetime, int delete_flag, String from_user, int seen, String From_Ip) {
        this.title = title;
        this.content = content;
        this.users_id = user_id;
        this.datetime = datetime;
        this.delete_flag = delete_flag;
        this.from_user = from_user;
        this.seen = seen;
        this.From_Ip = From_Ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return users_id.getUsername();
    }

    public void setUser_id(users_tbl user_id) {
        this.users_id = user_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getFrom_Ip() {
        return From_Ip;
    }

    public void setFrom_Ip(String from_Ip) {
        From_Ip = from_Ip;
    }
}
