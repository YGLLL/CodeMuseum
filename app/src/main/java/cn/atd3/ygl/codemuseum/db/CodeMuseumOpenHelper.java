package cn.atd3.ygl.codemuseum.db;

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
            "message_addressee text," +
            "message_content text," +
            "message_paper text," +
            "message_type integer)";
    public static final String USER="create table user(" +
            "uid integer," +
            "name text," +
            "pwd text," +
            "beat_token text)";

    public CodeMuseumOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(MESSAGE);// 创建用户消息表
        db.execSQL(USER);//创建用户信息表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists message");//如果存在message表，就删除它
        db.execSQL("drop table if exists beattoken");
        onCreate(db);
    }
}
