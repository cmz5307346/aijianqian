package com.ajq.aijieqian102.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.adapter.GuideAdapter;
import com.ajq.aijieqian102.base.BaseActivity;
import com.ajq.aijieqian102.component.YYCircleindicator;
import com.ajq.aijieqian102.fragment.GuideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class AboutusActivity extends BaseActivity {
    private LinearLayout mLl_back;
    private TextView tvTitle;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initView() {
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("关于我们");

//        final YYCircleindicator indicator = (YYCircleindicator) findViewById(R.id.indicator);
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        List<Fragment> list = new ArrayList<>();
//        list.add(GuideFragment.newInstance(R.mipmap.bg_guide1, 1));
//        list.add(GuideFragment.newInstance(R.mipmap.bg_guide2, 2));
//        list.add(GuideFragment.newInstance(R.mipmap.bg_guide3, 3));
//        list.add(GuideFragment.newInstance(R.mipmap.bg_guide4_2, 4));
//        indicator.initData(list.size(), 0);
//        indicator.setCurrentPage(0);
//        GuideAdapter guideAdapter = new GuideAdapter(getSupportFragmentManager(), list);
//        viewPager.setAdapter(guideAdapter);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                indicator.setCurrentPage(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://p2p.elongpay.com/static/mashanghong/intro.html");
    }

    @Override
    public void initListener() {
        mLl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
