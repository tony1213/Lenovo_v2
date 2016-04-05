package com.overtech.lenovo.activity.business.common.password;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;

/*
*@author Tony
*@description 重置密码
*/
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitleContent;
    private Button mDoNext;
    private EditTextWithDelete mNewPassword;
    private EditTextWithDelete mNewPasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }
    private void init(){
        mTitleContent = (TextView) findViewById(R.id.tv_common_title_content);
        mTitleContent.setText(R.string.commom_title_reset_password);
        mNewPassword=(EditTextWithDelete)findViewById(R.id.et_reset_password_new);
        mNewPasswordConfirm=(EditTextWithDelete)findViewById(R.id.et_reset_password_confirm);
        mDoNext=(Button)findViewById(R.id.btn_reset_password);
        mDoNext.setOnClickListener(this);
    }
    private void valicatePassword(){
        String sNewPassword=mNewPassword.getText().toString().trim();
        String sNewPasswordConfirm=mNewPasswordConfirm.getText().toString().trim();
        if (!(TextUtils.equals("",sNewPassword)||TextUtils.equals("",sNewPasswordConfirm))){
            if ((sNewPassword.length()>=6&&sNewPassword.length()<=18)&&(sNewPasswordConfirm.length()>=6&&sNewPasswordConfirm.length()<=18)){
                if (TextUtils.equals(sNewPassword,sNewPasswordConfirm)){
                    Intent intent=new Intent(this,ResetPasswordSuccessActivity.class);
                    startActivity(intent);
                }else {
                    Utilities.showToast("两次密码输入不相同",this);
                }
            }else {
               Utilities.showToast("密码长度为6—18位",this);
            }
        }else {
            Utilities.showToast("输入不能为空",this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_common_title_back:
                this.finish();
                break;
            case R.id.btn_reset_password:
                valicatePassword();
                break;
        }
    }
}
