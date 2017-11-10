package com.ajq.aijieqian102.activity;


import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseActivity;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.aijieqian102.fragment.ToolsCarFragment;
import com.ajq.aijieqian102.fragment.ToolsLoanFragment;
import com.ajq.aijieqian102.fragment.ToolsLoseFragment;
import com.ajq.aijieqian102.fragment.ToolsOldFragment;
import com.ajq.aijieqian102.fragment.ToolsPersonFragment;

public class ToolsBaseActivity extends BaseActivity {
    private LinearLayout mLl_back;
    private TextView mTv_title;
    private String mTitle;
    private InputMethodManager imm;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_tools_base;
    }

    @Override
    public void init() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void initView() {
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTitle = getIntent().getStringExtra("title");
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = null;
        mTv_title.setText(mTitle);
        switch (mTitle) {
            case "车险计算":
                fragment = new ToolsCarFragment();
                break;
            case "个税计算":
                fragment = new ToolsPersonFragment();
                break;
            case "贷款计算":
                fragment = new ToolsLoanFragment();
                break;
            case "养老金计算":
                fragment = new ToolsOldFragment();
                break;
            case "失业险计算":
                fragment = new ToolsLoseFragment();
                break;
        }
        transaction.add(R.id.main_frame_layout, fragment);
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void initListener() {
        mLl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
                finish();
            }
        });
    }
}
