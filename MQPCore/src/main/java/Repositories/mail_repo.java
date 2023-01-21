package Repositories;

import Models.mail_tbl;
import Models.users_tbl;

public interface mail_repo
{

    //Insert new mail start
    public mail_tbl InsertnewMail(String title, String content, users_tbl user_id, String fromuser);
    //Insert new mail end

}
