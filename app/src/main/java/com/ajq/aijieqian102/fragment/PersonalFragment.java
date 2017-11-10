package com.ajq.aijieqian102.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.activity.KefuActivity;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.activity.AboutusActivity;
import com.ajq.aijieqian102.activity.HistoryActivity;
import com.ajq.aijieqian102.adapter.RecyclerViewAdapter;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.aijieqian102.bean.BannerInfo;
import com.ajq.aijieqian102.bean.LocalImageHolderView;
import com.ajq.aijieqian102.bean.ToolItem;
import com.ajq.aijieqian102.component.ShareDialog;
import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.PamramUtils;
import com.ajq.aijieqian102.util.Tools;
import com.ajq.aijieqian102.util.UmengUtils;
import com.ajq.creditapp.activity.CreditActivity;
import com.ajq.creditapp.bean.ReportInfo;
import com.ajq.creditapp.util.ToolsCredit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/19.
 */

public class PersonalFragment extends BaseFragment {
    private ImageView mIv_kefu;
    private LinearLayout mLl_good, mLl_aboutus, mLl_history, mLl_share, mLl_text;
    private RecyclerView mRv_tools;
    private List<ToolItem> mDatas;
    private TextView mTv_login;
    private BroadcastReceiver receiver;
    private TextView mTv_name, mTv_account, mTv_time;
    private LinearLayout mLl_info;
    private Button mBtn_logoff;
    private ConvenientBanner mCb_banner;
    RecyclerViewAdapter adapter;
    ArrayList<BannerInfo> mBannerInfos = new ArrayList<>();

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void init() {
        registerReceiver();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBanner();
    }


