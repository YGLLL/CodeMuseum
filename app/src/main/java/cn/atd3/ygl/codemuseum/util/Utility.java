package cn.atd3.ygl.codemuseum.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.atd3.ygl.codemuseum.db.CodeMuseumDB;

/**
 * Created by YGL on 2017/2/13.
 */

public class Utility {
    //在这里解析JSON数据

    /*******
    public static String urlAddT(String url){
        return url+"&t="+String.valueOf(new Date().getTime());
    }
     **************/

    public static void saveUserInfo(Context context,String key,String value){
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void saveUserInfo(Context context,String key,Boolean value){
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static void saveUserInfo(Context context,String key,int value){
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key,value);
        editor.commit();
    }
    //查询是否登陆
    public static Boolean IsSignedIn(Context context){
        CodeMuseumDB codeMuseumDB=CodeMuseumDB.getInstance(context);
        if(TextUtils.isEmpty(codeMuseumDB.readToken())){
            return false;
        }else {
            return true;
        }
    }
}
