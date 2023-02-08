package Repositories;

import Models.mail_tbl;
import Models.users_tbl;

import java.util.ArrayList;
import java.util.List;

public interface mail_repo
{

    //Insert new mail start
    public mail_tbl InsertnewMail(String title, String content, users_tbl user_id, String fromuser,String IP);
    //Insert new mail end

    //Update new mail start
    public mail_tbl UpdatenewMail(int mail_id,String title, String content, users_tbl user_id, String fromuser,String IP);
    //Update new mail end

    //Get mail with mail id start
    public mail_tbl GetMailById(int id);
    //Get mail with mail id end

    //Get all user mail with username start
    public List<mail_tbl> GetMailsByUserName(String username) throws Exception;
    //Get all user mail with username end

    //Get seen mail start
    public void SeenMail(int id, String IP) throws Exception;
    //Get seen mail end

    //Get delete mail start
    public void DeleteMail(int id, String IP) throws Exception;
    //Get delete mail end

}
