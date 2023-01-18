package Services.FileManager;

import Conf.Config;
import Functions.File_Hash_Lib;
import Models.files_tbl;
import Models.users_tbl;
import Repositories.files_repo;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager implements files_repo
{

    //Global variables
    StandardServiceRegistry SSR;
    Metadata Meta;
    SessionFactory SF;
    Session session;
    Transaction TA;
    String Root_Folder;


    //Constractor start
    public FileManager()
    {
        SSR=new StandardServiceRegistryBuilder().configure("DBConfigs/hibernate.cfg.xml").build();
        Meta=new MetadataSources(SSR).getMetadataBuilder().build();
        SF=Meta.getSessionFactoryBuilder().build();
        session=SF.openSession();
        TA=session.beginTransaction();

        Root_Folder= Config.Root_Dir;

    }
    //Constractor end


    //New file start
    @Override
    public boolean NewFile(File file)
    {
        String file_hash=File_Hash_Lib.HashFile(new File(Root_Folder + file.getName().toString()));
        boolean hash_check=new FileManager().FileExist(file_hash);

        if(hash_check == false)
        {
            try
            {
                //Create main folder start
                File dir = new File(Root_Folder);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                //Create main folder end

                //copy file to main folder start
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddHH-mm-ss");
                LocalDateTime now = LocalDateTime.now();
                Files.copy(file.toPath(), Paths.get(Root_Folder + (dtf.format(now).toString()) + "." + FilenameUtils.getExtension(file.getName())));
                //copy file to main folder end

                //Insert in database start
                files_tbl file_ = new files_tbl(
                        (dtf.format(now).toString()) + "." + FilenameUtils.getExtension(file.getName()),
                        FilenameUtils.getExtension(file.getName()),
                        file_hash,
                        Root_Folder + (dtf.format(now).toString())
                );

                session.save(file_);
                TA.commit();
                session.close();
                SF.close();
                SSR.close();
                //Insert in database end

            }
            catch (Exception e)
            {
                System.out.println("Error " + e.getMessage());
                return false;
            }
        }

        return true;
    }
    //New file end


    //Check file exist start
    @Override
    public boolean FileExist(String Hash)
    {
        Query hql=session.createQuery("from files_tbl where hash='"+Hash+"' ");
        return (hql.list().size()>0)?true:false;
    }
    //Check file exist end


    //Get file by hash start
    @Override
    public File GetFile(String Hash)
    {
        Query hql=session.createQuery("from files_tbl where hash='"+Hash+"' ");
        files_tbl file=(files_tbl) hql.list().get(0);
        File result=new File(file.getAddress());
        return result;
    }
    //Get file by hash end

}
