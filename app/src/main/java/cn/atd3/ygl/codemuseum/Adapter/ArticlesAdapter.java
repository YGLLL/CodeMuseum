package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.ArticleActivity;
import cn.atd3.ygl.codemuseum.model.Articles;

/**
 * Created by YGL on 2017/3/30.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private Context mcontext;
    private List<Articles> mlist;
    private static final String TAG="ArticlesAdapter";
    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title;
        TextView content;
        TextView praise;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.article_card_view);
            title=(TextView)view.findViewById(R.id.title);
            content=(TextView)view.findViewById(R.id.content);
            praise=(TextView)view.findViewById(R.id.praise);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mcontext==null){
            mcontext=parent.getContext();
        }
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_article,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        final Articles articles=mlist.get(position);
        holder.title.setText(articles.getTitle());
        holder.content.setText(articles.getContent());
        holder.praise.setText(articles.getPraise());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, ArticleActivity.class);
                Gson gson=new Gson();
                Log.i(TAG,gson.toJson(articles,Articles.class));
                intent.putExtra("Articles",gson.toJson(articles,Articles.class));
                mcontext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount(){
        return mlist.size();
    }
    public ArticlesAdapter(List list){
        mlist=list;
    }
}
