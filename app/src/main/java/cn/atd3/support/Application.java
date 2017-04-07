package cn.atd3.support;

import android.content.Context;

import cn.atd3.support.api.v1.ApiManager;

/**
 * Created by DXkite on 2017/2/14 0014.
 */
public class Application extends android.app.Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        // 记录日志
        CrashHandler.getInstance().init(getApplicationContext());
        // API初始化
        ApiManager.getInstance().init(getApplicationContext());

        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
