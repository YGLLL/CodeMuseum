package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.activity.MainActivity;
import cn.atd3.ygl.codemuseum.activity.SuperActivity;
import cn.atd3.ygl.codemuseum.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Pattern;

import static cn.atd3.ygl.codemuseum.service.BeatService.BEATTOKEN;
import static cn.atd3.ygl.codemuseum.util.SQLUtil.MYCOOKIE;

/**
 * Created by YGL on 2017/2/20.
 */

public class SignUpActivity extends SuperActivity{

    private EditText signup_userid;
    private EditText signup_email;
    private EditText signup_checkcode;
    private ImageView signup_checkcodepicture;
    private Button signup_refreshcheckcode;
    private Button signup_button;
    private RelativeLayout relativeLayout;

    private String userid="";
    private String email="";
    private String checkcode="";
    private Boolean needcode=false;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(cn.atd3.ygl.codemuseum.R.layout.signupactivity_layout);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("注册");

        signup_userid=(EditText)findViewById(cn.atd3.ygl.codemuseum.R.id.signup_userid);
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
        Apis.checkSignUpNeedCode(new ApiActions() {
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
        Apis.getCodePicture(new ApiActions() {
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
                checkname(userid);
            }else {
                closeProgressDialog();
                toastPrintf("用户名格式错误");
            }
        }else {
            closeProgressDialog();
            toastPrintf("用户名不能为空");
        }
    }
    //验证用户邮箱
    private void examineemail(){
        if(!TextUtils.isEmpty(email)){
            if(isEmailValid(email)){
                checkemail(email);
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
            jsonObject.put("code",checkcode);
            jsonString=String.valueOf(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Apis.userSignUp(jsonString, new ApiActions() {
            @Override
            public void userSignUp(boolean success,String message){
                closeProgressDialog();//关闭等待动画
                if(success){
                    //注册成功
                    SaveUserDataToSQL(message);
                }else {
                    getcheckcode();//刷新验证码
                    toastPrintf(message);
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }

    //保存用户数据到SQL
    private void SaveUserDataToSQL(final String cookie){
        Apis.getUserInformation(cookie,new ApiActions() {
            @Override
            public void getUserInformation(String id,String name,String email){
                if((!TextUtils.isEmpty(id))&&(!TextUtils.isEmpty(name))){
                    MYCOOKIE=cookie;
                    User user=new User();
                    user.setUid(id);
                    user.setName(name);
                    user.setEmail(email);
                    user.setCookie(cookie);
                    user.coverSave();

                    toastPrintf("注册成功");
                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
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
    private void checkname(final String value){
        Apis.checkUserId(value, new ApiActions() {
            @Override
            public void checkUserId(boolean have){
                if(have){
                    closeProgressDialog();
                    toastPrintf("用户名已被使用");
                }else {
                    examineemail();
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }
    private void checkemail(final String value){
        Apis.checkUserEmail(value, new ApiActions() {
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
