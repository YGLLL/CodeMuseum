package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}
