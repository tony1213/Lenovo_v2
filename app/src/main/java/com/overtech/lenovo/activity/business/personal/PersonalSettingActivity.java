package com.overtech.lenovo.activity.business.personal;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.overtech.lenovo.R;
import com.overtech.lenovo.utils.Utilities;

public class PersonalSettingActivity extends AppCompatActivity {
	private Toolbar toolBar;
	private CollapsingToolbarLayout collapsingLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalsetting);
		
		toolBar=(Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);//将toolbar设置成actionbar，清单文件中目前使用的是noactionbar 主题，如果改变后，此处必然会崩掉
		ActionBar actionBar=getSupportActionBar();
		actionBar.setHomeAsUpIndicator(android.R.drawable.ic_input_delete);//设置返回小图标
		actionBar.setDisplayHomeAsUpEnabled(true);
		toolBar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.showToast("你点击了退出", getBaseContext());
				finish();
			}
		});
		
		collapsingLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingLayout.setTitle("基本信息");
	}
}
