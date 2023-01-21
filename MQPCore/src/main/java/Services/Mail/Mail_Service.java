package Services.Mail;

import Models.mail_tbl;
import Models.users_tbl;
import Repositories.mail_repo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public mail_tbl InsertnewMail(String title, String content, users_tbl user_id, String fromuser)
    {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        mail_tbl mail=new mail_tbl(
                title,
                content,
                user_id,
                dtf.format(now),
                0,
                fromuser,
                0
        );

        session.save(mail);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();

        return mail;
    }
    //Insert new mail function end

}
