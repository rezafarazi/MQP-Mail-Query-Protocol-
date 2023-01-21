package Services.MailFiles;

import Conf.Config;
import Models.files_tbl;
import Models.mail_files_tbl;
import Models.mail_tbl;
import Repositories.mail_files_repo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Mail_Files_Service implements mail_files_repo
{

    //Global variables
    StandardServiceRegistry SSR;
    Metadata Meta;
    SessionFactory SF;
    Session session;
    Transaction TA;


    //Constractor start
    public Mail_Files_Service()
    {
        SSR=new StandardServiceRegistryBuilder().configure("DBConfigs/hibernate.cfg.xml").build();
        Meta=new MetadataSources(SSR).getMetadataBuilder().build();
        SF=Meta.getSessionFactoryBuilder().build();
        session=SF.openSession();
        TA=session.beginTransaction();
    }


    //Insert new mail files start
    @Override
    public void InsertNewFile(files_tbl file_id, mail_tbl mail_id)
    {
        mail_files_tbl Mail_Files_Tbl=new mail_files_tbl(file_id,mail_id,1);
        session.save(Mail_Files_Tbl);
        TA.commit();

        session.close();
        SF.close();
        SSR.close();
    }
    //Insert new mail files end


}
