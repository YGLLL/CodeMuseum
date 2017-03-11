package cn.atd3.ygl.codemuseum.activity.useractivity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.atd3.ygl.codemuseum.Adapter.PrivateSessionAdapter;
import cn.atd3.ygl.codemuseum.R;
import cn.atd3.ygl.codemuseum.model.PrivateSession;

/**
 * Created by YGL on 2017/3/9.
 */

public class PrivateSessionFragment extends Fragment {
    private List<PrivateSession> list = new ArrayList<PrivateSession>();
    private PrivateSessionAdapter privateSessionAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedlnstanceState) {
        View view = inflater.inflate(R.layout.privatesessionfragment_layout, group, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView) getActivity().findViewById(R.id.privatesessionlist);
        privateSessionAdapter = new PrivateSessionAdapter(getActivity(), R.layout.item_privatesessionadapter, list);
        listView.setAdapter(privateSessionAdapter);
        test();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Intent intent=new Intent(getActivity(),SessionWindow.class);
                startActivity(intent);
            }
        });
    }
    private void test(){
        for(int i=0;i<15;i++){
            PrivateSession privateSession =new PrivateSession();
            privateSession.setUserLogo(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
            privateSession.setUserName("userName"+i);
            privateSession.setMessageQuantity(String.valueOf(i));
            list.add(privateSession);
        }
        privateSessionAdapter.notifyDataSetChanged();
    }
}
