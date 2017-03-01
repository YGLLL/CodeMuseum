package cn.atd3.support;


public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 记录日志
        CrashHandler.getInstance().init(getApplicationContext());
    }
}
