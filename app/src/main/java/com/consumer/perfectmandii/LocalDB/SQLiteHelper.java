package com.consumer.perfectmandii.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.Model.BeforeLogin_Model;
import com.consumer.perfectmandii.Model.OTP;
import com.consumer.perfectmandii.Model.Product_Before_Login;
import com.consumer.perfectmandii.Model.Product_Model;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper
{
    static String DATABASE_NAME="Perfect_Mandi";

    public static final String TABLE_NAME="CustomerTable";


    public static final String Table_Column_ID="id";
    public static final String Table_Column_0_accountid="accountid";
    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_ImagePath="imagePath";
    public static final String Table_Column_3_Contact="contact";

    public static final String Table_Column_4_session="session";

    public static final String Table_Column_5_shop="shop";

    public static final String Table_Column_6_city="city";

    public static final String TABLE_NAME_1="Customer_OTP";
    public static final String Customer_Column_ID="id";
    public static final String Customer_Column_0_accountid="otp";


    public static final String TABLE_NAME_3="beforelogin";
    public static final String before_Column_ID="id";
    public static final String before_Column_0_accountid="code";

    public static final String TABLE_NAME_2="Product_Before_OTP";
    public static final String PB_Column_ID="id";
    public static final String PB_Column_Name="name";
    public static final String PB_Column_Desc="description";
    public static final String PB_Column_moq="moq";
    public static final String PB_Column_quan="quan";
    public static final String PB_Column_Path="path";
    public static final String PB_Column_code="code";
    public static final String PB_Column_provider="provider";
    public static final String PB_Column_price="price";


    public static final String TABLE_NAME_5="PRODUCT_BEFORE_LOGIN";
    public static final String PBL_Column_ID="id";
    public static final String PBL_Column_code="code";

    public static final String PB_TABLE_NAME="product_like";
    public static final String PB_product_id="product_id";
    public static final String PB_product_name="product_name";
    public static final String PB_product_desc="product_desc";
    public static final String PB_product_moq="product_moq";
    public static final String PB_product_quan="product_quan";
    public static final String PB_product_path="product_path";
    public static final String PB_product_code="product_code";
    public static final String PB_product_provider="product_provider";
    public static final String PB_product_price="product_price";

    public SQLiteHelper(Context context)
    {

        super(context, DATABASE_NAME, null, 17);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY,"+Table_Column_0_accountid+" VARCHAR ,"+Table_Column_1_Name+" VARCHAR,"+Table_Column_3_Contact+" VARCHAR, "+Table_Column_4_session+" VARCHAR,"+Table_Column_5_shop+" VARCHAR,"+Table_Column_2_ImagePath+" VARCHAR ,"+Table_Column_6_city+" VARCHAR)";
        database.execSQL(CREATE_TABLE);


        String CREATE_TABLE_1="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_1+" ("+Customer_Column_ID+" INTEGER PRIMARY KEY,"+Customer_Column_0_accountid+" VARCHAR)";
        database.execSQL(CREATE_TABLE_1);




        String CREATE_TABLE_2="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_2+"("+PB_Column_ID+"INTEGER PRIMARY KEY,"+PB_Column_Name+" VARCHAR ,"+PB_Column_Desc+" VARCHAR ,"+PB_Column_moq+" VARCHAR ,"+PB_Column_quan+" VARCHAR ,"+PB_Column_Path+" VARCHAR ,"+PB_Column_code+" VARCHAR ,"+PB_Column_provider+" VARCHAR ,"+PB_Column_price+" VARCHAR)";
        database.execSQL(CREATE_TABLE_2);




        String CREATE_TABLE_4="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_3+" ("+before_Column_ID+" INTEGER PRIMARY KEY,"+before_Column_0_accountid+" VARCHAR)";
        database.execSQL(CREATE_TABLE_4);



        String CREATE_TABLE_5="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_5+" ("+PBL_Column_ID+" INTEGER PRIMARY KEY, "+ PBL_Column_code +" VARCHAR )";

        database.execSQL(CREATE_TABLE_5);



        //CREATE TABLE  if not EXISTS product_like(product_id Integer AUTO_INCREMENT,product_name VARCHAR,product_desc VARCHAR,product_moq VARCHAR,product_quan VARCHAR,product_path VARCHAR,product_code VARCHAR,product_provider VARCHAR,product_price VARCHAR)
        String CREATE_TABLE_6="CREATE TABLE  IF NOT EXISTS "+ PB_TABLE_NAME +"("+PB_product_id +"Integer AUTO_INCREMENT,"+PB_product_name +"VARCHAR,"+ PB_product_desc +"VARCHAR,"+PB_product_moq +"VARCHAR,"+ PB_product_quan+ " VARCHAR,"+PB_product_path+" VARCHAR,"+PB_product_code+" VARCHAR,"+PB_product_provider+" VARCHAR,"+PB_product_price+" VARCHAR)";

        database.execSQL(CREATE_TABLE_6);
    }

    public void addbeforelogin(String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        values.put(PBL_Column_code, code);

        db.insert(TABLE_NAME_5, null, values);

        db.close();
    }



    // we have created a new method for reading all the courses.
    public ArrayList<Product_Before_Login> readBeforeProduct() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_5, null);

        // on below line we are creating a new array list.
        ArrayList<Product_Before_Login> codeArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                codeArrayList.add(new Product_Before_Login(pointcursor.getString(1)
                ));
            } while (pointcursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        pointcursor.close();
        return codeArrayList;
    }








        public void addNewCustomer(String accountid, String Name,String contact,String session,String shop,String profilepic,String city) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        values.put(Table_Column_0_accountid, accountid);
        values.put(Table_Column_1_Name, Name);
        values.put(Table_Column_3_Contact, contact);
        values.put(Table_Column_4_session, session);
        values.put(Table_Column_5_shop,shop);
        values.put(Table_Column_2_ImagePath, profilepic);
            values.put(Table_Column_6_city, city);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }


    // below is the method for deleting our course.
    public void deleteCustomer(String courseName) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "contact=?", new String[]{courseName});
        db.close();
    }

    public void addNewOTP(String otp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Customer_Column_0_accountid, otp);
        db.insert(TABLE_NAME_1, null, values);
        db.close();
    }


    public void BeforeLogin(String otp)
    {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        values.put(before_Column_0_accountid, otp);


        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME_3, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    // we have created a new method for reading all the courses.
    public ArrayList<BeforeLogin_Model> readBefore() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_3, null);

        // on below line we are creating a new array list.
        ArrayList<BeforeLogin_Model> codeArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                codeArrayList.add(new BeforeLogin_Model(pointcursor.getString(1)
                ));
            } while (pointcursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        pointcursor.close();
        return codeArrayList;
    }

    // we have created a new method for reading all the courses.
    public ArrayList<OTP> readOTP() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_1, null);

        // on below line we are creating a new array list.
        ArrayList<OTP> pointModelsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                pointModelsArrayList.add(new OTP(pointcursor.getString(1)
                ));
            } while (pointcursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        pointcursor.close();
        return pointModelsArrayList;
    }

    // we have created a new method for reading all the courses.
    public ArrayList<UserModel> readCustomer() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<UserModel> pointModelsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                pointModelsArrayList.add(new UserModel(pointcursor.getString(1),
                        pointcursor.getString(2),pointcursor.getString(3),
                        pointcursor.getString(4),pointcursor.getString(5),pointcursor.getString(6),pointcursor.getString(7)
                ));
            } while (pointcursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        pointcursor.close();
        return pointModelsArrayList;
    }


    // we have created a new method for reading all the courses.
    public ArrayList<Product_Model> readProduct() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor pointcursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);

        // on below line we are creating a new array list.
        ArrayList<Product_Model> pointModelsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (pointcursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                pointModelsArrayList.add(new Product_Model(pointcursor.getString(1),pointcursor.getString(2),pointcursor.getString(3),pointcursor.getString(4),pointcursor.getString(5),pointcursor.getString(6),pointcursor.getString(7),pointcursor.getString(8)
                ));
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
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_1);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_2);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_3);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_5);
        onCreate(db);


        db.execSQL("DROP TABLE IF EXISTS "+PB_TABLE_NAME);
        onCreate(db);
    }

}