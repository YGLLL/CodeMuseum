package cn.atd3.support.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 冯世昌 on 2017/3/1 0001.
 */

public class ApiManager {
    public  static int CLIENT_ID;
    public  static String CLIENT_TOKEN;
    public  static String TAG="ApiClient";
    private static String API_HOST="http://api.i.atd3.cn";
    private static String API_VERSION="v1.0";
    private static int timeOut=5000;


    static ApiManager instance=new ApiManager();

    public static int getTimeOut() {
        return timeOut;
    }

    public boolean init(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String msg=appInfo.metaData.getString("cn.atd3.api.ClientToken");
            int pos=msg.indexOf(":");
            CLIENT_ID=Integer.parseInt(msg.substring(0,pos));
            CLIENT_TOKEN=msg.substring(pos+1);
            Log.i(TAG," id:"+CLIENT_ID);
            Log.i(TAG," token:"+CLIENT_TOKEN);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  false;
        }
        return  true;
    }
    public static void setTimeOut(int timeOut) {
        ApiManager.timeOut = timeOut;
    }
    public  static ApiManager getInstance(){
        return instance;
    }

    public static  String action(String action){
        return action(action,null);
    }

    public static  String action(String action,String data){
        return action(action,data,"application/json");
    }

    public static  String action(String action,String data,String type)
    {
        String address=API_HOST+'/'+API_VERSION+'/'+action;
        String get_json="";
        try {
            URLConnection rulConnection=new URL(address).openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setConnectTimeout(timeOut);
            httpUrlConnection.setReadTimeout(timeOut);

            // 压入客户端验证信息
            httpUrlConnection.setRequestProperty("API-Client",""+CLIENT_ID);
            httpUrlConnection.setRequestProperty("API-Token",CLIENT_TOKEN);

            httpUrlConnection.setRequestProperty("User-Agent",TAG);
            if (data==null){
                httpUrlConnection.setRequestMethod("GET");
            }else{
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Content-Type", type);
                httpUrlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                httpUrlConnection.connect();
                OutputStream outputStream = httpUrlConnection.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            InputStream inputStream = httpUrlConnection.getInputStream();
            BufferedReader r=new BufferedReader(new InputStreamReader(inputStream));
            String l,out="";
            while (null!=(l=r.readLine())){
                out+=l+'\n';
            }
            get_json=out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return get_json;
    }
}
