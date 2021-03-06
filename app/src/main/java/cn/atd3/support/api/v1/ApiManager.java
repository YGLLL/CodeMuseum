package cn.atd3.support.api.v1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * Update by YGL on 2017/3/5.
 * API管理器
 *  修改日志：
 *      + 添加异常类输出
 *      + 添加版本命名空间区分
 */
public class ApiManager {
    private static int CLIENT_ID;
    private static String CLIENT_TOKEN;
    private static String APIS_AGENT="ApiClient";
    private static String API_HOST="http://dev.atd3.cn/api";
    private static int timeOut=5000;
    private static String META_NAME="cn.atd3.support.api.v1.ClientToken";
    private static final String TAG="ApiManager";

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

    public static  String action(String action)throws ServerException{
        return action(action,null,null);
    }
    public static  String action(String action,String data)throws ServerException{
        return action(action,data,null);
    }
    public static  String action(String action,StringBuffer myCookie)throws ServerException{
        return action(action,null,myCookie);
    }
    /**
     * @param action 地址
     * @param data json数据
     * @param myCookie cookie
     * @return 返回json
     * @throws ServerException
     */
    public static  String action(String action,String data,StringBuffer myCookie) throws ServerException {
        String address=API_HOST+'/'+action;
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
            httpUrlConnection.setRequestProperty("API-Client",String.valueOf(CLIENT_ID));
            Log.i(TAG,"CLIENT_ID"+CLIENT_ID+"end");
            httpUrlConnection.setRequestProperty("API-Token",CLIENT_TOKEN);
            Log.i(TAG,"CLIENT_TOKEN"+CLIENT_TOKEN+"end");
            httpUrlConnection.setRequestProperty("Apis-Agent",APIS_AGENT);
            if(myCookie!=null){
                if(!TextUtils.isEmpty(myCookie.toString())){
                    Log.i(TAG,"set Cookie:"+myCookie.toString()+"end");
                    httpUrlConnection.setRequestProperty("Cookie",myCookie.toString());
                }
            }

            // 连接服务器
            if (data==null){
                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.connect();
            }else{
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Content-Type","application/json");
                httpUrlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                httpUrlConnection.connect();
                OutputStream outputStream = httpUrlConnection.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            if(myCookie!=null) {
                myCookie.replace(0, myCookie.length(), httpUrlConnection.getHeaderField("Set-Cookie"));
                Log.i(TAG, "get Cookie:" + myCookie.toString() + "end");
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
            Log.e("Connect","connected exception",e);
            throw new ServerException("read response failed",e);
        }
        //下一步，拦截所有错误
        return response;
    }

    public static Bitmap getHttpPicture(String action)throws ServerException {
        String address=API_HOST+'/'+action;
        Bitmap bitmap=null;
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
            httpUrlConnection.setRequestProperty("API-Client",String.valueOf(CLIENT_ID));
            httpUrlConnection.setRequestProperty("API-Token",CLIENT_TOKEN);
            httpUrlConnection.setRequestProperty("Apis-Agent",APIS_AGENT);
            // 连接服务器
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            //接收服务器数据
            InputStream in=httpUrlConnection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length = -1;
            while ((length = in.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            byte[] picByte = bos.toByteArray();
            bos.close();
            in.close();
            bitmap= BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
        } catch (IOException e) {
            throw new ServerException("read response failed",e);
        }
        return bitmap;
    }
}
