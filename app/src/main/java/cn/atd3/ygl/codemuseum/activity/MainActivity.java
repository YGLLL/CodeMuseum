package cn.atd3.ygl.codemuseum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.useractivity.MessageActivity;
import cn.atd3.ygl.codemuseum.activity.useractivity.SettingActivity;
import cn.atd3.ygl.codemuseum.activity.useractivity.SigninActivity;
import cn.atd3.ygl.codemuseum.db.CodeMuseumDB;
import cn.atd3.ygl.codemuseum.model.User;
import cn.atd3.ygl.codemuseum.service.BeatService;
import cn.atd3.ygl.codemuseum.util.Utility;

/**
 * Created by YGL on 2017/2/22.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout noLoginNavLayout;
    private RelativeLayout loginedNavLayout;
    private Button login_or_reg;
    private TextView username;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle sls){
        super.onCreate(sls);
        setContentView(cn.atd3.ygl.codemuseum.R.layout.mainactivity_layout);

        toolbar = (Toolbar) findViewById(cn.atd3.ygl.codemuseum.R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(cn.atd3.ygl.codemuseum.R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, cn.atd3.ygl.codemuseum.R.string.navigation_drawer_open, cn.atd3.ygl.codemuseum.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(cn.atd3.ygl.codemuseum.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        username=(TextView)view.findViewById(R.id.username);
        noLoginNavLayout=(RelativeLayout)view.findViewById(R.id.noLoginNavLayout);
        loginedNavLayout=(RelativeLayout) view.findViewById(R.id.loginedNavLayout);
        loginedNavLayout.setVisibility(View.INVISIBLE);
        login_or_reg=(Button)view.findViewById(cn.atd3.ygl.codemuseum.R.id.login_or_register);
        login_or_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SigninActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });

        IfSignedIn();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle navigation view item clicks here.
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
        drawer.closeDrawers();
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
    protected void onStart(){
        super.onStart();
        IfSignedIn();
    }

    private void IfSignedIn(){
        if(Utility.IsSignedIn(MainActivity.this)){
            noLoginNavLayout.setVisibility(View.INVISIBLE);
            loginedNavLayout.setVisibility(View.VISIBLE);
            CodeMuseumDB codeMuseumDB=CodeMuseumDB.getInstance(MainActivity.this);
            User user=codeMuseumDB.readUser();
            username.setText(user.getName());
            Intent intent=new Intent(MainActivity.this, BeatService.class);
            startService(intent);
        }
    }
}
