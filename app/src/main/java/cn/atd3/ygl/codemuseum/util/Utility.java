package cn.atd3.ygl.codemuseum.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by YGL on 2017/2/13.
 */

public class Utility {
    //在这里解析JSON数据
    // TODO：心跳包Token用数据库或者文件存储
    public static String beattoken="";

    public static String urlAddT(String url){
        return url+"&t="+String.valueOf(new Date().getTime());
    }
}
