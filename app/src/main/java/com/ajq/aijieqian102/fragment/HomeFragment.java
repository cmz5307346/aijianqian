package com.ajq.aijieqian102.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.activity.ProductInfoActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.adapter.HotAdapter;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.component.EmptyViewManager;
import com.ajq.aijieqian102.component.YYGridView;
import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.ImageLoader;
import com.ajq.aijieqian102.util.PamramUtils;
import com.ajq.aijieqian102.util.Tools;
import com.ajq.aijieqian102.util.UmengUtils;
import com.ajq.creditapp.activity.CreditActivity;
import com.ajq.creditapp.bean.ReportInfo;
import com.ajq.creditapp.util.ToolsCredit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/6/19.
 */

public class HomeFragment extends BaseFragment {
    private BroadcastReceiver receiver;
    private YYGridView mGridView;
    private TextView mTitle;
    private TextView mAppName, mAppTitle;
    private TextView mSum, mUnit;
    private TextView mTagLeft, mTagRight;
    private TextView mBtn, mHotTitle;
    private ImageView mIcon, mRecommend;
    private HotAdapter hotAdapter;
    private LinearLayout mLl_renhang;
    private TextView mTv_time;
    private TextView mTv_staus;
    private EmptyViewManager emptyViewManager;
    private View loan_content;

    @Override
    public void init() {
        hotAdapter = new HotAdapter(getActivity());
        registerReceiver();
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View rootView) {
        emptyViewManager = (EmptyViewManager) rootView.findViewById(R.id.EmptyViewManager);
        mTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mAppName = (TextView) rootView.findViewById(R.id.home_name);
        mAppTitle = (TextView) rootView.findViewById(R.id.home_title);
        mIcon = (ImageView) rootView.findViewById(R.id.home_icon);
        mRecommend = (ImageView) rootView.findViewById(R.id.home_recommend);
        mSum = (TextView) rootView.findViewById(R.id.tv_sum);
        mUnit = (TextView) rootView.findViewById(R.id.unit);
        mTagLeft = (TextView) rootView.findViewById(R.id.home_tagLeft);
        mTagRight = (TextView) rootView.findViewById(R.id.home_tagRight);
        mHotTitle = (TextView) rootView.findViewById(R.id.home_hottitle);
        mGridView = (YYGridView) rootView.findViewById(R.id.home_gridview);
        mBtn = (TextView) rootView.findViewById(R.id.home_btn);
        mLl_renhang = (LinearLayout) rootView.findViewById(R.id.lv_renhang);
        mTv_staus = (TextView) rootView.findViewById(R.id.tv_home_status);
        mTv_time = (TextView) rootView.findViewById(R.id.tv_home_time);
        loan_content = rootView.findViewById(R.id.loan_content);
        mTitle.setText(getString(R.string.app_name));
        mGridView.setAdapter(hotAdapter);
        //判断是否登录
        if (ToolsCredit.getLoginStatus(PublicDef.NO_LOGINED).equals(PublicDef.LOGINED)) {
            drawLogined();
        }
    }

    private void drawLogined() {
        mTv_staus.setText(getTitleStatus());
        if (!ToolsCredit.getLogInData(ToolsCredit.getLogInName(""), "0").equals("0")) {
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(ToolsCredit.getLogInData(ToolsCredit.getLogInName(""), "0"));
            String retStrFormatNowDate = sdFormatter.format(date);
            mTv_time.setText("最后更新时间:" + retStrFormatNowDate);
        }
    }

    public void drawLogoff() {
        mTv_staus.setText("查看你的银行信用报告");
        mTv_time.setText("独家数据 官方解读");
    }

    @Override
    public void initData() {
        getData();
        getLoanData();
    }

    @Override
    public void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductInfo productInfo = hotAdapter.getItem(i);
                Tools.saveRecord(productInfo);
                Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", productInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengUtils.homeRecommend(getContext());
//                BaseActivityWeb.start(getActivity(), PamramUtils.getaccessUrl(), PamramUtils.getappName());
                BaseActivityWeb.start(getActivity(), "https://m.iqianzhan.com/outsideRegister.html?qd=aijieqian1", "大额周转 急速审批 闪电到账");
            }
        });
        mLl_renhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUtils.credit(getContext());
