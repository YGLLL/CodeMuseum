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
import java.util.Date;
import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.MainActivity;
import cn.atd3.ygl.codemuseum.service.BeatService;
import cn.atd3.ygl.codemuseum.util.HttpCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpPictureCallbackListener;
import cn.atd3.ygl.codemuseum.util.HttpUtil;

import static cn.atd3.ygl.codemuseum.service.BeatService.beattoken;

/**
 * Created by YGL on 2017/2/20.
 */

public class SigninActivity extends AppCompatActivity{

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

        getcheckcode();//查询验证码

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

    //查询是否需要验证码
    private void getcheckcode(){
        User.checkSignInNeedCode(new ApiActions() {
            @Override
            public void checkSignInNeedCode(boolean need){
                if(need){
                    needcode=true;
                    getcheckcodepicture();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示验证码相关控件
                            checkcodelayout.setVisibility(View.VISIBLE);
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
                        checkcodepicture.setImageBitmap(bitmap);
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
    private void examinename(final String value){
        if(TextUtils.isEmpty(value)){
            closeProgressDialog();
            toastPrintf("请输入用户名");
            return;
        }
        User.checkUserId(value, new ApiActions() {
            @Override
            public void checkUserId(boolean have){
                if(have){
                    //用户名存在
                    examineuserObject();
                }else {
                    closeProgressDialog();
                    toastPrintf("用户名不存在");
                }
            }
            @Override
            public void serverException(ServerException e) {
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
        User.userSignIn(jsonString, new ApiActions() {
            @Override
            public void userSignIn(boolean success,String message){
                closeProgressDialog();//关闭等待动画
                if(success){
                    beattoken=message;
                    Intent intent=new Intent(SigninActivity.this, BeatService.class);
                    startService(intent);
                    getInfo();//查询用户信息
                }else {
                    if(message.equals("codeerror")){
                        getcheckcode();//刷新验证码
                        toastPrintf("验证码错误");
                    }else {
                        getcheckcode();//刷新验证码
                        toastPrintf("密码错误");
                    }
                }
            }
            @Override
            public void serverException(ServerException e) {
                e.printStackTrace();
            }
        });
    }

    //查询用户信息
    private void getInfo(){
        User.getUserInformation(beattoken, new ApiActions() {
            @Override
            public void getUserInformation(String information){
                Intent mainintent=new Intent();
                mainintent.putExtra("information",information);
                setResult(RESULT_OK,mainintent);
                toastPrintf("登陆成功");
                finish();
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
