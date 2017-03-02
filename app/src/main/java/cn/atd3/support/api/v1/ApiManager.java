package cn.atd3.support.api.v1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.ClientNoFoundException;

/**
 * Created by DXkite on 2017/3/1 0001.
 * API管理器
 *  修改日志：
 *      + 添加异常类输出
 *      + 添加版本命名空间区分
 */
public class ApiManager {
    private static int CLIENT_ID;
    private static String CLIENT_TOKEN;
    private static String TAG="ApiClient";
    private static String API_HOST="http://api.i.atd3.cn";
    private static String API_VERSION="v1.0";
    private static int timeOut=5000;
    private static String META_NAME="cn.atd3.support.api.v1.ClientToken";

    private static ApiManager instance=new ApiManager();

    private ApiManager() {
    }

    public static int getTimeOut() {
        return timeOut;
    }

    /**
     * 初始化API
     * @param context 应用环境
     * @return 初始化情况
     */
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
            throw new ClientNoFoundException("client info no found in manifast",e.getCause());
        }
        return  true;
    }

    /**
     * 设置请求超时
     * @param timeOut 超时时间
     */
    public static void setTimeOut(int timeOut) {
        ApiManager.timeOut = timeOut;
    }

    /**
     * 获取APIManager的示例化对象
     * @return ApiManager对象
     */
    public  static ApiManager getInstance(){
        return instance;
    }

    /**
     * 发送请求到服务器 [GET]
     * @param action 请求数据的接口
     * @return 服务器返回的内容
     * @throws ServerException
     */
    public static  String action(String action)throws ServerException {
        return action(action,null,null);
    }

    /**
     * 发送JSON文本到服务器
     * @param action 请求数据的接口
     * @param data 发送到服务器的文本数据
     * @return 服务器返回的内容
     * @throws ServerException
     */
    public static  String action(String action,String data)throws ServerException {
        return action(action,data,"application/json");
    }

    /**
     * 发送JSON数据到服务器
     * @param action 请求数据的接口
     * @param data 发送到服务器的数据
     * @return 服务器返回的内容
     * @throws ServerException
     */
    public static  String action(String action,JSONObject data)throws ServerException {
        return action(action,data.toString(),"application/json");
    }

    /**
     * 发送String数据到服务器，自动添加客户端授权信息
     * @param action 请求数据的接口
     * @param data 发送到服务器的数据
     * @param type 数据的类型
     * @return 服务器返回的内容
     * @throws ServerException
     */
    public static  String action(String action,String data,String type) throws ServerException {
        String address=API_HOST+'/'+API_VERSION+'/'+action;
        String response="";
        try {
            Log.e("Connect","prepare connect");
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
            Log.e("Connect","connected");
            InputStream inputStream = httpUrlConnection.getInputStream();
            BufferedReader r=new BufferedReader(new InputStreamReader(inputStream));
            String l,out="";
            while (null!=(l=r.readLine())){
                out+=l+'\n';
            }
            response=out;
            httpUrlConnection.disconnect();
        } catch (IOException e) {
            Log.e("Connect","connected exception",e);
            throw new ServerException("read response failed",e);
        }
        return response;
    }
}
