package cn.atd3.support.api.v1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.atd3.support.api.ServerException;

import static cn.atd3.ygl.codemuseum.service.BeatService.BEATTOKEN;

/**
 * Created by DXkite on 2017/3/1 0001.
 * Update by YGL on 2017/3/5
 * 用户API接口
 */
public class User {
    /**
     * 检测注册是否需要验证码
     */
    public static void checkSignUpNeedCode(final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String get=ApiManager.action("user/signupcode");
                    JSONObject object=new JSONObject(get);
                    if (object.has("return")){
                        apiActions.checkSignUpNeedCode(object.getBoolean("return"));
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /**
     * 检测登陆是否需要验证码
     */
    public static void checkSignInNeedCode(final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String get=ApiManager.action("user/signincode");
                    JSONObject object=new JSONObject(get);
                    if (object.has("return")){
                        apiActions.checkSignInNeedCode(object.getBoolean("return"));
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    获取验证码图片
     */
    public static void getCodePicture(final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    apiActions.getCodePicture(ApiManager.getHttpPicture("verify_image"));
                }catch (ServerException e){
                    apiActions.serverException(e);
                }
            }
        }).start();
    }

    /*
    查询用户名是否存在
     */
    public static void checkUserId(final String value,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonvalue=new JSONObject();
                    jsonvalue.put("name",value);
                    String get=ApiManager.action("user/checkname",jsonvalue);
                    JSONObject jsonObject=new JSONObject(get);
                    if (jsonObject.has("return")){
                        apiActions.checkUserId(jsonObject.getBoolean("return"));
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    查询用户邮箱是否存在
     */
    public static void checkUserEmail(final String value,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonvalue=new JSONObject();
                    jsonvalue.put("email",value);
                    String get=ApiManager.action("user/checkemail",jsonvalue);
                    JSONObject jsonObject=new JSONObject(get);
                    if (jsonObject.has("return")){
                        apiActions.checkUserEmail(jsonObject.getBoolean("return"));
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    注册
     */
    public static void userSignUp(final String value,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String get=ApiManager.action("user/signup",value);
                    JSONObject jsonObject=new JSONObject(get);
                    if(jsonObject.has("error")){
                        apiActions.userSignUp(false,"codeerror");
                    }else {
                        jsonObject=jsonObject.getJSONObject("return");
                        if(jsonObject.has("uid")){
                            //注册成功
                            apiActions.userSignUp(true,jsonObject.toString());
                        }
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    登陆
     */
    public static void userSignIn(final String value,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String get=ApiManager.action("user/signin",value);
                    JSONObject jsonObject=new JSONObject(get);
                    if(jsonObject.has("error")){
                        apiActions.userSignIn(false,"codeerror");
                    }else {
                        String returnstring=jsonObject.getString("return");
                        if(returnstring.equals("false")){
                            apiActions.userSignIn(false,"passworderror");
                        }else {
                            if(returnstring.equals("true")){
                                jsonObject=jsonObject.getJSONObject("token");
                                apiActions.userSignIn(true,jsonObject.getString("user"));
                            }
                        }
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    发送心跳包
     */
    public static void beatHeart(final String lastToken,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonvalue=new JSONObject();
                    jsonvalue.put("token",lastToken);
                    String get=ApiManager.action("user/beat",jsonvalue);
                    JSONObject jsonObject=new JSONObject(get);
                    if (jsonObject.has("return")){
                        apiActions.beatHeart(jsonObject.getJSONObject("return").getString("token"));
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    查询登陆用户信息
     */
    public static void getUserInformation(final String token,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("user",token);
                    jsonObject.put("expire",3600);
                    JSONObject jsonvalue=new JSONObject();
                    jsonvalue.put("token",jsonObject);
                    String get=ApiManager.action("user/info",jsonvalue);
                    jsonvalue=new JSONObject(get);
                    if (jsonvalue.has("return")){
                        apiActions.getUserInformation(jsonvalue.toString());
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    查询用户公开信息
     */
    public static void getUserPublicInformation(final int[] uids,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray=new JSONArray();
                    for(int i:uids){
                        jsonArray.put(i);
                    }

                    JSONObject jsonvalue=new JSONObject();
                    jsonvalue.put("uids",jsonArray);
                    String get=ApiManager.action("user/publicinfo",jsonvalue);
                    jsonvalue=new JSONObject(get);
                    if (jsonvalue.has("return")){
                        apiActions.getUserPublicInformation(jsonvalue.toString());
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    发送私信
     */
    public static void sendMessage(final String message,final int uid,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("type",5);
                    jsonObject.put("message",message);
                    jsonObject.put("to",uid);

                    JSONObject user=new JSONObject();
                    user.put("user",BEATTOKEN);

                    jsonObject.put("token",user);
                    String get=ApiManager.action("msg/send",jsonObject);
                    apiActions.sendMessage(get);
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    接收信息
     */
    public static void inboxmessage(final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject=new JSONObject();
                    JSONObject user=new JSONObject();
                    user.put("user",BEATTOKEN);
                    jsonObject.put("token",user);
                    String get=ApiManager.action("msg/inbox",jsonObject);
                    apiActions.inboxmessage(get);
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }
}
