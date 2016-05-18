package com.overtech.lenovo.widget.cycleviewpager;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.overtech.lenovo.R;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

/**
 * ImageView 创建工厂
 *
 * @author Overtech Will
 */
public class ViewFactory {
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = new ImageView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ScaleType.CENTER_CROP);
//		imageView.setAdjustViewBounds(true);
//		Log.e("imageView对象地址", imageView.toString());
        ImageLoader.getInstance().displayImage(url, imageView,
                R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Config.RGB_565);
        return imageView;
    }
}
