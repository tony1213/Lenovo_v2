package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.knowledge.KnowledgeDetailActivity;
import com.overtech.lenovo.activity.business.knowledge.KnowledgeFilterActivity;
import com.overtech.lenovo.activity.business.knowledge.KnowledgeSearchActivity;
import com.overtech.lenovo.activity.business.knowledge.Model;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMainAdapter;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMoreAdapter;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskListAdapter;
import com.overtech.lenovo.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeFragment extends BaseFragment implements View.OnClickListener {

    private ListView mainlist;
    private ListView morelist;
    private RelativeLayout mDoFilter;
    private RelativeLayout mDoSearch;
    private List<Map<String, Object>> mainList2;
    private ClassifyMainAdapter mainAdapter;
    private ClassifyMoreAdapter moreAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initModle();
        initView();
    }


    private void initView() {
        mainlist = (ListView) mRootView.findViewById(R.id.classify_mainlist);
        morelist = (ListView) mRootView.findViewById(R.id.classify_morelist);
        mDoFilter=(RelativeLayout)mRootView.findViewById(R.id.rl_title_knowledge_filter);
        mDoSearch=(RelativeLayout)mRootView.findViewById(R.id.rl_title_knowledge_search);
        mDoFilter.setOnClickListener(this);
        mDoSearch.setOnClickListener(this);
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
                Utilities.showToast(parent.getItemAtPosition(position).toString(), getActivity());
                Intent intent = new Intent(getActivity(), KnowledgeDetailActivity.class);
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

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.rl_title_knowledge_filter:
                Utilities.showToast("doFilter",getActivity());
                intent.setClass(getActivity(), KnowledgeFilterActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_title_knowledge_search:
                Utilities.showToast("doSearch",getActivity());
                intent.setClass(getActivity(), KnowledgeSearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
