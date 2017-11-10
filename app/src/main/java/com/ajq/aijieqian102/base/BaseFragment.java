package com.ajq.aijieqian102.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.ajq.aijieqian102.util.Tools;


/**
 * Created by Administrator on 2017/6/19.
 */

public abstract class BaseFragment extends Fragment {
    private View rootView;//缓存Fragment view
    private boolean mRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            initView(rootView);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    public void init() {

    }

    public void initView(View rootView) {
    }

    public void initData() {

    }

    public void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRefresh) {
            onRefresh();
            mRefresh = false;
        }
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());

    }

    @Override
    public void onStop() {
        if (!Tools.isAppOnForeground(getActivity())) {
            //app 进入后台
            mRefresh = true;
        }
        super.onStop();
    }

    public void onRefresh() {

    }
}
