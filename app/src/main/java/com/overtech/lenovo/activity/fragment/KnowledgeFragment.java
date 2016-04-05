package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.overtech.lenovo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import com.overtech.lenovo.activity.business.knowledge.KnowledgeDetailActivity;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.activity.business.knowledge.Model;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMainAdapter;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMoreAdapter;

public class KnowledgeFragment extends Fragment {

    private View convertView;
    private ListView mainlist;
    private ListView morelist;
    private List<Map<String, Object>> mainList2;
    private ClassifyMainAdapter mainAdapter;
    private ClassifyMoreAdapter moreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView=inflater.inflate(R.layout.fragment_knowledge, container, false);
        initModle();
        initView();
        return convertView;
    }
    private void initView(){
        mainlist = (ListView) convertView.findViewById(R.id.classify_mainlist);
        morelist = (ListView) convertView.findViewById(R.id.classify_morelist);
        mainAdapter = new ClassifyMainAdapter(getActivity(), mainList2);
        mainAdapter.setSelectItem(0);
        mainlist.setAdapter(mainAdapter);

        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initAdapter(Model.MORELISTTXT[position]);
                mainAdapter.setSelectItem(position);
                mainAdapter.notifyDataSetChanged();
            }
        });
        mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // 一定要设置这个属性，否则ListView不会刷新
        initAdapter(Model.MORELISTTXT[0]);

        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moreAdapter.setSelectItem(position);
                moreAdapter.notifyDataSetChanged();
                Utilities.showToast(parent.getItemAtPosition(position).toString(),getActivity());
                Intent intent=new Intent(getActivity(), KnowledgeDetailActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initAdapter(String[] array) {
        moreAdapter = new ClassifyMoreAdapter(getActivity(), array);
        morelist.setAdapter(moreAdapter);
        moreAdapter.notifyDataSetChanged();
    }
    private void initModle() {
        mainList2 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < Model.LISTVIEWIMG.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", Model.LISTVIEWIMG[i]);
            map.put("txt", Model.LISTVIEWTXT[i]);
            mainList2.add(map);
        }
    }

}
