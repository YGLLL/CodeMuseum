package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.Fragment;
import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.atd3.ygl.codemuseum.Adapter.MessageAdapter;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.UserMessage;

/**
 * Created by YGL on 2017/3/9.
 */

public class MessageFragment extends Fragment {
    private ListView listView;
    private MessageAdapter messageAdapter;
    private List<UserMessage> userMessageList=new ArrayList<UserMessage>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedlnstanceState){
        View view=inflater.inflate(R.layout.messagefragment_layout,group,false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        listView=(ListView)getActivity().findViewById(R.id.messagelist);
        messageAdapter=new MessageAdapter(getActivity(),R.layout.item_messageadapter,userMessageList);
        listView.setAdapter(messageAdapter);

        test();
    }

    private void test(){
        for(int i=0;i<10;i++){
            UserMessage userMessage=new UserMessage();
            userMessage.setMessage_time("time"+i);
            userMessage.setMessage_sender("sender"+i);
            userMessage.setMessage_address("address"+i);
            userMessage.setMessage_content("content"+i);
            userMessageList.add(userMessage);
        }
        messageAdapter.notifyDataSetChanged();
    }
}
