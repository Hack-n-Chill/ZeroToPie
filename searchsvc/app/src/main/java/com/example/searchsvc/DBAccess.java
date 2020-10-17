package com.example.searchsvc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBAccess
{

    public  static List<String> getAreas(Context ctx) {
        List<String> list = new ArrayList<>();
        DBWrapper dbw= new DBWrapper(ctx);
        SQLiteDatabase sqdb = dbw.getReadableDatabase();

        Cursor cursor = null;
        StringBuffer sql = new StringBuffer("SELECT area FROM area_master");

        try {
            cursor = sqdb.rawQuery(sql.toString(), null);
            int rows = cursor.getCount();
            int i =0;
            if (rows > 0) {
                cursor.moveToFirst();
                do {

                    String loc = cursor.getString(cursor.getColumnIndex("AREA"));
                    list.add(loc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.i("qry","getAreas() called");
        sqdb.close();

        return list;
    }
    public static List<String> getServices(Context ctx)
    {
        List<String> list = new ArrayList<>();
        DBWrapper dbw = new DBWrapper(ctx);
        SQLiteDatabase sqdb = dbw.getReadableDatabase();

        Cursor cursor = null;
        StringBuffer sql = new StringBuffer("SELECT service FROM service_master");

        try {
            cursor = sqdb.rawQuery(sql.toString(), null);
            int rows = cursor.getCount();
            int i =0;
            if (rows > 0) {
                cursor.moveToFirst();
                do {

                    String service = cursor.getString(cursor.getColumnIndex("SERVICE"));
                    list.add(service);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.i("qry","getServices() called");
        sqdb.close();

        return list;
    }
    public static List<String> getSpecifications(Context ctx,String serviceType)
    {
        List<String> list=new ArrayList<>();
        DBWrapper dbw = new DBWrapper(ctx);
        SQLiteDatabase sqdb = dbw.getReadableDatabase();

        Cursor cursor = null;
        StringBuffer query1=new StringBuffer("SELECT service_code FROM service_master sm WHERE sm.service='"+serviceType+"'");
        int serviceCode=0;
        try
        {
            cursor = sqdb.rawQuery(query1.toString(), null);
            int rows = cursor.getCount();
            int i =0;
            if (rows > 0) {
                cursor.moveToFirst();
                do {
                    serviceCode=cursor.getInt(cursor.getColumnIndex("SERVICE_CODE")) ;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }

        StringBuffer query2 = new StringBuffer("SELECT specification FROM specification_master spec_mast" +
                " WHERE spec_mast.service_code="+serviceCode);
        try
        {
            cursor = sqdb.rawQuery(query2.toString(), null);
            int rows = cursor.getCount();
            int i=0;
            if(rows > 0){
                cursor.moveToFirst();
                do{
                    String specification = cursor.getString(cursor.getColumnIndex("SPECIFICATION"));
                    list.add(specification);
                }while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i("qry","getSpecifications() called");
        sqdb.close();
        return list;
    }
    public static List<String> getResults(Context ctx,String area,String serviceType,String specification)
    {
        List<String> resultList=new ArrayList<>();
        DBWrapper dbw = new DBWrapper(ctx);
        SQLiteDatabase sqdb = dbw.getReadableDatabase();
        String tableName=serviceType;
        Cursor cursor = null;
        StringBuffer query=new StringBuffer("SELECT name FROM "+tableName+" tname,area_master amaster,specification_master smaster WHERE " +
                "tname.area_code=amaster.area_code AND tname.tag=smaster.specification  AND amaster.area='"+area+
                "' AND smaster.specification='"+specification+"'");
        try
        {
            cursor = sqdb.rawQuery(query.toString(), null);
            int rows = cursor.getCount();
            int i=0;
            if(rows > 0){
                cursor.moveToFirst();
                do{
                    String result = cursor.getString(cursor.getColumnIndex("NAME"));
                    resultList.add(result);
                }while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.i("qry","Search results found");
        sqdb.close();
        return resultList;
    }
   static class DBWrapper extends SQLiteOpenHelper
   {
       public DBWrapper(Context context) {
           super(context, "locessentdb", null, 5);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {

           db.execSQL("CREATE TABLE AREA_MASTER(AREA TEXT NOT NULL,AREA_CODE INTEGER NOT NULL,PRIMARY KEY(AREA_CODE))");
           db.execSQL("CREATE TABLE SERVICE_MASTER(SERVICE TEXT NOT NULL,SERVICE_CODE INTEGER NOT NULL,PRIMARY KEY(SERVICE_CODE))");
           db.execSQL("CREATE TABLE RESTAURANTS(SN INTEGER NOT NULL,AREA_CODE INTEGER NOT NULL,TAG TEXT,NAME TEXT,PRIMARY KEY(SN))");
           db.execSQL("CREATE TABLE SHOPPING(SN INTEGER NOT NULL,AREA_CODE INTEGER NOT NULL,TAG TEXT,NAME TEXT,PRIMARY KEY(SN))");
           db.execSQL("CREATE TABLE SPECIFICATION_MASTER(SERVICE_CODE INTEGER NOT NULL,SPECIFICATION TEXT)");
           db.execSQL("INSERT INTO AREA_MASTER VALUES('SALT LAKE',1)");
           db.execSQL("INSERT INTO AREA_MASTER VALUES('RAJARHAT',2)");
           db.execSQL("INSERT INTO AREA_MASTER VALUES('NORTH KOLKATA',3)");
           db.execSQL("INSERT INTO AREA_MASTER VALUES('SOUTH KOLKATA',4)");
           db.execSQL("INSERT INTO AREA_MASTER VALUES('CENTRAL KOLKATA',5)");
           db.execSQL("INSERT INTO SERVICE_MASTER VALUES('SHOPPING',1)");
           db.execSQL("INSERT INTO SERVICE_MASTER VALUES('RESTAURANTS',2)");
           db.execSQL("INSERT INTO SERVICE_MASTER VALUES('HOSPITALS',3)");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(1,'CLOTHING')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(1,'SHOES')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(1,'GROCERY')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(2,'CHINESE')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(2,'ITALIAN')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(2,'NORTH INDIAN/MUGHLAI')");
           db.execSQL("INSERT INTO SPECIFICATION_MASTER VALUES(2,'DESSERTS')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(1,1,'CHINESE','Momo I Am')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(2,1,'ITALIAN','Pizza Hut')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(3,1,'NORTH INDIAN/MUGHLAI','Rang De Basanti Dhaba')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(4,1,'DESSERTS','Kookie Jar')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(5,2,'CHINESE','Chufang the Kithchen')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(6,2,'ITALIAN','The Bahamas')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(7,2,'NORTH INDIAN/MUGHLAI','Arsalan')");
           db.execSQL("INSERT INTO RESTAURANTS VALUES(8,2,'DESSERTS','Bangla Misti Hub')");



           Log.d("query","3 tables created:AREA_MASTER,SERVICE_MASTER,RESTAURANTS");
       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int i, int i1) {
           db.execSQL("drop table if exists AREA_MASTER");
           db.execSQL("drop table if exists SERVICE_MASTER");
           db.execSQL("drop table if exists RESTAURANTS");
           db.execSQL("drop table if exists SPECIFICATION_MASTER");
           onCreate(db);
       }
   }

}
