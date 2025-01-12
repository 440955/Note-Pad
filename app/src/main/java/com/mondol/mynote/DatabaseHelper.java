package com.mondol.mynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    String Date = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
    String Time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm"));

    String tableName ="MyTable";
    public static final String BD_NAME ="My_note";
    public static final int DB_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, BD_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table MyTable(id INTEGER primary key autoincrement, type TEXT, title TEXT, details TEXT, date TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists MyTable");
    }

    public void addData(String Type, String Title, String Details){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues conval = new ContentValues();
        conval.put("type", Type);
        conval.put("title", Title);
        conval.put("details", Details);
        conval.put("date", Date);
        conval.put("time", Time);
        sqLiteDatabase.insert("MyTable", null, conval);
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from MyTable order by id DESC", null);
        return cursor;
    }

    public void updateData(String id, String update_type, String update_title, String update_details){
        SQLiteDatabase sqLiteDatabase= getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE MyTable SET type= '"+update_type+"', title= '"+update_title+"', details= '"+update_details+"', time= '"+Time+"', date='"+Date+"' WHERE ID= '"+id+"' ");
    }

    public void delete(String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("delete from MyTable where id like "+id);
    }

    public Cursor searchData(String key){
        SQLiteDatabase sqLiteDatabase= getReadableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("select * from MyTable where title like '%"+key+"%' order by id desc", null);
        return cursor;
    }

    public Cursor getShare(String ID){
        SQLiteDatabase sqLiteDatabase= getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from MyTable where id="+ID, null);
        return cursor;
    }


}
