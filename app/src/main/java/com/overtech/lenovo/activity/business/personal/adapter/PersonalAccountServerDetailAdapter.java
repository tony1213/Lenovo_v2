package com.overtech.lenovo.activity.business.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.personal.PersonalAccountServerDetailActivity;
import com.overtech.lenovo.entity.person.PersonalAccount;

import java.util.List;


/**
 * Created by Overtech on 16/5/3.
 */
public class PersonalAccountServerDetailAdapter extends RecyclerView.Adapter<PersonalAccountServerDetailAdapter.MyViewHolder> {
    private Context ctx;
    private List<PersonalAccount.Workorder> datas;

    public PersonalAccountServerDetailAdapter(Context ctx, List<PersonalAccount.Workorder> datas) {
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((PersonalAccountServerDetailActivity) ctx).getLayoutInflater().inflate(R.layout.item_recyclerview_personal_account_server_detail, parent, false);//如果此处只用inflate（R.layout，null）；则只会生成view,不会设置layoutparams
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PersonalAccount.Workorder data = datas.get(position);
        holder.workorder_code.setText(data.workorder_code);
        holder.datetime.setText(data.datetime);
        holder.issue_description.setText(data.issue_description);
        holder.issue_type.setText(data.issue_type);
        if (data.task_type.equals("0")) {
            holder.taskType.setText("待接单");
        } else if (data.task_type.equals("1")) {
            holder.taskType.setText("待预约");
        } else if (data.task_type.equals("2")) {
            holder.taskType.setText("等待上门");
        } else if(data.task_type.equals("3")){
            holder.taskType.setText("待解决");
        }else if (data.task_type.equals("4")) {
            holder.taskType.setText("待关单");
        } else if (data.task_type.equals("5")) {
            holder.taskType.setText("待结单");
        } else {
            holder.taskType.setText("完成");
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView workorder_code;
        TextView datetime;
        TextView taskType;
        TextView issue_type;
        TextView issue_description;

        public MyViewHolder(View itemView) {
            super(itemView);
            workorder_code = (TextView) itemView.findViewById(R.id.tv_workorder_code);
            datetime = (TextView) itemView.findViewById(R.id.tv_workorder_datetime);
            taskType = (TextView) itemView.findViewById(R.id.tv_workorder_task_type);
            issue_type = (TextView) itemView.findViewById(R.id.tv_workorder_issue_type);
            issue_description = (TextView) itemView.findViewById(R.id.tv_workorder_issue_description);
        }
    }
}
