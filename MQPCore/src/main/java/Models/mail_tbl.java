package Models;

import javax.persistence.*;

@Entity
public class mail_tbl
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String content;

    @ManyToOne
    private users_tbl user_id;

    private String datetime;

    private int delete_flag;

    private String from_user;

    private int seen;


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

    public users_tbl getUser_id() {
        return user_id;
    }

    public void setUser_id(users_tbl user_id) {
        this.user_id = user_id;
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

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}
