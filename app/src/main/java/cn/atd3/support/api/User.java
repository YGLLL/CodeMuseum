package cn.atd3.support.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 冯世昌 on 2017/3/1 0001.
 */

public class User {
    /**
     * 检测登陆是否需要验证码git
     */
    public  static boolean signinCode(){
        String get=ApiManager.action("user/signincode");
        try {
            JSONObject object=new JSONObject(get);
            if (object.has("return")){
                return object.getBoolean("return");
            }
        } catch (JSONException e) {
            return  false;
        }
        return  false;
    }
}
