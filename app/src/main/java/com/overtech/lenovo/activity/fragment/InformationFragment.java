package com.overtech.lenovo.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.infomation.adapter.InformationAdapter;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;

public class InformationFragment extends BaseFragment {
    private View convertView;
    private RecyclerView mInformation;
    private InformationAdapter adapter;
    private List<Information> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_infomation, container, false);
        init();
        return convertView;
    }

    protected void init() {
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
        adapter.setOnItemButtonClickListener(new InformationAdapter.OnItemButtonClickListener() {

            @Override
            public void buttonClick(View v, int position) {
                Utilities.showToast("您评论了第" + position + "条记录", getActivity());
            }
        });
        mInformation = (RecyclerView) convertView.findViewById(R.id.recycler_information);
        mInformation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mInformation.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mInformation.setAdapter(adapter);
    }
}
