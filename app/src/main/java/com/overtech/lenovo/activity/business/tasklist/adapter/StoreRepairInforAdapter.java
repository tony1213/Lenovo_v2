package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.StoreInfo;

import java.util.List;

/**
 * Created by Overtech on 16/4/29.
 */
public class StoreRepairInforAdapter extends RecyclerView.Adapter<StoreRepairInforAdapter.MyViewHolder> {
    private List<StoreInfo.RepairInfo> datas;
    private Context ctx;

    public StoreRepairInforAdapter(Context ctx, List<StoreInfo.RepairInfo> datas) {
        this.datas = datas;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_recyclerview_store_repair_infor, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StoreInfo.RepairInfo data = datas.get(position);
        holder.tvWorkorderCode.setText(data.workorder_code);
        holder.tvIssueType.setText(data.issue_type);
        holder.tvIssueDescription.setText(data.issue_description);
        holder.tvSolution.setText(data.solution);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvWorkorderCode;
        public AppCompatTextView tvIssueType;
        public AppCompatTextView tvIssueDescription;
        public AppCompatTextView tvSolution;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvWorkorderCode = (AppCompatTextView) itemView.findViewById(R.id.tv_workorder_code);
            tvIssueType = (AppCompatTextView) itemView.findViewById(R.id.tv_issue_type);
            tvIssueDescription = (AppCompatTextView) itemView.findViewById(R.id.tv_issue_description);
            tvSolution = (AppCompatTextView) itemView.findViewById(R.id.tv_solution);
        }
    }
}
