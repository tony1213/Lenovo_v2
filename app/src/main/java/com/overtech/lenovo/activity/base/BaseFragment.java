package com.overtech.lenovo.activity.base;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.fragment.callback.FragmentCallback;
import com.overtech.lenovo.activity.fragment.callback.FragmentInterface;
import com.overtech.lenovo.config.Debug;
import android.view.View.OnTouchListener;
import com.overtech.lenovo.widget.progressdialog.CustomProgressDialog;


public abstract class BaseFragment extends Fragment implements
        FragmentInterface, OnTouchListener {
    protected View mRootView;
    public CustomProgressDialog progressDialog;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        Debug.log("BaseFragment==onCreateView", "执行到这了");
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog
                        .createDialog(getActivity());
            }
        }
    }

    /**
     * 打开对话框
     */
    public void startProgress(String content) {
        progressDialog.setMessage(content);
        progressDialog.show();
        Debug.log("startProgress====showing", progressDialog.isShowing() + ""
                + progressDialog);

        imageView = (ImageView) progressDialog
                .findViewById(R.id.loadingImageView);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * 关闭对话框
     */
    public void stopProgress() {
        Debug.log("stopProgress====showing", progressDialog.isShowing() + ""
                + progressDialog);
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
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
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}