    @Override
    public void initView(View rootView) {
        mIv_kefu = (ImageView) rootView.findViewById(R.id.iv_personal_kefu);
        mLl_aboutus = (LinearLayout) rootView.findViewById(R.id.ll_personal_aboutus);
        mLl_good = (LinearLayout) rootView.findViewById(R.id.ll_personal_good);
        mLl_history = (LinearLayout) rootView.findViewById(R.id.ll_personal_history);
        mLl_share = (LinearLayout) rootView.findViewById(R.id.ll_personal_share);
        mLl_text = (LinearLayout) rootView.findViewById(R.id.ll_personal_logintext);
        mRv_tools = (RecyclerView) rootView.findViewById(R.id.id_recyclerview_horizontal);
        mTv_login = (TextView) rootView.findViewById(R.id.tv_personal_login);
        mTv_name = (TextView) rootView.findViewById(R.id.tv_personal_name);
        mTv_account = (TextView) rootView.findViewById(R.id.tv_personal_account);
        mTv_time = (TextView) rootView.findViewById(R.id.tv_personal_time);
        mLl_info = (LinearLayout) rootView.findViewById(R.id.ll_personal_info);
        mBtn_logoff = (Button) rootView.findViewById(R.id.btn_personal_exit);
        mCb_banner = (ConvenientBanner) rootView.findViewById(R.id.cb_personal_banner);
        getData();
        LinearLayoutManager ms = new LinearLayoutManager(getActivity());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        mRv_tools.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
        adapter = new RecyclerViewAdapter(getActivity(), mDatas);  //初始化适配器
        mRv_tools.setAdapter(adapter);  // 对 recyclerview 添加数据内容
        mCb_banner.setPageIndicator(new int[]{R.mipmap.ic_indicator_nor, R.mipmap.ic_indicator_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        if (PamramUtils.isInAudit()) {
            mCb_banner.setVisibility(View.GONE);
        } else {
            mCb_banner.setVisibility(View.VISIBLE);
        }
        if (PamramUtils.isService()) {
            mIv_kefu.setVisibility(View.VISIBLE);
        } else {
            mIv_kefu.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        super.initData();
        getBanner();
    }

    private void getData() {
        mDatas = new ArrayList<>();
        mDatas.add(new ToolItem(R.mipmap.tools_car, getString(R.string.tools_car)));
        mDatas.add(new ToolItem(R.mipmap.tools_person, getString(R.string.tools_person)));
        mDatas.add(new ToolItem(R.mipmap.tools_loan, getString(R.string.tools_loan)));
        mDatas.add(new ToolItem(R.mipmap.tools_old, getString(R.string.tools_old)));
        mDatas.add(new ToolItem(R.mipmap.tools_lose, getString(R.string.tools_lose)));

    }

    private void getBanner() {
        HttpUtil.getBannerAd(HttpUtil.BANNERAD_BOTTOM, Tools.getBannerAdButtomVersion(0) + "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                JsonArray ads = data.get("ads").getAsJsonArray();
                int currentVersion = data.get("version").getAsInt();
                int localVersion = Tools.getBannerAdButtomVersion(0);
                if (localVersion < currentVersion) {
                    Tools.writeBannerAdButtomVersion(currentVersion);
                    Tools.writeFile(getActivity(), PublicDef.WEB_BANNERADBUTTOM_FILE, ads.toString());
                } else {
                    String json = Tools.readFile(getActivity(), PublicDef.WEB_BANNERADBUTTOM_FILE);
                    if (json != null) {
                        ads = new JsonParser().parse(json).getAsJsonArray();
                    }
                }
                Gson gson = new Gson();
                mBannerInfos.clear();
                if (ads != null) {
                    for (int i = 0; i < ads.size(); i++) {
                        BannerInfo productInfo = gson.fromJson(ads.get(i).getAsJsonObject(), BannerInfo.class);
                        mBannerInfos.add(productInfo);
                    }
                }
                mCb_banner.post(new Runnable() {
                    @Override
                    public void run() {
                        mCb_banner.setPages(new CBViewHolderCreator() {
                            @Override
                            public Object createHolder() {
                                return new LocalImageHolderView();
                            }
                        }, mBannerInfos).setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                UmengUtils.personalBanner(getActivity());
                                BaseActivityWeb.start(getActivity(), mBannerInfos.get(position).getAccessUrl(), "");
                            }
                        });
                        if (mBannerInfos.size() > 1) {
                            mCb_banner.setCanLoop(true);
                            mCb_banner.setPointViewVisible(true);
                            mCb_banner.notifyDataSetChanged();
                        } else {
                            mCb_banner.setPointViewVisible(false);
                            mCb_banner.setCanLoop(false);
                            mCb_banner.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void initListener() {
        mIv_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KefuActivity.start(getActivity());
            }
        });
        mLl_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    ToolsCredit.NormalDialog("您未安装任何没有应用市场", getActivity());
                    e.printStackTrace();
                }
            }
        });
        mLl_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutusActivity.class);
                startActivity(intent);
            }
        });
        mTv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CreditActivity.start(getActivity());
              Toast.makeText(getActivity(),"该功能暂未开放，敬请期待！",Toast.LENGTH_LONG).show();
            }
        });
        mBtn_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogCustomAttr();
            }
        });
        mLl_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        mLl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(getActivity(), R.style.CustomDialog);
            }
        });
        //判断是否登录
        if (ToolsCredit.getLoginStatus(PublicDef.NO_LOGINED).equals(PublicDef.LOGINED)) {
            List<ReportInfo> datas = ToolsCredit.getDataList(ToolsCredit.getLogInName(""));
            String name;
            if (datas.size() != 0) {
                name = datas.get(0).getRealName();
            } else {
                name = "***";
            }
            drawLogined(name, ToolsCredit.getLogInName(""), ToolsCredit.getLogInData(ToolsCredit.getLogInName(""), ""));
        }
    }

    //注册广播接受者

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PublicDef.LOGIN)) {
                    String name = intent.getStringExtra("name");
                    String account = intent.getStringExtra("account");
                    String time = intent.getStringExtra("time");
                    drawLogined(name, account, time);
                } else if (intent.getAction().equals(PublicDef.LOG_OFF)) {
                    drawLogoff();
                } else if (intent.getAction().equals(PublicDef.ACTION_REFRESH)) {
                    if (PamramUtils.isInAudit()) {
                        mCb_banner.setVisibility(View.GONE);
                    } else {
                        mCb_banner.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        IntentFilter intent = new IntentFilter();
        intent.addAction(PublicDef.LOGIN);
        intent.addAction(PublicDef.LOG_OFF);
        intent.addAction(PublicDef.ACTION_REFRESH);
        getActivity().registerReceiver(receiver, intent);
    }

    private void drawLogoff() {
        mLl_text.setVisibility(View.VISIBLE);
        mLl_info.setVisibility(View.GONE);
    }

    private void drawLogined(String name, String account, String time) {
        mLl_text.setVisibility(View.GONE);
        mLl_info.setVisibility(View.VISIBLE);
        mTv_name.setText(name);
        mTv_account.setText(account);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String retStrFormatNowDate = sdFormatter.format(date);
        mTv_time.setText(retStrFormatNowDate);
        String loginName = ToolsCredit.getLogInName("");
        String realName = ToolsCredit.getRealName(loginName);
        if (!realName.equals("")) {
            mTv_name.setText(realName);
        }
    }

    //注销广播
    public static void sendLogOff(Context context) {
        Intent intent = new Intent();
        intent.setAction(PublicDef.LOG_OFF);
        context.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCb_banner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCb_banner.stopTurning();
    }

    //dialog
    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#FFFFFF"))//
                .cornerRadius(5)//
                .content("是否确定退出登录?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#666666"))//
                .dividerColor(Color.parseColor("#666666"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#666666"), Color.parseColor("#666666"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
                .show();
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {

                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        drawLogoff();
                        ToolsCredit.writePassWord("");
                        ToolsCredit.writeLoginStatus(PublicDef.NO_LOGINED);
                        sendLogOff(getActivity());
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
