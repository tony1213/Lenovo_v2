package com.overtech.lenovo.activity.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.knowledge.KnowledgeDetailActivity;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMainAdapter;
import com.overtech.lenovo.activity.business.knowledge.adapter.ClassifyMoreAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.knowledge.Knowledges;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.ScreenTools;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KnowledgeFragment extends BaseFragment {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private TextView title;
    private ListViewCompat lvContracts;
    //    private ContractAdapter adapter;
    private ArrayAdapter<Knowledges.KnowledgeAndContract> adapter;
    private List<Knowledges.KnowledgeAndContract> datas;
    private List<Knowledges.KnowledgeAndContract> contractDatas;
    private SearchView searchView;
    private PopupWindow popupWindow;
    private ListView mainlist;
    private ListView morelist;
    private List<Map<String, Object>> mainList2;
    private ClassifyMainAdapter mainAdapter;
    private ClassifyMoreAdapter moreAdapter;
    private String uid;
    private boolean isLoginOut = false;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Logger.e("knowledge后台传过来的数据" + json);
            Knowledges bean = gson.fromJson(json, Knowledges.class);
            if (bean == null) {
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -2 || st == -1||st==500) {
                stopProgress();
                if (!isLoginOut) {
                    isLoginOut = true;
                    Utilities.showToast(bean.msg, getActivity());
                    SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                return;
            }
            super.handleMessage(msg);
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.KNOWLEDGE_PUBLIC_SUCCESS:
                    isLoginOut = false;
                    datas = bean.body.data;
                    if (datas == null || datas.size() == 0) {
                        stopProgress();
                        Utilities.showToast("无数据", getActivity());
                        return;
                    }
                    if (mainAdapter == null) {
                        mainAdapter = new ClassifyMainAdapter(getActivity(), datas);
                        mainAdapter.setSelectItem(0);
                        mainlist.setAdapter(mainAdapter);
                    } else {
                        mainAdapter.setData(datas);
                        mainAdapter.setSelectItem(0);
                        mainAdapter.notifyDataSetChanged();
                    }
                    mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    if (moreAdapter == null) {
                        moreAdapter = new ClassifyMoreAdapter(getActivity(), datas.get(0).knowledges);
                        moreAdapter.setSelectItem(0);
                        morelist.setAdapter(moreAdapter);
                    } else {
                        moreAdapter.setData(datas.get(0).knowledges);
                        moreAdapter.notifyDataSetChanged();
                    }

                    break;
                case StatusCode.KNOWLEDGE_CONTRACT_SUCCESS:
                    isLoginOut = false;
                    contractDatas = bean.body.data;
//                    if (adapter == null) {
//                        adapter = new ContractAdapter(contractDatas, getActivity());
                    adapter = new ArrayAdapter<Knowledges.KnowledgeAndContract>(getActivity(), android.R.layout.simple_spinner_item, contractDatas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lvContracts.setAdapter(adapter);
//                    } else {
////                        adapter.setData(contractDatas);
//                        adapter.
//                        adapter.notifyDataSetChanged();
//                    }

                    break;
                case StatusCode.KNOWLEDGE_CONTENT_SUCCESS:
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uid = (String) SharePreferencesUtils.get(getActivity(), SharedPreferencesKeys.UID, "");
        initView();
        initPopupWindow();
        initData(10021, "0", null);
        initContractData();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Logger.e("knowledge fragment 菜单创建了");
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        title = (TextView) getActivity().findViewById(R.id.tv_toolbar_title);
        title.setVisibility(View.VISIBLE);
        title.setText("知识");
        inflater.inflate(R.menu.menu_knowledge, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(true);
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                Logger.e("onActionExpandListener====Expand");
//                title.setAlpha(0);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Logger.e("onActionExpandListener=====Collapse");
//                title.setAlpha(1);
//                return true;
//            }
//        });
//        title.setVisibility(View.GONE);
//        title.setText("知识");
        actionBar.show();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.e("searchview 搜索开始执行了");
                if (!TextUtils.isEmpty(query)) {
//                    searchView.onActionViewCollapsed();
                    initData(10021, null, query);
                }else{
                    initData(10021, "0", null);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.e("onQueryTextChange==newText===="+newText.length()+"===="+newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_contract:
//                popupWindow.showAtLocation(toolbar, Gravity.NO_GRAVITY, toolbar.getWidth() / 5 * 3, toolbar.getHeight());
                PopupWindowCompat.showAsDropDown(popupWindow, toolbar, 0, 0, GravityCompat.END);
                searchView.onActionViewCollapsed();
                return true;
            default:
//                Utilities.showToast("默认支持", getActivity());
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setVisibility(View.VISIBLE);
            title.setText("知识");
            initContractData();
        } else {
            title.setVisibility(View.GONE);
        }
    }

    private void initContractData() {
        Requester requester = new Requester();
        requester.cmd = 10020;
        requester.uid = uid;
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
                    msg.what = StatusCode.KNOWLEDGE_CONTRACT_SUCCESS;
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

    private void initData(int cmd, String knowledgeType, String keyword) {
        startProgress("加载中...");
        Requester requester = new Requester();
        requester.cmd = cmd;
        requester.uid = uid;
        requester.body.put("contract_code", knowledgeType);
        requester.body.put("keyword", keyword);
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
                    msg.what = StatusCode.KNOWLEDGE_PUBLIC_SUCCESS;
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

    private void initPopupWindow() {

        View contentView = getActivity().getLayoutInflater().inflate(R.layout.layout_popupwindow_contract, null);
        lvContracts = (ListViewCompat) contentView.findViewById(R.id.lv_contracts);
        lvContracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Utilities.showToast("您点击了" + position, getActivity());
                popupWindow.dismiss();
                initData(10021, contractDatas.get(position).contract_code, null);
            }
        });
        popupWindow = new PopupWindow(contentView, ScreenTools.instance(getActivity()).dip2px(56 * 3), ScreenTools.instance(getActivity()).dip2px(144));
        popupWindow.setFocusable(true);//设置焦点，此时的事件交给popupwindow自己处理，
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置背景点击空白处和back可以让pop消失
    }

    private void initView() {
        mainlist = (ListView) mRootView.findViewById(R.id.classify_mainlist);
        morelist = (ListView) mRootView.findViewById(R.id.classify_morelist);
        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                moreAdapter.setData(datas.get(position).knowledges);
                moreAdapter.setSelectItem(0);
                moreAdapter.notifyDataSetChanged();
                mainAdapter.setSelectItem(position);
                mainAdapter.notifyDataSetChanged();
            }
        });
        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moreAdapter.setSelectItem(position);
                moreAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity(), KnowledgeDetailActivity.class);
                intent.putExtra("knowledge_id", (String) moreAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

}
