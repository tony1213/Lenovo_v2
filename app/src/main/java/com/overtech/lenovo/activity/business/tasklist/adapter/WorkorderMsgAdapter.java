package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.tasklist.WorkorderMsgActivity;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Overtech on 16/4/29.
 */
public class WorkorderMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Task> data;
    private Context ctx;
    public static final int NORMAL = 0;
    public static final int LOADING_MORE_FOOTER = 1;
    public static final int LOADING = 0x0;
    public static final int RELAX = 0x1;
    public static final int NODATA=0x2;
    public int curLoadState=-1;

    public WorkorderMsgAdapter(List<Task> data, Context ctx) {
        this.data = data;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL) {
            NormalViewHolder viewHolder = new NormalViewHolder(((WorkorderMsgActivity) ctx).getLayoutInflater().inflate(R.layout.item_recyclerview_workorder_msg, parent, false));
            return viewHolder;
        } else {
            LoadingMoreHolder viewHolder = new LoadingMoreHolder(((WorkorderMsgActivity) ctx).getLayoutInflater().inflate(R.layout.loading_more_footerview, parent, false));
            return viewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LOADING_MORE_FOOTER;
        } else {
            return NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            Task task = data.get(position);
            ((NormalViewHolder) holder).title.setText(task.title);
            ((NormalViewHolder) holder).content.setText(task.content);
            ((NormalViewHolder) holder).username.setText(task.username);
            ((NormalViewHolder) holder).datetime.setText(task.datetime);
        } else if (holder instanceof LoadingMoreHolder) {
            if (curLoadState == LOADING) {
                ((LoadingMoreHolder) holder).tvLoadingMsg.setText("正在加载中...");
            } else if (curLoadState == RELAX) {
                ((LoadingMoreHolder) holder).tvLoadingMsg.setText("上拉加载更多...");
            }else if(curLoadState==NODATA){
                (  (LoadingMoreHolder)holder).tvLoadingMsg.setText("没有更多数据");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size() + 1;
        }
    }
    public void addMore(List<Task> newData){
        if(newData!=null){
            int preLastNormalPosition=data.size()-1;
            data.addAll(newData);
            notifyItemRangeInserted(preLastNormalPosition,newData.size());
        }
    }
    public void pulldownFresh(List<Task> newData){
        if(newData!=null){
            this.data=newData;
            notifyDataSetChanged();
        }
    }
    public void changeLoadingState(int curLoadState){
        this.curLoadState=curLoadState;
        notifyItemChanged(getItemCount()-1);
    }
    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView datetime;
        public TextView username;
        public TextView content;

        public NormalViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_msg_title);
            content = (TextView) itemView.findViewById(R.id.tv_msg_content);
            datetime = (TextView) itemView.findViewById(R.id.tv_msg_datetime);
            username = (TextView) itemView.findViewById(R.id.tv_msg_username);
        }
    }

    public class LoadingMoreHolder extends RecyclerView.ViewHolder {
        public TextView tvLoadingMsg;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            tvLoadingMsg = (TextView) itemView.findViewById(R.id.tv_footerview_loading);
        }
    }
}
