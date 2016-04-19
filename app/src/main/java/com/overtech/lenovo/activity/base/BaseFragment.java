package com.overtech.lenovo.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.overtech.lenovo.activity.fragment.callback.FragmentCallback;
import com.overtech.lenovo.activity.fragment.callback.FragmentInterface;
import com.overtech.lenovo.config.Debug;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.widget.dialog.CustomProgressDialog;


public abstract class BaseFragment extends Fragment implements
        FragmentInterface, OnTouchListener {
    protected View mRootView;
    public CustomProgressDialog newFragment;
    public HttpEngine httpEngine;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        Logger.e("BaseFragment" + "==" + "onCreateView");
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        if (httpEngine == null) {//在这里进行初始化，是因为setUserVisibleHint()竟然会有不执行的情况，原因不知
            httpEngine = HttpEngine.getInstance();
            httpEngine.initContext(getActivity());
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Logger.e("执行到BaseFragment" + "setUserVisibleHint" + "====" + isVisibleToUser);
        if (isVisibleToUser) {
            if (httpEngine == null) {//在此处初始化的原因这个方法执行很早，两个地方都初始化双重保险
                httpEngine = HttpEngine.getInstance();
                httpEngine.initContext(getActivity());
            }
        }
    }

    /**
     * 打开对话框
     */
    public void startProgress(String content) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment pre = getActivity().getSupportFragmentManager().findFragmentByTag("custom");
        if (pre != null) {
            ft.remove(pre);
        }
        ft.addToBackStack(null);
        newFragment = CustomProgressDialog.newInstance(content);
        newFragment.setCancelable(false);
        newFragment.show(ft, "custom");
    }

    /**
     * 关闭对话框
     */
    public void stopProgress() {

        if (newFragment.getDialog() != null) {
            if (newFragment.getDialog().isShowing()) {
                newFragment.dismiss();
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Logger.e("执行到" + "onActivityCreate");
        setHasOptionsMenu(true);// 如果让fragment使用菜单此处为true
        afterCreate(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    /**
     * 模拟后退键
     */
    protected void back() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View,
     * android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 拦截触摸事件，防止传递到下一层的View
        return true;
    }

    public void dispatchCommand(int id, Bundle args) {
        if (getActivity() instanceof FragmentCallback) {
            FragmentCallback callback = (FragmentCallback) getActivity();
            callback.onFragmentCallback(this, id, args);
        } else {
            throw new IllegalStateException(
                    "The host activity does not implement FragmentCallback.");
        }
    }

    public void refreshViews() {

    }

    public boolean commitData() {
        return false;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (newFragment != null) {
            newFragment.dismiss();
        }
    }
}