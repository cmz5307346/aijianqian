package com.ajq.aijieqian102.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ajq.aijieqian102.R;

import static android.view.Gravity.CENTER;
/*
* 状态页
* 加载中，加载失败,空页面，数据页
* */


public class EmptyViewManager extends RelativeLayout {

    TextView mEmptyTextView;
    LinearLayout mRetryLayout;
    PrassTextView mRetryButton;
    ProgressBar mProgressBar;
    TextView mNoDataTextView;

    public EmptyViewManager(Context context) {
        super(context);
        init(context);
    }

    public EmptyViewManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public EmptyViewManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyViewManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        initLoad(context);
        initRetry(context);
        initNoData(context);

    }

    private void initNoData(Context context) {
        mNoDataTextView = new TextView(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_IN_PARENT);
        mNoDataTextView.setLayoutParams(lp);
        mNoDataTextView.setText(context.getString(R.string.noData));
        mNoDataTextView.setTextColor(Color.parseColor("#cccccc"));
        mNoDataTextView.setTextSize(22);
        addView(mNoDataTextView);
        //mNoDataTextView.setVisibility(GONE);
    }

    private void initLoad(Context context) {
        mProgressBar = new ProgressBar(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_IN_PARENT);
        //  mProgressBar.setColor(Color.parseColor("#ec5e96"));
        mProgressBar.setLayoutParams(lp);
        addView(mProgressBar);
        // mProgressBar.setVisibility(GONE);
    }

    private void initRetry(Context context) {
        mRetryLayout = new LinearLayout(context);
        mRetryLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRetryLayout.setGravity(CENTER);
        mRetryLayout.setOrientation(LinearLayout.VERTICAL);

        mEmptyTextView = new TextView(context);
        LinearLayout.LayoutParams emptyLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        emptyLp.setMargins(0, 0, 0, 30);
        mEmptyTextView.setText(context.getString(R.string.noWeb));
        mEmptyTextView.setTextSize(14);
        mEmptyTextView.setTextColor(Color.parseColor("#cccccc"));
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_erro);
        mEmptyTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        mEmptyTextView.setCompoundDrawablePadding(20);
        mEmptyTextView.setLayoutParams(emptyLp);
        mEmptyTextView.setGravity(CENTER);

        mRetryButton = new PrassTextView(context, "刷新");
        mRetryButton.setBorderWidth(1);
        mRetryButton.setRadius(8);
        mRetryButton.setBorderColor(Color.parseColor("#0071eb"));
        mRetryButton.setBgColor(Color.parseColor("#ffffff"));
        mRetryButton.setPressBgColor(Color.parseColor("#0071eb"));
        mRetryButton.setPressTextColor(Color.parseColor("#ffffff"));
        mRetryButton.setmTextColor(Color.parseColor("#0071eb"));
        LinearLayout.LayoutParams Retrylp = new LinearLayout.LayoutParams(164, 56);
        mRetryButton.setLayoutParams(Retrylp);
        mRetryLayout.addView(mEmptyTextView);
        mRetryLayout.addView(mRetryButton);
        mRetryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmptyInterface != null) {
                    mEmptyInterface.doRetry();
                }
            }
        });
        addView(mRetryLayout);
        //mRetryLayout.setVisibility(GONE);
    }


    public void setType(EmptyStyle type) {
        mRetryLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mNoDataTextView.setVisibility(View.GONE);
        switch (type) {
            case EmptyStyle_LOADING:
                setVisibility(VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case EmptyStyle_RETRY:
                setVisibility(VISIBLE);
                mRetryLayout.setVisibility(View.VISIBLE);
                break;
            case EmptyStyle_NODATA:
                setVisibility(VISIBLE);
                mNoDataTextView.setVisibility(View.VISIBLE);
                break;
            case EmptyStyle_NOSERVER:
                setVisibility(VISIBLE);
                break;
            case EmptyStyle_NORMAL:
                setVisibility(GONE);
                break;

            default:
                break;
        }
    }

    public void setRetry() {
        setType(EmptyStyle.EmptyStyle_RETRY);
    }

    public void setLoading() {
        setType(EmptyStyle.EmptyStyle_LOADING);

    }

    public void setNodata() {
        setType(EmptyStyle.EmptyStyle_NODATA);
    }

    public void setNormal() {
        setType(EmptyStyle.EmptyStyle_NORMAL);
    }

    public void setNoDataDefault(String string) {
        mNoDataTextView.setText(string);
    }

    public void setRetryStringDefault(String string) {
        news_retry_string.setText(string);
    }

    public void setButtonStringDefault(String string) {
        mRetryButton.setText(string);
    }

    EmptyInterface mEmptyInterface;
    EmptyNOServerInterface mEmptyNOServerInterface;
    private TextView news_retry_string;

    public void setEmptyInterface(EmptyInterface emptyInterface) {
        mEmptyInterface = emptyInterface;
    }

    public interface EmptyInterface {
        public void doRetry();
    }

    public void setEmptyNOServerInterface(EmptyNOServerInterface emptyInterface) {
        mEmptyNOServerInterface = emptyInterface;
    }

    public interface EmptyNOServerInterface {
        public void onClick();
    }

    public enum EmptyStyle {
        EmptyStyle_LOADING, EmptyStyle_RETRY, EmptyStyle_NODATA, EmptyStyle_NORMAL, EmptyStyle_NOSERVER;
    }
}
