package Services.Users;

import Functions.Hash;
import Models.users_tbl;
import Repositories.users_repo;
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
import java.util.List;

public class Users_Service implements users_repo
{


    //Global variables
    StandardServiceRegistry SSR;
    Metadata Meta;
    SessionFactory SF;
    Session session;
    Transaction TA;


    //Constractor function start
    public Users_Service()
    {
        SSR=new StandardServiceRegistryBuilder().configure("DBConfigs/hibernate.cfg.xml").build();
        Meta=new MetadataSources(SSR).getMetadataBuilder().build();
        SF=Meta.getSessionFactoryBuilder().build();
        session=SF.openSession();
        TA=session.beginTransaction();
    }
    //Constractor function end


    //Get login start
    @Override
    public users_tbl Login(String username, String password) throws Exception
    {
        Query hql=session.createQuery("FROM users_tbl where username='"+username+"' and password='"+Hash.GetSha256(password)+"'");
        List result = hql.list();

        if(result.size()>0)
        {
            return ((users_tbl) result.get(0));
        }
        else
        {
            throw new Exception("Cannot find user");
        }
    }
    //Get login end


    //Insert function start
    @Override
    public boolean insert(String name, String family, String username, String password, String email, String phone)
    {
        if(CheckUserExist(username))
        {
            //Get Datetime
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String last_edit_date = dtf.format(now);
            String login_token = Hash.GetSha256(last_edit_date + name + family + username + password);
            String fp_token = "";

            users_tbl user = new users_tbl(
                    name,
                    family,
                    username,
                    password,
                    last_edit_date,
                    login_token,
                    fp_token,
                    email,
                    phone
            );

            session.save(user);
            TA.commit();
            session.close();
            SF.close();
            SSR.close();

            return true;

        }
        else
        {
            System.out.println("User exist");
            return false;
        }

    }
    //Insert function end


    //Select user by id start
    @Override
    public users_tbl SelectById(int id)
    {
        Query hql=session.createQuery("FROM users_tbl");
        return (users_tbl) hql.list().get(0);
    }
    //Select user by id end


    //Update user start
    @Override
    public void update(int id, String name, String family, String username, String password, String email, String phone)
    {
//Get Datetime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String last_edit_date=dtf.format(now);
        String login_token = Hash.GetSha256(last_edit_date + name + family + username + password);
        String fp_token="";

        users_tbl user=new users_tbl(
                id,
                name,
                family,
                username,
                password,
                last_edit_date,
                login_token,
                fp_token,
                email,
                phone
        );

        session.update(user);
        TA.commit();
        session.close();
        SF.close();
        SSR.close();
    }
    //Update user end


    //Check exist user start
    @Override
    public boolean CheckUserExist(String username)
    {
        Query hql=session.createQuery("from users_tbl where username='"+username+"' ");
        return (hql.list().size()<=1)?false:true;
    }
    //Check exist user end


    //Get user by username function start
    @Override
    public users_tbl GetUserByUsername(String username) throws Exception {
        Query hql=session.createQuery("FROM users_tbl where username='"+username+"'");
        List result = hql.list();

        if(result.size()>0)
        {
            return ((users_tbl) result.get(0));
        }
        else
        {
            throw new Exception("Cannot find user");
        }
    }
    //Get user by username function end


}