//                Intent intent = new Intent(getActivity(), CreditActivity.class);
//                startActivity(intent);
                Toast.makeText(getActivity(),"该功能暂未开放，敬请期待！",Toast.LENGTH_LONG).show();
            }
        });
        emptyViewManager.setEmptyInterface(new EmptyViewManager.EmptyInterface() {
            @Override
            public void doRetry() {
                getData();
            }
        });
    }

    private void initLoanView() {

        if (PamramUtils.getappName().equals("")) {
            loan_content.setVisibility(View.GONE);
        } else {
            loan_content.setVisibility(View.VISIBLE);
            mAppName.setText(PamramUtils.getappName());
            mAppTitle.setText(PamramUtils.gettitle());
            ImageLoader.loadCenterCrop(getActivity(), PamramUtils.geticonUrl(), mIcon);
            Glide.with(getActivity()).load(PamramUtils.getamountUrl()).dontAnimate().into(mRecommend);
            mSum.setText(getnumber(PamramUtils.getamount()));
            mUnit.setText(getUnit(PamramUtils.getamount()));
            mTagLeft.setText(PamramUtils.gettagLeft());
            mTagRight.setText(PamramUtils.gettagRight());
            mBtn.setText(PamramUtils.getbtnTitle());
            mHotTitle.setText(PamramUtils.gethomeTitle());
        }


    }

    private void getData() {
        if (hotAdapter.getCount() == 0) {
            emptyViewManager.setLoading();
        } else {
            emptyViewManager.setNormal();
        }
        HttpUtil.getHomeProductList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mGridView.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyViewManager.setRetry();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                int code = jsonObject.get("ErrorCode").getAsInt();
                if (code == 0) {
                    Gson gson = new Gson();
                    final ArrayList<ProductInfo> arrayList = new ArrayList<ProductInfo>();
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    int pageIndex = data.get("pageIndex").getAsInt();
                    int totalCount = data.get("totalCount").getAsInt();
                    JsonArray jsonArray = data.get("products").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ProductInfo productInfo = gson.fromJson(jsonArray.get(i).getAsJsonObject(), ProductInfo.class);
                        arrayList.add(productInfo);
                    }
                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (arrayList.size() == 0) {
                                emptyViewManager.setNodata();
                            } else {
                                emptyViewManager.setNormal();
                            }

                            hotAdapter.setDatas(arrayList);
                        }
                    });

                }
            }
        });

    }

    private void getLoanData() {

        HttpUtil.GetOnlineParam(PamramUtils.APPTYPE_LOAN, Tools.getPackageName(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mGridView.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyViewManager.setRetry();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonStr = response.body().string();
                Log.d("HomeFragment", "onResponse: "+jsonStr);
                String paramsjosn;
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("data");
                    Log.d("HomeFragment", "onResponse: "+jsonObject.toString());
                    paramsjosn = jsonObject.getString("params");
                    int currentVersion = jsonObject.getInt("version");
                    int localVersion = Tools.getOnlineParamVersion(0);
                    Log.d("HomeFragment", "localVersion: "+localVersion);
                    if (localVersion <= currentVersion) {
                        Tools.writeOnlineParamVersion(currentVersion);
                        Tools.writeFile(getActivity(), PublicDef.WEB_ONLINEPARAM_FILE, paramsjosn);

                    } else {
                        paramsjosn = Tools.readFile(getActivity(), PublicDef.WEB_ONLINEPARAM_FILE);
                    }
                    Log.d("HomeFragment", "onResponse: "+paramsjosn.toString());
                    PamramUtils.setParamsMap(PamramUtils.jsonToObject(paramsjosn));
                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            initLoanView();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String getnumber(String str) {
        if (str.equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        m.find();
        return m.group();
    }

    private String getUnit(String str) {
        if (str.equals("")) {
            return "";
        }
        String reg = "[^\u4e00-\u9fa5]";
        return str.replaceAll(reg, "");
    }

    private String getTitleStatus() {
        List<ReportInfo> data = ToolsCredit.getDataList(ToolsCredit.getLogInName(""));
        if (ToolsCredit.getType(0) == 4) {
            if (data.size() != 0) {
                try {
                    ReportInfo info = data.get(0);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long time = format.parse(info.getCreateTime()).getTime();
                    if (System.currentTimeMillis() - time < 1000 * 3600 * 24 * 30) {
                        return "点滴信用，贵在累积！";
                    } else if (System.currentTimeMillis() - time < 1000 * 3600 * 24 * 90) {
                        return "关注信用，就是关注财富！";
                    } else {
                        return "你的信用信息可能有更新哦！";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                if (ToolsCredit.getIsAnswer(ToolsCredit.getLogInName(""), false)) {
                    return "身份验证未通过！";
                } else {
                    return "你未进行身份验证！";
                }

            }

        } else if (ToolsCredit.getType(0) == 1) {
            ToolsCredit.writIsBuild(ToolsCredit.getLogInName(""), true);
            return "报告申请中";
        } else if (ToolsCredit.getType(0) == 2) {
            if (data.size() != 0) {
                return "点滴信用，贵在累积！";
            } else {
                if (ToolsCredit.getIsBuild(ToolsCredit.getLogInName(""), true)) {
                    ToolsCredit.writIsBuild(ToolsCredit.getLogInName(""), false);
                }
                return "新报告已生成";
            }
        }
        return "";
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PublicDef.LOGIN)) {
                    drawLogined();
                } else if (intent.getAction().equals(PublicDef.LOG_OFF)) {
                    drawLogoff();
                } else if (intent.getAction().equals(PublicDef.ACTION_REFRESH)) {
                    initLoanView();
                }
            }
        };
        IntentFilter intent = new IntentFilter();
        intent.addAction(PublicDef.LOGIN);
        intent.addAction(PublicDef.LOG_OFF);
        intent.addAction(PublicDef.ACTION_REFRESH);
        getActivity().registerReceiver(receiver, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

}
