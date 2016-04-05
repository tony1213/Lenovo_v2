package com.overtech.lenovo.activity.business.tasklist;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;

public class TaskInformationActivity extends BaseActivity {
    private TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);
        init();
    }

    protected void init() {
        mTitle = (TextView) findViewById(R.id.tv_task_information_title);

        mTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
