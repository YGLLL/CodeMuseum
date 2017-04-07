package cn.atd3.ygl.codemuseum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.Articles;

/**
 * Created by YGL on 2017/4/6.
 */

public class ArticleActivity extends SuperActivity {
    private TextView title;
    private TextView content;
    private static final String TAG="ArticleActivity";
    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.articleactivity_layout);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("文章");

        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        Intent intent=getIntent();
        String string=intent.getStringExtra("Articles");
        if(!TextUtils.isEmpty(string)){
            Log.i(TAG,string);
            Gson gson=new Gson();
            Articles articles=gson.fromJson(string,Articles.class);
            title.setText(articles.getTitle());
            content.setText(articles.getContent());
        }
    }
}
