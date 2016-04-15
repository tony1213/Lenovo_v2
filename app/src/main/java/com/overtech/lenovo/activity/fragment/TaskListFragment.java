package com.overtech.lenovo.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskInformationActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskListAdapter;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.tasklist.taskbean.AD;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;
import com.overtech.lenovo.entity.tasklist.taskbean.TaskBean;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.cycleviewpager.CycleViewPager;
import com.overtech.lenovo.widget.cycleviewpager.ViewFactory;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;
import com.overtech.lenovo.widget.progressdialog.WorkorderReceiveDialog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class TaskListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener, TaskListAdapter.OnItemClickListener {

    private View titleView, contentView;
    private PopupWindow popupWindow;
    private ImageView mNotification;
    private TextView mTitleFilter;
    private TextView mTaskAll;
    private TextView mTaskReceive;
    private TextView mTaskOrder;
    private TextView mTaskVisit;
    private TextView mTaskAccount;
    private TextView mTaskEvaluation;
    private TaskListAdapter adapter2;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CycleViewPager cycleViewPager;
    private List<Task> datas;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<AD> adImgs = new ArrayList<AD>();
    private Handler handler;// cyclerviewpager的handler
    private Runnable runnable;// cyclerviewpager的runnable
    private UIHandler uiHandler;


    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_task_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        titleView = (View) mRootView.findViewById(R.id.rl_task_list_title);
        mTitleFilter = (TextView) mRootView.findViewById(R.id.tv_task_list_filter);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mNotification = (ImageView) mRootView.findViewById(R.id.iv_task_notification);
        uiHandler = new UIHandler(getActivity());
        initRefreshLayout();//下拉刷新
        initRecyclerView();
        initEvent();
    }


    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) mRootView.findViewById(R.id.rl_modulename_refresh);
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));// 实现分割线
        startProgress("加载中");

        Requester requester = new Requester();
        requester.cmd = 10001;
        requester.uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, null);
        requester.body.put("taskSchedule", "all");
//        Logger.e(new Gson().toJson(requester));
        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.body().toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    stopProgress();
                    final String json = response.body().string();
                    Logger.e("后台返回的数据" + json);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            TaskBean bean = gson.fromJson(json, TaskBean.class);
                            int st = bean.st;
                            if (st == -2) {
                                Utilities.showToast(bean.msg, getActivity());
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (st == -1) {
                                Utilities.showToast(bean.msg, getActivity());
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (st == 0) {
                                datas = bean.body.data;
                                adImgs = bean.body.ad;
                                adapter2 = new TaskListAdapter(getActivity());
                                adapter2.setDatas(datas);
                                adapter2.setHeader(LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_header, null));
                                cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
                                for (int i = 0; i < adImgs.size(); i++) {
                                    views.add(ViewFactory.getImageView(getActivity(), adImgs.get(i).imageUrl));
                                }
                                cycleViewPager.setData(views, adImgs, new CycleViewPager.ImageCycleViewListener() {

                                    @Override
                                    public void onImageClick(String info, int position, View imageView) {
                                        ImageLoader.getInstance().displayImage(info, (ImageView) imageView, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Bitmap.Config.RGB_565);
                                        Utilities.showToast("您点击了图片" + position, getActivity());
                                    }
                                });
                                cycleViewPager.setTime(2000); // 设置轮播时间，默认5000ms
                                cycleViewPager.setIndicatorCenter();// 设置圆点指示图标组居中显示，默认靠右
                                mRecyclerView.setAdapter(adapter2);
                                adapter2.setOnItemClickListener(TaskListFragment.this);
                                handler = cycleViewPager.getHandler();
                                runnable = cycleViewPager.getRunnable();
                            } else {
                                //其他异常
                            }
                        }
                    });


                }
            }
        });
