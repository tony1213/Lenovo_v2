package com.overtech.lenovo.activity.business.knowledge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class KnowledgeDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView knowledgeContent;
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
                    knowledgeContent.setText(Html.fromHtml(bean.body.content, new UrlImageGetter(knowledgeContent), null));
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
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        knowledgeContent = (TextView) findViewById(R.id.tv_knowledge_content);
        knowledgeContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(0);
        actionBar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


    }

    public class UrlImageGetter implements Html.ImageGetter {
        private TextView container;

        public UrlImageGetter(TextView container) {
            this.container = container;
        }

        @Override
        public Drawable getDrawable(String source) {
            Logger.e("执行到这里了呀==getDrawable()===" + source);

            LevelListDrawable d = new LevelListDrawable();
            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
            new ImageGetterAsyncTask().execute(source, d);
            return d;
        }

        public class ImageGetterAsyncTask extends AsyncTask<Object, Void, Bitmap> {
            private LevelListDrawable mDrawable;

            @Override
            protected Bitmap doInBackground(Object... params) {

                mDrawable = (LevelListDrawable) params[1];
                try {
                    InputStream inputStream = new URL((String) params[0]).openStream();
                    return BitmapFactory.decodeStream(inputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    BitmapDrawable d = new BitmapDrawable(bitmap);
                    mDrawable.addLevel(1, 1, d);
                    mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    mDrawable.setLevel(1);
                }
                CharSequence t = container.getText();
                container.setText(t);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
