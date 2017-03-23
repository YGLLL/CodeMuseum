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
import android.widget.Toast;

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
                intent.putExtra("UserName",list.get(position).getUserName());
                intent.putExtra("Uid",list.get(position).getUid());
                startActivity(intent);
            }
        });
    }
    private void test(){
        list.clear();
        PrivateSession message=new PrivateSession();
        message.setUserLogo(BitmapFactory.decodeResource(getResources(), R.drawable.logopng));
        message.setUserName("message");
        message.setUid(40);
        list.add(message);
        PrivateSession yangguoliang=new PrivateSession();
        yangguoliang.setUserLogo(BitmapFactory.decodeResource(getResources(), R.drawable.logopng));
        yangguoliang.setUserName("yangguoliang");
        yangguoliang.setUid(10);
        list.add(yangguoliang);
        privateSessionAdapter.notifyDataSetChanged();
    }
}
