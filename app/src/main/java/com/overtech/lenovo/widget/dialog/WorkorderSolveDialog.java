package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;
import com.overtech.lenovo.utils.Utilities;

import java.lang.reflect.Field;

/**
 * Created by Overtech on 16/4/21.
 */
public class WorkorderSolveDialog extends DialogFragment {
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
    private TextInputEditText etIssueDescription;
    private TextInputEditText etSolve;

    public static WorkorderSolveDialog newInstance(int t, int p) {
        type = t;
        position = p;
        Bundle args = new Bundle();
        WorkorderSolveDialog fragment = new WorkorderSolveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static WorkorderSolveDialog newInstance(int t) {
        type = t;
        Bundle args = new Bundle();
        WorkorderSolveDialog fragment = new WorkorderSolveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_workorder_solve, null);
        etIssueDescription = (TextInputEditText) v.findViewById(R.id.et_workorder_issue_description);
        etSolve = (TextInputEditText) v.findViewById(R.id.et_workorder_solve);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("提示")
                .setView(v)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderSolveDialog.MAINACTIVITY) {

                        } else {
                            String des = etIssueDescription.getText().toString().trim();
                            String sol = etSolve.getText().toString().trim();
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doSolveNegativeClick(des, sol);
                        }
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
