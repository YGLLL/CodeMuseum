package cn.atd3.ygl.codemuseum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by YGL on 2017/2/13.
 */

public class HttpUtil {
    public static void sendHttpRequest(final String address, final String jsonString, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection=(HttpURLConnection) url.openConnection();
                    // 设置允许输出
                    connection.setDoOutput(true);
                    //设置为POST模式
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 设置User-Agent: Fiddler
                    connection.setRequestProperty("Apis-Agent", "Fiddler");
                    // 设置contentType
                    connection.setRequestProperty("Content-Type", "application/json");

                    //向服务器写数据
                    OutputStream os = connection.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.close();

                    //接收服务器数据
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuffer response=new StringBuffer();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    in.close();

                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestPicture(final String address, final HttpPictureCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 设置User-Agent: Fiddler
                    connection.setRequestProperty("Apis-Agent", "Fiddler");
                    // 设置contentType
                    connection.setRequestProperty("Content-Type", "application/json");

                    //接收服务器数据
                    InputStream in=connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = in.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    byte[] picByte = bos.toByteArray();
                    bos.close();
                    in.close();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(picByte, 0, picByte.length);

                    if(listener!=null){
                        listener.onFinish(bitmap);
                    }
                }catch (Exception e){
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
