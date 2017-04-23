package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.activity.MainActivity;
import cn.atd3.ygl.codemuseum.model.User;
import cn.atd3.ygl.codemuseum.service.BeatService;

/**
 * Created by YGL on 2017/3/26.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder>{
    private Context mcontext;
    private List<String> mlist;
    private final String TAG="SettingAdapter";
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout item_setting;
        public ViewHolder(View view){
            super(view);
            item_setting=(RelativeLayout) view;
            textView=(TextView)view.findViewById(R.id.setting_name);
        }
    }

    public SettingAdapter(List list){
        mlist=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewTybe){
        if (mcontext==null){
            mcontext=parent.getContext();
        }
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_setting,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        String itemString=mlist.get(position);
        viewHolder.item_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        DataSupport.deleteAll(User.class);
                        Intent mainintent=new Intent(mcontext, MainActivity.class);
                        mcontext.startActivity(mainintent);
                        Log.i(TAG,"退出登录");
                        break;
                    default:
                }
            }
        });
        viewHolder.textView.setText(itemString);
    }
    @Override
    public int getItemCount(){
        return mlist.size();
    }
}
