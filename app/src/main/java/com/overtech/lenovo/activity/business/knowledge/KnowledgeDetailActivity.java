package com.overtech.lenovo.activity.business.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.knowledge.Knowledges;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;

import java.io.IOException;

public class KnowledgeDetailActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private ProgressBar bar;
    private ImageView mDoBack;
    private ImageView mDoShare;
    private String knowledge_id;
    private String uid;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("后台传过来的数据" + json);
            Knowledges bean = gson.fromJson(json, Knowledges.class);
            int st = bean.st;
            if (st == -2 || st == -1) {
                stopProgress();
                Utilities.showToast(bean.msg, KnowledgeDetailActivity.this);
                SharePreferencesUtils.put(KnowledgeDetailActivity.this, SharedPreferencesKeys.UID, "");
                Intent intent = new Intent(KnowledgeDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, KnowledgeDetailActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, KnowledgeDetailActivity.this);
                    break;
                case StatusCode.KNOWLEDGE_CONTENT_SUCCESS:
                    webView.loadDataWithBaseURL(null, "<html><body>"+bean.body.content+"<body><html>", "text/html", "utf-8", null);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_knowledge_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        knowledge_id = getIntent().getStringExtra("knowledge_id");
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        Logger.e("前面传过来的值是" + knowledge_id);
        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.webViewProgressBar);
        mDoBack = (ImageView) findViewById(R.id.iv_knowledge_detail_back);
        mDoShare = (ImageView) findViewById(R.id.iv_knowledge_detail_share);

        mDoBack.setOnClickListener(this);
        mDoShare.setOnClickListener(this);
        webView.requestFocus();
        webView.setInitialScale(25);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);//启用支持javascript
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setBuiltInZoomControls(true);
        settings.setBlockNetworkImage(false);
        initData();
    }

    private void initData() {
        Requester requester = new Requester();
        requester.uid = uid;
        requester.cmd = 10022;
        requester.body.put("knowledge_id", knowledge_id);
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
                    msg.what = StatusCode.KNOWLEDGE_CONTENT_SUCCESS;
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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_knowledge_detail_share:
                Utilities.showToast("您点击了分享", this);
                break;
            case R.id.iv_knowledge_detail_back:
                finish();
                break;
        }
    }
}
