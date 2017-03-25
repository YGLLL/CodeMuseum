package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.activity.MainActivity;
import cn.atd3.ygl.codemuseum.activity.SuperActivity;
import cn.atd3.ygl.codemuseum.service.BeatService;
import cn.atd3.ygl.codemuseum.util.HttpCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpPictureCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by YGL on 2017/2/20.
 */

public class SignUpActivity extends SuperActivity{

    private EditText signup_userid;
    private EditText signup_userpassword1;
    private EditText signup_userpassword2;
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

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(cn.atd3.ygl.codemuseum.R.layout.signupactivity_layout);

        signup_userid=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_userid);
        signup_userpassword1=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_userpassword1);
        signup_userpassword2=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_userpassword2);
        signup_email=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_email);
        signup_checkcode=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_checkcode);
        signup_checkcodepicture=(ImageView)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_checkcodepicture);
        signup_refreshcheckcode=(Button)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_refreshcheckcode);
        signup_button=(Button)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_button);
        relativeLayout=(RelativeLayout)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_checkcodelayout);
        //隐藏验证码相关控件
        relativeLayout.setVisibility(View.INVISIBLE);

        getcheckcode();//查询验证码

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                userid=signup_userid.getText().toString();
                userpossword1=signup_userpassword1.getText().toString();
                userpossword2=signup_userpassword2.getText().toString();
                email=signup_email.getText().toString();
                checkcode=signup_checkcode.getText().toString();
                examineuserid();
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
        User.checkSignUpNeedCode(new ApiActions() {
            @Override
            public void checkSignUpNeedCode(boolean need){
                if(need){
                    needcode=true;
                    getcheckcodepicture();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示验证码相关控件
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }

    //获取验证码图片
    private void getcheckcodepicture(){
        User.getCodePicture(new ApiActions() {
            @Override
            public void getCodePicture(final Bitmap bitmap){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        signup_checkcodepicture.setImageBitmap(bitmap);
                    }
                });
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }

    //验证用户名
    private void examineuserid(){
        if(!TextUtils.isEmpty(userid)){
            if(isUserValid(userid)){
                checknameoremail("name",userid);
            }else {
                closeProgressDialog();
                toastPrintf("用户名格式错误");
            }
        }else {
            closeProgressDialog();
            toastPrintf("用户名不能为空");
        }
    }
    //验证用户密码
    private void examineuserpossword(){
        if((!TextUtils.isEmpty(userpossword1))&&(!TextUtils.isEmpty(userpossword2))){
            if(userpossword1.equals(userpossword2)){
                examineemail();
            }else {
                closeProgressDialog();
                toastPrintf("密码不一致");
            }
        }else {
            closeProgressDialog();
            toastPrintf("密码不能为空");
        }
    }
    //验证用户邮箱
    private void examineemail(){
        if(!TextUtils.isEmpty(email)){
            if(isEmailValid(email)){
                checknameoremail("email",email);
            }else {
                closeProgressDialog();
                toastPrintf("邮箱格式错误");
            }
        }else {
            closeProgressDialog();
            toastPrintf("邮箱不能为空");
        }
    }
    //验证验证码
    private void examinecheckcode(){
        if(needcode){
            if(!TextUtils.isEmpty(checkcode)){
                commituserObject();
            }else {
                closeProgressDialog();
                toastPrintf("验证码不能为空");
            }
        }else {
            commituserObject();
        }
    }

    //提交注册数据
    private void commituserObject(){
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
        User.userSignUp(jsonString, new ApiActions() {
            @Override
            public void userSignUp(boolean success,String message){
                closeProgressDialog();//关闭等待动画
                if(success){
                    //注册成功
                    toastPrintf("注册成功");
                    finish();
                }else {
                    getcheckcode();//刷新验证码
                    toastPrintf("验证码错误");
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean  isUserValid(String username) {
        return  Pattern.compile("^[\\w\\u4e00-\\u9add]{4,13}$").matcher(username).find();
    }
    private boolean isEmailValid(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-]+\\.))+([a-zA-Z0-9]{2,4})+$").matcher(email).matches();
    }
    private void checknameoremail(final String key,final String value){
        if(key.equals("name")){
            User.checkUserId(value, new ApiActions() {
                @Override
                public void checkUserId(boolean have){
                    if(have){
                        closeProgressDialog();
                        toastPrintf("用户名已被使用");
                    }else {
                        examineuserpossword();
                    }
                }
                @Override
                public void serverException(ServerException e) {
                    e.printStackTrace();
                }
            });
        }else {
            User.checkUserEmail(value, new ApiActions() {
                @Override
                public void checkUserEmail(boolean have){
                    if(have){
                        closeProgressDialog();
                        toastPrintf("邮箱已经注册过");
                    }else {
                        examinecheckcode();
                    }
                }
                @Override
                public void serverException(ServerException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**   * 显示进度对话框   */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("提交数据");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**   * 关闭进度对话框   */
    private void closeProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    //在主线程打印Toast
    private void toastPrintf(final String string){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignUpActivity.this,string, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
