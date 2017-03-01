package cn.atd3.support;


/**
 * Created by DXkite on 2017/2/14 0014.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 记录日志
        CrashHandler.getInstance().init(getApplicationContext());
    }
}
