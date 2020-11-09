package com.consumer.perfectmandii.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.consumer.perfectmandii.Room.DAO.daoclass;
import com.consumer.perfectmandii.Room.EntityClass;

@Database(entities = {EntityClass.class}, version = 1)
public abstract class DatabaseClass extends RoomDatabase
{
    public abstract daoclass getDao();

    public static DatabaseClass instance;


    public static DatabaseClass getDatabase(final Context context) {
        if (instance == null) {
            synchronized (DatabaseClass.class) {
                instance = Room.databaseBuilder(context, DatabaseClass.class, "DATABASE").allowMainThreadQueries().build();
            }
        }
        return instance;


    }
}
