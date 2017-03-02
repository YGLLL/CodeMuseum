package cn.atd3.support.api.v1;

import org.json.JSONException;
import org.json.JSONObject;

import cn.atd3.support.api.ServerException;

/**
 * Created by DXkite on 2017/3/1 0001.
 * 用户API接口
 */
public class User {

    /**
     * 检测注册是否需要验证码
     * @return
     * @throws ServerException
     */
    public  static boolean signInCode() throws ServerException {
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
     * @return
     * @throws ServerException
     */
    public  static boolean signUpCode() throws ServerException {
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

    /**
     * 发送心跳包
     * @param lastToken 上次心跳包的内容
     * @return 本次更新的心跳包
     * @throws ServerException
     */
    public  static String beatHeart(String lastToken) throws ServerException {
        try {
            JSONObject data=new JSONObject();
            data.put("token",lastToken);
            String get=ApiManager.action("user/beat",data);
            JSONObject object=new JSONObject(get);
            if (object.has("return")){
                return object.getJSONObject("return").getString("token");
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }
}
