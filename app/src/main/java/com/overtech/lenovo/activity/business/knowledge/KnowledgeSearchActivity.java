package com.overtech.lenovo.activity.business.knowledge;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.widget.EditTextWithDelete;

public class KnowledgeSearchActivity extends BaseActivity {
    private EditTextWithDelete mEditTextWithDeleteSearch;
    private ImageView mDoBack;
    private GridView mGridViewHot;
    private ListView mAutoListView;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_knowledge_search;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        findViewById();
        initEvent();

    }
    private void findViewById(){
        mDoBack=(ImageView)findViewById(R.id.imageView_knowledge_back);
        mGridViewHot=(GridView)findViewById(R.id.gridView_knowledge_search_hot);
        mEditTextWithDeleteSearch=(EditTextWithDelete)findViewById(R.id.et_knowledge_search);
        mAutoListView=(ListView)findViewById(R.id.lv_knowledge_search_hot);
    }
    private void initEvent(){
        mDoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
