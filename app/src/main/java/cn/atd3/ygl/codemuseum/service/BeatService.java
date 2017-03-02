package cn.atd3.ygl.codemuseum.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.User;

import static cn.atd3.ygl.codemuseum.util.Utility.beattoken;

/**
 * Created by YGL on 2017/2/27.
 * 心跳包发送服务
 *
 */
public class BeatService extends Service{
    final  String TAG="BeatServer";
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Log.v(TAG,"before_beat:"+beattoken);
                try {
                    beattoken=User.beatHeart(beattoken);
                    Log.v(TAG,"after_beat:"+beattoken);
                } catch (ServerException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(runnable,50,50, TimeUnit.SECONDS);
        return super.onStartCommand(intent,flags,startId);
    }
}
