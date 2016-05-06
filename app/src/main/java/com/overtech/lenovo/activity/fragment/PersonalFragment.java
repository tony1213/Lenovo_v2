package com.overtech.lenovo.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.personal.PersonalAccountDetailActivity;
import com.overtech.lenovo.activity.business.personal.PersonalAccountServerDetailActivity;
import com.overtech.lenovo.activity.business.personal.PersonalSettingActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.person.Person;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Transformation;

import java.io.IOException;


public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mAvator;
    private TextView tv_finance;
    private TextView tv_month_workorder_amount;
    private TextView tv_year_workorder_amount;
    private LinearLayout mAccountDetail;
    private LinearLayout mAccountServerDetail;
    private RatingBar rb_satisfaction;
    private LinearLayout setting;
    private String uid;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("personal====" + json);
            Person bean = gson.fromJson(json, Person.class);
            int st = bean.st;
            if (st == -1 || st == -2) {
                if (st == -2 || st == -1) {
                    stopProgress();
                    SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                    Utilities.showToast(bean.msg, getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.PERSONAL_SUCCESS:
                    ImageLoader.getInstance().displayImage(bean.body.avator, mAvator,
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
                                    return "personal fragment";
                                }
                            }, Config.RGB_565);
                    tv_finance.setText(bean.body.finance);
                    tv_month_workorder_amount.setText(bean.body.month_workorder_amount + "单");
                    tv_year_workorder_amount.setText(bean.body.year_workorder_amount + "单");
                    if (bean.body.satisfaction != null)
                        rb_satisfaction.setRating(Float.parseFloat(bean.body.satisfaction));
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_personal;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mAvator = (ImageView) mRootView.findViewById(R.id.iv_avator);
        tv_finance = (TextView) mRootView.findViewById(R.id.tv_finance);
        tv_month_workorder_amount = (TextView) mRootView.findViewById(R.id.tv_month_workorder_amount);
        tv_year_workorder_amount = (TextView) mRootView.findViewById(R.id.tv_year_workorder_amount);
        mAccountDetail = (LinearLayout) mRootView.findViewById(R.id.ll_account_detail);
        mAccountServerDetail = (LinearLayout) mRootView.findViewById(R.id.ll_account_server_detail);
        rb_satisfaction = (RatingBar) mRootView.findViewById(R.id.rb_satisfaction);
        setting = (LinearLayout) mRootView.findViewById(R.id.ll_personal_setting);
        uid = ((MainActivity) getActivity()).getUid();

        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10010;
        requester.uid = uid;
        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                RequestExceptBean bean = new RequestExceptBean();
                bean.st = 0;
                bean.msg = "网络异常";
                Message msg = uiHandler.obtainMessage();
                msg.what = StatusCode.FAILED;
                msg.obj = gson.toJson(bean);
                uiHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.PERSONAL_SUCCESS;
                    msg.obj = json;
                } else {
                    ResponseExceptBean bean = new ResponseExceptBean();
                    bean.st = response.code();
                    bean.msg = response.message();
                    msg.what = StatusCode.SERVER_EXCEPTION;
                    msg.obj = gson.toJson(bean);
                }
                uiHandler.sendMessage(msg);
            }
        });

        mAccountDetail.setOnClickListener(this);
        mAccountServerDetail.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        ((MainActivity) getActivity()).getSupportActionBar().hide();
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
            case R.id.ll_account_detail:
                Intent intent2 = new Intent(getActivity(), PersonalAccountDetailActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_account_server_detail:
                Intent intent3=new Intent(getActivity(),PersonalAccountServerDetailActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }


}
