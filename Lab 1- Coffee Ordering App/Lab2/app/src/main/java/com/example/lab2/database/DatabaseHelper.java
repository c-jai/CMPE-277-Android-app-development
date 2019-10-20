package com.example.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LAB2_DATABASE.DB";
    public static final String TABLE_1_NAME = "COFFEE_RATES";
    public static final String COL_1_0 = "ID";
    public static final String COL_1_1 = "COFFEE_NAME";
    public static final String COL_1_2 = "COFFEE_COST";
    public  static final String table_1_query = "CREATE TABLE " + TABLE_1_NAME + "(" + COL_1_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_1_1 + " TEXT, " + COL_1_2 + " REAL)";

    public  static final String TABLE_2_NAME = "ORDERS";
    public static final String COL_2_0 = "ID";
    public static final String COL_2_1 = "EMAIL";
    public static final String COL_2_2 = "COFFEE";
    public static final String COL_2_3 = "PAYMENT";
    public  static final String table_2_query = "CREATE TABLE " + TABLE_2_NAME + "(" + COL_2_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_2_1 + " TEXT, " + COL_2_2 + " TEXT, "+ COL_2_3 + " REAL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_1_query);
        db.execSQL(table_2_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertOrder(String emailid, String coffee, Double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_1, emailid);
        contentValues.put(COL_2_2, coffee);
        contentValues.put(COL_2_3,price);
        long result = db.insert(TABLE_2_NAME, null, contentValues);
        if(result == -1)
            return false;
        return true;
    }

    public void setPrice(String coffee, Double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1, coffee);
        contentValues.put(COL_1_2, price);
        db.insert(TABLE_1_NAME, null, contentValues);
    }

    public Cursor getPrice(String coffee){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{ coffee };
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_1_NAME+" WHERE " + COL_1_1 + " = ?", params);
        return result;
    }

    public Cursor getOrders(String emailid){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{ emailid };
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_2_NAME+" WHERE " + COL_2_1 + " = ?", params);
        return result;
    }
}
