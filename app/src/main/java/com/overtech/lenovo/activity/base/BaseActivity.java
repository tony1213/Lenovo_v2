package com.overtech.lenovo.activity.base;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.widget.dialogeffects.NiftyDialogBuilder;
import com.overtech.lenovo.widget.progressdialog.CustomProgressDialog;


public abstract class BaseActivity extends AppCompatActivity {
    public NiftyDialogBuilder dialogBuilder;
    public CustomProgressDialog progressDialog;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    public HttpEngine httpEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpEngine = HttpEngine.getInstance();
        httpEngine.initContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutIds());
        afterCreate(savedInstanceState);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(progressDialog==null){
            progressDialog=CustomProgressDialog.createDialog(this);
            imageView = (ImageView) progressDialog.findViewById(R.id.loadingImageView);
            animationDrawable = (AnimationDrawable) imageView.getBackground();
        }
    }
    public void startProgress(String content){
        progressDialog.setMessage(content);
        progressDialog.show();
        animationDrawable.start();
    }
    protected abstract int getLayoutIds();

    protected abstract void afterCreate(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dialogBuilder.isShowing()) {
            if(animationDrawable.isRunning()){
                animationDrawable.stop();
            }
            dialogBuilder.dismiss();
        }
    }
}
