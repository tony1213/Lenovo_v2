package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech on 16/4/18.
 * 用于到场的对话框管理
 */
public class WorkorderHomeDialog extends DialogFragment {
    public static final int MAIN_ACTIVITY=0;
    public static final int DETAIL_ACTIVITY=1;
    private static int type;
    public static WorkorderHomeDialog newInstance(int t){
        type=t;
        Bundle args=new Bundle();
        WorkorderHomeDialog fragment=new WorkorderHomeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder
                .setTitle("提示")
                .setMessage("你已经到场了吗？")
                .setNegativeButton("已到场", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type== WorkorderHomeDialog.MAIN_ACTIVITY){
                            ((MainActivity)getActivity()).taskListFragment.doHomeNegativeClick();
                        }else{
                            ((TaskDetailActivity)getActivity()).taskInfoFrag.doHomeNegativeClick();
                        }
                    }
                })
                .setPositiveButton("尚未到场", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==WorkorderHomeDialog.MAIN_ACTIVITY){
                            ((MainActivity)getActivity()).taskListFragment.doHomePositiveClick();
                        }else{
                            ((TaskDetailActivity)getActivity()).taskInfoFrag.doHomePositiveClick();;
                        }
                    }
                });
        return builder.create();
    }
}
