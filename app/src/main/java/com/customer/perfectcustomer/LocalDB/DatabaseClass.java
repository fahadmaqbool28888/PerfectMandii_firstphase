package com.customer.perfectcustomer.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.Model.FreightForwarder;
import com.customer.perfectcustomer.Model.Product.productTABLE;
import com.customer.perfectcustomer.Model.RFQMODEL.rfq_model;

import java.util.ArrayList;


public class DatabaseClass extends SQLiteOpenHelper
{
    //sqLiteHelper.addNewCustomer(userModel.accountid,userModel.Name,userModel.contact,userModel.session,userModel.shop,userModel.profilepic,userModel.city);


    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "mandidb";

    // below int is our database version
    private static final int DB_VERSION = 7;

    private static final String PRODUCT_TABLE="product_table";
    private static final String PT_ID="pt_id";
    private static final String PT_NAME="pt_name";

    private static final String RFQ_TABLE="rfq_table";
    private static final String RFQ_ID="rfq_id";
    private static final String RFQ_OCID="rfq_ocid";
    private static final String RFQ_I_D="rfq_i_d";
    private static final String RFQ_r_f_q="rfq_r_f_q";


    // below variable is for our table name.
    private static final String TABLE_NAME = "Customer";
    private static final String FREIGHT_TABLE_NAME = "freightforwarder";

    private static final String FREIGHT_ID="fr_id";
    private static final String FREIGHT_NAME="fr_name";
    private static final String FREIGHT_ADDRESS="fr_address";
    private static final String FREIGHT_CONTACT="fr_contact";

    private static final String FREIGHT_BY="fr_by";

    // below variable is for our id column.
    private static final String ID_COL = "id";


    String accountid,name,contact,session,shop,profilepic,city;
    private static final String ACCOUNT_ID="accountid";

    private static final String NAME="name";

    private static final String CONTACT="contact";

    private static final String SESSION="session";

    private static final String SHOP="shop";

    private static final String PROFILE_PIC="profilepic";

    private static final String CITY="city";

    String createTable= "CREATE TABLE "+ TABLE_NAME+ "("+ID_COL+" INTEGER PRIMARY KEY, "+ACCOUNT_ID+" TEXT,"+NAME+" TEXT,"+ CONTACT +" TEXT,"+SESSION+" TEXT,"+ SHOP +" TEXT,"+ PROFILE_PIC + " TEXT,"+CITY+" TEXT)";

    String createfreight= "CREATE TABLE "+ FREIGHT_TABLE_NAME+ "("+FREIGHT_ID+" INTEGER PRIMARY KEY, "+FREIGHT_NAME+" TEXT,"+FREIGHT_ADDRESS+" TEXT,"+ FREIGHT_CONTACT +" TEXT,"+FREIGHT_BY+" TEXT)";

    String createproductlist="CREATE TABLE " + PRODUCT_TABLE + "("+PT_ID+ " INTEGER PRIMARY KEY, " + PT_NAME + " TEXT  )";

    /*    private static final String RFQ_TABLE="rfq_table";
    private static final String RFQ_ID="rfq_id";
    private static final String RFQ_OCID="rfq_ocid";
    private static final String RFQ_I_D="rfq_i_d";
    private static final String RFQ_r_f_q="rfq_r_f_q";*/
    String createrfq= "CREATE TABLE "+ RFQ_TABLE+ "("+RFQ_ID+" INTEGER PRIMARY KEY, "+RFQ_OCID+" TEXT,"+RFQ_I_D+" TEXT,"+ RFQ_r_f_q +" TEXT)";


