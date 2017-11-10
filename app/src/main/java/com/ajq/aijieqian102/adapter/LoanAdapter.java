package com.ajq.aijieqian102.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ajq.aijieqian102.util.Tools;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.util.ImageLoader;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class LoanAdapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder> {
    private Context context;

    public LoanAdapter(Context context) {
        super(R.layout.item_loan_recyclerview);
        this.context = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {
        helper.setText(R.id.item_loan_name, item.getName());
        helper.setText(R.id.item_loan_title, item.getTitle());
        helper.setText(R.id.item_loan_describe, item.getProDescribe());
        ImageLoader.loadCenterCrop(context, item.getIcon(), (ImageView) helper.getView(R.id.item_loan_icon));
        final ImageView tagIcon = helper.getView(R.id.item_loan_tagicon);
        if (!item.getTagIcon().equals("")){
            Glide.with(context).load(item.getTagIcon()).asBitmap().dontAnimate().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    double b = Tools.getLoanImgscale(context);
                    int w = (int) (resource.getWidth() * b);
                    int h = (int) (resource.getHeight() * b);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    tagIcon.setLayoutParams(layoutParams);
                    tagIcon.setImageBitmap(resource);
                    tagIcon.setVisibility(View.VISIBLE);
                }
            });
        }else {
            tagIcon.setVisibility(View.GONE);
        }

    }
}
