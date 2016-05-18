package com.overtech.lenovo.activity.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.personal.PersonalAccountDetailActivity;
import com.overtech.lenovo.activity.business.personal.PersonalAccountServerDetailActivity;
import com.overtech.lenovo.activity.business.personal.PersonalSettingActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.person.Person;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.popwindow.DimPopupWindow;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mAvator;
    private TextView tv_finance;
    private TextView tv_month_workorder_amount;
    private TextView tv_year_workorder_amount;
    private LinearLayout mAccountDetail;
    private LinearLayout mAccountServerDetail;
    private DimPopupWindow dimPopupWindow;
    private RatingBar rb_satisfaction;
    private LinearLayout setting;
    private String uid;
    public final int CAMERA = 0x1;
    public final int PHOTO = 0x2;
    private File camera;
    private Uri cameraUri;
    private String avatorPath;
    private UIHandler uiHandler = new UIHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("personal====" + json);
            Person bean = gson.fromJson(json, Person.class);
            int st = bean.st;
            if (st == -1 || st == -2) {
                if (st == -2 || st == -1) {
                    stopProgress();
                    SharePreferencesUtils.put(getActivity(), SharedPreferencesKeys.UID, "");
                    Utilities.showToast(bean.msg, getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
                case StatusCode.PERSONAL_SUCCESS:
                    ImageLoader.getInstance().displayImage(bean.body.avator, mAvator,
                            R.mipmap.icon_avator_default, R.mipmap.ic_launcher,
                            new Transformation() {

                                @Override
                                public Bitmap transform(Bitmap source) {
                                    // TODO Auto-generated method stub
                                    return ImageCacheUtils.toRoundBitmap(source);
                                }

                                @Override
                                public String key() {
                                    // TODO Auto-generated method stub
                                    return "personal fragment";
                                }
                            }, Config.RGB_565);
                    tv_finance.setText(bean.body.finance);
                    tv_month_workorder_amount.setText(bean.body.month_workorder_amount + "单");
                    tv_year_workorder_amount.setText(bean.body.year_workorder_amount + "单");
                    if (bean.body.satisfaction != null)
                        rb_satisfaction.setRating(Float.parseFloat(bean.body.satisfaction));
                    break;
                case StatusCode.PERSONAL_UPLOAD_AVATOR_SUCCESS:
                    Utilities.showToast(bean.msg, getActivity());
                    break;
            }
            swipeRefreshLayout.setRefreshing(false);
            stopProgress();
        }
    };


    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_personal;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mAvator = (ImageView) mRootView.findViewById(R.id.iv_avator);
        tv_finance = (TextView) mRootView.findViewById(R.id.tv_finance);
        tv_month_workorder_amount = (TextView) mRootView.findViewById(R.id.tv_month_workorder_amount);
        tv_year_workorder_amount = (TextView) mRootView.findViewById(R.id.tv_year_workorder_amount);
        mAccountDetail = (LinearLayout) mRootView.findViewById(R.id.ll_account_detail);
        mAccountServerDetail = (LinearLayout) mRootView.findViewById(R.id.ll_account_server_detail);
        rb_satisfaction = (RatingBar) mRootView.findViewById(R.id.rb_satisfaction);
        setting = (LinearLayout) mRootView.findViewById(R.id.ll_personal_setting);
        swipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefresh);
        uid = ((MainActivity) getActivity()).getUid();

        swipeRefreshLayout.setColorSchemeColors(R.array.material_colors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });
        mAvator.setOnClickListener(this);
        mAccountDetail.setOnClickListener(this);
        mAccountServerDetail.setOnClickListener(this);
        setting.setOnClickListener(this);
        startProgress("加载中");
        startLoading();
    }

    private void startLoading() {
        Requester requester = new Requester();
        requester.cmd = 10010;
        requester.uid = uid;
        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
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
                    msg.what = StatusCode.PERSONAL_SUCCESS;
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
        // TODO Auto-generated method stub
        ((MainActivity) getActivity()).getSupportActionBar().hide();
//		ActionBar actionBar = ((MainActivity) getActivity())
//				.getSupportActionBar();
//		actionBar.setTitle("我的");
//		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_main);
//		toolbar.setNavigationIcon(R.drawable.icon_tab_personal_selected);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_personal_setting:

                Intent intent = new Intent(getActivity(),
                        PersonalSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_account_detail:
                Intent intent2 = new Intent(getActivity(), PersonalAccountDetailActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_account_server_detail:
                Intent intent3 = new Intent(getActivity(), PersonalAccountServerDetailActivity.class);
                startActivity(intent3);
                break;
            case R.id.iv_avator:
                showPopupWindow();
                break;
            case R.id.bt_select_from_camera:
                openCamera();
                dimPopupWindow.dismiss();
                break;
            case R.id.bt_select_from_photo:
                openPhoto();
                dimPopupWindow.dismiss();
                break;
            case R.id.bt_select_none:
                dimPopupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    private void openPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        camera = new File(dir, "avator" + ".jpg");

        cameraUri = Uri.fromFile(camera);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Utilities.showToast("请检查内存卡", getActivity());
            return;
        }
        switch (requestCode) {
            case CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    avatorPath = camera.getAbsolutePath();
                    String[] strings = avatorPath.split("\\.");
                    startUploadAvator(avatorPath, strings[strings.length - 1]);
                    Bitmap bitmap = BitmapFactory.decodeFile(avatorPath);
                    mAvator.setImageBitmap(ImageCacheUtils.toRoundBitmap(bitmap));
                }
                break;
            case PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    avatorPath = getPhoto(data.getData());
                    if (avatorPath != null) {
                        String[] strings = avatorPath.split("\\.");
                        startUploadAvator(avatorPath, strings[strings.length - 1]);
                        Bitmap bitmap = BitmapFactory.decodeFile(avatorPath);
                        mAvator.setImageBitmap(ImageCacheUtils.toRoundBitmap(bitmap));
                    } else {
                        Utilities.showToast("获取相册图片失败，请重新尝试或使用相机", getActivity());
                    }
                }
                break;
        }
    }

    private void startUploadAvator(String avatorPath, String name) {
        startProgress("正在上传");
        String fileStr = "";
        try {
            FileInputStream fis = new FileInputStream(avatorPath);
            byte[] buffer = new byte[fis.available()];
            while (fis.read(buffer) != -1) {
                fileStr += Base64.encodeToString(buffer, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Requester requester = new Requester();
        requester.cmd = 10013;
        requester.uid = uid;
        requester.body.put("type", "3");
        requester.body.put("content", fileStr);
        requester.body.put("name", name);
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message msg = uiHandler.obtainMessage();
                RequestExceptBean bean = new RequestExceptBean();
                bean.st = 0;
                bean.msg = "网络异常";
                msg.what = StatusCode.FAILED;
                msg.obj = gson.toJson(bean);
                uiHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    msg.what = StatusCode.PERSONAL_UPLOAD_AVATOR_SUCCESS;
                    msg.obj = response.body().string();
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

    private String getPhoto(Uri data) {
        ContentResolver resolver = getActivity().getContentResolver();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(data, proj, null, null, null);
        int columIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToNext()) {
            return cursor.getString(columIndex);
        }
        return null;
    }

    private void showPopupWindow() {
        if (dimPopupWindow == null) {
            dimPopupWindow = new DimPopupWindow(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dim_pop_add_idcard, null);
            Button camera = (Button) view.findViewById(R.id.bt_select_from_camera);
            Button photo = (Button) view.findViewById(R.id.bt_select_from_photo);
            Button cancle = (Button) view.findViewById(R.id.bt_select_none);
            camera.setOnClickListener(this);
            photo.setOnClickListener(this);
            cancle.setOnClickListener(this);
            dimPopupWindow.setContentView(view);
            dimPopupWindow.setInAnimation(R.anim.register_add_idcard_in);
        }
        dimPopupWindow.showAtLocation(getActivity().getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, getResources().getDimensionPixelOffset(getResources().getIdentifier("navigation_bar_height", "dimen", "android")));
    }

}
