package Services.Mail;

import Models.mail_tbl;
import Models.users_tbl;
import Repositories.mail_repo;
import Services.Users.Users_Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Mail_Service implements mail_repo
{

    //Global variables
    StandardServiceRegistry SSR;
    Metadata Meta;
    SessionFactory SF;
    Session session;
    Transaction TA;


    //Constractor start
    public Mail_Service()
    {
        SSR=new StandardServiceRegistryBuilder().configure("DBConfigs/hibernate.cfg.xml").build();
        Meta=new MetadataSources(SSR).getMetadataBuilder().build();
        SF=Meta.getSessionFactoryBuilder().build();
        session=SF.openSession();
        TA=session.beginTransaction();
    }


    //Insert new mail function start
    @Override
    public mail_tbl InsertnewMail(String title, String content, users_tbl user, String fromuser,String IP)
    {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        mail_tbl mail=new mail_tbl(
                title,
                content,
                user,
                dtf.format(now),
                0,
                fromuser,
                0,
                IP
        );

        session.save(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();

        return mail;
    }
    //Insert new mail function end


    //Get mail with mail id start
    @Override
    public mail_tbl GetMailById(int id)
    {
        Query hql=session.createQuery("from mail_tbl where id="+id);
        mail_tbl mail=(mail_tbl) hql.list().get(0);
        return mail;
    }
    //Get mail with mail id end


    //Get all user mail with username start
    @Override
    public List<mail_tbl> GetMailsByUserName(String username) throws Exception
    {
        users_tbl user = new Users_Service().GetUserByUsername(username);
        List<mail_tbl> commentList=session.createQuery("from mail_tbl where users_id="+user.getId()).getResultList();
        return commentList;
    }
    //Get all user mail with username end


    //Get seen mail start
    @Override
    public void SeenMail(int id , String IP) throws Exception {

        mail_tbl mail=new Mail_Service().GetMailById(id);

        if(mail.getFrom_Ip().equals(IP)) {
            mail.setSeen(1);
        }

        session.update(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();
    }
    //Get seen mail end


    //Get delete mail start
    @Override
    public void DeleteMail(int id , String IP) throws Exception {
        mail_tbl mail=new Mail_Service().GetMailById(id);

        if(mail.getFrom_Ip().equals(IP)) {
            mail.setDelete_flag(1);
        }

        session.update(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();
    }
    //Get delete mail end


}
