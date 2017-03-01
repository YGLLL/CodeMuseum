package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserMessage;
import cn.atd3.ygl.codemuseum.util.HttpCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpUtil;
import cn.atd3.ygl.codemuseum.util.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGL on 2017/2/20.
 */

public class MessageActivity extends AppCompatActivity{
    private ListView listView;
    private MessageAdapter messageAdapter;
    private List<UserMessage> userMessageList=new ArrayList<UserMessage>();
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.messageactivity_layout);

        listView=(ListView)findViewById(R.id.messagelist);
        messageAdapter=new MessageAdapter(this,R.layout.item_messageadapter,userMessageList);
        listView.setAdapter(messageAdapter);

        test();
    }

    private void test(){
        for(int i=0;i<10;i++){
            UserMessage userMessage=new UserMessage();
            userMessage.setMessage_time("time"+i);
            userMessage.setMessage_sender("sender"+i);
            userMessage.setMessage_address("address"+i);
            userMessage.setMessage_content("content"+i);
            userMessageList.add(userMessage);
        }
        messageAdapter.notifyDataSetChanged();
    }

    /*************************
    private void inboxmessage(){
        String url=urlAddT(atdinbox);
        String jsonstring="";
        try{
            JSONObject jsonObject=new JSONObject();
            JSONObject user=new JSONObject();
            user.put("user",beattoken);
            jsonObject.put("token",user);

            jsonstring=jsonObject.toString();
        }catch (JSONException e){e.printStackTrace();}
        Log.e("sendmessage()url",url);
        Log.e("sendmessage()jsonstring",jsonstring);
        HttpUtil.sendHttpRequest(url,jsonstring, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("inboxmessage()",getinbox(response));

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

     //这样解析容易造成错误
    private String getinbox(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray returnstr=jsonObject.getJSONArray("return");
            jsonObject=returnstr.getJSONObject(0);
            response=jsonObject.getString("data");
        }catch (JSONException e){e.printStackTrace();}
        return  response;
    }

    private void sendmessage(){
        String url=urlAddT(atdsendmassage);
        String jsonstring="";
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("message","回复我");
            jsonObject.put("to",11);
            jsonObject.put("type",5);

            JSONObject user=new JSONObject();
            user.put("user",beattoken);

            jsonObject.put("token",user);

            jsonstring=jsonObject.toString();
        }catch (JSONException e){e.printStackTrace();}
        HttpUtil.sendHttpRequest(url,jsonstring, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("sendmessage()",response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    /********************************************/
}
