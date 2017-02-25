package com.example.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ygl.codemuseum.R;
import com.example.ygl.codemuseum.model.UserMessage;
import com.example.ygl.codemuseum.util.MessageAdapter;

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
}
