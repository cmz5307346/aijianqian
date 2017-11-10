package com.ajq.aijieqian102.bean;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.ajq.aijieqian102.util.ImageLoader;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class LocalImageHolderView implements Holder<BannerInfo>,Serializable {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerInfo data) {
        ImageLoader.load(context, data.getShowImg(), imageView);
    }
}
