package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.TaskProcess;

import java.util.List;

public class TaskInfoFragAdapter extends BaseAdapter {
    private Context ctx;
    private List<TaskProcess> datas;
    private OnButtonClickListener listener;

    public TaskInfoFragAdapter(Context ctx, List<TaskProcess> datas) {
        this.ctx = ctx;
        this.datas = datas;
    }

    public interface OnButtonClickListener {
        void onClick(View view);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return datas.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(ctx,
                    R.layout.item_listview_task_process, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TaskProcess task = datas.get(position);
        if (task.taskType.equals(TaskProcess.CREATE)) {
            vh.mTaskState.setText("开单");
            vh.mTaskTime.setText("开单时间：" + task.workorder_create_datetime);
            vh.mOther.setText("单号：" + task.workorder_code);
            vh.mButton.setVisibility(View.GONE);
        } else if (task.taskType.equals(TaskProcess.RECEIVE)) {//当订单状态到接单状态时
            vh.mTaskState.setText("接单");
            if (TextUtils.isEmpty(task.confirm_datetime)) {
                vh.mTaskTime.setText("");
                vh.mOther.setText("请接单");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("接单");
                vh.mButton.setTag("接单");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            } else {
                vh.mTaskTime.setVisibility(View.VISIBLE);
                vh.mTaskTime.setText("接单时间：" + task.confirm_datetime);
                vh.mOther.setText("");
                vh.mButton.setText("");
                vh.mButton.setTag("");
                vh.mButton.setVisibility(View.GONE);
            }
        } else if (task.taskType.equals(TaskProcess.APPOINT)) {//订单预约时的业务
            vh.mTaskState.setText("预约");
            if (TextUtils.isEmpty(task.appointment_home_datetime)) {
                vh.mOther.setText("");
                vh.mTaskTime.setText("请和报修人员预约");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("预约");
                vh.mButton.setTag("预约");
                Drawable drawable = ctx.getResources().getDrawable(R.drawable.selector_task_appoint);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                vh.mButton.setCompoundDrawables(drawable, null, null, null);
            } else {
                if (TextUtils.isEmpty(task.home_datetime)) {
                    vh.mTaskTime.setText("预约时间：" + task.appointment_home_datetime);
                    vh.mOther.setText("已预约");
                    vh.mButton.setVisibility(View.VISIBLE);
                    vh.mButton.setText("改约");
                    vh.mButton.setTag("改约");
                    Drawable drawable = ctx.getResources().getDrawable(R.drawable.selector_task_change);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    vh.mButton.setCompoundDrawables(drawable, null, null, null);
                } else {
                    vh.mTaskTime.setText("预约时间：" + task.appointment_home_datetime);
                    vh.mOther.setText("已完成");
                    vh.mButton.setVisibility(View.GONE);
                    vh.mButton.setTag("");
                    vh.mButton.setCompoundDrawables(null, null, null, null);
                }
            }
        } else if (task.taskType.equals(TaskProcess.HOME)) {//到场
            vh.mTaskState.setText("到场");
            if (TextUtils.isEmpty(task.home_datetime)) {
                vh.mTaskTime.setText("请按时到场");
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("到场");
                vh.mButton.setTag("到场");
                Drawable drawable = ctx.getResources().getDrawable(R.drawable.selector_task_visit);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                vh.mButton.setCompoundDrawables(drawable, null, null, null);
            } else {
                vh.mTaskTime.setText("到场时间：" + task.home_datetime);
                vh.mOther.setText("已到场");
                vh.mButton.setVisibility(View.GONE);
                vh.mButton.setTag("");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            }

        } else if (task.taskType.equals(TaskProcess.SOLUTION)) {
            vh.mTaskState.setText("解决");
            if (TextUtils.isEmpty(task.solution) && TextUtils.isEmpty(task.shutdown_datetime)) {
                vh.mTaskTime.setText("请提交解决方案");
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("解决方案");
                vh.mButton.setTag("解决方案");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            } else if (!TextUtils.isEmpty(task.solution) && TextUtils.isEmpty(task.shutdown_datetime)) {
                vh.mTaskTime.setText(task.feedback_solved_datetime);
                vh.mOther.setText(task.solution);
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("解决方案");
                vh.mButton.setTag("解决方案");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            } else {
                vh.mTaskTime.setText(task.feedback_solved_datetime);
                vh.mOther.setText(task.solution);
                vh.mButton.setVisibility(View.GONE);
                vh.mButton.setTag("");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            }
        } else if (task.taskType.equals(TaskProcess.SHUTDOWN)) {
            vh.mTaskState.setText("关单");
            if (TextUtils.isEmpty(task.shutdown_datetime)) {
                vh.mTaskTime.setText("等待关单");
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.GONE);
                vh.mButton.setTag("");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            } else {
                vh.mTaskTime.setText(task.shutdown_datetime);
                vh.mOther.setText("已关单");
                vh.mButton.setVisibility(View.GONE);
                vh.mButton.setTag("");
                vh.mButton.setCompoundDrawables(null, null, null, null);
            }
        } else if (task.taskType.equals(TaskProcess.EVALUATE)) {
            vh.mTaskState.setText("评价");
            vh.mTaskTime.setText(task.feedback);
            vh.mOther.setText("");
            vh.mButton.setVisibility(View.GONE);
            vh.mButton.setTag("");
            vh.mButton.setCompoundDrawables(null, null, null, null);
        } else if (task.taskType.equals(TaskProcess.ACCOUNT)) {
            if (datas.get(datas.size() - 1).taskType.equals(TaskProcess.ACCOUNT)) {
                vh.mTaskState.setText("结单");
                vh.mTaskTime.setText("未结单");
                vh.mOther.setText("");
            } else {
                vh.mTaskState.setText("结单");
                vh.mTaskTime.setText("已结单");
                vh.mOther.setText("");
            }
            vh.mButton.setTag("");
            vh.mButton.setVisibility(View.GONE);
            vh.mButton.setCompoundDrawables(null, null, null, null);
        } else if (task.taskType.equals(TaskProcess.COMPLITE)) {
            vh.mTaskState.setText("完成");
            vh.mTaskTime.setText("工单已完成");
            vh.mOther.setText("");
            vh.mButton.setTag("");
            vh.mButton.setVisibility(View.GONE);
            vh.mButton.setCompoundDrawables(null, null, null, null);
        }

        vh.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView mTaskState;
        TextView mTaskTime;
        TextView mOther;
        TextView mButton;

        public ViewHolder(View view) {
            mTaskState = (TextView) view
                    .findViewById(R.id.tv_task_process_state);
            mTaskTime = (TextView) view.findViewById(R.id.tv_task_process_time);
            mOther = (TextView) view.findViewById(R.id.tv_task_process_other);
            mButton = (TextView) view.findViewById(R.id.bt_task_action);

        }
    }

}
