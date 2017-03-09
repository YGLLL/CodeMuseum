package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserPrivateMessage;

/**
 * Created by YGL on 2017/3/9.
 */

public class PrivateMessageAdapter extends ArrayAdapter<UserPrivateMessage> {
    private int layoutId;
    public PrivateMessageAdapter(Context context, int layoutId, List<UserPrivateMessage> list){
        super(context,layoutId,list);
        this.layoutId=layoutId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        UserPrivateMessage userPrivateMessage=getItem(position);
        View view;
        ViewUtil vu;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(layoutId, null);
            vu=new ViewUtil();
            vu.userLogo=(ImageView)view.findViewById(R.id.userlogo);
            vu.userName=(TextView)view.findViewById(R.id.username);
            vu.messageQuantity=(TextView)view.findViewById(R.id.messageQuantity);
            view.setTag(vu);
        }else {
            view=convertView;
            vu=(ViewUtil)view.getTag();
        }
        vu.userLogo.setImageBitmap(userPrivateMessage.getUserLogo());
        vu.userName.setText(userPrivateMessage.getUserName());
        vu.messageQuantity.setText(userPrivateMessage.getMessageQuantity());
        return view;
    }
    class ViewUtil{
        ImageView userLogo;
        TextView userName;
        TextView messageQuantity;
    }
}