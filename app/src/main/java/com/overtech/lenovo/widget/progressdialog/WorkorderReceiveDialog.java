package com.overtech.lenovo.widget.progressdialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech on 16/4/15.
 */
public class WorkorderReceiveDialog extends DialogFragment {
    /**
     * 从mainactivty的tasklistfragment中接单
     */
    public static final int MAINACTIVITY=0;
    /**
     * 从detailactivity的taskinformation中接单
     */
    public static final int DETAILACTIVITY=1;
    private static int type;

     public static WorkorderReceiveDialog newInstance(int t) {
        type=t;
        Bundle args = new Bundle();

        WorkorderReceiveDialog fragment = new WorkorderReceiveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("你确定要接此单？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==WorkorderReceiveDialog.MAINACTIVITY){
                            ((MainActivity)getActivity()).taskListFragment.doNegativeClick();
                        }else{
                            ((TaskDetailActivity)getActivity()).taskInfoFrag.doNegativeClick();
                        }
                    }
                })
                .setPositiveButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==WorkorderReceiveDialog.MAINACTIVITY){
                            ((MainActivity)getActivity()).taskListFragment.doPositiveClick();
                        }else{
                            ((TaskDetailActivity)getActivity()).taskInfoFrag.doPositiveClick();
                        }
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==WorkorderReceiveDialog.MAINACTIVITY){
                            ((MainActivity)getActivity()).taskListFragment.doNeutralClick();
                        }else{
                            ((TaskDetailActivity)getActivity()).taskInfoFrag.doNeturalClick();
                        }
                    }
                });
        return builder.create();
    }
}
