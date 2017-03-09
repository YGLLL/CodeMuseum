package cn.atd3.ygl.codemuseum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.atd3.support.api.ServerException;
import cn.atd3.support.api.v1.ApiActions;
import cn.atd3.support.api.v1.User;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.useractivity.MessageActivity;
import cn.atd3.ygl.codemuseum.activity.useractivity.SettingActivity;
import cn.atd3.ygl.codemuseum.activity.useractivity.SigninActivity;

import static cn.atd3.ygl.codemuseum.service.BeatService.beattoken;

/**
 * Created by YGL on 2017/2/22.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout noLoginNavLayout;
    private LinearLayout loginedNavLayout;
    private Button login_or_reg;
    private TextView username;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(cn.atd3.ygl.codemuseum.R.layout.mainactivity_layout);

        Toolbar toolbar = (Toolbar) findViewById(cn.atd3.ygl.codemuseum.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(cn.atd3.ygl.codemuseum.R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, cn.atd3.ygl.codemuseum.R.string.navigation_drawer_open, cn.atd3.ygl.codemuseum.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(cn.atd3.ygl.codemuseum.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        noLoginNavLayout=(RelativeLayout)view.findViewById(R.id.noLoginNavLayout);
        loginedNavLayout=(LinearLayout)view.findViewById(R.id.loginedNavLayout);
        loginedNavLayout.setVisibility(View.INVISIBLE);
        login_or_reg=(Button)view.findViewById(cn.atd3.ygl.codemuseum.R.id.login_or_register);
        login_or_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SigninActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        username=(TextView)view.findViewById(R.id.username);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case cn.atd3.ygl.codemuseum.R.id.messagemenu:
                Intent messageActivityIntent=new Intent(MainActivity.this,MessageActivity.class);
                startActivity(messageActivityIntent);
                break;
            case cn.atd3.ygl.codemuseum.R.id.settingmenu:
                Intent settingActivityIntent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(settingActivityIntent);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(cn.atd3.ygl.codemuseum.R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(cn.atd3.ygl.codemuseum.R.id.drawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    noLoginNavLayout.setVisibility(View.INVISIBLE);
                    loginedNavLayout.setVisibility(View.VISIBLE);
                    String string=data.getStringExtra("information");
                    setUserName(string);
                }
                break;
            default:
        }
    }

    private void setUserName(String string){
        Log.i("xxx",string);
        String userName="";
        try{
            JSONObject jsonObject=new JSONObject(string);
            jsonObject=jsonObject.getJSONObject("return");
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String key=(String) iterator.next();
                jsonObject=jsonObject.getJSONObject(key);
                userName=jsonObject.getString("name");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        username.setText(userName);
    }
}
