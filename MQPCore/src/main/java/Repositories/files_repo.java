package Repositories;

import Models.files_tbl;

import java.io.File;
import java.io.IOException;

public interface files_repo
{


    public boolean NewFile(File file,String hash,String file_extention) throws IOException;

    public boolean FileExist(String Hash);

    public File GetFile(String Hash);

    public files_tbl GetFileTbl(String Hash);

}
