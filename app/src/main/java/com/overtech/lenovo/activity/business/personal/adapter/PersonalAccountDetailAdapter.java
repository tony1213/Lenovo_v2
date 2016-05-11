package com.overtech.lenovo.activity.business.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        View view = ((PersonalAccountDetailActivity) ctx).getLayoutInflater().inflate(R.layout.item_recyclerview_personal_account_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PersonalAccount.Workorder data = datas.get(position);
        holder.workorder_code.setText(data.workorder_code);
        holder.datetime.setText(data.datetime);
        holder.standard_fee_amount.setText(data.standard_fee_amount);
        holder.workorder_contract.setText(data.contract);
        if (data.isPay.equals("0")) {
            holder.workorder_pay_status.setText("未支付");
        } else if (data.isPay.equals("1")) {
            holder.workorder_pay_status.setText("已支付");
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView workorder_code;
        TextView datetime;
        TextView standard_fee_amount;
        TextView workorder_contract;
        TextView workorder_pay_status;

        public MyViewHolder(View itemView) {
            super(itemView);
            workorder_code = (TextView) itemView.findViewById(R.id.tv_workorder_code);
            datetime = (TextView) itemView.findViewById(R.id.tv_workorder_datetime);
            standard_fee_amount = (TextView) itemView.findViewById(R.id.tv_workorder_standard_fee_amount);
            workorder_contract = (TextView) itemView.findViewById(R.id.tv_workorder_contract);
            workorder_pay_status = (TextView) itemView.findViewById(R.id.tv_workorder_pay_status);
        }

    }
}
