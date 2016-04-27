package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.PropertyAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.PropertyInfo;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;


public class PropertyFragment extends BaseFragment {
    private ListView mProperty;
    private List<PropertyInfo.Property> datas;
    private PropertyAdapter adapter;
    private View header;
    private boolean isFirstLoading = true;
    private String uid;
    private String workorderCode;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("property===="+json);
            PropertyInfo bean = gson.fromJson(json, PropertyInfo.class);
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
                case StatusCode.WORKORDER_PROPERTY_SUCCESS:
                    List<PropertyInfo.Property> datas = bean.body.data;
                    adapter = new PropertyAdapter(getActivity(), datas);
                    mProperty.setAdapter(adapter);
                    isFirstLoading = false;

                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_property;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mProperty = (ListView) mRootView.findViewById(R.id.lv_property);
        header = LayoutInflater.from(getActivity()).inflate(
                R.layout.item_headerview_property, null);
        mProperty.addHeaderView(header);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //开始网络加载
            if (isFirstLoading) {
                uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "");
                workorderCode = ((TaskDetailActivity) getActivity()).getWorkorderCode();
                startProgress("加载中...");
                Requester requester = new Requester();
                requester.uid = uid;
                requester.cmd = 10007;
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
                            msg.what = StatusCode.WORKORDER_PROPERTY_SUCCESS;
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
        } else {
            //取消网络加载
        }
    }

}
