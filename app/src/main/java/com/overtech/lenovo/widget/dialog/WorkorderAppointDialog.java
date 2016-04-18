package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech on 16/4/18.
 * 用于预约的对话框管理
 */
public class WorkorderAppointDialog extends DialogFragment {
    public static final int MAIN_ACTIVITY = 0;
    public static final int DETAIL_ACTIVITY = 1;
    private static int type;


    public static WorkorderAppointDialog newInstance(int t) {
        type = t;
        Bundle arg = new Bundle();
        WorkorderAppointDialog fragment = new WorkorderAppointDialog();
        fragment.setArguments(arg);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_workorder_appoint, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(v)
                .setTitle("请选择您的上门时间")
                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderAppointDialog.MAIN_ACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doAppointNegativeClick();
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doAppointNegativeClick();
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderAppointDialog.MAIN_ACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doAppointPositiveClick();
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doAppointPositiveClick();
                        }
                    }
                });

        return builder.create();
    }
}
