package Repositories;

import java.io.File;
import java.io.IOException;

public interface files_repo
{


    public boolean NewFile(File file) throws IOException;

    public boolean FileExist(String Hash);

    public File GetFile(String Hash);


}
