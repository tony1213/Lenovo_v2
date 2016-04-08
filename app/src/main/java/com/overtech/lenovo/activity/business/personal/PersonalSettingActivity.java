package com.overtech.lenovo.activity.business.personal;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;

public class PersonalSettingActivity extends BaseActivity implements OnClickListener {
    private Toolbar toolBar;
    private CollapsingToolbarLayout collapsingLayout;
    private TextView mEditBasic;
    private TextView mEditTec;
    private TextView mEditCa;
    private EditTextWithDelete etPhone;
    private EditTextWithDelete etQQ;
    private EditTextWithDelete etWeChat;
    private EditTextWithDelete etEmail;
    private EditTextWithDelete etCity;
    private EditTextWithDelete etAddress;
    private EditTextWithDelete etEdu;
    private EditTextWithDelete etEnglish;
    private EditTextWithDelete eWorkYears;
    private EditTextWithDelete etIdentity;
    private EditTextWithDelete etIdStyle;
    private EditTextWithDelete etIdCard;
    @Override
    protected int getLayoutIds() {
        return R.layout.activity_personalsetting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        collapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mEditBasic= (TextView) findViewById(R.id.tv_edit_basic);
        mEditTec= (TextView) findViewById(R.id.tv_edit_tec);
        mEditCa= (TextView) findViewById(R.id.tv_edit_ca);
        findViewById(R.id.et_personal_phone);
        findViewById(R.id.et_personal_qq);
        setSupportActionBar(toolBar);//将toolbar设置成actionbar，清单文件中目前使用的是noactionbar 主题，如果改变后，此处必然会崩掉
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_input_delete);//设置返回小图标
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(this);
        collapsingLayout.setTitle("基本信息");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tool_bar:
                finish();
                break;
            case R.id.tv_edit_basic:

                break;
            case R.id.tv_edit_tec:

                break;
            case R.id.tv_edit_ca:

                break;
        }
    }
}
