package cn.atd3.support.api.v1;

import org.json.JSONException;
import org.json.JSONObject;

import cn.atd3.support.api.ServerConnectException;

/**
 * Created by DXkite on 2017/3/1 0001.
 * 用户API接口
 */

public class User {
    /**
     * 检测登陆是否需要验证码
     */
    public  static boolean signInCode() throws ServerConnectException {
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
    /**
     * 检测注册是否需要验证码
     */
    public  static boolean signUpCode() throws ServerConnectException {
        String get=ApiManager.action("user/signupcode");
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
    public  static String beatHeart(String lastToken) throws ServerConnectException {
        JSONObject data=new JSONObject();
        try {
            data.put("token",lastToken);
            String get=ApiManager.action("user/signupcode",data.toString());
            JSONObject object=new JSONObject(get);
            if (object.has("token")){
                return object.getString("token");
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }
}
