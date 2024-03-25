package Repositories;

import Models.mail_tbl;
import Models.users_tbl;

import java.util.ArrayList;
import java.util.List;

public interface mail_repo
{

    //Insert new mail start
    public mail_tbl InsertnewMail(String title, String content, users_tbl user, String fromuser,String touser,String IP);
    //Insert new mail end

    //Insert new mail by id start
    public mail_tbl InsertnewMailById(int id,String title, String content, users_tbl user, String fromuser,String touser,String IP);
    //Insert new mail by id end

    //Update new mail start
    public mail_tbl UpdatenewMail(int mail_id,String title, String content, users_tbl user_id, String fromuser,String IP);
    //Update new mail end

    //Get mail with mail id start
    public mail_tbl GetMailById(int id);
    //Get mail with mail id end

    //Get user mailbox by username start
    public List<mail_tbl> GetMailBoxByUsername(String username) throws Exception;
    //Get user mailbox by username end

    //Get all user mail with username start
    public List<mail_tbl> GetMailsByUserName(String username) throws Exception;
    //Get all user mail with username end

    //Get all mails in a user and other user start
    public List<mail_tbl> GetUserAndOtherUserMails(String username1,String username2) throws Exception;
    //Get all mails in a user and other user end

    //Get seen mail start
    public void SeenMail(int id, String IP) throws Exception;
    //Get seen mail end

    //Get delete mail start
    public boolean DeleteMail(int id, String IP) throws Exception;
    //Get delete mail end

    //Get delete mail start
    public boolean DeleteMail(int id) throws Exception;
    //Get delete mail end

    //Get last mail start
    public mail_tbl LastMail() throws Exception;
    //Get last mail end

}
