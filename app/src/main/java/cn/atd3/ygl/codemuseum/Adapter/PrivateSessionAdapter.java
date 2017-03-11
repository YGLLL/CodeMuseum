package cn.atd3.ygl.codemuseum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.PrivateSession;

/**
 * Created by YGL on 2017/3/9.
 */

public class PrivateSessionAdapter extends ArrayAdapter<PrivateSession> {
    private int layoutId;
    public PrivateSessionAdapter(Context context, int layoutId, List<PrivateSession> list){
        super(context,layoutId,list);
        this.layoutId=layoutId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        PrivateSession privateSession =getItem(position);
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
        vu.userLogo.setImageBitmap(privateSession.getUserLogo());
        vu.userName.setText(privateSession.getUserName());
        vu.messageQuantity.setText(privateSession.getMessageQuantity());
        return view;
    }
    class ViewUtil{
        ImageView userLogo;
        TextView userName;
        TextView messageQuantity;
    }
}