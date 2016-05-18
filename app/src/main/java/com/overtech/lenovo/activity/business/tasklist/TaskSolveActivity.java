package com.overtech.lenovo.activity.business.tasklist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.taskbean.Body;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

/**
 * Created by Overtech on 16/5/18.
 */
public class TaskSolveActivity extends BaseActivity implements View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvWorkorderCode;
    private TextView tvWorkorderTaskType;
    private AppCompatEditText etWorkorderIssueDescription;
    private AppCompatButton btSubmitIssue;
    private AppCompatEditText etWorkorderSolve;
    private AppCompatEditText etPictureDescription;
    private AppCompatButton btOpenCamera;
    public static String SAVED_IMAGE_DIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/DJOMS/camera/";
    public static final int REQUEST_CAMERA_CODE = 0;
    private String cameraPath;
    private String uid;
    private String workorder_code;
    private String issueDescription;
    private String solve;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("后台拿到的数据==" + json);

            TaskBean bean = gson.fromJson(json, TaskBean.class);
            if (bean == null) {
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                Utilities.showToast(bean.msg, TaskSolveActivity.this);
                SharePreferencesUtils.put(TaskSolveActivity.this, SharedPreferencesKeys.UID, "");
                Intent intent = new Intent(TaskSolveActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            Body body = bean.body;
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, TaskSolveActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, TaskSolveActivity.this);
                    break;
                case StatusCode.WORKORDER_SOLVE_ACTION_SUCCESS:
                    Utilities.showToast(bean.msg, TaskSolveActivity.this);
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_task_solve;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        workorder_code = getIntent().getStringExtra("workorder_code");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        collapsingToolbarLayout.setTitle("解决方案");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(0);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


        tvWorkorderCode = (TextView) findViewById(R.id.tv_workorder_code);
        tvWorkorderTaskType = (TextView) findViewById(R.id.tv_workorder_task_type);
        etWorkorderIssueDescription = (AppCompatEditText) findViewById(R.id.et_workorder_issue_description);
        btSubmitIssue = (AppCompatButton) findViewById(R.id.bt_submit_issue);
        etWorkorderSolve = (AppCompatEditText) findViewById(R.id.et_workorder_solve);
        etPictureDescription = (AppCompatEditText) findViewById(R.id.et_picture_description);
        btOpenCamera = (AppCompatButton) findViewById(R.id.bt_open_camera);

        tvWorkorderCode.setText(workorder_code);
        btSubmitIssue.setOnClickListener(this);
        btOpenCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit_issue:
                issueDescription = etWorkorderIssueDescription.getText().toString().trim();
                if (TextUtils.isEmpty(issueDescription)) {
                    Utilities.showToast("问题描述为空", this);
                    return;
                }
                solve = etWorkorderSolve.getText().toString().trim();
                if (TextUtils.isEmpty(solve)) {
                    Utilities.showToast("请输入解决方案", this);
                    return;
                }

                upLoading();
                break;
            case R.id.bt_open_camera:
                Utilities.showToast("暂未开放", this);
                openCamera();
                break;
        }
    }

    private void upLoading() {
        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10002;
        requester.uid = uid;
        requester.body.put("taskType", "3");
        requester.body.put("workorder_code", workorder_code);
        requester.body.put("issue_description", issueDescription);
        requester.body.put("solution", solve);
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message msg = uiHandler.obtainMessage();
                RequestExceptBean bean = new RequestExceptBean();
                bean.st = 0;
                bean.msg = "网络异常";
                msg.what = StatusCode.FAILED;
                msg.obj = gson.toJson(bean);
                uiHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.WORKORDER_SOLVE_ACTION_SUCCESS;
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

    private void openCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraPath = SAVED_IMAGE_DIA_PATH + System.currentTimeMillis() + ".png";
            File dir = new File(SAVED_IMAGE_DIA_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(cameraPath);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        } else {
            Utilities.showToast("没有sd卡", this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Logger.e("照相获取成功");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Logger.e("设置返回了");
    }
}
