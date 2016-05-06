package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.tasklist.WorkorderMsgActivity;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;

import java.util.List;

/**
 * Created by Overtech on 16/4/29.
 */
public class WorkorderMsgAdapter extends RecyclerView.Adapter<WorkorderMsgAdapter.MyViewHolder> {
    private List<Task> data;
    private Context ctx;

    public WorkorderMsgAdapter(List<Task> data, Context ctx) {
        this.data = data;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(((WorkorderMsgActivity) ctx).getLayoutInflater().inflate(R.layout.item_recyclerview_workorder_msg, parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task=data.get(position);
        holder.title.setText(task.title);
        holder.content.setText(task.content);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public MyViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.tv_msg_title);
            content= (TextView) itemView.findViewById(R.id.tv_msg_content);
        }
    }
}
