package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.R;

/**
 * Created by YGL on 2017/3/11.
 */

public class SessionWindow extends AppCompatActivity {
    private ActionBar actionBar;
    private EditText sendmessage;
    private Button send;
    @Override
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.sessionwindow_layout);

        actionBar=getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("UserName"));

        sendmessage=(EditText)findViewById(R.id.sendmessage);
        send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text();
            }
        });
    }

    private void text(){
        User.sendMessage(sendmessage.getText().toString(),getIntent().getIntExtra("Uid",-1), new ApiActions() {
            @Override
            public void sendMessage(String string){
                Log.i("xxx",string);
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
        User.inboxmessage(new ApiActions() {
            @Override
            public void inboxmessage(String message){
                Log.i("xxx",message);
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }
}
