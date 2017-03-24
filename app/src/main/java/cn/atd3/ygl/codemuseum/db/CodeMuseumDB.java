package cn.atd3.ygl.codemuseum.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.atd3.ygl.codemuseum.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGL on 2017/2/12.
 */

public class CodeMuseumDB {
    //封装数据库
    public static final String DB_NAME="code_museum";
    public static final int DB_VERSION=4;//2017.3.24更新数据库
    private static CodeMuseumDB codeMuseumDB;
    private SQLiteDatabase db;

    //将构造器隐藏，使用getInstance得到CodeMuseumDB对象，确保全局只有一个CodeMuseumDB对象
    private CodeMuseumDB(Context context){
        CodeMuseumOpenHelper dbHelper=new CodeMuseumOpenHelper(context,DB_NAME,null,DB_VERSION);
        db=dbHelper.getWritableDatabase();
    }

    public synchronized static CodeMuseumDB getInstance(Context context){
        if(codeMuseumDB==null){
            codeMuseumDB=new CodeMuseumDB(context);
        }
        return codeMuseumDB;
    }

    public void saveUserMessage(UserMessage usermessage){
        if(usermessage!=null){
            ContentValues values=new ContentValues();
            values.put("message_time",usermessage.getMessage_time());
            values.put("message_sender",usermessage.getMessage_sender());
            values.put("message_paper",usermessage.getMessage_paper());
            values.put("message_content",usermessage.getMessage_content());
            db.insert("message",null,values);
        }
    }

    public List<UserMessage> readUserMessage(){
        List<UserMessage> userMessageList=new ArrayList<UserMessage>();
        Cursor cursor=db.query("message",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                UserMessage userMessage=new UserMessage();
                userMessage.setMessage_time(cursor.getString(cursor.getColumnIndex("message_time")));
                userMessage.setMessage_paper(cursor.getString(cursor.getColumnIndex("message_paper")));
                userMessage.setMessage_sender(cursor.getString(cursor.getColumnIndex("message_sender")));
                userMessage.setMessage_content(cursor.getString(cursor.getColumnIndex("message_content")));
                userMessageList.add(userMessage);
            }while (cursor.moveToNext());
        }
        return userMessageList;
    }

    public void saveUser(int uid,String name,String pwd,String beat_token){
        ContentValues contentValues=new ContentValues();
        contentValues.put("uid",uid);
        contentValues.put("name",name);
        contentValues.put("pwd",pwd);
        contentValues.put("beat_token",beat_token);
        db.insert("user",null,contentValues);
    }
    public void updateToken(String token){
        ContentValues contentValues=new ContentValues();
        contentValues.put("beat_token",token);
        db.update("user",contentValues,null,null);
    }
    public String readToken(){
        Cursor cursor=db.query("user",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex("beat_token"));
        }else {
            return null;
        }
    }
    public String readUserName(){
        Cursor cursor=db.query("user",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex("name"));
        }else {
            return null;
        }
    }
}
