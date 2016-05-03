package com.overtech.lenovo.activity.business.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.business.personal.PersonalAccountDetailActivity;
import com.overtech.lenovo.entity.person.PersonalAccount;

import java.util.List;


/**
 * Created by Overtech on 16/5/3.
 */
public class PersonalAccountDetailAdapter extends RecyclerView.Adapter<PersonalAccountDetailAdapter.MyViewHolder> {
    private Context ctx;
    private List<PersonalAccount.Workorder> datas;

    public PersonalAccountDetailAdapter(Context ctx, List<PersonalAccount.Workorder> datas) {
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((PersonalAccountDetailActivity) ctx).getLayoutInflater().inflate(R.layout.item_recyclerview_personal_account_detail, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PersonalAccount.Workorder data = datas.get(position);
        holder.workorder_code.setText(data.workorder_code);
        holder.datetime.setText(data.datetime);
        holder.issue_description.setText(data.issue_description);
        holder.issue_type.setText(data.issue_type);
        holder.standard_fee_amount.setText(data.standard_fee_amount);
        holder.solution.setText(data.solution);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView workorder_code;
        TextView datetime;
        TextView standard_fee_amount;
        TextView issue_type;
        TextView issue_description;
        TextView solution;

        public MyViewHolder(View itemView) {
            super(itemView);
            workorder_code = (TextView) itemView.findViewById(R.id.tv_workorder_code);
            datetime = (TextView) itemView.findViewById(R.id.tv_workorder_datetime);
            standard_fee_amount = (TextView) itemView.findViewById(R.id.tv_workorder_standard_fee_amount);
            issue_type = (TextView) itemView.findViewById(R.id.tv_workorder_issue_type);
            issue_description = (TextView) itemView.findViewById(R.id.tv_workorder_issue_description);
            solution = (TextView) itemView.findViewById(R.id.tv_workorder_solution);
        }
    }
}
