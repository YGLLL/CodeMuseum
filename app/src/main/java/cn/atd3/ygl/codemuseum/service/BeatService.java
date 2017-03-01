package cn.atd3.ygl.codemuseum.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.atd3.support.api.ServerConnectException;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.util.HttpCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpUtil;

import static cn.atd3.ygl.codemuseum.util.Utility.beattoken;

/**
 * Created by YGL on 2017/2/27.
 */

public class BeatService extends Service{
    private final String atdhome="http://api.i.atd3.cn";
    private final String key="token=c7b04d1534f1ed7bb9241cf5fe6ea11e&client=1";
    private final String atdbeat=atdhome+"/v1.0/user/beat?"+key;
    final  String TAG="Beat_Server";
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"before_beat:"+beattoken);
                try {
                    beattoken=User.beatHeart(beattoken);
                    Log.i(TAG,"after_beat:"+beattoken);
                } catch (ServerConnectException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(runnable,50,50, TimeUnit.SECONDS);
        return super.onStartCommand(intent,flags,startId);
    }
}
