package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.personal.PersonalSettingActivity;
import com.overtech.lenovo.picasso.Transformation;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.widget.bitmap.ImageLoader;


public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mAvator;
    private LinearLayout setting;
    private String imageUrl = "http://img0w.pconline.com.cn/pconline/1309/03/3452566_13.jpg";

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_personal;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mAvator = (ImageView) mRootView.findViewById(R.id.iv_avator);
        setting = (LinearLayout) mRootView
                .findViewById(R.id.ll_personal_setting);
        ImageLoader.getInstance().displayImage(imageUrl, mAvator,
                R.mipmap.icon_avator_default, R.mipmap.ic_launcher,
                new Transformation() {

                    @Override
                    public Bitmap transform(Bitmap source) {
                        // TODO Auto-generated method stub
                        return ImageCacheUtils.toRoundBitmap(source);
                    }

                    @Override
                    public String key() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                }, Config.RGB_565);

        setting.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
//		ActionBar actionBar = ((MainActivity) getActivity())
//				.getSupportActionBar();
//		actionBar.setTitle("我的");
//		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_main);
//		toolbar.setNavigationIcon(R.drawable.icon_tab_personal_selected);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_personal_setting:
                Intent intent = new Intent(getActivity(),
                        PersonalSettingActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }
    }
}
