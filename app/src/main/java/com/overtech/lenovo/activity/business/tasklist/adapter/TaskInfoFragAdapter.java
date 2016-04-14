package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.TaskProcess;

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
    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.listener=listener;
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
        if (task.taskType == TaskProcess.CREATE) {
            vh.mTaskState.setText("开单");
            vh.mTaskTime.setText("开单时间：" + task.workorder_create_datetime);
            vh.mOther.setText("单号：" + task.workorder_code);
            vh.mButton.setVisibility(View.GONE);
        } else if (task.taskType == TaskProcess.RECEIVE) {//当订单状态到接单状态时
            vh.mTaskState.setText("接单");
            if (TextUtils.isEmpty(task.assigned_datetime)) {
                vh.mTaskTime.setText("");
                vh.mOther.setText("请接单");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("接单");
                vh.mButton.setTag("接单");
            } else {
                vh.mTaskTime.setVisibility(View.VISIBLE);
                vh.mTaskTime.setText("接单时间" + task.assigned_datetime);
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.GONE);
            }
        } else if (task.taskType == TaskProcess.ORDER) {//订单预约时的业务
            vh.mTaskState.setText("预约");
            if(TextUtils.isEmpty(task.appointment_datetime)){
                vh.mOther.setText("请和报修人员预约");
                vh.mTaskTime.setText("");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("预约");
                vh.mButton.setTag("预约");
            }else{
                vh.mTaskTime.setText("预约时间："+task.appointment_datetime);
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("改约");
                vh.mButton.setTag("改约");
            }
        }else if(task.taskType==TaskProcess.VISIT){//订单
            vh.mTaskState.setText("到场");
            if(TextUtils.isEmpty(task.appointment_home_datetime)){
                vh.mOther.setText("请提交你的到场时间");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("到场");
                vh.mButton.setTag("到场");
            }else{
                vh.mTaskTime.setText("到场时间："+task.appointment_home_datetime);
                vh.mOther.setText("");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("更改");
                vh.mButton.setTag("更改");
            }
        }else if(task.taskType==TaskProcess.RESOLVE){
                vh.mTaskState.setText("完成");
            if(TextUtils.isEmpty(task.solution)){
                vh.mTaskTime.setText("");
                vh.mOther.setText("请提交解决方案");
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("解决方案");
                vh.mButton.setTag("解决方案");
            }else{
                vh.mTaskTime.setText("");
                vh.mOther.setText(task.solution);
                vh.mButton.setVisibility(View.VISIBLE);
                vh.mButton.setText("解决方案");
                vh.mButton.setTag("解决方案");
            }
        }else if(task.taskType==TaskProcess.EVALUATE){
            vh.mTaskState.setText("评价");
            vh.mTaskTime.setText("");
            vh.mOther.setText(task.solution);
            vh.mButton.setVisibility(View.GONE);
        }

        vh.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
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
        AppCompatButton mButton;

        public ViewHolder(View view) {
            mTaskState = (TextView) view
                    .findViewById(R.id.tv_task_process_state);
            mTaskTime = (TextView) view.findViewById(R.id.tv_task_process_time);
            mOther = (TextView) view.findViewById(R.id.tv_task_process_other);
            mButton = (AppCompatButton) view.findViewById(R.id.bt_task_action);
        }
    }

}
