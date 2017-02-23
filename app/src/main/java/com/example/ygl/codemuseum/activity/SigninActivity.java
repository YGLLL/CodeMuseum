package com.example.ygl.codemuseum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ygl.codemuseum.R;

/**
 * Created by YGL on 2017/2/20.
 */

public class SigninActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.signinactivity_layout);
        Button goto_signupactivity=(Button)findViewById(R.id.goto_signupactivity);
        goto_signupactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SigninActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
