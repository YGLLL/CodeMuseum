package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.Apis;
import cn.atd3.ygl.codemuseum.Adapter.SettingAdapter;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.SuperActivity;

/**
 * Created by YGL on 2017/2/20.
 */

public class SettingActivity extends SuperActivity{
    private List<String> list;
    private SettingAdapter settingAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(R.layout.settingactivity_layout);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("设置");

        list=new ArrayList<String>();
        test();
        settingAdapter=new SettingAdapter(list);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(SettingActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(settingAdapter);

        /*
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
        */
    }

    private void test(){
        list.add("关于CodeMuseum");
        list.add("退出登录");
    }
}
