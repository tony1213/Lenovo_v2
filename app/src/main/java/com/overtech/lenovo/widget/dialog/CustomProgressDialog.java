package com.overtech.lenovo.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.debug.Logger;

/**
 * 按照google推荐的dialog自定义加载框
 */
public class CustomProgressDialog extends DialogFragment {
    private Dialog dialog;
    private ImageView imageView;
    private TextView tvMsg;
    private AnimationDrawable animationDrawable;
    private static String msg;

    public static CustomProgressDialog newInstance(String content) {
        msg = content;
        CustomProgressDialog progressDialog = new CustomProgressDialog();
        Bundle args = new Bundle();
        args.putInt("num", 0);
        progressDialog.setArguments(args);
        return progressDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Logger.e("onCreateView执行了");
        View customView = inflater.inflate(R.layout.layout_custom_progress_dialog, container, false);
        if (customView == null) {
//            Logger.e("CustomeFragement的对象为空");
        } else {
//            Logger.e("CustomeFragment的对象不为空");
        }
        imageView = (ImageView) customView.findViewById(R.id.iv_progress_loading);
        if (imageView == null) {
            Logger.e("ImageView 对象获取失败");
        } else {
//            Logger.e("ImageView 对象获取成功");
        }

        tvMsg = (TextView) customView.findViewById(R.id.tv_msg_loading);
        if (tvMsg == null) {
//            Logger.e("tvMsg获取对象失败 ");
        } else {
//            Logger.e("tvMsg获取对象成功");
        }
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        return customView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Logger.e("onCreateDialog执行了");
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Logger.e("onCreate执行了");
        setStyle(STYLE_NO_TITLE, R.style.CustomProgressDialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        Logger.e("onActivityCreated执行了");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
//        Logger.e("onAttach执行了");
        super.onAttach(activity);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//        Logger.e("onInflate执行了");
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onResume() {
//        Logger.e("onResume执行了");
        super.onResume();
    }

    @Override
    public void onStart() {
//        Logger.e("onStart执行了");
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        Logger.e("onViewCreated执行了");
        if (msg != null) {
            tvMsg.setText(msg);
        } else {
            tvMsg.setText("加载中...");
        }
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        super.onViewCreated(view, savedInstanceState);
    }

}