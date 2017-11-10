package com.ajq.aijieqian102.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.ajq.aijieqian102.util.Tools;


/**
 * Created by Administrator on 2017/6/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private boolean mRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        init();
        initView();
        initData();
        initListener();
    }


    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();


    public void init() {

    }

    //初始化控件
    public void initView() {
    }

    public void initData() {
    }

    public void initListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRefresh) {
            onRefresh();
            mRefresh = false;
        }

        if (!"MainActivity".equals(Tools.getRunningActivityName())) {
            MobclickAgent.onPageStart(Tools.getRunningActivityName());
        }
        //统计时长
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!"MainActivity".equals(Tools.getRunningActivityName())) {
            MobclickAgent.onPageEnd(Tools.getRunningActivityName());
        }
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        if (!Tools.isAppOnForeground(BaseActivity.this)) {
            //app 进入后台
            mRefresh = true;
        }
        super.onStop();
    }

    public void onRefresh() {

    }


}
