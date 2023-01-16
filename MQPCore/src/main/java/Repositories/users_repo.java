package Repositories;

import Models.users_tbl;

public interface users_repo
{

    //Inesert
    public boolean insert(String name,String family,String username,String password,String email,String phone);

    //Update
    public void update(int id,String name,String family,String username,String password,String email,String phone);

    //Select by id
    public users_tbl SelectById(int id);

    //Check user exist
    public boolean CheckUserExist(String username);

}
