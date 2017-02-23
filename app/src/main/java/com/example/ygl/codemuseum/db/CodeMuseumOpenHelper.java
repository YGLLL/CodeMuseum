package com.example.ygl.codemuseum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YGL on 2017/2/12.
 */

public class CodeMuseumOpenHelper extends SQLiteOpenHelper {
    /**
     *用户消息表
     */
    public static final String MESSAGE="create table message(" +
            "id integer primary key autoincrement," +
            "message_time text," +
            "message_sender text," +
            "message_address text," +
            "message_content text)";

    public CodeMuseumOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(MESSAGE);// 创建用户消息表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
