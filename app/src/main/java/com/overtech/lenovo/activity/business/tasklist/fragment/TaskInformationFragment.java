package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskInfoFragAdapter;
import com.overtech.lenovo.config.Debug;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.tasklist.TaskProcess;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.picasso.Callback;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

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
    private String workorderCode;
    private boolean isFirstLoading = true;//第一次加载
    private UIHandler uiHandler;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_task_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        mTaskProcess = (ListView) mRootView.findViewById(R.id.lv_task_process);
        tvRepairPerson = (TextView) mRootView.findViewById(R.id.tv_repair_person);
        tvRepairPersonContactInfo = (TextView) mRootView.findViewById(R.id.tv_repair_person_contact_information);
        tvRepairAddress = (TextView) mRootView.findViewById(R.id.tv_repair_address);
        ivRepairContactInfo = (ImageView) mRootView.findViewById(R.id.iv_repair_person_contact_information);
        workorderCode = ((TaskDetailActivity) getActivity()).getWorkorderCode();

        datas = new ArrayList<TaskProcess>();
//        datas.add(new TaskProcess("开单", "2016/01/22", "单号：20160122-0008", ""));
//        datas.add(new TaskProcess("接单", "", "", "接单"));
//        datas.add(new TaskProcess("预约", "2016/01/22 10:50", "", "改约"));
//        datas.add(new TaskProcess("到场", "2016/01/22 11:30", "", "到场"));
//        datas.add(new TaskProcess("完成", "2016/01/22 12:00", "", "解决方案"));
//        datas.add(new TaskProcess("评价", "2016/01/22 14:00", "问题已经解决，态度认真负责", ""));
        adapter = new TaskInfoFragAdapter(getActivity(), datas);
        adapter.setOnButtonClickListener(new TaskInfoFragAdapter.OnButtonClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTaskProcess.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("TaskINfor"+"===="+"onResume");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Debug.log("taskinfofragment==setUserVisibleHint==", isVisibleToUser + "");
        if (isVisibleToUser) {
            if (isFirstLoading) {
                uiHandler = new UIHandler(getActivity());
                startProgress("加载中...");
                Requester requester = new Requester();
                requester.uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "-1");
                requester.cmd = 10003;
                Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
                Call call = httpEngine.createRequestCall(request);
                call.enqueue(new com.squareup.okhttp.Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        stopProgress();
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        stopProgress();
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String json = response.body().string();
                                    if (response.isSuccessful()) {
                                        JSONObject jsonObject = new JSONObject(json);
                                        int st = jsonObject.getInt("st");
                                        if (st == 0) {
                                            jsonObject.getJSONObject("body");
                                        }
                                    } else {
                                        Utilities.showToast("服务器异常", getActivity());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });


            }

            // 开始网络加载
        } else {
            // 取消网络加载
        }
    }

    /**
     * 确认接单
     */
    public void doNegativeClick(){
        Utilities.showToast("你接单了",getActivity());
    }

    /**
     * 拒绝接单
     */
    public void doPositiveClick(){
        Utilities.showToast("你拒绝了",getActivity());
    }

    /**
     * 取消
     */
    public void doNeturalClick(){
        Utilities.showToast("你取消了",getActivity());
    }

}
