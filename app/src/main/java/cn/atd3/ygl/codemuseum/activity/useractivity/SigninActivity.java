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

import org.json.JSONException;
import org.json.JSONObject;

import cn.atd3.support.api.JSONListener;
import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.MainActivity;
import cn.atd3.ygl.codemuseum.service.BeatService;
import cn.atd3.ygl.codemuseum.util.HttpCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpPictureCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpUtil;

import static cn.atd3.ygl.codemuseum.util.Utility.beattoken;

/**
 * Created by YGL on 2017/2/20.
 */

public class SigninActivity extends AppCompatActivity{
    private final String atdhome="http://api.i.atd3.cn";
    private final String key="token=c7b04d1534f1ed7bb9241cf5fe6ea11e&client=1";
    private final String atdsignin=atdhome+"/v1.0/user/signin?"+key;
    private final String atdinfo=atdhome+"/v1.0/user/info?"+key;
    private final String atdsendmassage=atdhome+"/v1.0/msg/send?"+key;
    private final String atdinbox=atdhome+"/v1.0/msg/inbox?"+key;
    private final String atdcode=atdhome+"/v1.0/verify_image?"+key;
    private final String atdneedcode=atdhome+"/v1.0/user/signincode?"+key;
    private final String atdcheckname=atdhome+"/v1.0/user/checkname?"+key;

    private EditText userId;
    private EditText userPassword;
    private EditText checkcode;
    private ImageView checkcodepicture;
    private Button refreshcheckcode;
    private Button loginButton;
    private Button goto_signupactivity;
    private RelativeLayout checkcodelayout;

    private String userIdstr="";
    private String userPasswordstr="";
    private String checkcodestr="";
    private Boolean needcode=false;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.signinactivity_layout);

        userId=(EditText)findViewById(R.id.userid);
        userPassword=(EditText)findViewById(R.id.userpassword);
        checkcode=(EditText)findViewById(R.id.checkcode);
        checkcodepicture=(ImageView)findViewById(R.id.checkcodepicture);
        refreshcheckcode=(Button)findViewById(R.id.refreshcheckcode);
        checkcodelayout=(RelativeLayout)findViewById(R.id.checkcodelayout);
        loginButton=(Button)findViewById(R.id.login_button);
        goto_signupactivity=(Button)findViewById(R.id.goto_signupactivity);
        //隐藏验证码相关控件
        checkcodelayout.setVisibility(View.INVISIBLE);

        getCheckCode();//查询验证码

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                userIdstr=userId.getText().toString();
                userPasswordstr=userPassword.getText().toString();
                checkcodestr=checkcode.getText().toString();
                examinename(userIdstr);
            }
        });

        refreshcheckcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcheckcodepicture();
            }
        });

        goto_signupactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SigninActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

//    //查询是否需要验证码
//    private void getCheckCode(){
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    if (User.signInCode()){
//                        needcode=true;
//                        getcheckcodepicture();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //显示验证码相关控件
//                                checkcodelayout.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                } catch (ServerException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

    private void getCheckCode(){
        User.checkSignInNeedCode(new JSONListener() {
            @Override
            public void onSuccess(JSONObject object) {
                Toast.makeText(getApplicationContext(),object.toString(),Toast.LENGTH_SHORT).show();
                try {
                    if (object.has("return")){
                        if (object.getBoolean("return")) {
                            needcode = true;
                            getcheckcodepicture();
                            checkcodelayout.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerException e) {
                Toast.makeText(getApplicationContext(),"服务器连接错误："+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取验证码图片
    private void getcheckcodepicture(){
        HttpUtil.sendHttpRequestPicture(atdcode, new HttpPictureCallbackListener() {
            @Override
            public void onFinish(final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkcodepicture.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //验证用户名
    private void examinename(final String value){
        if(TextUtils.isEmpty(value)){
            closeProgressDialog();
            toastPrintf("请输入用户名");
            return;
        }
        String jsonString="";
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",value);
            jsonString=jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        HttpUtil.sendHttpRequest(atdcheckname, jsonString, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String str=jsonObject.getString("return");
                    if(str.equals("true")){
                        //用户名存在
                        examineuserObject();
                    }else {
                        closeProgressDialog();
                        toastPrintf("用户名不存在");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //验证用户密码和验证码
    private void examineuserObject(){
        if(TextUtils.isEmpty(userPasswordstr)){
            closeProgressDialog();
            toastPrintf("请输入密码");
            return;
        }

        if(needcode){
            if(!TextUtils.isEmpty(checkcodestr)){
                commituserObject();
            }else {
                closeProgressDialog();
                toastPrintf("验证码不能为空");
            }
        }else {
            commituserObject();
        }
    }

    //提交用户数据
    private void commituserObject(){
        String jsonString="";
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",userIdstr);
            jsonObject.put("passwd",userPasswordstr);
            jsonObject.put("code",checkcodestr);
            jsonString=jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        HttpUtil.sendHttpRequest(atdsignin, jsonString, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                signinfeedback(response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //登陆反馈
    private void signinfeedback(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.has("error")){
                getCheckCode();//刷新验证码
                closeProgressDialog();
                toastPrintf("验证码错误");
            }else {
                String returnstring=jsonObject.getString("return");
                if(returnstring.equals("false")){
                    getCheckCode();//刷新验证码
                    closeProgressDialog();
                    toastPrintf("密码错误");
                }else {
                    if(returnstring.equals("true")){
                        jsonObject=jsonObject.getJSONObject("token");
                        beattoken=jsonObject.getString("user");
                        Intent intent=new Intent(SigninActivity.this, BeatService.class);
                        startService(intent);
                        closeProgressDialog();
                        toastPrintf("登陆成功");
                        Intent mainintent=new Intent(SigninActivity.this,MainActivity.class);
                        startActivity(mainintent);
                        finish();
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**   * 显示进度对话框   */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登陆中");
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
                Toast.makeText(SigninActivity.this,string, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
