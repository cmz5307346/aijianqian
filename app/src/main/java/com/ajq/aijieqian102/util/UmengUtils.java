package com.ajq.aijieqian102.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/7/5.
 * 友盟自定义事件
 */

public class UmengUtils {
    //爱借钱tab
    public static void tabHome(Context mContext) {
        MobclickAgent.onEvent(mContext, "tabHome");
    }

    //贷款超市tab
    public static void tabLoan(Context mContext) {
        MobclickAgent.onEvent(mContext, "tabLoan");
    }

    //信用卡tab
    public static void tabCard(Context mContext) {
        MobclickAgent.onEvent(mContext, "tabCard");
    }

    //个人中心tab
    public static void tabPersonal(Context mContext) {
        MobclickAgent.onEvent(mContext, "tabPersonal");
    }

    //首页弹窗
    public static void homeDialog(Context mContext) {
        MobclickAgent.onEvent(mContext, "homeDialog");
    }

    //贷款超市banner
        public static void loanBanner(Context mContext) {
        MobclickAgent.onEvent(mContext, "loanBanner");
    }

    //个人中心banner
    public static void personalBanner(Context mContext) {
        MobclickAgent.onEvent(mContext, "personalBanner");
    }

    //进入查信用
    public static void credit(Context mContext) {
        MobclickAgent.onEvent(mContext, "credit");
    }

    //筛选功能
    public static void filter(Context mContext) {
        MobclickAgent.onEvent(mContext, "filter");
    }

    //首页推荐位
    public static void homeRecommend(Context mContext) {
        MobclickAgent.onEvent(mContext, "homeRecommend");
    }

    //贷款超市贷款产品
    public static void loanProduct(Context mContext, String var) {
        MobclickAgent.onEvent(mContext, "loanProduct", var);
    }

    //首页贷款产品
    public static void homeProduct(Context mContext, String var) {
        MobclickAgent.onEvent(mContext, "homeProduct", var);
    }
}
