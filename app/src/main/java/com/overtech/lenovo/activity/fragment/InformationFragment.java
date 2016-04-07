package com.overtech.lenovo.activity.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter.OnItemButtonClickListener;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
//

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class InformationFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private RecyclerView mInformation;
    private BGARefreshLayout mRefreshLayout;
    private InformationAdapter adapter;
    private List<Information> datas;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_infomation;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mInformation = (RecyclerView) mRootView.findViewById(R.id.recycler_information);
        mRefreshLayout = (BGARefreshLayout) mRootView.findViewById(R.id.rl_modulename_refresh_info);

        mInformation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mInformation.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        datas = new ArrayList<Information>();
        datas.add(new Information(
                "http://avatar.csdn.net/F/C/3/1_heaimnmn.jpg",
                "李小姐",
                "星巴克门店运维项目3.1正式上线，第一阶段涉及江苏、浙江、安徽三个省，欢迎大家主动承接。请联系QQ:6657468,6657468@qq.com",
                new String[]{"http://img3.3lian.com/2014/s4/42/d/44.jpg",
                        "http://img2.3lian.com/img2007/19/51/025.jpg",
                        "http://img2.3lian.com/img2007/23/15/005.jpg"},
                12312414));
        datas.add(new Information(
                "http://avatar.csdn.net/6/B/8/1_projectlover.jpg",
                "罗小姐",
                "[上海招聘] 招聘T2桌面工程师一名，税前6000，五险一金，2.15到岗。有意愿者请按标准简历模板（见知识库）填写后发至hr@dajutech.com",
                new String[]{"http://t1.niutuku.com/960/10/10-192927.jpg",
                        "http://img3.3lian.com/2014/s5/38/d/91.jpg",
                        "http://www.taopic.com/uploads/allimg/110922/10023-11092211201726.jpg"},
                213124));
        adapter = new InformationAdapter(getActivity(), datas);
        adapter.setOnItemButtonClickListener(new OnItemButtonClickListener() {

            @Override
            public void buttonClick(View v, int position) {
                // TODO Auto-generated method stub
                Utilities.showToast("您评论了第" + position + "条记录", getActivity());
            }
        });
        mInformation.setAdapter(adapter);
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
