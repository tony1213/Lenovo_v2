package com.overtech.lenovo.activity.business.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class TaskInformationActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView tvContractCode;
    private TextView tvContractName;
    private TextView tvContractStartTime;
    private TextView tvContractEndTime;
    private TextView tvContractDate;
    private TextView tvContractPartyA;
    private TextView tvContractPartyB;
    private String workorder_code;
    private String uid;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("项目说明："+json);
            TaskBean bean = gson.fromJson(json, TaskBean.class);
            if (bean == null) {
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                Utilities.showToast(bean.msg, TaskInformationActivity.this);
                SharePreferencesUtils.put(TaskInformationActivity.this, SharedPreferencesKeys.UID, "");
                Intent intent = new Intent(TaskInformationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, TaskInformationActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, TaskInformationActivity.this);
                    break;
                case StatusCode.WORKORDER_CONTRACT_SUCCESS:
                    tvContractCode.setText(bean.body.contract_code);
                    tvContractName.setText(bean.body.contract_name);
                    tvContractStartTime.setText(bean.body.contract_start_time);
                    tvContractEndTime.setText(bean.body.contract_end_time);
                    tvContractDate.setText(bean.body.contract_date);
                    tvContractPartyA.setText(bean.body.contract_party_a);
                    tvContractPartyB.setText(bean.body.contract_party_b);
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_task_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        workorder_code = getIntent().getStringExtra("workorder_code");
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("项目信息");
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvContractCode = (TextView) findViewById(R.id.tv_contract_code);
        tvContractName = (TextView) findViewById(R.id.tv_contract_name);
        tvContractStartTime = (TextView) findViewById(R.id.tv_contract_start_time);
        tvContractEndTime = (TextView) findViewById(R.id.tv_contract_end_time);
        tvContractDate = (TextView) findViewById(R.id.tv_contract_date);
        tvContractPartyA = (TextView) findViewById(R.id.tv_contract_party_a);
        tvContractPartyB = (TextView) findViewById(R.id.tv_contract_party_b);
        initData();
    }

    private void initData() {
        startProgress("加载中...");
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10041;
        requester.body.put("workorder_code", workorder_code);
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
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
            public void onResponse(final Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.WORKORDER_CONTRACT_SUCCESS;
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
