package com.vendor.perfectmandii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import com.vendor.perfectmandii.Model.Vendor;
import com.vendor.perfectmandii.Model.userVendor;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{


    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "PerfectMandi";

    // below int is our database version
    private static final int DB_VERSION = 8;

    // below variable is for our table name.
    private static final String TABLE_NAME = "user_table";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    private static final String storeID_COL = "storeid";

    // below variable is for our course name column
    private static final String user_COL = "user_userid";

    // below variable id for our course duration column.
    private static final String pass_COL = "user_pass";

    // below variable id for our course duration column.
    private static final String sess_COL = "user_sess";
    // below variable id for our course duration column.
    private static final String name_COL = "user_vendorname";
    // below variable id for our course duration column.
    private static final String store_COL = "user_storename";

    // below variable id for our course duration column.
    private static final String imagepath_COL = "user_imagepath";
    private static final String status_COL = "store_status";


    // creating a constructor for our database handler.
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are

        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + user_COL + " TEXT,"
                + pass_COL + " TEXT,"
                + sess_COL + " TEXT,"
                + name_COL + " TEXT,"
                + store_COL + " TEXT,"
                + storeID_COL + " TEXT,"
                + imagepath_COL + " TEXT,"+ status_COL + " TEXT)";


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);

    }

    // this method is use to add new course to our sqlite database.
    public void addNewUser(String userid, String userpass,String usersess,String vendorname,String Storename,String storeid,String imagepath,String status) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.

        values.put(user_COL, userid);
        values.put(pass_COL, userpass);
        values.put(sess_COL, usersess);
        values.put(name_COL, vendorname);
        values.put(store_COL, Storename);
        values.put(storeID_COL, storeid);
        values.put(imagepath_COL, imagepath);
        values.put(status_COL,status);
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }



    public void deleteRow(String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+user_COL+"='"+value+"'");
        db.close();
    }

    public void UpdateRow(String id,String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE  " + TABLE_NAME+" SET  " +status_COL +"='"+value+  "' WHERE "+storeID_COL+"='"+id+"'");
        db.close();
    }



    // we have created a new method for reading all the courses.
    public ArrayList<userVendor> readVendor() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<userVendor> pointModelsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                pointModelsArrayList.add(new userVendor(pointcursor.getString(1),
                        pointcursor.getString(2),pointcursor.getString(3),
                        pointcursor.getString(4),pointcursor.getString(5),pointcursor.getString(6),pointcursor.getString(7)
                        ,pointcursor.getString(8)));
            } while (pointcursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        pointcursor.close();
        return pointModelsArrayList;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);



    }

}