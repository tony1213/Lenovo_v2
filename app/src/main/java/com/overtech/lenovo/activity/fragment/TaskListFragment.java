package com.overtech.lenovo.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.app.CustomApplication;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskInformationActivity;
import com.overtech.lenovo.activity.business.tasklist.WorkorderMsgActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskListAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.tasklist.taskbean.AD;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.cycleviewpager.CycleViewPager;
import com.overtech.lenovo.widget.cycleviewpager.ViewFactory;
import com.overtech.lenovo.widget.dialog.WorkorderAppointDialog;
import com.overtech.lenovo.widget.dialog.WorkorderHomeDialog;
import com.overtech.lenovo.widget.dialog.WorkorderReceiveDialog;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends BaseFragment implements View.OnClickListener, TaskListAdapter.OnItemClickListener {

    private View titleView, contentView;
    private TextView mCity;
    private PopupWindow popupWindow;
    private ImageView mNotification;
    private TextView mTitleFilter;
    private TextView mTaskAll;
    private TextView mTaskReceive;
    private TextView mTaskOrder;
    private TextView mTaskVisit;
    private TextView mTaskAccount;
    private TextView mTaskEvaluation;
    private AppCompatTextView noPage;
    private TaskListAdapter workorderAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CycleViewPager cycleViewPager;
    private List<Task> datas;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<AD> adImgs = new ArrayList<AD>();
    private String uid;//用户唯一标识
    private String curTaskType;//记录当前状态
    private boolean isLoginOut = false;//默认登录状态
    private Handler handler;// cyclerviewpager的handler
    private Runnable runnable;// cyclerviewpager的runnable
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("后台返回的数据====" + json);
            if (json == null) {
                stopProgress();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                return;
            }
            TaskBean bean = gson.fromJson(json, TaskBean.class);
            if (bean == null) {
                stopProgress();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                Utilities.showToast("无数据", getActivity());
                return;//加上无数据页面
            }
            int st = bean.st;
            if (st == -2 || st == -1) {
                stopProgress();
                if (!isLoginOut) {
                    isLoginOut = true;
                    Utilities.showToast(bean.msg, getActivity());
                    SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    StackManager.getStackManager().popAllActivitys();
                }
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.WORKORDER_AD_SUCCESS:
                    isLoginOut = false;
                    adImgs = bean.body.ad;
                    views.clear();
                    if (adImgs == null) {

                    } else {
                        for (int i = 0; i < adImgs.size(); i++) {
                            views.add(ViewFactory.getImageView(getActivity(), adImgs.get(i).imageUrl));
                        }
                    }
                    cycleViewPager.setData(views, adImgs, new CycleViewPager.ImageCycleViewListener() {

                        @Override
                        public void onImageClick(String info, int position, View imageView) {
                            ImageLoader.getInstance().displayImage(info, (ImageView) imageView, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Bitmap.Config.RGB_565);
                            Utilities.showToast("您点击了图片" + position, getActivity());
                        }
                    });
                    break;
                case StatusCode.WORKORDER_ALL_SUCCESS:
                    isLoginOut = false;
                    datas = bean.body.data;
                    if (datas == null || datas.size() == 0) {
                        noPage.setText("暂时没有分配给您的工单");
                        noPage.setVisibility(View.VISIBLE);
                    } else {
                        noPage.setVisibility(View.GONE);
                    }
                    workorderAdapter.setDatas(datas);
                    if (mRecyclerView.getAdapter() == null) {
                        mRecyclerView.setAdapter(workorderAdapter);
                    } else {
                        workorderAdapter.notifyDataSetChanged();
                        handler.post(runnable);
                    }
                    break;
                case StatusCode.WORKORDER_RECEIVE_SUCCESS:
                case StatusCode.WORKORDER_APPOINT_SUCCESS:
                case StatusCode.WORKORDER_HOME_SUCCESS:
                case StatusCode.WORKORDER_ACCOUNT_SUCCESS:
                case StatusCode.WORKORDER_EVALUATE_SUCCSS:
                    isLoginOut = false;
                    if (st == -2 || st == -1) {
                        SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                        Utilities.showToast(bean.msg, getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (st == 0) {
                        datas = bean.body.data;
                        if (datas == null || datas.size() == 0) {
                            noPage.setText("未找到指定订单");
                            noPage.setVisibility(View.VISIBLE);
                        } else {
                            noPage.setVisibility(View.GONE);
                        }
                        workorderAdapter.setDatas(datas);
                        workorderAdapter.notifyDataSetChanged();
                        handler.post(runnable);//刷新之后发现轮播图不会再自动刷新了
                    }
                    break;
                case StatusCode.WORKORDER_RECEIVE_ACTION_SUCCESS:
                case StatusCode.WORKORDER_APPOINT_ACTION_SUCCESS:
                case StatusCode.WORKORDER_HOME_ACTION_SUCCESS:
                    isLoginOut = false;
                    if (st == -2 || st == -1) {
                        Utilities.showToast(bean.msg, getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (st == 0) {
                        String taskType = bean.body.taskType;
                        int p = msg.arg1;
                        int t = msg.arg2;
                        if (Integer.parseInt(taskType) == t + 1) {//工单状态更新成功
                            datas.get(p).taskType = taskType;
                            workorderAdapter.notifyDataSetChanged();
                        } else {
                            Utilities.showToast("任务单更新失败", getActivity());
                        }
                    }
                    break;
            }
            stopProgress();
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    };


    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_task_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        titleView = mRootView.findViewById(R.id.rl_task_list_title);
        mCity = (TextView) mRootView.findViewById(R.id.tv_location_city);
        mTitleFilter = (TextView) mRootView.findViewById(R.id.tv_task_list_filter);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mNotification = (ImageView) mRootView.findViewById(R.id.iv_task_notification);
        noPage = (AppCompatTextView) mRootView.findViewById(R.id.tv_no_page);
        uid = ((MainActivity) getActivity()).getUid();
        mCity.setText(((CustomApplication) getActivity().getApplication()).city);
        initRefreshLayout();//下拉刷新
        initAD();
        initRecyclerView();
        initEvent();
    }


    private void initRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefresh);
        mRefreshLayout.setColorSchemeColors(R.array.material_colors);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.e("tasklistfragment 下拉刷新 currentTask" + curTaskType);
                Requester requester = new Requester();
                requester.cmd = 10001;
                requester.uid = uid;
                requester.body.put("taskType", curTaskType);
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
                            if (curTaskType.equals("-1")) {
                                msg.what = StatusCode.WORKORDER_ALL_SUCCESS;
                            } else if (curTaskType.equals("0")) {
                                msg.what = StatusCode.WORKORDER_RECEIVE_SUCCESS;
                            } else if (curTaskType.equals("1")) {
                                msg.what = StatusCode.WORKORDER_APPOINT_SUCCESS;
                            } else if (curTaskType.equals("2")) {
                                msg.what = StatusCode.WORKORDER_HOME_SUCCESS;
                            } else if (curTaskType.equals("10")) {
                                msg.what = StatusCode.WORKORDER_EVALUATE_SUCCSS;
                            } else if (curTaskType.equals("5")) {
                                msg.what = StatusCode.WORKORDER_ACCOUNT_SUCCESS;
                            }
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
        });
    }

    private void initAD() {
        workorderAdapter = new TaskListAdapter(getActivity());
        workorderAdapter.setHeader(LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_header, null));
        workorderAdapter.setOnItemClickListener(TaskListFragment.this);
        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        cycleViewPager.setTime(5000); // 设置轮播时间，默认5000ms
        cycleViewPager.setIndicatorCenter();// 设置圆点指示图标组居中显示，默认靠右
        handler = cycleViewPager.getHandler();
        runnable = cycleViewPager.getRunnable();

        Requester requester = new Requester();
        requester.cmd = 10000;
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
                    msg.what = StatusCode.WORKORDER_AD_SUCCESS;
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

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));// 实现分割线
        startProgress("加载中");
        curTaskType = "-1";//请求所有
        Requester requester = new Requester();
        requester.cmd = 10001;
        requester.uid = uid;
        requester.body.put("taskSchedule", "-1");
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
                    msg.what = StatusCode.WORKORDER_ALL_SUCCESS;
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


    private void initEvent() {
        mTitleFilter.setOnClickListener(this);
        mNotification.setOnClickListener(this);
    }


    @Override
    public void onItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Task task = datas.get(position);
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        intent.putExtra("workorder_code", task.workorder_code);
        startActivity(intent);
    }

    @Override
    public void onButtonItemClick(View view, int position) {
        // TODO Auto-generated method stub
        if (view.getTag().equals("待接单")) {
//            Utilities.showToast("您接了条目" + position + "的工单", getActivity());
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_receive");
            if (pre != null) {
                ft.remove(pre);
            }
            ft.addToBackStack(null);
            WorkorderReceiveDialog workorderDialog = WorkorderReceiveDialog.newInstance(WorkorderReceiveDialog.MAINACTIVITY, position);
            workorderDialog.show(ft, "workorder_receive");
        } else if (view.getTag().equals("待预约")) {
//            Utilities.showToast("你确定预约" + position + "的工单", getActivity());
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_appoint");
            if (pre != null) {
                ft.remove(pre);
            }
            ft.addToBackStack(null);
            WorkorderAppointDialog workorderAppointDialog = WorkorderAppointDialog.newInstance(WorkorderAppointDialog.MAIN_ACTIVITY, position);
            workorderAppointDialog.show(ft, "workorder_appoint");

        } else if (view.getTag().equals("待上门")) {
//            Utilities.showToast("请输入你预约的上门时间" + position + "的工单", getActivity());
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder_home");
            if (pre != null) {
                ft.remove(pre);
            }
            ft.addToBackStack(null);
            WorkorderHomeDialog workorderHomeDialog = WorkorderHomeDialog.newInstance(WorkorderHomeDialog.MAIN_ACTIVITY, position);
            workorderHomeDialog.show(ft, "workorder_home");
        } else if (view.getTag().equals("待解决")) {
            Utilities.showToast("请在工单详情页面进行问题反馈", getActivity());
        }
    }

    @Override
    public void onLogItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), TaskInformationActivity.class);
        intent.putExtra("workorder_code", datas.get(position).workorder_code);
        startActivity(intent);
    }

    /**
     * 接单对话框确认
     */
    public void doReceiveNegativeClick(final int position) {
        startProgress("请等待接单结果");
        Utilities.showToast("您接单了" + position, getActivity());
        Task task = datas.get(position);
        String workorderCode = task.workorder_code;
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
                    msg.arg1 = position;
                    msg.arg2 = 0;//记录当前请求工单的状态
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
     * 接单对话框取消
     */
    public void doReceivePositiveClick(int position) {
        Utilities.showToast("您取消了", getActivity());
    }

    /**
     * 预约对话框确认
     */
    public void doAppointNegativeClick(final int position, String selectTime) {
        startProgress("预约中");
        Task task = datas.get(position);
        String workorderCode = task.workorder_code;
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
                    msg.arg1 = position;
                    msg.arg2 = 1;//记录当前请求工单的状态
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
    public void doAppointPositiveClick(int position) {
    }

    /**
     * 到场对话框确认
     */
    public void doHomeNegativeClick(final int position) {
        startProgress("加载中");
        Task task = datas.get(position);
        String workorderCode = task.workorder_code;
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
                    msg.arg1 = position;
                    msg.arg2 = 2;//记录当前请求工单的状态
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
    public void doHomePositiveClick(int position) {
        Utilities.showToast("没有上门", getActivity());
    }


    @Override
    public void onClick(View v) {
        Requester requester = new Requester();
        requester.cmd = 10001;
        requester.uid = uid;

        switch (v.getId()) {
            case R.id.tv_task_list_filter:
                showWindow(titleView);
                break;
            case R.id.iv_task_notification:
//                mNotification.setImageResource(R.drawable.anim_task_notification);
//                AnimationDrawable anmation = (AnimationDrawable) mNotification.getDrawable();
//                anmation.start();

                Intent intent = new Intent(getActivity(), WorkorderMsgActivity.class);
//                Intent intent=new Intent(getActivity(), TaskSolveActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_task_all:
                curTaskType = "-1";
                popupWindow.dismiss();
                mTitleFilter.setText(mTaskAll.getText());

                startProgress("加载中");
                requester.body.put("taskType", "-1");
                Request requestAll = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callAll = httpEngine.createRequestCall(requestAll);
                callAll.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_ALL_SUCCESS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            case R.id.tv_task_receive:
                popupWindow.dismiss();
                curTaskType = "0";
                mTitleFilter.setText(mTaskReceive.getText());

                startProgress("加载中");
                requester.body.put("taskType", "0");
                Request requestReceive = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callReceive = httpEngine.createRequestCall(requestReceive);
                callReceive.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_RECEIVE_SUCCESS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            case R.id.tv_task_order:
                popupWindow.dismiss();
                curTaskType = "1";
                mTitleFilter.setText(mTaskOrder.getText());

                startProgress("加载中");
                requester.body.put("taskType", "1");
                Request requestAppoint = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callAppoint = httpEngine.createRequestCall(requestAppoint);
                callAppoint.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_APPOINT_SUCCESS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            case R.id.tv_task_visit:
                popupWindow.dismiss();
                curTaskType = "2";
                mTitleFilter.setText(mTaskVisit.getText());

                startProgress("加载中");
                requester.body.put("taskType", "2");
                Request requestHome = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callHome = httpEngine.createRequestCall(requestHome);
                callHome.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_HOME_SUCCESS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            case R.id.tv_task_account:
                popupWindow.dismiss();
                curTaskType = "5";
                mTitleFilter.setText(mTaskAccount.getText());

                startProgress("加载中");
                requester.body.put("taskType", "5");//待结单
                Request requestAccount = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callAccount = httpEngine.createRequestCall(requestAccount);
                callAccount.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_ACCOUNT_SUCCESS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            case R.id.tv_task_evaluation:
                popupWindow.dismiss();
                curTaskType = "10";
                mTitleFilter.setText(mTaskEvaluation.getText());

                startProgress("加载中");
                requester.body.put("taskType", "10");//待评价
                Request requestEvaluate = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
                Call callEvaluate = httpEngine.createRequestCall(requestEvaluate);
                callEvaluate.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = uiHandler.obtainMessage();
                        msg.what = StatusCode.FAILED;
                        msg.obj = request.body().toString();
                        uiHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Message msg = uiHandler.obtainMessage();
                        if (response.isSuccessful()) {
                            msg.what = StatusCode.WORKORDER_EVALUATE_SUCCSS;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        } else {
                            msg.what = StatusCode.SERVER_EXCEPTION;
                            msg.obj = response.body().string();
                            uiHandler.sendMessage(msg);
                        }
                    }
                });
                break;

        }

    }

    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = layoutInflater.inflate(R.layout.layout_pop_task_list, null);
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(contentView, contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), true);
            //初始化popupwindow的内容
            mTaskAll = (TextView) contentView.findViewById(R.id.tv_task_all);
            mTaskReceive = (TextView) contentView.findViewById(R.id.tv_task_receive);
            mTaskOrder = (TextView) contentView.findViewById(R.id.tv_task_order);
            mTaskVisit = (TextView) contentView.findViewById(R.id.tv_task_visit);
            mTaskAccount = (TextView) contentView.findViewById(R.id.tv_task_account);
            mTaskEvaluation = (TextView) contentView.findViewById(R.id.tv_task_evaluation);

            mTaskAll.setOnClickListener(this);
            mTaskReceive.setOnClickListener(this);
            mTaskOrder.setOnClickListener(this);
            mTaskVisit.setOnClickListener(this);
            mTaskAccount.setOnClickListener(this);
            mTaskEvaluation.setOnClickListener(this);
        }
        popupWindow.setFocusable(true);// 使其聚集
        popupWindow.setOutsideTouchable(true); // 设置允许在外点击消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        int xPos = titleView.getWidth() / 2 - popupWindow.getWidth() / 2;
        Logger.e("popupWindow的宽度" + popupWindow.getWidth() + "   contentView的宽度" + contentView.getWidth());
        popupWindow.showAsDropDown(parent, xPos, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }
}
