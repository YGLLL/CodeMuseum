package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.SuperActivity;

/**
 * Created by YGL on 2017/2/20.
 */

public class SettingActivity extends SuperActivity{
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.settingactivity_layout);
        Button button=(Button)findViewById(R.id.checkem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apis.checkemailcode(2633, new ApiActions() {
                    @Override
                    public void checkemailcode(String message){
                        Log.i("xxx",message+"end");
                    }
                    @Override
                    public void serverException(ServerException e) {

                    }
                });
            }
        });
    }
}
