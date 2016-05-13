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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.utils.ImageCacheUtils;
import com.overtech.lenovo.utils.ScreenTools;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.ninegrid.CustomImageView;
import com.overtech.lenovo.widget.ninegrid.NineGridlayout;
import com.squareup.picasso.Transformation;

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
        if (datas == null) {
            Logger.e("InformationAdapter==getItemCount==" + 0);
        } else {
            Logger.e("InformationAdapter==getItemCount==" + datas.size());
        }
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder vh, final int position) {
        Logger.e("InformationAdapter==onBindViewHolder()===" + position);
        Information.InforItem info = datas.get(position);
        ImageLoader.getInstance().displayImage(info.create_user_img, vh.avator, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        return ImageCacheUtils.toRoundBitmap(source);
                    }

                    @Override
                    public String key() {
                        return position + "";
                    }
                },
                Config.RGB_565);// 处理头像
        vh.name.setText(info.create_user_name);
        vh.description.setText(info.create_user_content);
        if (info.create_img.isEmpty()) {
            vh.ivOneImg.setVisibility(View.GONE);
            vh.picture.setVisibility(View.GONE);
        } else if (info.create_img.size() == 1) {
            vh.picture.setVisibility(View.GONE);
            vh.ivOneImg.setVisibility(View.VISIBLE);
            handlerOneImage(vh, info.create_img.get(0).img);
        } else {
            vh.picture.setVisibility(View.VISIBLE);
            vh.ivOneImg.setVisibility(View.GONE);
            vh.picture.setImagesData(info.create_img);
        }

        vh.time.setText(info.create_datetime);

        vh.commend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.buttonClick(v, position, -1, null);
                }
            }
        });// 为评论注册点击事件
        vh.llCommentContainer.removeAllViews();
        for (int i = 0; i < info.comment.size(); i++) {
            final Information.Comment comment = info.comment.get(i);
            TextView textView = new TextView(ctx);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setText(comment.comment_user + ":" + comment.comment_content);
            vh.llCommentContainer.addView(textView);
            for (Information.CommentResponse response : comment.comment_response) {
                TextView responseContent = new TextView(ctx);
                LinearLayout.LayoutParams resTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(resTvParams);
                responseContent.setText(response.comment_user + " 回复 " + comment.comment_user + ":" + response.comment_content);
                vh.llCommentContainer.addView(responseContent);
            }
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.buttonClick(v, position, finalI, comment);
                    }
                }
            });
        }
    }

    private void handlerOneImage(MyViewHolder vh, String url) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(ctx);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(245);//当前使用真机对应的dp值
        imageHeight = screentools.dip2px(151);
        if (imageWidth > totalWidth) {
            imageWidth = totalWidth;
            imageHeight = (int) (imageWidth * 0.618);//采用黄金比例
        }

        ViewGroup.LayoutParams layoutparams = vh.ivOneImg.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        vh.ivOneImg.setLayoutParams(layoutparams);
        vh.ivOneImg.setClickable(true);
        vh.ivOneImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        vh.ivOneImg.setImageUrl(url);

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
        NineGridlayout picture;
        CustomImageView ivOneImg;
        TextView time;
        ImageView commend;
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
            picture = (NineGridlayout) convertView
                    .findViewById(R.id.ninegrid_information);
            ivOneImg = (CustomImageView) convertView
                    .findViewById(R.id.iv_oneimg);
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
        void buttonClick(View v, int position, int commentPosition, Information.Comment comment);
    }
}
