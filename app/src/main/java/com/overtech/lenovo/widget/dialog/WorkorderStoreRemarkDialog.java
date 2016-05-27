package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.ViewGroup;

import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech on 16/4/28.
 */
public class WorkorderStoreRemarkDialog extends DialogFragment {
    public static WorkorderStoreRemarkDialog newInstance() {
        WorkorderStoreRemarkDialog fragment = new WorkorderStoreRemarkDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatEditText editText = new AppCompatEditText(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        editText.setHint("门店备注");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("说说")
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TaskDetailActivity) getActivity()).storeInfoFrag.doPositiveClick(editText.getText().toString().trim());
                    }
                });

        return builder.create();
    }
}
