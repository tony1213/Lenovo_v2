package com.overtech.lenovo.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.app.CustomApplication;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskInformationActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskListAdapter;
import com.overtech.lenovo.activity.business.tasklist.adapter.TitleFilterPopWindowAdapter;
import com.overtech.lenovo.entity.tasklist.ADInfo;
import com.overtech.lenovo.entity.tasklist.Task;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.cycleviewpager.CycleViewPager;
import com.overtech.lenovo.widget.cycleviewpager.ViewFactory;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskListAdapter.OnItemClickListener;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class TaskListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, OnItemClickListener {

    private View convertView, titleView, popView;
    private TextView mTitleFilter;
    private PopupWindow popupWindow;
    private ArrayList<String> groups;
    private ListView lv_group;
    private TitleFilterPopWindowAdapter adapter;
    private TaskListAdapter adapter2;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CycleViewPager cycleViewPager;
    private List<Task> datas;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private Handler handler;// cyclerviewpager的handler
    private Runnable runnable;// cyclerviewpager的runnable
    private String[] imageUrls = {
            "http://tupian.enterdesk.com/2012/1103/gha/1/enterdesk%20%2812%29.jpg",
            "http://img1.3lian.com/img013/v4/81/d/71.jpg",
            "http://img1.3lian.com/img013/v4/81/d/66.jpg",
            "http://pic20.nipic.com/20120409/9188247_091601398179_2.jpg",};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_task_list, container, false);
        findViewById();
        initRefreshLayout();
        initRecyclerView();
        initCycleViewPager();
        initEvent();
        return convertView;
    }

    private void findViewById() {
        titleView = (View) convertView.findViewById(R.id.rl_task_list_title);
        mTitleFilter = (TextView) convertView.findViewById(R.id.tv_task_list_filter);
        mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);

    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) convertView.findViewById(R.id.rl_modulename_refresh);
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));// 实现分割线
        datas = new ArrayList<Task>();
        Task task1 = new Task("", "20160309-0001", "2016-03-11  11:30", "网络问题",
                "WIFI无法正常使用", "携带装备", "langitude", "longitude", "2小时上门", "1",
                "接单");
        Task task2 = new Task("", "20160309-0002", "2016-03-12  12:30",
                "POS软件问题", "pos无法正常使用", "携带装备", "langitude", "longitude",
                "6小时上门", "0", "评价");
        Task task3 = new Task("", "20160309-0003", "2016-03-13  13:30", "网络问题",
                "哈哈无法正常使用", "携带装备", "langitude", "longitude", "2小时上门", "0",
                "接单");
        Task task4 = new Task("", "20160309-0004", "2016-03-14  14:30", "网络问题",
                "pos无法正常使用", "携带装备", "langitude", "longitude", "3小时上门", "1",
                "评价");
        Task task5 = new Task("", "20160309-0005", "2016-03-15  15:30", "网络问题",
                "设备无法正常使用", "携带装备", "langitude", "longitude", "4小时上门", "0",
                "接单");
        Task task6 = new Task("", "20160309-0006", "2016-03-16  16:30", "网络问题",
                "设备无法正常使用", "携带装备", "langitude", "longitude", "3小时上门", "0",
                "评价");
        Task task7 = new Task("", "20160309-0007", "2016-03-17  17:30", "网络问题",
                "设备无法正常使用", "携带装备", "langitude", "longitude", "1小时上门", "0",
                "接单");
        Task task8 = new Task("", "20160309-0008", "2016-03-18  18:30", "网络问题",
                "设备无法正常使用", "携带装备", "langitude", "longitude", "3小时上门", "0",
                "评价");

        datas.add(task1);
        datas.add(task2);
        datas.add(task3);
        datas.add(task4);
        datas.add(task5);
        datas.add(task6);
        datas.add(task7);
        datas.add(task8);

        adapter2 = new TaskListAdapter(getActivity());
        adapter2.setDatas(datas);
        adapter2.setHeader(LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_header, null));
        adapter2.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter2);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {// 目前recyclerview的第一个item是轮播图，所以在此判断当第一个可见的是轮播图时，并且空闲时清除之前的任务重新开始新的任务
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initCycleViewPager() {
        ImageLoader.getInstance().initContext(getActivity());// 初始化ImageView;
        // cycleViewPager = (CycleViewPager) getChildFragmentManager()//
        // 绝对不能用getActivity().getSupportFragmentManager()
        // .findFragmentById(R.id.fragment_cycle_viewpager_content);//因为fragment标签不再在fragment中，所以此处不再需要

        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);

        handler = cycleViewPager.getHandler();
        runnable = cycleViewPager.getRunnable();

        for (int i = 0; i < imageUrls.length; i++) {
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i);
            infos.add(info);
        }

        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), infos.get(i)
                    .getUrl()));
        }

        cycleViewPager.setData(views, infos, new CycleViewPager.ImageCycleViewListener() {

            @Override
            public void onImageClick(ADInfo info, int position, View imageView) {
                ImageLoader.getInstance().displayImage(info.getUrl(), (ImageView) imageView, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Bitmap.Config.RGB_565);
                Utilities.showToast("您点击了图片" + position, getActivity());
            }
        });
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        // 设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }


    private void initEvent() {
        mTitleFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(titleView);
            }
        });
    }

    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = layoutInflater.inflate(R.layout.layout_pop_task_list, null);
            lv_group = (ListView) popView.findViewById(R.id.lvGroup);
            // 加载数据
            groups = new ArrayList<String>();
            groups.add("所有工单");
            groups.add("接单");
            groups.add("预约");
            groups.add("上门");
            groups.add("结单");
            groups.add("评价");
            adapter = new TitleFilterPopWindowAdapter(getActivity(), groups);

            lv_group.setAdapter(adapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(popView, 150, WindowManager.LayoutParams.WRAP_CONTENT);
        }


        popupWindow.setFocusable(true);// 使其聚集
        popupWindow.setOutsideTouchable(true); // 设置允许在外点击消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        popupWindow.showAsDropDown(parent, xPos, 0);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Toast.makeText(getActivity(), ((CustomApplication) getActivity().getApplication()).city, Toast.LENGTH_LONG).show();
                Log.e("获取城市",((CustomApplication) getActivity().getApplication()).city);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                mTitleFilter.setText(groups.get(position));
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Utilities.showToast("您点击了条目" + position, getActivity());
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        Bundle bundle = new Bundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onButtonItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Utilities.showToast("您接了条目" + position + "的工单", getActivity());
    }

    @Override
    public void onLogItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Utilities.showToast("您点击了条目" + position + "的log", getActivity());
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
}
