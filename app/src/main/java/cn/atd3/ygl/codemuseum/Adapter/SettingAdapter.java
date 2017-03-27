package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.atd3.ygl.codemuseum.R;

/**
 * Created by YGL on 2017/3/26.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder>{
    private Context mcontext;
    private List<String> mlist;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View view){
            super(view);
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
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        String string=mlist.get(position);
        viewHolder.textView.setText(string);
    }

    @Override
    public int getItemCount(){
        return mlist.size();
    }
}
