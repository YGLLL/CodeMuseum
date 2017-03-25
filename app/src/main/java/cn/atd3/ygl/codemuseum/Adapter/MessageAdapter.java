package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserMessage;

import java.util.List;

/**
 * Created by YGL on 2017/2/23.
 */

public class MessageAdapter extends ArrayAdapter<UserMessage> {
    private int layoutId;
    public MessageAdapter(Context context,int layoutId,List<UserMessage> list){
        super(context,layoutId,list);
        this.layoutId=layoutId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        UserMessage userMessage=getItem(position);
        View view;
        ViewUtil vu;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(layoutId, null);
            vu=new ViewUtil();
            vu.message_time=(TextView) view.findViewById(R.id.message_time_text);
            vu.message_sender=(TextView)view.findViewById(R.id.message_sender_text);
            vu.message_address=(TextView)view.findViewById(R.id.message_address_text);
            vu.message_content=(TextView)view.findViewById(R.id.message_content_text);
            view.setTag(vu);
        }else {
            view=convertView;
            vu=(ViewUtil) view.getTag();
        }
        vu.message_time.setText(userMessage.getMessage_time());
        vu.message_sender.setText(userMessage.getMessage_sender());
        vu.message_address.setText(userMessage.getMessage_paper());
        vu.message_content.setText(userMessage.getMessage_content());
        return view;
    }
    class ViewUtil{
        TextView message_time;
        TextView message_sender;
        TextView message_address;
        TextView message_content;
    }
}