    public DatabaseClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        System.out.println("From"+createTable);
        db.execSQL(createTable);
        db.execSQL(createfreight);
        db.execSQL(createrfq);
        db.execSQL(createproductlist);

    }

    public boolean addUser(String accountid,String name,String contact,String session,String shop,String profilepic,String city)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(ACCOUNT_ID,accountid);
        contentValues.put(NAME,name);
        contentValues.put(CONTACT,contact);
        contentValues.put(SESSION,session);
        contentValues.put(SHOP,shop);
        contentValues.put(PROFILE_PIC,profilepic);
        contentValues.put(CITY,city);


        long val=db.insert(TABLE_NAME,null,contentValues);

        if (val!=-1)
        {
            return true;
        }
        return false;
    }
    public boolean addRFQ(rfq_model rfqmodel)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(RFQ_OCID,rfqmodel.ocid);
        contentValues.put(RFQ_I_D,rfqmodel.i_d);
        contentValues.put(RFQ_r_f_q,rfqmodel.r_f_q);
        long val=db.insert(RFQ_TABLE,null,contentValues);

        return val != -1;
    }

    public boolean yes_RFQ(rfq_model rfqmodel)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + RFQ_TABLE + " where " + RFQ_OCID + " = " + "'"+rfqmodel.ocid+"'" +" and " + RFQ_r_f_q + " = " +  "'"+rfqmodel.ocid+"'" ;

        System.out.println(Query);
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }
    }

    public boolean addFreight(FreightForwarder freightForwarder)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(FREIGHT_NAME,freightForwarder.name);
        contentValues.put(FREIGHT_ADDRESS,freightForwarder.address);
        contentValues.put(FREIGHT_CONTACT,freightForwarder.contact);
        contentValues.put(FREIGHT_BY,freightForwarder.fby);



        long val=db.insert(FREIGHT_TABLE_NAME,null,contentValues);

        return val != -1;
    }

    public boolean addlistProduct(productTABLE producttable)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(PT_NAME,producttable.name);




        long val=db.insert(PRODUCT_TABLE,null,contentValues);

        return val != -1;
    }


    public void delete()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

    }

    public   ArrayList<FreightForwarder> getFreightList()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ArrayList<FreightForwarder> arrayList=new ArrayList<>();

        Cursor cursor=db.rawQuery("Select * FROM "+FREIGHT_TABLE_NAME,null);

        if (cursor.moveToFirst())
        {
            do
            {
                arrayList.add(new FreightForwarder(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
            }
            while (cursor.moveToNext());
        }

        return  arrayList;
    }

    public   ArrayList<productTABLE> getProductLIST()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ArrayList<productTABLE> arrayList=new ArrayList<>();

        Cursor cursor=db.rawQuery("Select * FROM "+PRODUCT_TABLE,null);

        if (cursor.moveToFirst())
        {
            do
            {
                arrayList.add(new productTABLE(cursor.getInt(0),cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }

        return  arrayList;
    }

    public boolean checkAllready(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + FREIGHT_TABLE_NAME + " where " + FREIGHT_NAME + " = " + "'"+name+"'";

        System.out.println(Query);
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }
    }

    public boolean checkproductexist(productTABLE producttable)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + PRODUCT_TABLE + " where " + PT_NAME + " = " + "'"+producttable.name+"'";

        System.out.println(Query);
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }
    }

    // below is the method for deleting our course.
    public boolean delete_rfq(String courseName) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
       return  db.delete(RFQ_TABLE, "rfq_ocid=?", new String[]{courseName})>0;

    }
    public ArrayList<FreightForwarder>  cready(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FreightForwarder> arrayList=new ArrayList<>();
        String Query = "Select * from " + FREIGHT_TABLE_NAME + " where " + FREIGHT_NAME + " = " + "'"+name+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst())
        {
            do
            {
                arrayList.add(new FreightForwarder(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
            }
            while (cursor.moveToNext());
        }

        return  arrayList;
    }



    public ArrayList<rfq_model>  getRFQlist()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<rfq_model> arrayList=new ArrayList<>();
        String Query = "Select * from " + RFQ_TABLE ;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst())
        {
            do
            {
                arrayList.add(new rfq_model(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }
            while (cursor.moveToNext());
        }

        return  arrayList;
    }

  public   ArrayList<UserModel> arrayList()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ArrayList<UserModel> arrayList=new ArrayList<>();

        Cursor cursor=db.rawQuery("Select * FROM "+TABLE_NAME,null);

        if (cursor.moveToFirst())
        {
            do
            {
                arrayList.add(new UserModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));
            }
            while (cursor.moveToNext());
        }

    return  arrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FREIGHT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RFQ_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);

        onCreate(db);
    }
}
