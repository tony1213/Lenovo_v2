package com.overtech.lenovo.activity.business.knowledge;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.Utilities;

public class KnowledgeDetailActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private ProgressBar bar;
    private ImageView mDoBack;
    private ImageView mDoShare;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_knowledge_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.webViewProgressBar);
        mDoBack = (ImageView) findViewById(R.id.iv_knowledge_detail_back);
        mDoShare = (ImageView) findViewById(R.id.iv_knowledge_detail_share);

        mDoBack.setOnClickListener(this);
        mDoShare.setOnClickListener(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//启用支持javascript
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        initUrl();
    }

    private void initUrl() {
        String url = "http://m.ly.com/scenery/index.html";
        webView.loadUrl(url);
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
