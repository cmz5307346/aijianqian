package com.ajq.aijieqian102.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.util.Tools;
import com.ajq.aijieqian102.util.UmengUtils;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class PopDialog extends Dialog {

    private ImageView showimg;
    private ImageView close;
    private String imgurl, accessUrl;

    public PopDialog(@NonNull Context context) {
        super(context);
    }

    public PopDialog(@NonNull Context context, @StyleRes int themeResId, String imgurl, String accessUrl) {
        super(context, themeResId);
        this.imgurl = imgurl;
        this.accessUrl = accessUrl;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_popmsg);
        setCancelable(false);
        showimg = (ImageView) findViewById(R.id.dialog_showImg);
        close = (ImageView) findViewById(R.id.dialog_close);
        showimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivityWeb.start(getContext(), accessUrl, "");
                dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Glide.with(getContext()).load(imgurl).asBitmap().dontAnimate().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                showimg.setImageBitmap(resource);
                Tools.writePopMsgData(System.currentTimeMillis());
                UmengUtils.homeDialog(getContext());
                show();
            }
        });
    }

    protected PopDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
