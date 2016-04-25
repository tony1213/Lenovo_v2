package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.app.CustomApplication;
import com.overtech.lenovo.entity.tasklist.taskbean.Task;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

import java.util.List;

public class TaskListAdapter extends Adapter<TaskListAdapter.MyViewHolder> {
    private Context ctx;
    private List<Task> datas;
    private OnItemClickListener mClickListener;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View headerView;
    private double latitude;
    private double longitude;

    public TaskListAdapter(Context ctx) {
        this.ctx = ctx;
        longitude = ((CustomApplication) ctx.getApplicationContext()).longitude;
        latitude = ((CustomApplication) ctx.getApplicationContext()).latitude;
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
        return headerView == null ? (datas == null ? 0 : datas.size()) : (datas == null ? 1 : datas.size() + 1);

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
        ImageLoader.getInstance().displayImage(task.taskLogo, holder.taskLogo, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Bitmap.Config.RGB_565);
        holder.workorder_code.setText(task.workorder_code);
        holder.workorder_create_datetime.setText(task.workorder_create_datetime);
        holder.issue_type.setText(task.issue_type);
        holder.issue_resume.setText(task.issue_resume);
        holder.remarks.setText(task.remarks);
        if (TextUtils.isEmpty(task.latitude) || TextUtils.isEmpty(task.longitude)) {
            holder.taskDistance.setText("未获取位置信息");
        } else {
            if (longitude == 0 || latitude == 0) {
                holder.taskDistance.setText("获取当前位置信息失败");
            } else {
                LatLng curLoc = new LatLng(latitude, longitude);
                LatLng desLoc = new LatLng(Double.parseDouble(task.latitude), Double.parseDouble(task.longitude));
                double dis = DistanceUtil.getDistance(curLoc, desLoc);
                holder.taskDistance.setText(dis / 1000 + "km");
            }
        }
        if (task.appointment_home_datetime == 0) {
            holder.appointment_home_datetime.setText("");
        } else {
            holder.appointment_home_datetime.setText(Utilities.getTimeBetween(System.currentTimeMillis() / 1000, task.appointment_home_datetime));
        }
        if (task.taskType.equals("0")) {
            holder.btTaskType.setVisibility(View.VISIBLE);
            holder.btTaskType.setTag("待接单");
            holder.btTaskType.setText("接单");
            holder.taskWaitEvaluate.setVisibility(View.GONE);
        } else if (task.taskType.equals("1")) {
            holder.btTaskType.setVisibility(View.VISIBLE);
            holder.btTaskType.setTag("待预约");
            holder.btTaskType.setText("预约");
            holder.taskWaitEvaluate.setVisibility(View.GONE);

        } else if (task.taskType.equals("2")) {
            holder.btTaskType.setVisibility(View.VISIBLE);
            holder.btTaskType.setTag("待上门");
            holder.btTaskType.setText("上门");
            holder.taskWaitEvaluate.setVisibility(View.GONE);
        } else if (task.taskType.equals("3")) {
            holder.btTaskType.setVisibility(View.VISIBLE);
            holder.btTaskType.setTag("待解决");
            holder.btTaskType.setText("解决");
            holder.taskWaitEvaluate.setVisibility(View.GONE);
        } else if (task.taskType.equals("4")) {
            holder.taskWaitEvaluate.setVisibility(View.VISIBLE);
            holder.taskWaitEvaluate.setText("等待评价");
            holder.btTaskType.setVisibility(View.GONE);
            holder.btTaskType.setTag("");
        } else if (task.taskType.equals("5")) {
            holder.taskWaitEvaluate.setVisibility(View.VISIBLE);
            holder.taskWaitEvaluate.setText("等待结单");
            holder.btTaskType.setVisibility(View.GONE);
            holder.btTaskType.setTag("");
        } else if (task.taskType.equals("6")) {
            holder.taskWaitEvaluate.setVisibility(View.VISIBLE);
            holder.taskWaitEvaluate.setText("等待关单");
            holder.btTaskType.setVisibility(View.GONE);
            holder.btTaskType.setTag("");
        } else if (task.taskType.equals("7")) {
            holder.taskWaitEvaluate.setVisibility(View.VISIBLE);
            holder.taskWaitEvaluate.setText("已完成");
            holder.btTaskType.setVisibility(View.GONE);
            holder.btTaskType.setTag("");
        }
        if (task.isUrgent.equals("1")) {
            holder.taskUrgency.setVisibility(View.VISIBLE);
        } else if (task.isUrgent.equals("0")) {
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
            holder.btTaskType.setOnClickListener(new OnClickListener() {

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
        TextView workorder_code;
        TextView workorder_create_datetime;
        TextView issue_type;
        TextView issue_resume;
        TextView remarks;
        TextView taskDistance;
        TextView appointment_home_datetime;
        Button btTaskType;
        TextView taskWaitEvaluate;
        ImageView taskUrgency;

        public MyViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            if (view == headerView)
                return;
            taskItem = (RelativeLayout) view.findViewById(R.id.rl_task_item);
            taskLogo = (ImageView) view.findViewById(R.id.iv_task_logo);
            workorder_code = (TextView) view.findViewById(R.id.tv_task_no);
            workorder_create_datetime = (TextView) view.findViewById(R.id.tv_task_date);
            issue_type = (TextView) view.findViewById(R.id.tv_task_class);
            issue_resume = (TextView) view
                    .findViewById(R.id.tv_task_description);
            remarks = (TextView) view.findViewById(R.id.tv_task_remark);
            taskDistance = (TextView) view.findViewById(R.id.tv_task_distance);
            appointment_home_datetime = (TextView) view
                    .findViewById(R.id.tv_task_visit_time);
            btTaskType = (Button) view.findViewById(R.id.bt_task_receive);
            taskWaitEvaluate = (TextView) view
                    .findViewById(R.id.tv_task_wait_evaluate);
            taskUrgency = (ImageView) view.findViewById(R.id.iv_task_urgency);
        }

    }
}
