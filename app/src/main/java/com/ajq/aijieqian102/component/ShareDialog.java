package com.ajq.aijieqian102.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;

import com.ajq.aijieqian102.R;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class ShareDialog extends Dialog {

    private ImageView showimg;
    private ImageView close;
    private String imgurl, accessUrl;

    public ShareDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    public ShareDialog(@NonNull Context context, @StyleRes int themeResId, String imgurl, String accessUrl) {
        super(context, themeResId);
        this.imgurl = imgurl;
        this.accessUrl = accessUrl;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_sharemsg);
        setCancelable(false);
        close = (ImageView) findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        show();
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
