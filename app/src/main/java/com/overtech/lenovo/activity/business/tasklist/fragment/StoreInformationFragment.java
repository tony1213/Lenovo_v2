package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.StoreInfoAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.StoreInfo;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.itemdecoration.DividerGridItemDecoration;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class StoreInformationFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvStoreName;
    private LinearLayout llStoreRemarks;
    private StoreInfoAdapter adapter;
    private boolean isInforFirstLoading = true;
    private boolean isRemarkFirstLoading = true;
    private String uid;
    private String workorderCode;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("store information==" + json);
            StoreInfo bean = gson.fromJson(json, StoreInfo.class);
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                Utilities.showToast(bean.msg, getActivity());
                SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID,"");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.WORKORDER_STORE_INFORMATION_SUCCESS:
                    String name = bean.body.name;
                    List<StoreInfo.ImageUrl> imageList = bean.body.imageList;
                    tvStoreName.setText(name);
                    adapter = new StoreInfoAdapter(getActivity(), imageList);
                    mRecyclerView.setAdapter(adapter);
                    isInforFirstLoading = false;

                    break;
                case StatusCode.WORKORDER_STORE_REMARK_SUCCESS:
                    List<StoreInfo.Remark> remarks = bean.body.remarks;
                    for (StoreInfo.Remark remark : remarks) {
                        TextView textView = new TextView(getActivity());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textView.setLayoutParams(params);
                        textView.setText(remark.create_datetime + "[" + remark.create_user_name + "]" + remark.create_content);
                        llStoreRemarks.addView(textView);
                    }
                    isRemarkFirstLoading = false;
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_store_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_store_info);
        tvStoreName = (TextView) mRootView.findViewById(R.id.tv_store_name);
        llStoreRemarks = (LinearLayout) mRootView.findViewById(R.id.ll_store_remarks);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //开始网络加载
            if (isInforFirstLoading) {
                uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "");
                workorderCode = ((TaskDetailActivity) getActivity()).getWorkorderCode();
                startProgress("加载中...");
                Requester requester = new Requester();
                requester.uid = uid;
                requester.cmd = 10005;
                requester.body.put("workorder_code", workorderCode);
                Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
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
                            msg.what = StatusCode.WORKORDER_STORE_INFORMATION_SUCCESS;
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
            if (isRemarkFirstLoading) {
                Requester requester = new Requester();
                requester.uid = uid;
                requester.cmd = 10006;
                requester.body.put("workorder_code", workorderCode);
                Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
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
                            msg.what = StatusCode.WORKORDER_STORE_REMARK_SUCCESS;
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
    }

}
