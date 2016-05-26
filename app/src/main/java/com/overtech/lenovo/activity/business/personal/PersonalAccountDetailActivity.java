package com.overtech.lenovo.activity.business.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.personal.adapter.PersonalAccountDetailAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.person.PersonalAccount;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Overtech on 16/5/3.
 */
public class PersonalAccountDetailActivity extends BaseActivity {
    private Toolbar toolBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout noPageUI;
    private PersonalAccountDetailAdapter adapter;
    private String uid;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("personalaccount detail====" + json);
            PersonalAccount bean = gson.fromJson(json, PersonalAccount.class);
            if (bean == null) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                SharePreferencesUtils.put(PersonalAccountDetailActivity.this, SharedPreferencesKeys.UID, "");
                Utilities.showToast(bean.msg, PersonalAccountDetailActivity.this);
                Intent intent = new Intent(PersonalAccountDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            if (st == 1) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                stopProgress();
                Utilities.showToast(bean.msg, PersonalAccountDetailActivity.this);
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, PersonalAccountDetailActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, PersonalAccountDetailActivity.this);
                    break;
                case StatusCode.PERSONAL_ACCOUNT_DETAIL_SUCCESS:
                    if(bean.body==null||bean.body.data==null||bean.body.data.size()==0){
                        swipeRefreshLayout.setVisibility(View.GONE);
                        noPageUI.setVisibility(View.VISIBLE);
                    }else{
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        noPageUI.setVisibility(View.GONE);
                        adapter = new PersonalAccountDetailAdapter(PersonalAccountDetailActivity.this, bean.body.data);
                        recyclerView.setAdapter(adapter);
                    }
                    break;
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_personal_account_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        noPageUI= (LinearLayout) findViewById(R.id.ll_nopage);

        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("对账单");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(R.array.material_colors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        startProgress("加载中");
        initData();
    }

    private void initData() {
        Requester requester = new Requester();
        requester.cmd = 10014;
        requester.uid = uid;
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
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
                    msg.what = StatusCode.PERSONAL_ACCOUNT_DETAIL_SUCCESS;
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
    }
}
