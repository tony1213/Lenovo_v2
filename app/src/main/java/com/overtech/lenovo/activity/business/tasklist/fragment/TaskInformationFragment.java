package com.overtech.lenovo.activity.business.tasklist.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskInfoFragAdapter;
import com.overtech.lenovo.entity.tasklist.TaskProcess;

public class TaskInformationFragment extends BaseFragment {
    private ListView mTaskProcess;
    private TaskInfoFragAdapter adapter;
    private List<TaskProcess> datas;
    private View convertView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_task_information, container, false);
        init();
        return convertView;
    }


    protected void init() {
        mTaskProcess = (ListView) convertView.findViewById(R.id.lv_task_process);
        datas = new ArrayList<TaskProcess>();
        datas.add(new TaskProcess("开单", "2016/01/22", "单号：20160122-0008", ""));
        datas.add(new TaskProcess("接单", "", "", "接单"));
        datas.add(new TaskProcess("预约", "2016/01/22 10:50", "", "改约"));
        datas.add(new TaskProcess("到场", "2016/01/22 11:30", "", "到场"));
        datas.add(new TaskProcess("完成", "2016/01/22 12:00", "", "解决方案"));
        datas.add(new TaskProcess("评价", "2016/01/22 14:00", "问题已经解决，态度认真负责", ""));
        adapter = new TaskInfoFragAdapter(getActivity(), datas);
        mTaskProcess.setAdapter(adapter);
    }

}
