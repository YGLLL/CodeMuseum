package cn.atd3.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DXkite on 2017/2/14 0014.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static  String name = "CrashHandler";
    private static final String PATH = "atd/crash";
    private static CrashHandler instance = new CrashHandler();
    private Thread.UncaughtExceptionHandler handler;
    private Context context;
    private Map<String, String> infos = new HashMap<String, String>();
    @SuppressLint("SimpleDateFormat")
    private DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    private CrashHandler() {
        Log.d(TAG, "init crash handler");
    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        this.handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d(TAG, "hand error!");
        if (!handlerException(ex) && handler != null) {
            handler.uncaughtException(thread, ex);
        } else {
            Log.d(TAG, "kill self");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    protected boolean handlerException(Throwable ex) {
        Log.e(TAG, "handler exception",ex);
        if (ex == null) return false;
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,"程序异常，记录日志中...",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        this.save(ex);
        return true;
    }

    protected void insertDeviceInfo() {
        Log.d(TAG, "prepare device info");
        PackageManager pm = context.getPackageManager();
        try {
            name=context.getPackageName();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            infos.put("versionName", pi.versionName == null ? "null" : pi.versionName);
            infos.put("versionCode", pi.versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            name="DxCrashLog";
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                infos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void save(Throwable ex) {
        Log.e(TAG,"log error",ex);
        Log.d(TAG, "prepare save file");
        insertDeviceInfo();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> enter : infos.entrySet()) {
            sb.append("\t\t").append(enter.getKey()).append("\t:\t").append(enter.getValue()).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printwriter = new PrintWriter(writer);
        ex.printStackTrace(printwriter);
        Throwable cause = ex.getCause();
        Log.d(TAG, "log causes...");
        while (cause != null) {
            cause.printStackTrace(printwriter);
            cause = cause.getCause();
        }
        printwriter.close();
        sb.append(writer.toString());
        String time = format.format(System.currentTimeMillis());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() + File.separator + PATH;
            File dir = new File(path);
            Log.d(TAG, "prepare directory:" + path);
            if (!dir.exists() && dir.mkdirs()) {
                Log.d(TAG, "create log directory");
            }
            try {
                Log.d(TAG, "start write file");
                File file = new File(path + File.separator + name + "_" + time + "_"+System.currentTimeMillis()+".log");
                if (!file.exists() && file.createNewFile()) {
                    Log.d(TAG, "create log file:" + file.getAbsolutePath());
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(sb.toString().getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error writer file!", e);
            }
        }
        Log.d(TAG, "end crash log");
        Log.i(TAG, "write log...");
    }
}
