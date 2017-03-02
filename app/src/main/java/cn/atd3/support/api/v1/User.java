package cn.atd3.support.api.v1;

import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import cn.atd3.support.api.JSONListener;
import cn.atd3.support.api.ServerException;

/**
 * Created by DXkite on 2017/3/1 0001.
 * 用户API接口
 */
public class User {

    /**
     * 检测登陆是否需要验证码
     *
     * @return 是否需要验证码
     * @throws ServerException
     */
    public static boolean signInCode() throws ServerException {
        String get = ApiManager.action("user/signincode");
        try {
            JSONObject object = new JSONObject(get);
            if (object.has("return")) {
                return object.getBoolean("return");
            }
        } catch (JSONException e) {
            throw new ServerException("server response format exception", e);
        }
        return false;
    }


    public static void checkSignInNeedCode(final ApiActions action) {
        new Thread(){
            @Override
            public void run() {
                boolean need= false;
                try {
                    need = signInCode();
                    // 准备发送到UI线程
                    Looper.prepare();
                    action.checkSignInNeedCode(need);
                    Looper.loop();
                } catch (ServerException e) {
                    Looper.prepare();
                    action.serverException(e);
                    Looper.loop();
                }

            }
        }.start();
    }

    public static void checkSignInNeedCode(final JSONListener action) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String get = ApiManager.action("user/signincode");
                    JSONObject object = new JSONObject(get);
                    // 准备发送到UI线程
                    Looper.prepare();
                    action.onSuccess(object);
                    Looper.loop();
                } catch (JSONException e) {
                    // 准备发送到UI线程
                    Looper.prepare();
                    action.onError(new ServerException("server response format exception", e));
                    Looper.loop();
                } catch (ServerException e) {
                    // 准备发送到UI线程
                    Looper.prepare();
                    action.onError(e);
                    Looper.loop();
                }
            }
        }.start();
    }

    /**
     * 检测注册是否需要验证码
     *
     * @return 是否需要验证码
     * @throws ServerException
     */
    public static boolean signUpCode() throws ServerException {
        String get = ApiManager.action("user/signupcode");
        try {
            JSONObject object = new JSONObject(get);
            if (object.has("return")) {
                return object.getBoolean("return");
            }
        } catch (JSONException e) {
            throw new ServerException("server response format exception", e);
        }
        return false;
    }

    /**
     * 发送心跳包
     *
     * @param lastToken 上次心跳包的Token
     * @return 本次更新的心跳包Token
     * @throws ServerException
     */
    public static String beatHeart(String lastToken) throws ServerException {
        try {
            JSONObject data = new JSONObject();
            data.put("token", lastToken);
            String get = ApiManager.action("user/beat", data);
            JSONObject object = new JSONObject(get);
            if (object.has("return")) {
                return object.getJSONObject("return").getString("token");
            }
        } catch (JSONException e) {
            throw new ServerException("server response format exception", e);
        }
        return null;
    }
}
