package com.ajq.aijieqian102.application;


import com.umeng.analytics.MobclickAgent;
import com.ajq.creditapp.application.CreditApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/7/13.
 */
public class YYApplication extends CreditApplication {
    private static YYApplication instance;

    public static YYApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (YYApplication) CreditApplication.getInstance();
//        JPushInterface.init(this);
        initUmeng();

    }

    private void initUmeng() {
        //设置场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //捕获程序崩溃日志
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setDebugMode(true);
    }

}
