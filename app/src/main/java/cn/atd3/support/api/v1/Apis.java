package cn.atd3.support.api.v1;

import android.icu.text.IDNA;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

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
public class Apis {
    private final static String TAG="Apis";

    /**
     * 检测注册是否需要验证码
     */
    public static void checkSignUpNeedCode(final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String get=ApiManager.action("user/signupCodeIfy");
                    Log.i(TAG,"checkSignUpNeedCode:"+get+"end");
                    Gson gson=new Gson();
                    BooleanModel booleanModel =gson.fromJson(get, BooleanModel.class);
                    if(TextUtils.isEmpty(booleanModel.error)){
                        Log.i(TAG,"checkSignUpNeedCodehead.data"+String.valueOf(booleanModel.data));
                            apiActions.checkSignUpNeedCode(booleanModel.data);
                    }else {
                        Log.e(TAG,"error:"+ booleanModel.error+",message:"+ booleanModel.message);
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
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
                    String get=ApiManager.action("user/signinCodeIfy");
                    Gson gson=new Gson();
                    BooleanModel booleanModel =gson.fromJson(get, BooleanModel.class);
                    if(TextUtils.isEmpty(booleanModel.error)){
                        Log.i(TAG,"checkSignInNeedCodebooleanData.data"+String.valueOf(booleanModel.data));
                            apiActions.checkSignUpNeedCode(booleanModel.data);
                    }else {
                        Log.e(TAG,"error:"+ booleanModel.error+",message:"+ booleanModel.message);
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
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
                    String get=ApiManager.action("user/checkNameExist",jsonvalue.toString());
                    Gson gson=new Gson();
                    BooleanModel booleanModel =gson.fromJson(get,BooleanModel.class);
                    if(TextUtils.isEmpty(booleanModel.error)){
                            apiActions.checkUserId(booleanModel.data);
                    }else {
                        Log.e(TAG,"error:"+ booleanModel.error+",message:"+ booleanModel.message);
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
                    String get=ApiManager.action("user/checkEmailExist",jsonvalue.toString());
                    Gson gson =new Gson();
                    BooleanModel booleanModel =gson.fromJson(get,BooleanModel.class);
                    if(TextUtils.isEmpty(booleanModel.error)){
                        apiActions.checkUserEmail(booleanModel.data);
                    }else {
                        Log.e(TAG,"error:"+ booleanModel.error+",message:"+ booleanModel.message);
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
                    StringBuffer myCookie=new StringBuffer();
                    String get=ApiManager.action("user/signupWithoutPasswd",value,myCookie);
                    Log.i(TAG,"signupWithoutPasswd"+get);
                    Gson gson=new Gson();
                    Head head=gson.fromJson(get,Head.class);
                    if(TextUtils.isEmpty(head.error)){
                        apiActions.userSignUp(true,myCookie.toString());
                    }else {
                        Log.e(TAG,"error:"+ head.error+",message:"+ head.message);
                        apiActions.userSignUp(false,head.message);
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
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
                    Gson gson=new Gson();
                    Head head=gson.fromJson(get,Head.class);
                    if(TextUtils.isEmpty(head.error)){
                        BooleanModel booleanModel=gson.fromJson(get,BooleanModel.class);
                        if (booleanModel.data){
                            apiActions.userSignIn(true,null);
                        }
                    }else {
                        Log.e(TAG,"userSignInerror:"+ head.error+",message:"+ head.message);
                        apiActions.userSignIn(false,head.message);
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
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
                    String get=ApiManager.action("user/beat",jsonvalue.toString());
                    apiActions.beatHeart(get);
                    Log.i("xxx",get+"end");
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
    public static void getUserInformation(final String cookie, final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG,"getUserInformation cookie:"+cookie);
                    StringBuffer sb=new StringBuffer(cookie);
                    String get=ApiManager.action("user/info",sb);
                    Gson gson=new Gson();
                    Head head=gson.fromJson(get,Head.class);
                    Log.i(TAG,"getUserInformation"+get);
                    if(TextUtils.isEmpty(head.error)){
                        //获取信息，解析
                        InfoModel infoModel=gson.fromJson(get,InfoModel.class);
                        InfoData infoData=infoModel.data;
                        apiActions.getUserInformation(infoData.id,infoData.name,infoData.email);
                        Log.i(TAG,"get:"+get);
                    }else {
                        Log.e(TAG,"getUserInformation"+ head.error+",message:"+ head.message);
                    }
                }catch (ServerException e){
                    apiActions.serverException(e);
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
                    String get=ApiManager.action("user/publicinfo",jsonvalue.toString());
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
                    String get=ApiManager.action("msg/send",jsonObject.toString());
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
                    String get=ApiManager.action("msg/inbox",jsonObject.toString());
                    apiActions.inboxmessage(get);
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }

    /*
    提交邮箱验证码
     */
    public static void checkemailcode(final int emailcode,final ApiActions apiActions){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("token",BEATTOKEN);
                    jsonObject.put("value",emailcode);
                    Log.i("xxx","commit:"+jsonObject.toString());
                    String get=ApiManager.action("user/checkemailcode",jsonObject.toString());
                    apiActions.checkemailcode(get);
                }catch (ServerException e){
                    apiActions.serverException(e);
                }catch (JSONException e){
                    apiActions.serverException(new ServerException("server response format exception", e));
                }
            }
        }).start();
    }
    //model
    class Head {
        public String error;
        public String message;
    }
    class BooleanModel extends Head {
        public Boolean data;
    }
    class SignUpModel extends Head {
        public SignUpData data;
    }
    class SignUpData{
        //"uid":9,"token":"MTYuZWVkOWJjZDU4MTg4Zjk3YjMzM2M0NTA5ZGExNDI3MzM="
        public int uid;
        public String token;
    }
    class InfoModel extends Head{
        public InfoData data;
    }
    class InfoData{
        //{"error":null,"message":null,"data":{"id":"48","name":"cxfvxx","email":"frgsfjhdSff@dfd.com","available":"0","avatar":"0","ip":"121.31.251.86"}}
        String id;
        String name;
        String email;
    }
}