package com.overtech.lenovo.activity.business.knowledge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.knowledge.Knowledges;

import java.util.List;

/**
 * Created by Overtech on 16/4/27.
 */
public class ContractAdapter extends BaseAdapter {
    private List<Knowledges.KnowledgeAndContract> datas;
    private Context ctx;

    public ContractAdapter(List<Knowledges.KnowledgeAndContract> datas, Context context) {
        this.datas = datas;
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_popupwindow_contract, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (datas != null) {
            vh.tvContract.setText(datas.get(position).contract_name);
        }
        return convertView;
    }

    public void setData(List<Knowledges.KnowledgeAndContract> contractDatas) {
        this.datas = contractDatas;
    }

    class ViewHolder {
        public TextView tvContract;

        public ViewHolder(View view) {
            tvContract = (TextView) view.findViewById(R.id.tv_item_contract);
        }
    }
}
