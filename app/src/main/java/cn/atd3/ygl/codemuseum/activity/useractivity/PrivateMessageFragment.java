package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.atd3.ygl.codemuseum.Adapter.PrivateMessageAdapter;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserPrivateMessage;

/**
 * Created by YGL on 2017/3/9.
 */

public class PrivateMessageFragment extends Fragment {
    private List<UserPrivateMessage> list=new ArrayList<UserPrivateMessage>();
    private PrivateMessageAdapter privateMessageAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedlnstanceState){
        View view=inflater.inflate(R.layout.privatemessagefragment_layout,group,false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        listView=(ListView) getActivity().findViewById(R.id.privatemessagelist);
        privateMessageAdapter=new PrivateMessageAdapter(getActivity(),R.layout.item_privatemessageadapter,list);
        listView.setAdapter(privateMessageAdapter);
        test();
    }
    private void test(){
        for(int i=0;i<15;i++){
            UserPrivateMessage userPrivateMessage=new UserPrivateMessage();
            userPrivateMessage.setUserLogo(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
            userPrivateMessage.setUserName("userName"+i);
            userPrivateMessage.setMessageQuantity(String.valueOf(i));
            list.add(userPrivateMessage);
        }
        privateMessageAdapter.notifyDataSetChanged();
    }
}
