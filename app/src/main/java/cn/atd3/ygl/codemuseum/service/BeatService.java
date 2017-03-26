package cn.atd3.ygl.codemuseum.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.db.CodeMuseumDB;
import cn.atd3.ygl.codemuseum.model.User;

/**
 * Created by YGL on 2017/2/27.
 * 心跳包发送服务
 *
 */
public class BeatService extends Service{
    public static String BEATTOKEN="";
    private CodeMuseumDB codeMuseumDB;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        codeMuseumDB=CodeMuseumDB.getInstance(BeatService.this);
        User user=codeMuseumDB.readUser();
        BEATTOKEN=user.getBeat_token();
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
                            if(jsonObject.has("return")){
                                if (jsonObject.getBoolean("return")==false){
                                    //token失效，重新登陆
                                    signin();
                                }else {
                                    String nextToken=jsonObject.getJSONObject("return").getString("token");
                                    BEATTOKEN=nextToken;
                                    codeMuseumDB.updateUser(CodeMuseumDB.BEAT_TOKEN,nextToken);
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
        User user=codeMuseumDB.readUser();
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
                    codeMuseumDB.updateUser(CodeMuseumDB.BEAT_TOKEN,message);
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }
}
