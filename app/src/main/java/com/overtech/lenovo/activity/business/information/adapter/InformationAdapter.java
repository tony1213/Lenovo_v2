package com.overtech.lenovo.activity.business.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
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
import com.overtech.lenovo.utils.ImageUtils;
import com.overtech.lenovo.utils.ScreenTools;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.overtech.lenovo.widget.ninegrid.CustomImageView;
import com.overtech.lenovo.widget.ninegrid.NineGridlayout;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InformationAdapter extends Adapter<RecyclerView.ViewHolder> {
    private List<Information.InforItem> datas;
    private Context ctx;
    private OnItemButtonClickListener listener;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int PULLUP_LOAD_MORE = 0;
    public static final int LOADING_MORE = 1;
    private int load_more_status = 0;

    public InformationAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public InformationAdapter(Context ctx, List<Information.InforItem> datas) {
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
//            Logger.e("InformationAdapter==getItemCount==" + 0);
        } else {
//            Logger.e("InformationAdapter==getItemCount==" + datas.size());
        }
        return datas == null ? 0 : datas.size() + 1;
    }

    /**
     * 下拉刷新添加数据
     *
     * @param newDatas
     */
    public void addItem(List<Information.InforItem> newDatas) {
        datas.clear();
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * 上拉加载添加数据
     *
     * @param newDatas
     */
    public void addMoreItem(List<Information.InforItem> newDatas) {
        if (datas == null) {//第一次加载时
            datas = newDatas;
            notifyDataSetChanged();
        } else {
            int preLastPosition = datas.size() - 1;
            datas.addAll(newDatas);
            notifyItemRangeInserted(preLastPosition, newDatas.size());
        }
    }

    public void changeLoadMoreStatus(int load_more_status) {
        this.load_more_status = load_more_status;
        notifyItemChanged(datas.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        if (vh instanceof NormalViewHolder) {
            Logger.e("InformationAdapter==onBindViewHolder()===" + position);
            Information.InforItem info = datas.get(position);
            ImageLoader.getInstance().displayImage(info.create_user_img, ((NormalViewHolder) vh).avator, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            return ImageUtils.toRoundBitmap(source);
                        }

                        @Override
                        public String key() {
                            return position + "";
                        }
                    },
                    Config.RGB_565);// 处理头像
            ((NormalViewHolder) vh).name.setText(info.create_user_name);
            ((NormalViewHolder) vh).description.setText(info.create_user_content);
            if (info.create_img.isEmpty()) {
                ((NormalViewHolder) vh).ivOneImg.setVisibility(View.GONE);
                ((NormalViewHolder) vh).picture.setVisibility(View.GONE);
            } else if (info.create_img.size() == 1) {
                ((NormalViewHolder) vh).picture.setVisibility(View.GONE);
                ((NormalViewHolder) vh).ivOneImg.setVisibility(View.VISIBLE);
                handlerOneImage(((NormalViewHolder) vh), info.create_img.get(0).img);
            } else {
                ((NormalViewHolder) vh).picture.setVisibility(View.VISIBLE);
                ((NormalViewHolder) vh).ivOneImg.setVisibility(View.GONE);
                ((NormalViewHolder) vh).picture.setImagesData(info.create_img);
            }

            ((NormalViewHolder) vh).time.setText(info.create_datetime);

            ((NormalViewHolder) vh).commend.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (listener != null) {
                        listener.buttonClick(v, position, -1, null);
                    }
                }
            });// 为评论注册点击事件
            ((NormalViewHolder) vh).llCommentContainer.removeAllViews();
            for (int i = 0; i < info.comment.size(); i++) {
                final Information.Comment comment = info.comment.get(i);
                TextView textView = new TextView(ctx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                textView.setText(Html.fromHtml("<font color=\'#19A4D7\'>" + comment.comment_user + "</font>" + ":" + comment.comment_content));
                ((NormalViewHolder) vh).llCommentContainer.addView(textView);

                if (comment.comment_response != null) {
                    for (Information.CommentResponse response : comment.comment_response) {
                        TextView responseUser = new TextView(ctx);
                        LinearLayout.LayoutParams resTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        responseUser.setLayoutParams(resTvParams);
                        responseUser.setText(Html.fromHtml("<font color=\'#19A4D7\'>" + response.comment_user + "</font>"
                                + " 回复 "
                                + "<font color=\'#19A4D7\'>" + comment.comment_user + "</font>"
                                + ":" + response.comment_content));

                        ((NormalViewHolder) vh).llCommentContainer.addView(responseUser);
                    }
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
        } else if (vh instanceof MyFooterViewHolder) {
            if (load_more_status == PULLUP_LOAD_MORE) {
                ((MyFooterViewHolder) vh).tvLoading.setText("上拉加载更多");
            } else if (load_more_status == LOADING_MORE) {
                ((MyFooterViewHolder) vh).tvLoading.setText("正在加载中");
            }
        }
    }

    private void handlerOneImage(NormalViewHolder vh, String url) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            NormalViewHolder vh = new NormalViewHolder(LayoutInflater.from(ctx).inflate(
                    R.layout.item_recyclerview_information, parent, false));
            return vh;
        } else {
            MyFooterViewHolder vh = new MyFooterViewHolder(LayoutInflater.from(ctx).inflate(R.layout.loading_more_footerview, parent, false));
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }
    public Information.InforItem getItem(int position){
        return datas.get(position);
    }
    class MyFooterViewHolder extends ViewHolder {
        TextView tvLoading;

        public MyFooterViewHolder(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_footerview_loading);
        }
    }

    class NormalViewHolder extends ViewHolder {
        ImageView avator;
        TextView name;
        TextView description;
        NineGridlayout picture;
        CustomImageView ivOneImg;
        TextView time;
        ImageView commend;
        LinearLayout llCommentContainer;

        public NormalViewHolder(View convertView) {
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
