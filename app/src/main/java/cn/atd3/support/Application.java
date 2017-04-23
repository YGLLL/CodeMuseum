package cn.atd3.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import cn.atd3.support.api.v1.ApiManager;
import cn.atd3.ygl.codemuseum.model.User;

import static cn.atd3.ygl.codemuseum.util.Utility.MYCOOKIE;

/**
 * Created by DXkite on 2017/2/14 0014.
 */
public class Application extends LitePalApplication {
    private static final String TAG = "Application";
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        // 记录日志
        CrashHandler.getInstance().init(getApplicationContext());
        // API初始化
        ApiManager.getInstance().init(getApplicationContext());

        //将数据库储存的cockie读取到内存
        if(DataSupport.findFirst(User.class)!=null){
            User user= DataSupport.findFirst(User.class);
            Log.i(TAG,"user.getCookie():"+user.getCookie());
            MYCOOKIE=user.getCookie();
        }else {
            Log.i(TAG,"DataSupport.findFirst(User.class):no data");
        }

        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
