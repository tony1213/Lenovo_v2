package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.Task;
import com.overtech.lenovo.picasso.Picasso;

public class TaskListAdapter extends Adapter<TaskListAdapter.MyViewHolder> {
    private Context ctx;
    private List<Task> datas;
    private OnItemClickListener mClickListener;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View headerView;

    public TaskListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 条目的事件点击接口
     *
     * @author Overtech
     */
    public interface OnItemClickListener {
        /**
         * 条目点击事件回调
         */
        void onItemClick(View view, int position);

        /**
         * 条目log点击事件回调
         */
        void onLogItemClick(View view, int position);

        /**
         * 条目按钮点击事件，当前条目就可能一个button，所以目前这样设计
         */
        void onButtonItemClick(View view, int position);
    }

    public void setHeader(View headerView) {
        this.headerView = headerView;
    }

    public void setDatas(List<Task> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return headerView == null ? datas.size() : datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (headerView == null)
            return TYPE_NORMAL;
        if (headerView != null && position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    public int getRealPosition(MyViewHolder holder) {
        return headerView == null ? holder.getLayoutPosition() : holder
                .getLayoutPosition() - 1;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // TODO Auto-generated method stub
        if (getItemViewType(position) == TYPE_HEADER)
            return;
        final int pos = getRealPosition(holder);
        Task task = datas.get(pos);
        if (new Random().nextFloat() > 0.5) {
            Picasso.with(ctx).load(R.mipmap.icon_tasklist_default1)
                    .into(holder.taskLogo);
        } else {
            Picasso.with(ctx).load(R.mipmap.icon_tasklist_default2)
                    .into(holder.taskLogo);
        }
        holder.taskNo.setText(task.getTaskNo());
        holder.taskDate.setText(task.getTaskDate());
        holder.taskClass.setText(task.getTaskClass());
        holder.taskDescription.setText(task.getTaskDescription());
        holder.taskRemark.setText(task.getTaskRemark());
        holder.taskDistance.setText("8km");
        holder.taskVisitTime.setText(task.getTaskVisitTime());
        if (task.getTaskType().equals("接单")) {
            holder.taskReceive.setVisibility(View.VISIBLE);
            holder.taskWaitEvaluate.setVisibility(View.GONE);
        } else if (task.getTaskType().equals("评价")) {
            holder.taskReceive.setVisibility(View.GONE);
            holder.taskWaitEvaluate.setVisibility(View.VISIBLE);
        }
        if (task.getIsUrgent().equals("1")) {
            holder.taskUrgency.setVisibility(View.VISIBLE);
        } else if (task.getIsUrgent().equals("0")) {
            holder.taskUrgency.setVisibility(View.GONE);
        }
        if (mClickListener != null) {
            holder.taskLogo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mClickListener.onLogItemClick(v, pos);
                }
            });
            holder.taskReceive.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mClickListener.onButtonItemClick(v, pos);
                }
            });
            holder.taskItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mClickListener.onItemClick(v, pos);
                }
            });
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO Auto-generated method stub
        if (headerView != null && viewType == TYPE_HEADER) {
            return new MyViewHolder(headerView);
        }
        MyViewHolder vh = new MyViewHolder(LayoutInflater.from(ctx).inflate(
                R.layout.item_recyclerview_tasklist, parent, false));

        return vh;
    }

    class MyViewHolder extends ViewHolder {
        RelativeLayout taskItem;
        ImageView taskLogo;
        TextView taskNo;
        TextView taskDate;
        TextView taskClass;
        TextView taskDescription;
        TextView taskRemark;
        TextView taskDistance;
        TextView taskVisitTime;
        Button taskReceive;
        TextView taskWaitEvaluate;
        ImageView taskUrgency;

        public MyViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            if (view == headerView)
                return;
            taskItem = (RelativeLayout) view.findViewById(R.id.rl_task_item);
            taskLogo = (ImageView) view.findViewById(R.id.iv_task_logo);
            taskNo = (TextView) view.findViewById(R.id.tv_task_no);
            taskDate = (TextView) view.findViewById(R.id.tv_task_date);
            taskClass = (TextView) view.findViewById(R.id.tv_task_class);
            taskDescription = (TextView) view
                    .findViewById(R.id.tv_task_description);
            taskRemark = (TextView) view.findViewById(R.id.tv_task_remark);
            taskDistance = (TextView) view.findViewById(R.id.tv_task_distance);
            taskVisitTime = (TextView) view
                    .findViewById(R.id.tv_task_visit_time);
            taskReceive = (Button) view.findViewById(R.id.bt_task_receive);
            taskWaitEvaluate = (TextView) view
                    .findViewById(R.id.tv_task_wait_evaluate);
            taskUrgency = (ImageView) view.findViewById(R.id.iv_task_urgency);
        }

    }
}
