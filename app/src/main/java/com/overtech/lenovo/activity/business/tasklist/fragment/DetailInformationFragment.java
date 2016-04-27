package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.DetailInfo;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class DetailInformationFragment extends BaseFragment {
    private TextView tvContractDescription;
    private TextView tvContractManager;
    private TextView tvContractManagerPhone;
    private TextView tvContractTechnicalSupport;
    private TextView tvContractTechnicalSupportPhone;
    private TextView tvContractTechnicalSupportQQGroup;
    private TextView tvContractTechnicalSupportWeChatGroup;
    private TextView tvRepairPerson;
    private TextView tvRepairDate;
    private TextView tvRepairPersonContactInformation;
    private TextView tvCreateTime;
    private TextView tvRepairAddress;
    private TextView tvRepairStore;
    private TextView tvStoreContactPhone;
    private TextView tvRemark;
    private TextView tvSlaResponseDatetime;
    private TextView tvSlaHomeDatetime;
    private TextView tvSlaSolvedDatetime;
    private String workorderCode;
    private String uid;
    private boolean isFirstLoading = true;//第一次加载
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("detail information " + json);
            DetailInfo bean = gson.fromJson(json, DetailInfo.class);
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
                case StatusCode.WORKORDER_DETAIL_INFORMATION_SUCCESS:
                    isFirstLoading=false;
                    DetailInfo.Contract contract = bean.body.contract;
                    DetailInfo.WorkorderMessage workorder_message = bean.body.workorder_message;
                    DetailInfo.SLA sla = bean.body.sla;
                    tvContractDescription.setText(contract.contract_description);
                    tvContractManager.setText(contract.contract_manager);
                    tvContractManagerPhone.setText(contract.contract_manager_phone);
                    tvContractTechnicalSupport.setText(contract.contract_technical_support);
                    tvContractTechnicalSupportPhone.setText(contract.contract_technical_support_phone);
                    tvContractTechnicalSupportQQGroup.setText(contract.contract_technical_support_qq_group);
                    tvContractTechnicalSupportWeChatGroup.setText(contract.contract_technical_support_wechat_group);

                    tvRepairPerson.setText(workorder_message.repair_person);
                    tvRepairDate.setText(workorder_message.repair_date);
                    tvRepairPersonContactInformation.setText(workorder_message.repair_person_contact_information);
                    tvCreateTime.setText(workorder_message.create_datetime);
                    tvRepairAddress.setText(workorder_message.repair_address);
                    tvRepairStore.setText(workorder_message.repair_store);
                    tvStoreContactPhone.setText(workorder_message.store_contact_phone);
                    tvRemark.setText(workorder_message.remark);

                    tvSlaResponseDatetime.setText(sla.sla_response_datetime);
                    tvSlaHomeDatetime.setText(sla.sla_home_datetime);
                    tvSlaSolvedDatetime.setText(sla.sla_solved_datetime);
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_detail_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        tvContractDescription = (TextView) mRootView.findViewById(R.id.tv_contract_description);
        tvContractManager = (TextView) mRootView.findViewById(R.id.tv_contract_manager);
        tvContractManagerPhone = (TextView) mRootView.findViewById(R.id.tv_contract_manager_phone);
        tvContractTechnicalSupport = (TextView) mRootView.findViewById(R.id.tv_contract_technical_support);
        tvContractTechnicalSupportPhone = (TextView) mRootView.findViewById(R.id.tv_contract_technical_support_phone);
        tvContractTechnicalSupportQQGroup = (TextView) mRootView.findViewById(R.id.tv_contract_technical_support_qq_group);
        tvContractTechnicalSupportWeChatGroup = (TextView) mRootView.findViewById(R.id.tv_contract_technical_support_wechat_group);

        tvRepairPerson = (TextView) mRootView.findViewById(R.id.tv_repair_person);
        tvRepairDate = (TextView) mRootView.findViewById(R.id.tv_repair_date);
        tvRepairPersonContactInformation = (TextView) mRootView.findViewById(R.id.tv_repair_person_contact_information);
        tvCreateTime = (TextView) mRootView.findViewById(R.id.tv_create_time);
        tvRepairAddress = (TextView) mRootView.findViewById(R.id.tv_repair_address);
        tvRepairStore = (TextView) mRootView.findViewById(R.id.tv_repair_store);
        tvStoreContactPhone = (TextView) mRootView.findViewById(R.id.tv_store_contact_phone);
        tvRemark = (TextView) mRootView.findViewById(R.id.tv_remark);

        tvSlaResponseDatetime = (TextView) mRootView.findViewById(R.id.tv_sla_response_datetime);
        tvSlaHomeDatetime = (TextView) mRootView.findViewById(R.id.tv_sla_home_datetime);
        tvSlaSolvedDatetime = (TextView) mRootView.findViewById(R.id.tv_sla_solved_datetime);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
//		Debug.log("DetailInformationFragment==", isVisibleToUser+"");
        if (isVisibleToUser) {
            if (isFirstLoading) {
                //开始网络加载
                uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "");
                workorderCode = ((TaskDetailActivity) getActivity()).getWorkorderCode();

                startProgress("加载中...");
                Requester requester = new Requester();
                requester.uid = uid;
                requester.cmd = 10004;
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
                            msg.what = StatusCode.WORKORDER_DETAIL_INFORMATION_SUCCESS;
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
            //取消加载
        }
    }

}
