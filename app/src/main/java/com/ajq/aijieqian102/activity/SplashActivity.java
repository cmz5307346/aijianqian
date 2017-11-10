package com.ajq.aijieqian102.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;


import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.adapter.GuideAdapter;
import com.ajq.aijieqian102.component.YYCircleindicator;
import com.ajq.aijieqian102.fragment.GuideFragment;
import com.ajq.aijieqian102.util.PamramUtils;
import com.ajq.aijieqian102.util.Tools;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends FragmentActivity {
    private Handler mHandler = new Handler();
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PamramUtils.getOnlineParam(SplashActivity.this, Tools.getPackageName());
        boolean isFirst = Tools.getFirst(true);
        if (isFirst) {
            initViewPage();
        } else {
            mHandler.postDelayed(mStartRunnable, 2000);
        }

    }

    private void initViewPage() {
        final YYCircleindicator indicator = (YYCircleindicator) findViewById(R.id.indicator);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        indicator.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        List<Fragment> list = new ArrayList<>();
        list.add(GuideFragment.newInstance(R.mipmap.bg_guide1, 1));
        list.add(GuideFragment.newInstance(R.mipmap.bg_guide2, 2));
        list.add(GuideFragment.newInstance(R.mipmap.bg_guide3, 3));
        list.add(GuideFragment.newInstance(R.mipmap.bg_guide4, 0));
        indicator.initData(list.size(), 0);
        indicator.setCurrentPage(0);
        GuideAdapter guideAdapter = new GuideAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(guideAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
