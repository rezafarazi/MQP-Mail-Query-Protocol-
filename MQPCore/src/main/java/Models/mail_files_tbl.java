package Models;
import javax.persistence.*;

@Entity
@Table(name = "mail_files_tbl")
public class mail_files_tbl
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private files_tbl file_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private mail_tbl mail_id;

    private int status;


    public mail_files_tbl() {
    }

    public mail_files_tbl(files_tbl file_id, mail_tbl mail_id, int status) {
        this.file_id = file_id;
        this.mail_id = mail_id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public files_tbl getFile_id() {
        return file_id;
    }

    public void setFile_id(files_tbl file_id) {
        this.file_id = file_id;
    }

    public mail_tbl getMail_id() {
        return mail_id;
    }

    public void setMail_id(mail_tbl mail_id) {
        this.mail_id = mail_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
