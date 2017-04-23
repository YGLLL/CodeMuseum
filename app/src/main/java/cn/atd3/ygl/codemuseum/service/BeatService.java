package cn.atd3.ygl.codemuseum.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.model.User;

/**
 * Created by YGL on 2017/2/27.
 * 心跳包发送服务
 *
 */
public class BeatService extends Service{
    public static String BEATTOKEN="";

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        //codeMuseumDB=CodeMuseumDB.getInstance(BeatService.this);
        //User user=codeMuseumDB.readUser();
        User user= DataSupport.findFirst(User.class);
        BEATTOKEN=user.getCookie();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Apis.beatHeart(BEATTOKEN, new ApiActions() {
                    @Override
                    public void beatHeart(String returnString){
                        try{
                            JSONObject jsonObject=new JSONObject(returnString);
                            if(jsonObject.getString("error").equals("null")){
                                jsonObject=jsonObject.getJSONObject("data");
                                String nextToken=jsonObject.getString("token");
                                BEATTOKEN=nextToken;
                                //codeMuseumDB.updateUser(CodeMuseumDB.BEAT_TOKEN,nextToken);
                                User user =new User();
                                user.setCookie(nextToken);
                                user.updateAll();
                            }else {
                                if(jsonObject.getString("error").equals("refreshTokenError")){
                                    signin();
                                }
                            }
                        }catch (JSONException e){e.printStackTrace();}
                    }
                    @Override
                    public void serverException(ServerException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(runnable,0,50, TimeUnit.SECONDS);
        return super.onStartCommand(intent,flags,startId);
    }

    private void signin(){
        User user=DataSupport.findFirst(User.class);
        String jsonString="";
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",user.getName());
            jsonObject.put("passwd",user.getPwd());
            jsonObject.put("code","");
            jsonString=jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        Apis.userSignIn(jsonString, new ApiActions() {
            @Override
            public void userSignIn(boolean success,String message){
                if(success){
                    BEATTOKEN=message;
                    User user=new User();
                    user.setCookie(BEATTOKEN);
                    user.updateAll();
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }
}
