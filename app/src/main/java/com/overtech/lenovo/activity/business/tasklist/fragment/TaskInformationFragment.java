package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskSolveActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskInfoFragAdapter;
import com.overtech.lenovo.config.Debug;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.TaskProcess;
import com.overtech.lenovo.entity.tasklist.taskbean.Body;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.dialog.WorkorderAppointDialog;
import com.overtech.lenovo.widget.dialog.WorkorderHomeDialog;
import com.overtech.lenovo.widget.dialog.WorkorderReceiveDialog;
import com.overtech.lenovo.widget.dialog.WorkorderSolveDialog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskInformationFragment extends BaseFragment {
    private ListView mTaskProcess;
    private TaskInfoFragAdapter adapter;
    private List<TaskProcess> datas;
    private TextView tvRepairPerson;
    private TextView tvRepairPersonContactInfo;
    private TextView tvRepairAddress;
    private ImageView ivRepairContactInfo;
    private TextView tvStandardFeeAmount;
    private LinearLayout llNotificationContainer;
    private TextView tvNotificationTime;
    private String repairPersonContactInformation;
    private String workorderCode;
    private String uid;
    private static final int SOLVE_CODE = 0;
    private boolean isFirstLoading = true;//第一次加载
    private UIHandler uiHandler = new UIHandler(getActivity()) {
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
                Utilities.showToast(bean.msg, getActivity());
                SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            Body body = bean.body;
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.WORKORDER_SUCCESS:
                    tvRepairPerson.setText(body.repair_person);
                    repairPersonContactInformation = body.repair_person_contact_information;
                    tvRepairPersonContactInfo.setText(repairPersonContactInformation);
                    tvRepairAddress.setText(body.repair_address);
                    tvStandardFeeAmount.setText("金额：" + body.standard_fee_amount);

                    String taskType = body.taskType;
                    //目前时间轴实现思路，根据工单状态自己构造函数，通过listview实现
                    datas.clear();
                    if (taskType.equals("-1")) {//开单
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        datas.add(task1);
                    } else if (taskType.equals("0")) {//接单
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", "", "", "", "", "", "", "", "");
                        datas.add(task1);
                        datas.add(task2);
                    } else if (taskType.equals("1")) {//预约
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", "", "", body.home_datetime, "", "", "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                    } else if (taskType.equals("2")) {//到场
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                    } else if (taskType.equals("3")) {//解决方案
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        TaskProcess task5 = new TaskProcess("3", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                        datas.add(task5);
                    } else if (taskType.equals("4")) {//待关单
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        TaskProcess task5 = new TaskProcess("3", "", "", "", "", "", "", body.solution, body.shutdown_datetime, "", "");
                        TaskProcess task6 = new TaskProcess("4", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                        datas.add(task5);
                        datas.add(task6);
                    } else if (taskType.equals("5") && TextUtils.isEmpty(body.feedback)) {//待结单，未评价
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        TaskProcess task5 = new TaskProcess("3", "", "", "", "", "", "", body.solution, body.shutdown_datetime, body.feedback_solved_datetime, "");
                        TaskProcess task6 = new TaskProcess("4", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        TaskProcess task7 = new TaskProcess("5", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                        datas.add(task5);
                        datas.add(task6);
                        datas.add(task7);
                    } else if (taskType.equals("5") && !TextUtils.isEmpty(body.feedback)) {//待结单，已评价
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        TaskProcess task5 = new TaskProcess("3", "", "", "", "", "", "", body.solution, body.shutdown_datetime, body.feedback_solved_datetime, "");
                        TaskProcess task6 = new TaskProcess("4", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        TaskProcess task10 = new TaskProcess("10", "", "", "", "", "", "", "", "", "", body.feedback);
                        TaskProcess task7 = new TaskProcess("5", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                        datas.add(task5);
                        datas.add(task6);
                        datas.add(task10);
                        datas.add(task7);
                    } else if (taskType.equals("6")) {
                        TaskProcess task1 = new TaskProcess("-1", workorderCode, body.workorder_create_datetime, "", "", "", "", "", "", "", "");
                        TaskProcess task2 = new TaskProcess("0", "", "", body.confirm_datetime, "", "", "", "", "", "", "");
                        TaskProcess task3 = new TaskProcess("1", "", "", "", body.appointment_datetime, "", body.home_datetime, "", "", "", "");
                        TaskProcess task4 = new TaskProcess("2", "", "", "", "", body.appointment_home_datetime, body.home_datetime, "", "", "", "");
                        TaskProcess task5 = new TaskProcess("3", "", "", "", "", "", "", body.solution, body.shutdown_datetime, body.feedback_solved_datetime, "");
                        TaskProcess task6 = new TaskProcess("4", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task1);
                        datas.add(task2);
                        datas.add(task3);
                        datas.add(task4);
                        datas.add(task5);
                        datas.add(task6);
                        if (!TextUtils.isEmpty(body.feedback)) {
                            TaskProcess task10 = new TaskProcess("10", "", "", "", "", "", "", "", "", "", body.feedback);
                            datas.add(task10);
                        }
                        TaskProcess task7 = new TaskProcess("5", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        TaskProcess task8 = new TaskProcess("6", "", "", "", "", "", "", "", body.shutdown_datetime, "", "");
                        datas.add(task7);
                        datas.add(task8);
                    }
                    if (mTaskProcess.getAdapter() != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        mTaskProcess.setAdapter(adapter);
                    }
                    isFirstLoading = false;
                    break;
                case StatusCode.WORKORDER_RECEIVE_ACTION_SUCCESS:
                case StatusCode.WORKORDER_APPOINT_ACTION_SUCCESS:
                case StatusCode.WORKORDER_HOME_ACTION_SUCCESS:
                case StatusCode.WORKORDER_SOLVE_ACTION_SUCCESS:
                    if (st == 1) {
                        Utilities.showToast(bean.msg, getActivity());
                    } else {
                        startLoading();
                    }
                    break;
                case StatusCode.WORKORDER_NOTIFICATION_SUCCESS:
                    if (!TextUtils.isEmpty(body.workorder_notification_datetime)) {
                        tvNotificationTime.setText(body.workorder_notification_datetime);
                    }
                    if (body.data != null) {
                        for (Task task : body.data) {
                            TextView tvMsg = new TextView(getActivity());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvMsg.setLayoutParams(params);
                            if (TextUtils.isEmpty(task.notification_item_time) && TextUtils.isEmpty(task.notification_item_who) && TextUtils.isEmpty(task.notification_item_content)) {
                                tvMsg.setText("没有最新通知");
                            } else {
                                tvMsg.setText(task.notification_item_time + "\n" + task.notification_item_who + ":" + task.notification_item_content);
                            }
                            llNotificationContainer.addView(tvMsg);
                        }
                    }
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_task_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        tvRepairPerson = (TextView) mRootView.findViewById(R.id.tv_repair_person);
        tvRepairPersonContactInfo = (TextView) mRootView.findViewById(R.id.tv_repair_person_contact_information);
        ivRepairContactInfo = (ImageView) mRootView.findViewById(R.id.iv_repair_person_contact_information);
        tvRepairAddress = (TextView) mRootView.findViewById(R.id.tv_repair_address);
        tvStandardFeeAmount = (TextView) mRootView.findViewById(R.id.tv_standard_fee_amount);
        mTaskProcess = (ListView) mRootView.findViewById(R.id.lv_task_process);
        llNotificationContainer = (LinearLayout) mRootView.findViewById(R.id.ll_workorder_notification_container);
        tvNotificationTime = (TextView) mRootView.findViewById(R.id.tv_workorder_notification_datetime);
        ivRepairContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + repairPersonContactInformation));
                startActivity(intent);
            }
        });
        datas = new ArrayList<TaskProcess>();
        adapter = new TaskInfoFragAdapter(getActivity(), datas);
        adapter.setOnButtonClickListener(new TaskInfoFragAdapter.OnButtonClickListener() {
            @Override
            public void onClick(View view) {
                String tag = view.getTag().toString();
                if (tag.equals("接单")) {
                    showReceiveDialog();
                } else if (tag.equals("预约")) {
                    showAppointDialog();
                } else if (tag.equals("改约")) {
                    showAppointDialog();
                } else if (tag.equals("到场")) {
                    showHomeDialog();
                } else if (tag.equals("解决方案")) {
                    showSolveDialog();
                } else {

                }
            }
        });
        loadingNotification();
    }

    private void loadingNotification() {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10008;
        requester.body.put("workorder_code", workorderCode);
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
                    msg.what = StatusCode.WORKORDER_NOTIFICATION_SUCCESS;
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


    private void showReceiveDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_receive");
        if (pre != null) {
            ft.remove(pre);
        }
        ft.addToBackStack(null);
        WorkorderReceiveDialog workorderDialog = WorkorderReceiveDialog.newInstance(WorkorderReceiveDialog.DETAILACTIVITY);
        workorderDialog.show(ft, "workorder_receive");
    }

    private void showAppointDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_appoint");
        if (pre != null) {
            ft.remove(pre);
        }
        ft.addToBackStack(null);
        WorkorderAppointDialog workorderAppointDialog = WorkorderAppointDialog.newInstance(WorkorderAppointDialog.DETAIL_ACTIVITY);
        workorderAppointDialog.show(ft, "workorder_appoint");
    }

    private void showHomeDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_home");
        if (pre != null) {
            ft.remove(pre);
        }
        ft.addToBackStack(null);
        WorkorderHomeDialog workorderHomeDialog = WorkorderHomeDialog.newInstance(WorkorderHomeDialog.DETAIL_ACTIVITY);
        workorderHomeDialog.show(ft, "workorder_home");
    }

    private void showSolveDialog() {
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_solve");
//        if (pre != null) {
//            ft.remove(pre);
//        }
//        ft.addToBackStack(null);
//        WorkorderSolveDialog workorderSolveDialog = WorkorderSolveDialog.newInstance(WorkorderSolveDialog.DETAILACTIVITY);
//        workorderSolveDialog.show(ft, "workorder_solve");
        Intent intent = new Intent(getActivity(), TaskSolveActivity.class);
        intent.putExtra("workorder_code", workorderCode);
        startActivityForResult(intent, SOLVE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("requestCode="+requestCode+"   resultCode="+resultCode);
        switch (requestCode) {
            case SOLVE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Logger.e("solve_code 已经执行到这里了");
                    startLoading();
                }else if(resultCode==Activity.RESULT_CANCELED){
                    startLoading();
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Debug.log("taskinfofragment==setUserVisibleHint==", isVisibleToUser + "");
        if (isVisibleToUser) {
            if (isFirstLoading) {
                uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "");
                workorderCode = ((TaskDetailActivity) getActivity()).getWorkorderCode();
                startProgress("加载中");
                startLoading();
                // 开始网络加载
            } else {
                // 取消网络加载
            }
        }
    }

    private void startLoading() {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10003;
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
                    msg.what = StatusCode.WORKORDER_SUCCESS;
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

    /**
     * 确认接单
     */

    public void doReceiveNegativeClick() {
        startProgress("请等待接单结果");
        Requester requester = new Requester();
        requester.cmd = 10002;
        requester.uid = uid;
        requester.body.put("taskType", "0");
        requester.body.put("workorder_code", workorderCode);
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
                    msg.what = StatusCode.WORKORDER_RECEIVE_ACTION_SUCCESS;
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

    /**
     * 取消接单
     */
    public void doReceivePositiveClick() {
        Utilities.showToast("你取消了", getActivity());
    }

    /**
     * 预约对话框确认
     */
    public void doAppointNegativeClick(String selectTime) {
        startProgress("预约中");
        Requester requester = new Requester();
        requester.cmd = 10002;
        requester.uid = uid;
        requester.body.put("taskType", "1");
        requester.body.put("workorder_code", workorderCode);
        requester.body.put("appointment_home_datetime", selectTime);
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
                    msg.what = StatusCode.WORKORDER_APPOINT_ACTION_SUCCESS;
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

    /**
     * 预约对话框取消
     */
    public void doAppointPositiveClick() {
        Utilities.showToast("预约取消了", getActivity());
    }

    /**
     * 到场对话框确认
     */
    public void doHomeNegativeClick() {
        Utilities.showToast("您到场了", getActivity());
        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10002;
        requester.uid = uid;
        requester.body.put("taskType", "2");
        requester.body.put("workorder_code", workorderCode);
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
                    msg.what = StatusCode.WORKORDER_HOME_ACTION_SUCCESS;
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

    /**
     * 到场对话框取消
     */
    public void doHomePositiveClick() {
        Utilities.showToast("您还没有到场", getActivity());
    }

    /**
     * 解决方案对话框确认
     */
    public void doSolveNegativeClick(String issueDescription, String solve) {
        if (TextUtils.isEmpty(issueDescription) || TextUtils.isEmpty(solve)) {
            Utilities.showToast("解决方案不能为空", getActivity());
        } else {
            startProgress("加载中");
            Requester requester = new Requester();
            requester.cmd = 10002;
            requester.uid = uid;
            requester.body.put("taskType", "3");
            requester.body.put("workorder_code", workorderCode);
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
    }

    /**
     * 解决方案对话框取消
     */
    public void doSolvePositiveClick() {

    }

}
