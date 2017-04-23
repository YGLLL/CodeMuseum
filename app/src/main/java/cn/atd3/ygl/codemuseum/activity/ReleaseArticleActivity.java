package cn.atd3.ygl.codemuseum.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import cn.atd3.ygl.codemuseum.R;

/**
 * Created by YGL on 2017/4/6.
 */

public class ReleaseArticleActivity extends SuperActivity {
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.releasearticleactivity_layout);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("发布");
    }
}
