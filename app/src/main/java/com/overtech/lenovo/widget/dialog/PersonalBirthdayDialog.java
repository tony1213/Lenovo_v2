package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.personal.PersonalSettingActivity;

/**
 * Created by Overtech Will on 16/4/25.
 * 个人设置里面，选择生日的对话框
 */
public class PersonalBirthdayDialog extends DialogFragment {
    private DatePicker datePicker;

    public static PersonalBirthdayDialog newInstance() {
        PersonalBirthdayDialog frag = new PersonalBirthdayDialog();
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_personal_birthday, null);
        datePicker = (DatePicker) view.findViewById(R.id.dialog_personal_birthday_datepicker);
        builder
                .setTitle("请选择您的生日")
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        ((PersonalSettingActivity) getActivity()).doPositiveClick(year + "-" + month + "-" + day);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
