package cn.atd3.ygl.codemuseum.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.model.User;
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

    public static final String UID="uid";
    public static final String NAME="name";
    public static final String PWD="pwd";
    public static final String BEAT_TOKEN="beat_token";

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

    public void saveUser(String key,String value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.insert("user",null,contentValues);
    }
    public void saveUser(String key,Boolean value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.insert("user",null,contentValues);
    }
    public void saveUser(String key,int value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.insert("user",null,contentValues);
    }
    public void updateUser(String key,String value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.update("user",contentValues,null,null);
    }
    public void updateUser(String key,Boolean value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.update("user",contentValues,null,null);
    }
    public void updateUser(String key,int value){
        ContentValues contentValues=new ContentValues();
        contentValues.put(key,value);
        db.update("user",contentValues,null,null);
    }
    public User readUser(){
        User user=new User();
        Cursor cursor=db.query("user",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
                user.setBeat_token(cursor.getString(cursor.getColumnIndex("beat_token")));
            }while (cursor.moveToNext());
        }
        return user;
    }
}
