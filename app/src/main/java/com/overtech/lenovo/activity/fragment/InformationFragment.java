package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter.OnItemButtonClickListener;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.itemdecoration.DividerItemDecoration;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

//

public class InformationFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener {

    private RecyclerView mInformation;
    private LinearLayout llCommentUpContainer;
    private AppCompatEditText etComment;
    private AppCompatButton btComment;
    private BGARefreshLayout mRefreshLayout;
    private InformationAdapter adapter;
    private List<Information> datas;
    private String uid;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Information bean = gson.fromJson(json, Information.class);
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
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.INFORMATION_SUCCESS:

                    break;
                case StatusCode.INFORMATION_COMMENT_SUCCESS:
                    if (adapter == null) {
                        adapter = new InformationAdapter(getActivity(), bean.body.data);
                        adapter.setOnItemButtonClickListener(new OnItemButtonClickListener() {

                            @Override
                            public void buttonClick(View v, int position) {
                                // TODO Auto-generated method stub
                                Utilities.showToast("您评论了第" + position + "条记录", getActivity());
                                llCommentUpContainer.setVisibility(View.VISIBLE);
                                etComment.setFocusable(true);
                            }
                        });
                        mInformation.setAdapter(adapter);
                    } else {
                        adapter.setData(bean.body.data);
                        adapter.notifyDataSetChanged();
                        mRefreshLayout.endRefreshing();
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_infomation;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        uid = ((MainActivity) getActivity()).getUid();
        mInformation = (RecyclerView) mRootView.findViewById(R.id.recycler_information);
         llCommentUpContainer= (LinearLayout) mRootView.findViewById(R.id.ll_comment_upload_container);
        etComment= (AppCompatEditText) mRootView.findViewById(R.id.et_comment);
        btComment= (AppCompatButton) mRootView.findViewById(R.id.bt_comment);
        mRefreshLayout = (BGARefreshLayout) mRootView.findViewById(R.id.rl_modulename_refresh_info);
        llCommentUpContainer.setVisibility(View.GONE);
        etComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    etComment.setFocusable(false);
                    llCommentUpContainer.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
        btComment.setOnClickListener(this);
        mInformation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mInformation.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        startProgress("加载中...");
        initData();
    }

    private void initData() {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10050;
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
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
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.INFORMATION_SUCCESS;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        initData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
        mRefreshLayout.endLoadingMore();

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_comment:
                String comment=etComment.getText().toString().trim();
                if(TextUtils.isEmpty(comment)){
                    Utilities.showToast("评论内容不能为空",getActivity());
                    return;
                }
                startUploadComment(comment);
                break;
        }
    }

    private void startUploadComment(String content) {

    }
}
