package cn.atd3.support.api.v1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.atd3.support.api.ServerConnectException;
import cn.atd3.support.api.ClientNoFoundException;

/**
 * Created by DXkite on 2017/3/1 0001.
 * API管理器
 *  修改日志：
 *      + 添加异常类输出
 *      + 添加版本命名空间区分
 */
public class ApiManager {
    public  static int CLIENT_ID;
    public  static String CLIENT_TOKEN;
    public  static String TAG="ApiClient";
    private static String API_HOST="http://api.i.atd3.cn";
    private static String API_VERSION="v1.0";
    private static int timeOut=5000;
    private static String META_NAME="cn.atd3.support.api.v1.ClientToken";

    static ApiManager instance=new ApiManager();

    public static int getTimeOut() {
        return timeOut;
    }

    public boolean init(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String msg=appInfo.metaData.getString(META_NAME);
            int pos= 0;
            if (msg != null) {
                pos = msg.indexOf(":");
                CLIENT_ID=Integer.parseInt(msg.substring(0,pos));
                CLIENT_TOKEN=msg.substring(pos+1);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new ClientNoFoundException("client info no find in manifast",e.getCause());
        }
        return  true;
    }
    public static void setTimeOut(int timeOut) {
        ApiManager.timeOut = timeOut;
    }
    public  static ApiManager getInstance(){
        return instance;
    }

    public static  String action(String action)throws ServerConnectException {
        return action(action,null);
    }

    public static  String action(String action,String data)throws ServerConnectException {
        return action(action,data,"application/json");
    }

    public static  String action(String action,String data,String type) throws ServerConnectException {
        String address=API_HOST+'/'+API_VERSION+'/'+action;
        String response="";
        try {
            // 创建连接
            HttpURLConnection httpUrlConnection =(HttpURLConnection)new URL(address).openConnection();
            // 设置服务器属性
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setConnectTimeout(timeOut);
            httpUrlConnection.setReadTimeout(timeOut);

            // 压入客户端验证信息
            httpUrlConnection.setRequestProperty("API-Client",""+CLIENT_ID);
            httpUrlConnection.setRequestProperty("API-Token",CLIENT_TOKEN);
            httpUrlConnection.setRequestProperty("User-Agent",TAG);

            // 连接服务器
            if (data==null){
                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.connect();
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
            response=out;
            httpUrlConnection.disconnect();
        } catch (IOException e) {
            throw new ServerConnectException("read response failed",e);
        }
        return response;
    }
}