//        String json = "{\"body\":{\"ad\":{\"imageUrl1\":\"http://a.hiphotos.baidu.com/zhidao/pic/item/21a4462309f79052f093a19e0ef3d7ca7acbd586.jpg\",\"imageUrl2\":\"http://img1.3lian.com/img13/c3/10/d/34.jpg\",\"imageUrl3\":\"http://img15.3lian.com/2015/h1/20/d/137.jpg\",\"imageUrl4\":\"http://img3.3lian.com/2013/c2/64/d/73.jpg\"},\"data\":[{\"appointment_home_datetime\":0,\"isUrgent\":\"0\",\"issue_resume\":\"WIFI无法正常使用\",\"issue_type\":\"网络问题\",\"latitude\":\"\",\"longitude\":\"\",\"remarks\":\"携带检测装备\",\"repair_person_contact_information\":\"18737134310\",\"taskLogo\":\"http://img.sj33.cn/uploads/allimg/201402/7-140206204500561.png\",\"taskType\":\"0\",\"workorder_code\":\"10001001\",\"workorder_create_datetime\":\"2016-04-11\"},{\"appointment_home_datetime\":0,\"isUrgent\":\"0\",\"issue_resume\":\"打印机无法正常使用\",\"issue_type\":\"未知问题\",\"latitude\":\"\",\"longitude\":\"\",\"remarks\":\"携带检测装备\",\"repair_person_contact_information\":\"18737134310\",\"taskLogo\":\"http://img.sj33.cn/uploads/allimg/201402/7-140206204500561.png\",\"taskType\":\"1\",\"workorder_code\":\"10001002\",\"workorder_create_datetime\":\"2016-04-11\"},{\"appointment_home_datetime\":1461233040,\"isUrgent\":\"1\",\"issue_resume\":\"WIFI无法正常使用\",\"issue_type\":\"网络问题\",\"latitude\":\"\",\"longitude\":\"\",\"remarks\":\"携带检测装备\",\"repair_person_contact_information\":\"18737134310\",\"taskLogo\":\"http://img.sj33.cn/uploads/allimg/201402/7-140206204500561.png\",\"taskType\":\"2\",\"workorder_code\":\"10001003\",\"workorder_create_datetime\":\"2016-04-11\"},{\"appointment_home_datetime\":0,\"isUrgent\":\"1\",\"issue_resume\":\"WIFI无法正常使用\",\"issue_type\":\"网络问题\",\"latitude\":\"\",\"longitude\":\"\",\"remarks\":\"携带检测装备\",\"repair_person_contact_information\":\"18737134310\",\"taskLogo\":\"http://img.sj33.cn/uploads/allimg/201402/7-140206204500561.png\",\"taskType\":\"3\",\"workorder_code\":\"10001004\",\"workorder_create_datetime\":\"2016-04-11\"},{\"appointment_home_datetime\":0,\"isUrgent\":\"1\",\"issue_resume\":\"WIFI无法正常使用\",\"issue_type\":\"网络问题\",\"latitude\":\"\",\"longitude\":\"\",\"remarks\":\"携带检测装备\",\"repair_person_contact_information\":\"18737134310\",\"taskLogo\":\"http://img.sj33.cn/uploads/allimg/201402/7-140206204500561.png\",\"taskType\":\"4\",\"workorder_code\":\"10001004\",\"workorder_create_datetime\":\"2016-04-11\"}]},\"msg\":\"success\",\"st\":0}";

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
            Utilities.showToast("您接了条目" + position + "的工单", getActivity());

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("workorder");
            if (pre != null) {
                ft.remove(pre);
            }
            ft.addToBackStack(null);
            WorkorderReceiveDialog workorderDialog = WorkorderReceiveDialog.newInstance(WorkorderReceiveDialog.MAINACTIVITY);
            workorderDialog.show(ft, "workorder");
        } else if (view.getTag().equals("待预约")) {
            Utilities.showToast("你确定预约" + position + "的工单", getActivity());
        } else if (view.getTag().equals("待上门")) {
            Utilities.showToast("请输入你预约的上门时间" + position + "的工单", getActivity());
        }
    }

    /**
     * 接单对话框确认
     */
    public void doNegativeClick() {

    }

    /**
     * 接单对话框拒绝
     */
    public void doPositiveClick() {

    }

    /**
     * 接单对话框取消
     */
    public void doNeutralClick() {

    }

    @Override
    public void onLogItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Utilities.showToast("您要进入" + position + "的项目", getActivity());
        Intent intent = new Intent(getActivity(), TaskInformationActivity.class);
        Bundle bundle = new Bundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束下拉刷新
                mRefreshLayout.endRefreshing();
            }
        }.execute();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束加载更多
                mRefreshLayout.endLoadingMore();
            }
        }.execute();

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_task_list_filter:
                showWindow(titleView);
                break;
            case R.id.iv_task_notification:
                mNotification.setImageResource(R.drawable.anim_task_notification);
                AnimationDrawable anmation = (AnimationDrawable) mNotification.getDrawable();
                anmation.start();
                break;
            case R.id.tv_task_all:
                mTitleFilter.setText(mTaskAll.getText());
                break;
            case R.id.tv_task_receive:
                mTitleFilter.setText(mTaskReceive.getText());
                break;
            case R.id.tv_task_order:
                mTitleFilter.setText(mTaskOrder.getText());
                break;
            case R.id.tv_task_visit:
                mTitleFilter.setText(mTaskVisit.getText());
                break;
            case R.id.tv_task_account:
                mTitleFilter.setText(mTaskAccount.getText());
                break;
            case R.id.tv_task_evaluation:
                mTitleFilter.setText(mTaskAccount.getText());
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
        Utilities.showToast("popupwindow中间的位置" + popupWindow.getWidth(), getActivity());
        popupWindow.showAsDropDown(parent, xPos, 0);
    }
}
