package com.example.ygl.codemuseum.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YGL on 2017/2/13.
 */

public class Utility {
    //在这里解析JSON数据

    //解析查询验证码返回JSON
    // {"return":{"need":false}}
    //{"return":{"need":true,"code":"\/verify.png?t=1487910827"}}
    public static String getcheckcodeforJson(String jsonstr){
        String need="";
        try{
            JSONObject jsonObject=new JSONObject(jsonstr);
            jsonObject=jsonObject.getJSONObject("return");
            need=jsonObject.getString("need");
            if(need.equals("false")){
                return need;
            }else {
                return jsonObject.getString("code");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
