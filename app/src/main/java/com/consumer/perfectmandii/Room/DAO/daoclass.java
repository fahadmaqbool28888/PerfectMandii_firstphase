package com.consumer.perfectmandii.Room.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.consumer.perfectmandii.Room.EntityClass;

import java.util.List;
@Dao
public interface daoclass
{
    /*   private int key;

    @ColumnInfo(name = "session")
    private String session;

    @ColumnInfo(name = "userid")
    private String userid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "imagepath")
    private String imagepath;
*/


    @Insert
    void insertAllData(EntityClass model);

    //Select All Data
    @Query("select * from  user")
    List<EntityClass> getAllData();

    //DELETE DATA
   /* @Query("delete from user where `key`= :id")
    void deleteData(int id);*/

    //Update Data

   /* @Query("update user SET userid= :userid ,username =:address, phoneno =:phoneno where `key`= :key")
    void updateData(String name, String phoneno, String address, int key);*/

}
