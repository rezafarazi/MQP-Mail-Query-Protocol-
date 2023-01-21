package Repositories;

import Models.files_tbl;
import Models.mail_tbl;

public interface mail_files_repo
{

    //Insert new files start
    public void InsertNewFile(files_tbl file_id, mail_tbl mail_id);
    //Insert new files end

}
