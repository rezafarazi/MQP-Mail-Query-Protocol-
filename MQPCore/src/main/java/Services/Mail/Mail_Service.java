package Services.Mail;

import Conf.Config;
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
    public mail_tbl InsertnewMail(String title, String content, users_tbl user, String fromuser,String touser,String IP)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        mail_tbl mail=new mail_tbl(
                0,
                title,
                content,
                user,
                dtf.format(now),
                0,
                fromuser,
                touser,
                0,
                1,
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


    //Insert new mail by id start

    @Override
    public mail_tbl InsertnewMailById(int id,String title, String content, users_tbl user, String fromuser, String touser, String IP)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        mail_tbl mail=new mail_tbl(
                id,
                title,
                content,
                user,
                dtf.format(now),
                0,
                fromuser,
                touser,
                0,
                1,
                IP
        );

        session.save(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();

        return mail;
    }
    //Insert new mail by id end


    //Update new mail function start
    @Override
    public mail_tbl UpdatenewMail(int mail_id, String title, String content, users_tbl user_id, String fromuser, String IP)
    {
        mail_tbl mail=new Mail_Service().GetMailById(mail_id);

        if(mail.getFrom_Ip().equals(IP) && mail.getFrom_user().equals(fromuser)){
            mail.setTitle(title);
            mail.setContent(content);
        }

        session.update(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();
        return mail;
    }
    //Update new mail function end


    //Get mail with mail id start
    @Override
    public mail_tbl GetMailById(int id)
    {
        Query hql=session.createQuery("from mail_tbl where id="+id);
        mail_tbl mail=(mail_tbl) hql.list().get(0);
        return mail;
    }
    //Get mail with mail id end


    //Get mail box by username start
    @Override
    public List<mail_tbl> GetMailBoxByUsername(String username) throws Exception
    {
        users_tbl user = new Users_Service().GetUserByUsername(username);
        List<mail_tbl> commentList=session.createQuery("from mail_tbl where users_id="+user.getId()).getResultList();
        return commentList;
    }
    //Get mail box by username end


    //Get all user mail with username start
    @Override
    public List<mail_tbl> GetMailsByUserName(String username) throws Exception
    {
        List<mail_tbl> commentList=session.createQuery("from mail_tbl where from_user like '"+ username +"@"+Config.DomainAddress+"' or to_user like '"+username+"@"+Config.DomainAddress+"' group by from_user order by id asc").getResultList();
        return commentList;
    }
    //Get all user mail with username end


    //Get all mails in a user and other user start
    @Override
    public List<mail_tbl> GetUserAndOtherUserMails(String username1,String username2) throws Exception
    {
        List<mail_tbl> commentList=session.createQuery("from mail_tbl where (from_user like '"+ username1 +"@"+Config.DomainAddress+"' and to_user like '"+username2+"') or (from_user like '"+ username2 +"' and to_user like '"+username1+"@"+Config.DomainAddress+"')").getResultList();
        return commentList;
    }
    //Get all mails in a user and other user end


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
    public boolean DeleteMail(int id,String IP) throws Exception
    {
        try
        {
            mail_tbl mail= GetMailById(id);

            if(mail.getFrom_Ip().equals(IP) && !IP.equals(Config.DomainAddress))
            {
                mail.setDelete_flag(1);
            }

            if(IP.equals(Config.DomainAddress))
            {
                mail.setDelete_flag(1);
            }

            session.update(mail);
            TA.commit();
            session.close();
            SF.close();
            SSR.close();

            return true;
        }
        catch (Exception e)
        {

        }

        return false;
    }

    @Override
    public boolean DeleteMail(int id) throws Exception
    {
        try
        {
            mail_tbl mail= GetMailById(id);

            mail.setDelete_flag(1);

            session.update(mail);
            TA.commit();
            session.close();
            SF.close();
            SSR.close();

            return true;
        }
        catch (Exception e)
        {

        }

        return false;
    }
    //Get delete mail end


    //Get Last meil start
    @Override
    public mail_tbl LastMail() throws Exception
    {
        Query hql=session.createQuery("from mail_tbl");
        mail_tbl mail=(mail_tbl) hql.list().get(hql.list().size()-1);
        return mail;
    }
    //Get Last meil end

}
