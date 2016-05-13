package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter;
import com.overtech.lenovo.activity.business.information.adapter.InformationAdapter.OnItemButtonClickListener;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//

public class InformationFragment extends BaseFragment implements View.OnClickListener {

    private ActionBar actionBar;
    private RecyclerView mInformation;
    public LinearLayout llCommentUpContainer;
    private AppCompatEditText etComment;
    private AppCompatButton btComment;
    //    private BGARefreshLayout mRefreshLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView title;
    private InformationAdapter adapter;
    private List<Information.InforItem> datas;
    private boolean isRefreshing;//用于标记当前是上拉刷新还是下拉加载更多
    private int curPage = 0;//记录当前的页码
    private Map mContentTree = new HashMap();
    private String uid;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("informationfragment  后台" + json);
            final Information bean = gson.fromJson(json, Information.class);
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
            if (bean.body == null) {
                stopProgress();
//                mRefreshLayout.endLoadingMore();
//                mRefreshLayout.endRefreshing();
                Utilities.showToast("暂时没有数据", getActivity());
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
                    refreshLayout.setRefreshing(false);
                    if (isRefreshing) {
                        datas = bean.body.data;
                    } else {
                        datas.addAll(bean.body.data);
                        Logger.e("INformation Fragment 此时datas的大小" + datas.size());
                    }
                    if (datas == null) {
                        Utilities.showToast("暂时没有数据", getActivity());
                        return;
                    }
                    curPage = datas.size() / 10;
                    if (adapter == null) {
                        adapter = new InformationAdapter(getActivity(), datas);
                        adapter.setOnItemButtonClickListener(new OnItemButtonClickListener() {

                            @Override
                            public void buttonClick(View v, int position, int contentPosition, Information.Comment comment) {
                                // TODO Auto-generated method stub
                                llCommentUpContainer.setVisibility(View.VISIBLE);
                                etComment.setFocusable(true);
                                etComment.setTag(new Object[]{position, datas.get(position).post_id, comment, contentPosition});
                            }
                        });
                        mInformation.setAdapter(adapter);
                    } else {
                        adapter.setData(datas);
                        adapter.notifyDataSetChanged();
                    }
//                    mRefreshLayout.endRefreshing();
//                    mRefreshLayout.endLoadingMore();
                    break;
                case StatusCode.INFORMATION_COMMENT_SUCCESS:
                    int p = msg.arg1;
                    int commentPosition = msg.arg2;
                    Information information = new Information();
                    if (commentPosition != -1) {//点击的已评论的内容
                        Information.CommentResponse response = information.new CommentResponse();
                        response.comment_content = bean.body.comment_content;
                        response.comment_user = bean.body.comment_user;
                        adapter.getDatas().get(p).comment.get(commentPosition).comment_response.add(response);//获取帖子集合-->获取帖子对象--》获取帖子回复的集合--》添加到回复的集合里
                    } else {//点击的是帖子评论
                        Information.Comment comment = information.new Comment();
                        comment.comment_content = bean.body.comment_content;
                        comment.comment_user = bean.body.comment_user;
                        adapter.getDatas().get(p).comment.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                    etComment.setText("");
                    llCommentUpContainer.setVisibility(View.GONE);
                    break;
                case StatusCode.INFORMATION_COMMENT_RESPONSE_SUCCESS:

                    break;
            }
            stopProgress();
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
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        title = (TextView) getActivity().findViewById(R.id.tv_toolbar_title);
        title.setText("信息");
        title.setVisibility(View.VISIBLE);
        initRefreshLayout();
        mInformation = (RecyclerView) mRootView.findViewById(R.id.recycler_information);
        mInformation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        llCommentUpContainer = (LinearLayout) mRootView.findViewById(R.id.ll_comment_upload_container);
        etComment = (AppCompatEditText) mRootView.findViewById(R.id.et_comment);
        btComment = (AppCompatButton) mRootView.findViewById(R.id.bt_comment);

        mInformation.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    lastPosition = layoutManager.findLastVisibleItemPosition();
                    Logger.e("informationFragment==lastPosition==" + lastPosition);
                    if (lastPosition == datas.size() - 1) {
//                        mRefreshLayout.setIsShowLoadingMoreView(true);
//                        mRefreshLayout.beginLoadingMore();
                    }
                }
            }
        });


        llCommentUpContainer.setVisibility(View.GONE);
        btComment.setOnClickListener(this);
        startProgress("加载中...");
        isRefreshing = true;
        initData(curPage);
    }

    private void initRefreshLayout() {
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.material_colors));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utilities.showToast("下拉刷新", getActivity());
                isRefreshing = true;
                curPage = 0;//下拉刷新默认请求最新的一条数据
                initData(curPage);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setVisibility(View.VISIBLE);
            title.setText("信息");
        } else {
            title.setVisibility(View.GONE);
        }
    }

    private void initData(int page) {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10050;
        requester.body.put("page", String.valueOf(page));
        requester.body.put("size", "10");
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
        actionBar.show();

    }

//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
//        isRefreshing = false;
//        initData(++curPage);
//        return true;
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                String commentContent = etComment.getText().toString().trim();
                Object[] objs = (Object[]) etComment.getTag();
                String post_id = (String) objs[1];
                int pos = (int) objs[0];
                int contentPosition = (int) objs[3];
                Information.Comment comment = (Information.Comment) objs[2];
                if (TextUtils.isEmpty(commentContent)) {
                    Utilities.showToast("评论内容不能为空", getActivity());
                    return;
                }
                startUploadComment(pos, post_id, commentContent, comment, contentPosition);
                break;
        }
    }

    private void startUploadComment(final int position, String id, final String content, Information.Comment comment, final int contentPosition) {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10051;
        requester.body.put("post_id", id);
        requester.body.put("comment_content", content);
        if (comment != null) {
            requester.body.put("comment_id", comment.comment_id);
        }
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
                    msg.what = StatusCode.INFORMATION_COMMENT_SUCCESS;
                    msg.obj = json;
                    msg.arg1 = position;
                    msg.arg2 = contentPosition;
                    mContentTree.put(position, content);
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
