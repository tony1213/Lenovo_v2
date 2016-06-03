package com.overtech.lenovo.activity.business.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.WorkorderMsgAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;

/**
 * 工单消息
 * Created by Overtech on 16/4/29.
 */
public class WorkorderMsgActivity extends BaseActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout noDataPage;
    private List<Task> data;
    private WorkorderMsgAdapter adapter;
    private String uid;
    private int curPage = 0;

    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("workordermsg   " + json);
            TaskBean bean = gson.fromJson(json, TaskBean.class);
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
                Utilities.showToast(bean.msg, WorkorderMsgActivity.this);
                SharePreferencesUtils.put(WorkorderMsgActivity.this, SharedPreferencesKeys.UID, "");
                Intent intent = new Intent(WorkorderMsgActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, WorkorderMsgActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, WorkorderMsgActivity.this);
                    break;
                case StatusCode.WORKORDER_MSG_SUCCESS:
                    data = bean.body.data;
                    if (!TextUtils.isEmpty(bean.body.msg_latest_datetime)) {
                        SharePreferencesUtils.put(WorkorderMsgActivity.this, SharedPreferencesKeys.LATEST_MSG_DATETIME, bean.body.msg_latest_datetime);
                    }
                    if (adapter == null) {
                        adapter = new WorkorderMsgAdapter(data, WorkorderMsgActivity.this);
                        recyclerView.setAdapter(adapter);
                        if (data.size() == 0) {
                            adapter.changeLoadingState(WorkorderMsgAdapter.NODATA);
                            noDataPage.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        } else {
                            noDataPage.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (swipeRefreshLayout.isRefreshing()) {
                            adapter.pulldownFresh(bean.body.data);
                        } else {
                            if (data.size() == 0) {
                                Utilities.showToast(getResources().getString(R.string.no_more_data), WorkorderMsgActivity.this);
                                adapter.changeLoadingState(WorkorderMsgAdapter.NODATA);
                            } else {
                                adapter.changeLoadingState(WorkorderMsgAdapter.RELAX);
                                adapter.addMore(bean.body.data);
                            }
                        }
                    }
                    curPage = (adapter.getItemCount() - 1) / 11;
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
        return R.layout.activity_workorder_msg;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.rv_workorder_msg);
        noDataPage = (LinearLayout) findViewById(R.id.ll_nopage);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("消息");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(R.array.material_colors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        initData(++curPage);
                        adapter.changeLoadingState(WorkorderMsgAdapter.LOADING);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        startProgress("加载中...");
        initData(curPage);
    }

    private void initData(int page) {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10040;
        requester.body.put("page", page + "");
        requester.body.put("size", "10");
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
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.WORKORDER_MSG_SUCCESS;
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
