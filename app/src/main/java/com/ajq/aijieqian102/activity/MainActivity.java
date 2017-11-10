package com.ajq.aijieqian102.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ajq.aijieqian102.bean.DataBean;
import com.ajq.aijieqian102.bean.WebSetJsonData;
import com.ajq.creditapp.util.ToolsCredit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseActivity;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.component.PopDialog;
import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.fragment.CardFragment;
import com.ajq.aijieqian102.fragment.HomeFragment;
import com.ajq.aijieqian102.fragment.LoanFragment;
import com.ajq.aijieqian102.fragment.PersonalFragment;
import com.ajq.aijieqian102.fragment.ToolsLoanFragment;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.PamramUtils;
import com.ajq.aijieqian102.util.Tools;
import com.ajq.aijieqian102.util.UmengUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends BaseActivity {

    private FragmentTabHost fragmentTabHost;
    private LayoutInflater layoutInflater;

    private BroadcastReceiver Broadcast;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        if (PublicDef.READ_LOCAL_JSON) {
            Tools.readLocalJson(this);
        } else {
            getWebProperty();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int flag = Tools.getSubmitFlag(0);
        if (flag != 0) {
            ScoreDialogDefault();
        }
    }

    @Override
    public void initView() {
        layoutInflater = LayoutInflater.from(this);
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.maincontent);
//        if (PamramUtils.isInAudit()) {
//            initAuditTabHost();
//        } else {
            initTabHost();
//        }

        if (getIntent() != null) {
            onPush(getIntent());
        }
        registerReceiver();
    }

    public void initAuditTabHost() {
        // 定义数组来存放Fragment界面
        Class fragmentArray[] = {ToolsLoanFragment.class, PersonalFragment.class};
        // 定义数组来存放按钮图片
        int mImageViewArray[] = {R.drawable.tab_icon_loan, R.drawable.tab_icon_me};
        // Tab选项卡的文字
        final String mTextviewArray[] = {"贷款试算", "个人中心"};
        fragmentTabHost.clearAllTabs();
        // 得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(mImageViewArray[i], mTextviewArray[i]));
            // 将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
            fragmentTabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        }

    }

    public void initTabHost() {
        // 定义数组来存放Fragment界面
        Class fragmentArray[] = {HomeFragment.class,
                LoanFragment.class, CardFragment.class, PersonalFragment.class,};
        // 定义数组来存放按钮图片
        int mImageViewArray[] = {R.drawable.tab_icon_home, R.drawable.tab_icon_loan, R.drawable.tab_icon_card, R.drawable.tab_icon_me};
        // Tab选项卡的文字
        final String mTextviewArray[] = {"亿龙贷", "贷款超市", "信用卡", "个人中心"};
        // 得到fragment的个数
        int childCount = fragmentTabHost.getTabWidget().getChildCount();
        int count = fragmentArray.length;
        if (childCount == count) {
            //tabhost的数量等于Fragment的数量就不执行
            return;
        }
        fragmentTabHost.clearAllTabs();
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(mImageViewArray[i], mTextviewArray[i]));
            // 将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
            fragmentTabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        }
        UmengUtils.tabHome(MainActivity.this);
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if (mTextviewArray[0].equals(s)) {
                    UmengUtils.tabHome(MainActivity.this);

                } else if (mTextviewArray[1].equals(s)) {
                    UmengUtils.tabLoan(MainActivity.this);

                } else if (mTextviewArray[2].equals(s)) {
                    UmengUtils.tabCard(MainActivity.this);

                } else if (mTextviewArray[3].equals(s)) {
                    UmengUtils.tabPersonal(MainActivity.this);
                }
            }
        });
        fragmentTabHost.setCurrentTab(1);
        fragmentTabHost.setCurrentTab(0);

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int resid, String title) {
        View view = layoutInflater.inflate(R.layout.tab_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(resid);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(title);
        return view;
    }

    @Override
    public void initData() {
        getdata();
    }


    private void getdata() {
        if (PamramUtils.isInAudit())
            return;
        HttpUtil.getgetPopMsg(Tools.getPopMsgVersio(0) + "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                JsonElement jsonElement = jsonObject.get("data");
                if (jsonElement != null && !jsonElement.isJsonNull()) {
                    JsonObject data = jsonElement.getAsJsonObject();
                    final int currentVersion = data.get("version").getAsInt();
                    final int localVersion = Tools.getPopMsgVersio(0);
                    if (localVersion < currentVersion) {
                        Tools.writePopMsgVersion(currentVersion);
                        Tools.writeFile(MainActivity.this, PublicDef.WEB_POPMSG_FILE, data.toString());
                    } else {
                        String json = Tools.readFile(MainActivity.this, PublicDef.WEB_POPMSG_FILE);
                        data = new JsonParser().parse(json).getAsJsonObject();
                    }
                    JsonElement urlElement = data.get("accessUrl");
                    if (urlElement != null && !urlElement.isJsonNull()) {
                        final String accessUrl = data.get("accessUrl").getAsString();
                        final String showImg = data.get("showImg").getAsString();
                        fragmentTabHost.post(new Runnable() {
                            @Override
                            public void run() {
                                if (localVersion < currentVersion) {
                                    new PopDialog(MainActivity.this, R.style.CustomDialog, showImg, accessUrl);
                                } else {
                                    long popdata = Tools.getPopMsgData(0);
                                    if (System.currentTimeMillis() - popdata > 3600000 * 24) {
                                        new PopDialog(MainActivity.this, R.style.CustomDialog, showImg, accessUrl);
                                    }
                                }
                            }
                        });
                    }

                }
            }
        });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        getdata();
        PamramUtils.getIsRefresh(MainActivity.this, Tools.getPackageName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onPush(intent);
    }

    //点击通知后页面跳转
    private void onPush(Intent intent) {
        if (PamramUtils.isInAudit()) {
            return;
        }
        if (intent.getStringExtra(PublicDef.PUSH_ASSIGN_TABID) != null) {
            String type = intent.getStringExtra(PublicDef.PUSH_ASSIGN_TABID);
            //跟type的值判断切换到第几页
            switch (type) {
                case "home":
                    fragmentTabHost.setCurrentTab(0);
                    break;
                case "loan market":
                    fragmentTabHost.setCurrentTab(1);
                    break;
                case "credit market":
                    fragmentTabHost.setCurrentTab(2);
                    break;
                default:
                    fragmentTabHost.setCurrentTab(0);
                    break;
            }
        } else if (intent.getStringExtra(PublicDef.PUSH_ASSIGN_URL) != null) {
            String url = intent.getStringExtra(PublicDef.PUSH_ASSIGN_URL);
            BaseActivityWeb.start(MainActivity.this, url, "");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Broadcast != null)
            unregisterReceiver(Broadcast);
    }

    private void getWebProperty() {

        HttpUtil.LoadUserInfoConfigMethod(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Gson gson = new Gson();
                WebSetJsonData data = gson.fromJson(jsonStr, WebSetJsonData.class);
                DataBean dataBean = data.getData();
                int localVersion = Tools.getWebSetVersion(0);
                if (dataBean != null) {
                    int currentVersion = dataBean.getVersion();
                    if (localVersion < currentVersion) {
                        Tools.writeWebSetVersion(currentVersion);
                        Tools.writeFile(MainActivity.this, PublicDef.WEB_PROPERTY_FILE, jsonStr);
                    }
                }
            }
        });
    }

    //注册广播接受者
    private void registerReceiver() {
        Broadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PublicDef.ACTION_REFRESH)) {
                    if (PamramUtils.isInAudit()) {
                        initAuditTabHost();
                    } else {
                        initTabHost();
                    }
                }
            }
        };
        IntentFilter intent = new IntentFilter();
        intent.addAction(PublicDef.ACTION_REFRESH);
        registerReceiver(Broadcast, intent);
    }

    private void ScoreDialogDefault() {
        Tools.writeSubmitFlag(0);
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                getString(R.string.scoreDialog_content))//
                .btnText(getString(R.string.scoreDialog_cancel), getString(R.string.scoreDialog_confirm))//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        try {
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (Exception e) {
                            ToolsCredit.NormalDialog(getString(R.string.dialog_notHaveMarket), MainActivity.this);
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }
        );
    }
}
