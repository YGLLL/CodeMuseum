package cn.atd3.ygl.codemuseum.util;

import android.util.Log;

import org.litepal.crud.DataSupport;

import cn.atd3.ygl.codemuseum.model.User;

/**
 * Created by YGL on 2017/4/23.
 */

public class SQLUtil {
    private static final String TAG = "SQLUtil";
    public static String MYCOOKIE;
    public static Boolean IsHaveUser(){
        User user= DataSupport.findFirst(User.class);
        Log.i(TAG,String.valueOf(DataSupport.findFirst(User.class)!=null));
        return user!=null;
    }
}
