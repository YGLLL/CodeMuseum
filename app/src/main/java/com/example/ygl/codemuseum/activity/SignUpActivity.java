package com.example.ygl.codemuseum.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ygl.codemuseum.R;
import com.example.ygl.codemuseum.util.HttpCallbackListener;
import com.example.ygl.codemuseum.util.HttpUtil;
import com.example.ygl.codemuseum.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by YGL on 2017/2/20.
 */

public class SignUpActivity extends AppCompatActivity{
    private String atdhome="http://site.i.atd3.cn";
    private String key="token=c7b04d1534f1ed7bb9241cf5fe6ea11e&t=1x&client=1";
    private String atdcode=atdhome+"/verify.png?";

    private EditText signup_userid;
    private EditText signup_userpossword1;
    private EditText signup_userpossword2;
    private EditText signup_email;
    private EditText signup_checkcode;
    private ImageView signup_checkcodepicture;
    private Button signup_refreshcheckcode;
    private Button signup_button;
    private RelativeLayout relativeLayout;

    private String userid="";
    private String userpossword1="";
    private String userpossword2="";
    private String email="";
    private String checkcode="";
    private Boolean needcode=false;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.signupactivity_layout);

        signup_userid=(EditText)findViewById(R.id.signup_userid);
        signup_userpossword1=(EditText)findViewById(R.id.signup_userpassword1);
        signup_userpossword2=(EditText)findViewById(R.id.signup_userpassword2);
        signup_email=(EditText)findViewById(R.id.signup_email);
        signup_checkcode=(EditText)findViewById(R.id.signup_checkcode);
        signup_checkcodepicture=(ImageView)findViewById(R.id.signup_checkcodepicture);
        signup_refreshcheckcode=(Button)findViewById(R.id.signup_refreshcheckcode);
        signup_button=(Button)findViewById(R.id.signup_button);
        relativeLayout=(RelativeLayout)findViewById(R.id.signup_checkcodelayout);
        //隐藏验证码相关控件
        relativeLayout.setVisibility(View.INVISIBLE);

        getcheckcode();//查询验证码

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid=signup_userid.getText().toString();
                userpossword1=signup_userpossword1.getText().toString();
                userpossword2=signup_userpossword2.getText().toString();
                email=signup_email.getText().toString();
                checkcode=signup_checkcode.getText().toString();
                if(examineObject()){
                    commituserObject();
                }else {
                    return;
                }
            }
        });

        signup_refreshcheckcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcheckcodepicture();
            }
        });
    }

    //查询是否需要验证码
    private void getcheckcode(){
        String url="http://site.i.atd3.cn/api/user/needcode?"+key;
        String jsonString="{}";
        HttpUtil.sendHttpRequest(url, jsonString, new HttpCallbackListener() {
            @Override
            public void onFinish(Object response) {
                String responsestr=response.toString();
                String str=Utility.getcheckcodeforJson(responsestr);
                if(!str.equals("false")){
                    needcode=true;
                    getcheckcodepicture();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
    //获取验证码图片
    private void getcheckcodepicture(){
        String url=atdcode+key+"&t="+String.valueOf(new Date().getTime());
        HttpUtil.sendHttpPicture(url, new HttpCallbackListener() {
            @Override
            public void onFinish(Object response) {
                final Bitmap bitmap=(Bitmap) response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        signup_checkcodepicture.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //提交注册数据
    private void commituserObject(){
        String url="http://site.i.atd3.cn/api/user/signup?"+key;
        String jsonString="";
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("email",email);
            jsonObject.put("name",userid);
            jsonObject.put("passwd",userpossword1);
            jsonObject.put("code",checkcode);
            jsonString=String.valueOf(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
        HttpUtil.sendHttpRequest(url, jsonString, new HttpCallbackListener() {
            @Override
            public void onFinish(Object response) {
                String returnstr=response.toString();
                try{
                    JSONObject jsonObject=new JSONObject(returnstr);
                    String str=jsonObject.getString("error");
                    if(!TextUtils.isEmpty(str)){
                        getcheckcodepicture();
                        if(str.equals("codeError")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUpActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.e("commituserObject",response.toString());
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //查询输入是否正确
    private Boolean examineObject(){
        if(!TextUtils.isEmpty(userid)){
            if(isUserValid(userid)){
            }else {
                Toast.makeText(this, "用户名格式错误", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!TextUtils.isEmpty(userpossword1)&&!TextUtils.isEmpty(userpossword2)){
            if(userpossword1.equals(userpossword2)){
            }else {
                Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!TextUtils.isEmpty(email)){
            if(isEmailValid(email)){
            }else {
                Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(needcode){
            if(!TextUtils.isEmpty(checkcode)){
            }else {
                Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    private boolean  isUserValid(String username) {
        return  Pattern.compile("^[\\w\\u4e00-\\u9add]{4,13}$").matcher(username).find();
    }
    private boolean isEmailValid(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-]+\\.))+([a-zA-Z0-9]{2,4})+$").matcher(email).matches();
    }
}
