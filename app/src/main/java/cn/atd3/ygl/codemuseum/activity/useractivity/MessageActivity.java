package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.SuperActivity;
import cn.atd3.ygl.codemuseum.util.SQLUtil;

/**
 * Created by YGL on 2017/2/20.
 */

public class MessageActivity extends SuperActivity{
    private Button message;
    private Button privateMessage;

    private MessageFragment messageFragment=new MessageFragment();
    private PrivateSessionFragment privateSessionFragment =new PrivateSessionFragment();
    private Fragment showingFragment=null;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle sls){
        if(!SQLUtil.IsHaveUser()){
            Intent intent=new Intent(this,SigninActivity.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(sls);
        setContentView(R.layout.messageactivity_layout);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("消息");

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
                switchFragment(privateSessionFragment,messageFragment);
                setButtonColor();
            }
        });
        privateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(messageFragment, privateSessionFragment);
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
