package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech Will on 16/4/15.
 */
public class WorkorderReceiveDialog extends DialogFragment {
    /**
     * 从mainactivty的tasklistfragment中接单
     */
    public static final int MAINACTIVITY = 0;
    /**
     * 从detailactivity的taskinformation中接单
     */
    public static final int DETAILACTIVITY = 1;
    private static int type;
    private static int position;

    public static WorkorderReceiveDialog newInstance(int t, int p) {
        type = t;
        position = p;
        Bundle args = new Bundle();

        WorkorderReceiveDialog fragment = new WorkorderReceiveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static WorkorderReceiveDialog newInstance(int t) {
        type = t;
        Bundle args = new Bundle();

        WorkorderReceiveDialog fragment = new WorkorderReceiveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("你确定要接此单？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderReceiveDialog.MAINACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doReceiveNegativeClick(position);
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doReceiveNegativeClick();
                        }
                    }
                })
                .setPositiveButton("接单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderReceiveDialog.MAINACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doReceivePositiveClick(position);
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doReceivePositiveClick();
                        }
                    }
                });

        return builder.create();
    }
}
