package cn.atd3.ygl.codemuseum.util;

import android.content.Context;
import android.os.Bundle;

import cn.atd3.support.Application;

/**
 * Created by YGL on 2017/3/29.
 */

public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate(){
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
