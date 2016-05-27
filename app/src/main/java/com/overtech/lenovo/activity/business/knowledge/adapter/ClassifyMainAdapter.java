package com.overtech.lenovo.activity.business.knowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.knowledge.Knowledges;

import java.util.List;

public class ClassifyMainAdapter extends BaseAdapter {

    private Context context;
    private List<Knowledges.KnowledgeAndContract> list;
    private int position = 0;
    private boolean islodingimg = true;
    Holder hold;

    public ClassifyMainAdapter(Context context, List<Knowledges.KnowledgeAndContract> list) {
        this.context = context;
        this.list = list;
    }

    public ClassifyMainAdapter(Context context, List<Knowledges.KnowledgeAndContract> list,
                               boolean islodingimg) {
        this.context = context;
        this.list = list;
        this.islodingimg = islodingimg;
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_classify_mainlist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            hold = (Holder) view.getTag();
        }
        hold.txt.setText(list.get(arg0).knowledge_type);
        hold.layout.setBackgroundResource(R.mipmap.icon_knowledge_button_up);
        if (arg0 == position) {
            hold.layout.setBackgroundResource(R.mipmap.icon_knowledge_button_down);
        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }

    public int getSelectItem() {
        return position;
    }

    public void setData(List<Knowledges.KnowledgeAndContract> datas) {
        this.list=datas;
    }
    public List<Knowledges.KnowledgeAndContract> getData(){
        return list;
    }
    private static class Holder {
        LinearLayout layout;
        TextView txt;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.mainitem_txt);
            layout = (LinearLayout) view.findViewById(R.id.mainitem_layout);
        }
    }
}
