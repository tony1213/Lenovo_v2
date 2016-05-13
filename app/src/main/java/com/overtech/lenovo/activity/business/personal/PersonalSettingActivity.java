package com.overtech.lenovo.activity.business.personal;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.activity.business.personal.adapter.PersonalSpinnerAdapter;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.person.Person;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.AppUtils;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.dialog.PersonalBirthdayDialog;
import com.overtech.lenovo.widget.popwindow.DimPopupWindow;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PersonalSettingActivity extends BaseActivity implements OnClickListener {
    private Toolbar toolBar;
    private CollapsingToolbarLayout collapsingLayout;
    private TextView mEditBasic;
    private TextView mEditTec;
    private TextView mEditCa;
    private ImageView ivAvator;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvBirthday;
    private TextView tvCertificateStatus;
    private AppCompatEditText etPhone;
    private AppCompatEditText etQQ;
    private AppCompatEditText etWeChat;
    private AppCompatEditText etEmail;
    private AppCompatSpinner spCity;
    private AppCompatEditText etAddress;
    private AppCompatSpinner spEdu;
    private AppCompatSpinner spEnglish;
    private AppCompatEditText etWorkYears;
    private AppCompatSpinner spIdentity;
    private AppCompatSpinner spIdStyle;
    private AppCompatEditText etIdCard;

    private AppCompatButton btUploadPositive;
    private AppCompatButton btUploadOppositive;
    private AppCompatButton btSaveUpload;
    private AppCompatCheckBox cbIdPositive;
    private AppCompatCheckBox cbIdOpposite;
    private AppCompatImageView ivPositiveIdcard;
    private AppCompatImageView ivOppositiveIdcard;
    private DimPopupWindow dimPopupWindow;
    private int curState;
    /**
     * 打开本地相册的requestcode.
     */
    public final int PHOTO = 0x1;
    /**
     * 打开照相机的requestcode.
     */
    private final int CAMERA = 0x2;
    private File outFile;
    private Uri cameraUri;
    private String idcardPositivePath;
    private String idcardOppositePath;
    private String uid;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("个人设置：" + json);
            Person bean = gson.fromJson(json, Person.class);
            if (bean == null) {
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st == -1 || st == -2) {
                stopProgress();
                Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                SharePreferencesUtils.put(PersonalSettingActivity.this, SharedPreferencesKeys.UID, "");
                Intent intent = new Intent();
                intent.setClass(PersonalSettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                StackManager.getStackManager().popAllActivitys();
                return;
            }
            if (st == 1) {
                stopProgress();
                Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                return;
            }
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                    break;
                case StatusCode.PERSONAL_SETTING_SUCCESS:
                    tvName.setText(bean.body.name);
                    tvBirthday.setText(bean.body.birthday);
                    if (TextUtils.equals(bean.body.sex, "1")) {
                        tvGender.setText("男");
                    } else if (TextUtils.equals(bean.body.sex, "0")) {
                        tvGender.setText("女");
                    }
                    if (bean.body.isCertificated.equals("0")) {
                        tvCertificateStatus.setText("尚未通过认证，请完善信息进行认证");
                    } else if (bean.body.isCertificated.equals("1")) {
                        tvCertificateStatus.setText("通过认证日期：" + bean.body.certificate_datetime);
                    }
                    etPhone.setText(bean.body.mobile);
                    etQQ.setText(bean.body.qq);
                    etWeChat.setText(bean.body.wechat);
                    etEmail.setText(bean.body.email);
                    etAddress.setText(bean.body.address);
                    PersonalSpinnerAdapter adapter = new PersonalSpinnerAdapter(PersonalSettingActivity.this, bean.body.territory_node_path);
                    spCity.setAdapter(adapter);
                    spCity.setPrompt(adapter.getDefault());

                    PersonalSpinnerAdapter adapter1 = new PersonalSpinnerAdapter(PersonalSettingActivity.this, bean.body.degree);
                    spEdu.setAdapter(adapter1);
                    for (Person.Type type : bean.body.degree) {
                        if (type.isDefault.equals("1")) {
                            Logger.e("最高学历" + type.name);
                            spEdu.setSelection(2);
                        }
                    }
                    spEdu.setPrompt(adapter1.getDefault());


                    PersonalSpinnerAdapter adapter2 = new PersonalSpinnerAdapter(PersonalSettingActivity.this, bean.body.english_ability);
                    spEnglish.setAdapter(adapter2);
                    spEnglish.setPrompt(adapter2.getDefault());

                    PersonalSpinnerAdapter adapter3 = new PersonalSpinnerAdapter(PersonalSettingActivity.this, bean.body.self_orientation);
                    spIdentity.setAdapter(adapter3);
                    spIdentity.setPrompt(adapter3.getDefault());


                    PersonalSpinnerAdapter adapter4 = new PersonalSpinnerAdapter(PersonalSettingActivity.this, bean.body.type_of_id);
                    spIdStyle.setAdapter(adapter4);
                    spIdStyle.setPrompt(adapter4.getDefault());

                    etWorkYears.setText(bean.body.working_life);
                    etIdCard.setText(bean.body.idcard);
                    ImageLoader.getInstance().displayImage(bean.body.avator, ivAvator, R.mipmap.icon_avator_default, R.mipmap.icon_common_default_error, new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            return ImageCacheUtils.toRoundBitmap(source);
                        }

                        @Override
                        public String key() {
                            return "personalsetting";
                        }
                    }, Bitmap.Config.RGB_565);
                    ImageLoader.getInstance().displayImage(bean.body.positive_identity_card, ivPositiveIdcard);
                    ImageLoader.getInstance().displayImage(bean.body.opposite_identity_card, ivOppositiveIdcard);
                    break;
                case StatusCode.PERSONAL_SETTING_UPDATE_SUCCESS:
                    Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                    if (bean.body.isCertificated.equals("0")) {
                        tvCertificateStatus.setText("尚未通过认证，请完善信息进行认证");
                    } else if (bean.body.isCertificated.equals("1")) {
                        tvCertificateStatus.setText("通过认证时间：" + bean.body.certificate_datetime);
                    }
                    etPhone.setEnabled(false);
                    etQQ.setEnabled(false);
                    etWeChat.setEnabled(false);
                    etEmail.setEnabled(false);
                    spCity.setEnabled(false);
                    etAddress.setEnabled(false);
                    spEdu.setEnabled(false);
                    spEnglish.setEnabled(false);
                    etWorkYears.setEnabled(false);
                    spIdentity.setEnabled(false);
                    spIdStyle.setEnabled(false);
                    etIdCard.setEnabled(false);
                    break;
                case StatusCode.PERSONAL_SETTING_UPLOAD_ID_POSITIVE:
                    if (st == 0) {
                        Logger.e("正面成功");
                        Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                        cbIdPositive.setVisibility(View.VISIBLE);
                    }
                    break;
                case StatusCode.PERSONAL_SETTING_UPLOAD_ID_OPPOSITE:
                    if (st == 0) {
                        Logger.e("反面成功");
                        Utilities.showToast(bean.msg, PersonalSettingActivity.this);
                        cbIdOpposite.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_personalsetting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        collapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mEditBasic = (TextView) findViewById(R.id.tv_edit_basic);
        mEditTec = (TextView) findViewById(R.id.tv_edit_tec);
        mEditCa = (TextView) findViewById(R.id.tv_edit_ca);

        ivAvator = (ImageView) findViewById(R.id.iv_avator);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        tvCertificateStatus = (TextView) findViewById(R.id.tv_certificated_status);
        etPhone = (AppCompatEditText) findViewById(R.id.et_personal_phone);
        etQQ = (AppCompatEditText) findViewById(R.id.et_personal_qq);
        etWeChat = (AppCompatEditText) findViewById(R.id.et_personal_wechat);
        etEmail = (AppCompatEditText) findViewById(R.id.et_personal_email);
        spCity = (AppCompatSpinner) findViewById(R.id.sp_personal_city);
        etAddress = (AppCompatEditText) findViewById(R.id.et_personal_address);

        spEdu = (AppCompatSpinner) findViewById(R.id.sp_personal_edu);
        spEnglish = (AppCompatSpinner) findViewById(R.id.sp_personal_english);
        etWorkYears = (AppCompatEditText) findViewById(R.id.et_personal_work_years);
        spIdentity = (AppCompatSpinner) findViewById(R.id.sp_personal_identity);
        spIdStyle = (AppCompatSpinner) findViewById(R.id.sp_persoanl_id);
        etIdCard = (AppCompatEditText) findViewById(R.id.et_personal_idcard);

        btUploadPositive = (AppCompatButton) findViewById(R.id.bt_upload_idcard_positive);
        btUploadOppositive = (AppCompatButton) findViewById(R.id.bt_upload_idcard_opposite);
        btSaveUpload = (AppCompatButton) findViewById(R.id.bt_save_upload);
        ivPositiveIdcard = (AppCompatImageView) findViewById(R.id.iv_positive_idcard);
        ivOppositiveIdcard = (AppCompatImageView) findViewById(R.id.iv_oppositive_idcard);
        cbIdPositive = (AppCompatCheckBox) findViewById(R.id.cb_idcard_positive_upload_success);
        cbIdOpposite = (AppCompatCheckBox) findViewById(R.id.cb_idcard_opposite_upload_success);
        mEditBasic.setOnClickListener(this);
        mEditTec.setOnClickListener(this);
        mEditCa.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        btUploadPositive.setOnClickListener(this);
        btUploadOppositive.setOnClickListener(this);
        btSaveUpload.setOnClickListener(this);

        setSupportActionBar(toolBar);//将toolbar设置成actionbar，清单文件中目前使用的是noactionbar 主题，如果改变后，此处必然会崩掉
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(0);//设置返回小图标
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(this);
        collapsingLayout.setTitle("基本信息");

        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        startLoading();
    }

    private void startLoading() {
        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10011;
        requester.uid = uid;
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
                    msg.what = StatusCode.PERSONAL_SETTING_SUCCESS;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case Toolbar.NO_ID:
                finish();
                break;
            case R.id.tv_edit_basic:
                etPhone.setEnabled(true);
                etQQ.setEnabled(true);
                etWeChat.setEnabled(true);
                etEmail.setEnabled(true);
                spCity.setEnabled(true);
                etAddress.setEnabled(true);
                break;
            case R.id.tv_edit_tec:
                spEdu.setEnabled(true);
                spEnglish.setEnabled(true);
                etWorkYears.setEnabled(true);
                spIdentity.setEnabled(true);
                break;
            case R.id.tv_edit_ca:
                spIdStyle.setEnabled(true);
                etIdCard.setEnabled(true);
                break;
            case R.id.tv_birthday:
//                因生日可以根据身份证获取，此处可不需要
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                Fragment pre = getSupportFragmentManager().findFragmentByTag("personal_birthday");
//                if (pre != null) {
//                    ft.remove(pre);
//                }
//                ft.addToBackStack(null);
//                PersonalBirthdayDialog birthdayDialog = PersonalBirthdayDialog.newInstance();
//                birthdayDialog.show(ft, "personal_birthday");
                break;
            case R.id.bt_upload_idcard_positive:
                curState = 0;
                showPopupWindow();
                break;
            case R.id.bt_upload_idcard_opposite:
                curState = 1;
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
            case R.id.bt_save_upload:
                String id = etIdCard.getText().toString().trim();
                if (!AppUtils.IDCardValidate(id)) {
                    Utilities.showToast("身份证号码不合法", this);
                    return;
                }
                startProgress("正在上传");
                Requester requester = new Requester();
                requester.uid = uid;
                requester.cmd = 10012;
                requester.body.put("name", tvName.getText().toString().trim());
                if (tvGender.getText().toString().trim().equals("男")) {
                    requester.body.put("sex", "1");
                } else {
                    requester.body.put("sex", "0");
                }
                requester.body.put("birthday", tvBirthday.getText().toString().trim());
                requester.body.put("mobile", etPhone.getText().toString().trim());
                requester.body.put("qq", etQQ.getText().toString().trim());
                requester.body.put("wechat", etWeChat.getText().toString().trim());
                requester.body.put("email", etEmail.getText().toString().trim());
                requester.body.put("city_id", spCity.getSelectedItem()==null?"":spCity.getSelectedItem().toString().trim());
                requester.body.put("address", etAddress.getText().toString().trim());
                requester.body.put("degree_id", spEdu.getSelectedItem().toString().trim());
                requester.body.put("english_id", spEnglish.getSelectedItem().toString().trim());
                requester.body.put("working_life", etWorkYears.getText().toString().trim());
                requester.body.put("self_orientate_id", spIdentity.getSelectedItem().toString().trim());
                requester.body.put("idcard_type_id", spIdStyle.getSelectedItem().toString().trim());
                requester.body.put("idcard", id);
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
                            msg.what = StatusCode.PERSONAL_SETTING_UPDATE_SUCCESS;
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

                break;
        }
    }

    /**
     * 获取拍照后的图片的路径
     */
    private String getPhotoPath(Uri imageUri) {
        ContentResolver resolver = getContentResolver();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(imageUri, proj, null, null, null);
        int columIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToNext()) {
            return cursor.getString(columIndex);
        }
        return null;
    }

    private void openPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (curState == 0) {
            outFile = new File(dir, "positiveIdcard" + ".jpg");
        } else if (curState == 1) {
            outFile = new File(dir, "oppositeIdcard" + ".jpg");
        }

        cameraUri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
        startActivityForResult(intent, CAMERA);
    }

    private void showPopupWindow() {
        if (dimPopupWindow == null) {
            dimPopupWindow = new DimPopupWindow(this);
            View view = getLayoutInflater().inflate(R.layout.layout_dim_pop_add_idcard, null);
            Button camera = (Button) view.findViewById(R.id.bt_select_from_camera);
            Button photo = (Button) view.findViewById(R.id.bt_select_from_photo);
            Button cancle = (Button) view.findViewById(R.id.bt_select_none);
            camera.setOnClickListener(this);
            photo.setOnClickListener(this);
            cancle.setOnClickListener(this);
            dimPopupWindow.setContentView(view);
            dimPopupWindow.setInAnimation(R.anim.register_add_idcard_in);
        }
        dimPopupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, getResources().getDimensionPixelOffset(getResources().getIdentifier("navigation_bar_height", "dimen", "android")));
    }

    public void doNegativeClick(String selectTime) {
        tvBirthday.setText(selectTime);
    }

    public void doPositiveClick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("内存卡错误", "请检查您的内存卡");
            return;
        }
        switch (requestCode) {
            case CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (curState == 0) {
                        idcardPositivePath = outFile.getAbsolutePath();
                        Logger.e("正面路径" + idcardPositivePath);
                        if (idcardPositivePath != null) {
                            String[] strings = idcardPositivePath.split("\\.");
                            startUploadIdPositive(idcardPositivePath, strings[strings.length - 1]);
                            ivPositiveIdcard.setImageBitmap(BitmapFactory.decodeFile(idcardPositivePath));
                        } else {
                            Utilities.showToast("获取相机图片失败，请重新尝试或使用相册", this);
                        }
                    } else if (curState == 1) {
                        idcardOppositePath = outFile.getAbsolutePath();
                        Logger.e("反面路径" + idcardOppositePath);
                        if (idcardOppositePath != null) {
                            String[] strings = idcardOppositePath.split("\\.");
                            startUploadIdOpposite(idcardOppositePath, strings[strings.length - 1]);
                            ivOppositiveIdcard.setImageBitmap(BitmapFactory.decodeFile(idcardOppositePath));
                        } else {
                            Utilities.showToast("获取相机图片失败，请重新尝试或使用相册", this);
                        }
                    }
                }
                break;
            case PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (curState == 0) {
                        idcardPositivePath = getPhotoPath(data.getData());
                        Logger.e("正面路径" + idcardPositivePath);
                        if (idcardPositivePath != null) {
                            String[] strings = idcardPositivePath.split("\\.");
                            startUploadIdPositive(idcardPositivePath, strings[strings.length - 1]);
                            ivPositiveIdcard.setImageBitmap(BitmapFactory.decodeFile(idcardPositivePath));
                        } else {
                            Utilities.showToast("获取相册图片失败,请重新尝试或使用相机", this);
                        }
                    } else if (curState == 1) {
                        idcardOppositePath = getPhotoPath(data.getData());
                        Logger.e("反面路径" + idcardPositivePath);
                        if (idcardOppositePath != null) {
                            String[] strings = idcardOppositePath.split("\\.");
                            startUploadIdOpposite(idcardOppositePath, strings[strings.length - 1]);
                            ivOppositiveIdcard.setImageBitmap(BitmapFactory.decodeFile(idcardOppositePath));
                        } else {
                            Utilities.showToast("获取相册图片失败，请重新尝试或使用相机", this);
                        }
                    }
                }
                break;
        }
    }

    private void startUploadIdPositive(String path, String name) {
        startProgress("正在上传");
        String fileStr = "";
        try {
            FileInputStream fis = new FileInputStream(path);
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
        requester.body.put("type", "1");
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
                    msg.what = StatusCode.PERSONAL_SETTING_UPLOAD_ID_POSITIVE;
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

    private void startUploadIdOpposite(String path, String name) {
        startProgress("正在上传");
        String fileStr = "";
        try {
            FileInputStream fis = new FileInputStream(path);
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
        requester.body.put("type", "2");
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
                    msg.what = StatusCode.PERSONAL_SETTING_UPLOAD_ID_OPPOSITE;
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
}
