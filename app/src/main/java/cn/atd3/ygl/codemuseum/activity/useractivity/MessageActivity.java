package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserMessage;
import cn.atd3.ygl.codemuseum.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import static cn.atd3.ygl.codemuseum.service.BeatService.beattoken;

/**
 * Created by YGL on 2017/2/20.
 */

public class MessageActivity extends AppCompatActivity{
    private Button message;
    private Button privateMessage;

    private MessageFragment messageFragment=new MessageFragment();
    private PrivateMessageFragment privateMessageFragment=new PrivateMessageFragment();
    private Fragment showingFragment=null;
    @Override
    protected void onCreate(Bundle sls){
        if(TextUtils.isEmpty(beattoken)){//临时使用的判断登陆的方法
            Intent intent=new Intent(this,SigninActivity.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(sls);
        setContentView(R.layout.messageactivity_layout);

        message=(Button)findViewById(R.id.message);
        privateMessage=(Button)findViewById(R.id.privatemessage);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.framelayout, messageFragment);
        transaction.commit();
        showingFragment=messageFragment;
        setButtonColor();

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(privateMessageFragment,messageFragment);
                setButtonColor();
            }
        });
        privateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(messageFragment,privateMessageFragment);
                setButtonColor();
            }
        });
    }

    private void switchFragment(Fragment hideFragment,Fragment showFragment){
        if(showingFragment!=showFragment){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if(showFragment.isAdded()){
                transaction.hide(hideFragment).show(showFragment).commit();
            }else {
                transaction.hide(hideFragment).add(R.id.framelayout,showFragment).commit();
            }
            showingFragment=showFragment;
        }
    }

    private void setButtonColor(){
        if(showingFragment==messageFragment){
            message.setBackgroundColor(Color.parseColor("#2E7D32"));
            privateMessage.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            privateMessage.setBackgroundColor(Color.parseColor("#2E7D32"));
            message.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
}
