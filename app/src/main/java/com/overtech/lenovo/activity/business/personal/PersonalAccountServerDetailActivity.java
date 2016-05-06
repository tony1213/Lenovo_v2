package com.overtech.lenovo.activity.business.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.personal.adapter.PersonalAccountServerDetailAdapter;
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
public class PersonalAccountServerDetailActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PersonalAccountServerDetailAdapter adapter;
    private String uid;
    private UIHandler uiHandler=new UIHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("服务明细===="+json);
            PersonalAccount bean = gson.fromJson(json, PersonalAccount.class);
            if (bean == null) {
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                SharePreferencesUtils.put(PersonalAccountServerDetailActivity.this, SharedPreferencesKeys.UID, "");
                Utilities.showToast(bean.msg, PersonalAccountServerDetailActivity.this);
                Intent intent = new Intent(PersonalAccountServerDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            switch (msg.what){
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg,PersonalAccountServerDetailActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg,PersonalAccountServerDetailActivity.this);
                    break;
                case StatusCode.PERSONAL_ACCOUNT_SERVER_SUCCESS:
                    adapter=new PersonalAccountServerDetailAdapter(PersonalAccountServerDetailActivity.this,bean.body.data);
                    recyclerView.setAdapter(adapter);
                    break;
            }
            stopProgress();
        }
    };
    @Override
    protected int getLayoutIds() {
        return R.layout.activity_personal_account_server_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("服务明细");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10015;
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
                    msg.what = StatusCode.PERSONAL_ACCOUNT_SERVER_SUCCESS;
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
