package com.example.dailybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MydatabaseHelper extends SQLiteOpenHelper {
    public static final String dailybook = "create table DailyBook("
            +"id integer primary key autoincrement,"
            + "writer text,"
            + "title text,"
            + "time text,"
            + "content text)";
    private Context mContext;

    public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dailybook);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {

    }

//    public long insertUserData(dailybook d)
//    {
//        String w=d.getWriter();
//        ContentValues values=new ContentValues();
//        values.put(writer,w);
//    }
}