package com.overtech.lenovo.activity.business.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.picasso.Transformation;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

import java.util.List;

public class InformationAdapter extends Adapter<InformationAdapter.MyViewHolder> {
    private List<Information.InforItem> datas;
    private Context ctx;
    private OnItemButtonClickListener listener;

    public InformationAdapter(Context ctx, List<Information.InforItem> datas) {
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder vh, final int position) {
        Information.InforItem info = datas.get(position);
        ImageLoader.getInstance().displayImage(info.create_user_img, vh.avator,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                new Transformation() {

                    @Override
                    public Bitmap transform(Bitmap source) {
                        return ImageCacheUtils.toRoundBitmap(source);
                    }

                    @Override
                    public String key() {
                        return null;
                    }
                }, Config.RGB_565);// 处理头像
        vh.name.setText(info.create_user_name);
        vh.description.setText(info.create_user_content);

        if (vh.itemAdapter == null) {
            vh.itemAdapter = new InformationItemAdapter(ctx, info.create_img);// 初始化item的adapter
        } else {
            vh.itemAdapter.setUrls(info.create_img);
            vh.itemAdapter.notifyDataSetChanged();// 当出现convertView重用的时候，就将adapter重新刷新
        }
        vh.picture.setAdapter(vh.itemAdapter);// 每个item内的图片描述
        vh.time.setText(info.create_datetime);

        vh.commend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.buttonClick(v, position);
                }
            }
        });// 为评论注册点击事件
        vh.llCommentContainer.removeAllViews();
        for (Information.Comment comment : info.comment) {
            TextView textView = new TextView(ctx);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setText(comment.comment_user + ":" + comment.comment_content);
            vh.llCommentContainer.addView(textView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO Auto-generated method stub
        MyViewHolder vh = new MyViewHolder(LayoutInflater.from(ctx).inflate(
                R.layout.item_recyclerview_information, parent, false));
        return vh;
    }

    class MyViewHolder extends ViewHolder {
        ImageView avator;
        TextView name;
        TextView description;
        GridView picture;
        TextView time;
        ImageView commend;
        InformationItemAdapter itemAdapter;// 为每一个ViewHolder绑定一个图片gridview的adapter
        LinearLayout llCommentContainer;

        public MyViewHolder(View convertView) {
            super(convertView);
            // TODO Auto-generated constructor stub
            avator = (ImageView) convertView
                    .findViewById(R.id.iv_information_avator);
            name = (TextView) convertView
                    .findViewById(R.id.tv_information_name);
            description = (TextView) convertView
                    .findViewById(R.id.tv_information_description);
            picture = (GridView) convertView
                    .findViewById(R.id.gv_information_picture);
            time = (TextView) convertView
                    .findViewById(R.id.tv_information_time);
            commend = (ImageView) convertView
                    .findViewById(R.id.iv_information_commend);
            llCommentContainer = (LinearLayout) convertView.findViewById(R.id.ll_comment_container);
        }

    }

    public List<Information.InforItem> getDatas() {
        return datas;
    }

    public void setData(List<Information.InforItem> datas) {
        this.datas = datas;
    }

    public void setOnItemButtonClickListener(OnItemButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemButtonClickListener {
        void buttonClick(View v, int position);
    }
}
