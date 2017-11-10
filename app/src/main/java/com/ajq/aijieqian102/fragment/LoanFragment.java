package com.ajq.aijieqian102.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ajq.aijieqian102.activity.ProductInfoActivity;
import com.ajq.aijieqian102.component.AutoVerticalScrollTextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.adapter.LoanAdapter;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.aijieqian102.bean.BannerInfo;
import com.ajq.aijieqian102.bean.LocalImageHolderView;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.component.EmptyViewManager;
import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.Tools;
import com.ajq.aijieqian102.util.UmengUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LoanFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private String[] loanCycle = {"", "1m", "3m", "6m", "12m"};
    private String[] loanRange = {"", "1k", "2-5k", "5k+", "5w+"};

    private int CycleIndex = 0, RangeIndex = 0;
    private ProgressBar progressBar;
    private View loan_toplayout;
    private TextView mTitle;
    private RadioGroup heade_rg_data, heade_rg_sum;
    private RadioButton heade_datarb1, heade_datarb2, heade_datarb3, heade_datarb4, heade_datarb5;
    private RadioButton heade_sumrb1, heade_sumrb2, heade_sumrb3, heade_sumrb4, heade_sumrb5;
    private LinearLayout mLl_range, mLl_cycle, mLl_top_cycle, mLl_top_range;
    private TextView mTv_loanRangeText, mTv_rangeText, mTv_loanCycleText, mTv_CycleText;
    private TextView mTv_topRangeText, mTv_topRange, mTv_topCycleText, mTv_topCycle;
    private ImageView mIv_range, mIv_cycle, mIv_topRange, mIv_topCycle;
    private RecyclerView mRecyclerView;
    private LoanAdapter loanAdapter;
    private ConvenientBanner convenientBanner;
    private EmptyViewManager emptyViewManager;
    private AutoVerticalScrollTextView mAmtv_notice;
    ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
    private int pageIndex = 1;
    private int number = 0;
    private String[] notice;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    mAmtv_notice.setText(notice[0]);
                    new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                SystemClock.sleep(3000);
                                handler.sendEmptyMessage(2);
                            }
                        }
                    }.start();
                    break;
                case 2:
                    mAmtv_notice.next();
                    number++;
                    mAmtv_notice.setText(notice[number % notice.length]);
                    break;
            }
        }
    };

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_loan;
    }

    @Override
    public void init() {
        loanAdapter = new LoanAdapter(getActivity());
    }

    @Override
    public void initData() {
        getBanner();
        getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
    }

    @Override
    public void initView(View rootView) {
        loan_toplayout = rootView.findViewById(R.id.loan_toplayout);
        emptyViewManager = (EmptyViewManager) rootView.findViewById(R.id.EmptyViewManager);
        mTitle = (TextView) rootView.findViewById(R.id.tv_title);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loan_progress);
        mTv_topRangeText = (TextView) rootView.findViewById(R.id.tv_top_loan_loanRangeText);
        mTv_topRange = (TextView) rootView.findViewById(R.id.tv_top_loan_RangeText);
        mTv_topCycleText = (TextView) rootView.findViewById(R.id.tv_top_loan_loanCycleText);
        mTv_topCycle = (TextView) rootView.findViewById(R.id.tv_top_loan_CycleText);
        mLl_top_cycle = (LinearLayout) rootView.findViewById(R.id.ll_top_loan_loanCycle);
        mLl_top_range = (LinearLayout) rootView.findViewById(R.id.ll_top_loan_loanRange);
        mIv_topRange = (ImageView) rootView.findViewById(R.id.iv_top_loan_Range);
        mIv_topCycle = (ImageView) rootView.findViewById(R.id.iv_top_loan_Cycle);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.loan_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(loanAdapter);
        loanAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mTitle.setText("贷款超市");
        addHeadBanner();
        addHeadOption();
    }

    @Override
    public void initListener() {
        mLl_top_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_topRangeText.setTextColor(Color.rgb(51, 51, 51));
                mTv_topRange.setTextColor(Color.rgb(51, 51, 51));
                mIv_topRange.setImageResource(R.mipmap.arrow_open);
                mTv_topCycleText.setTextColor(Color.rgb(153, 153, 153));
                mTv_topCycle.setTextColor(Color.rgb(153, 153, 153));
                mIv_topCycle.setImageResource(R.mipmap.arrow_close_no);
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_loancycle, null);
                final PopupWindow window = new PopupWindow(popupView);
                window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                heade_rg_sum = (RadioGroup) popupView.findViewById(R.id.heade_rg_sum);
                heade_sumrb1 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb1);
                heade_sumrb2 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb2);
                heade_sumrb3 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb3);
                heade_sumrb4 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb4);
                heade_sumrb5 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb5);
                //设置可以获取焦点
                window.setFocusable(true);
                //设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);
                //更新popupwindow的状态
                window.update();
                //以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(mLl_top_cycle, 0, 10);
                ((RadioButton) popupView.findViewWithTag(String.valueOf(RangeIndex))).setChecked(true);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        OptionInit();
                    }
                });
                heade_rg_sum.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        switch (i) {
                            case R.id.loan_heade_sumrb1:
                                mTv_rangeText.setText(heade_sumrb1.getText());
                                mTv_topRange.setText(heade_sumrb1.getText());
                                window.dismiss();
                                RangeIndex = 0;
                                break;
                            case R.id.loan_heade_sumrb2:
                                mTv_rangeText.setText(heade_sumrb2.getText());
                                mTv_topRange.setText(heade_sumrb2.getText());
                                window.dismiss();
                                RangeIndex = 1;
                                break;
                            case R.id.loan_heade_sumrb3:
                                mTv_rangeText.setText(heade_sumrb3.getText());
                                mTv_topRange.setText(heade_sumrb3.getText());
                                window.dismiss();
                                RangeIndex = 2;
                                break;
                            case R.id.loan_heade_sumrb4:
                                mTv_rangeText.setText(heade_sumrb4.getText());
                                mTv_topRange.setText(heade_sumrb4.getText());
                                window.dismiss();
                                RangeIndex = 3;
                                break;
                            case R.id.loan_heade_sumrb5:
                                mTv_rangeText.setText(heade_sumrb5.getText());
                                mTv_topRange.setText(heade_sumrb5.getText());
                                window.dismiss();
                                RangeIndex = 4;
                                break;
                        }
                        getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
                    }
                });
            }
        });
        mLl_top_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_topRangeText.setTextColor(Color.rgb(153, 153, 153));
                mTv_topRange.setTextColor(Color.rgb(153, 153, 153));
                mIv_topRange.setImageResource(R.mipmap.arrow_close_no);
                mTv_topCycleText.setTextColor(Color.rgb(51, 51, 51));
                mTv_topCycle.setTextColor(Color.rgb(51, 51, 51));
                mIv_topCycle.setImageResource(R.mipmap.arrow_open);
                // 构建一个popupwindow的布局
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_loanrange, null);
                heade_rg_data = (RadioGroup) popupView.findViewById(R.id.heade_rg_data);
                heade_datarb1 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb1);
                heade_datarb2 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb2);
                heade_datarb3 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb3);
                heade_datarb4 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb4);
                heade_datarb5 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb5);
                final PopupWindow window = new PopupWindow(popupView, getActivity().getWindowManager().getDefaultDisplay().getWidth(), popupView.getMeasuredHeight());
                window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                //设置可以获取焦点
                window.setFocusable(true);
                //设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);
                //更新popupwindow的状态
                window.update();
                //以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(mLl_top_range, 0, 10);
                ((RadioButton) popupView.findViewWithTag(String.valueOf(CycleIndex))).setChecked(true);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        OptionInit();
                    }
                });
                heade_rg_data.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        switch (i) {
                            case R.id.loan_heade_datarb1:
                                mTv_CycleText.setText(heade_datarb1.getText());
                                mTv_topCycle.setText(heade_datarb1.getText());
                                window.dismiss();
                                CycleIndex = 0;
                                break;
                            case R.id.loan_heade_datarb2:
                                mTv_CycleText.setText(heade_datarb2.getText());
                                mTv_topCycle.setText(heade_datarb2.getText());
                                window.dismiss();
                                CycleIndex = 1;
                                break;
                            case R.id.loan_heade_datarb3:
                                mTv_CycleText.setText(heade_datarb3.getText());
                                mTv_topCycle.setText(heade_datarb3.getText());
                                window.dismiss();
                                CycleIndex = 2;
                                break;
                            case R.id.loan_heade_datarb4:
                                mTv_CycleText.setText(heade_datarb4.getText());
                                mTv_topCycle.setText(heade_datarb4.getText());
                                window.dismiss();
                                CycleIndex = 3;
                                break;
                            case R.id.loan_heade_datarb5:
                                mTv_CycleText.setText(heade_datarb5.getText());
                                mTv_topCycle.setText(heade_datarb5.getText());
                                window.dismiss();
                                CycleIndex = 4;
                                break;
                        }
                        getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
                    }
                });
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;//滑动距离

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = loanAdapter.getHeaderLayout().getChildAt(1);
                totalDy += dy;
                if (totalDy >= view.getTop()) {
                    loan_toplayout.setVisibility(View.VISIBLE);
                } else {
                    loan_toplayout.setVisibility(View.GONE);
                }
            }
        });
        loanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ProductInfo productInfo = loanAdapter.getItem(position);
                UmengUtils.loanProduct(getContext(), productInfo.getName());
                Tools.saveRecord(productInfo);
                Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", productInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        emptyViewManager.setEmptyInterface(new EmptyViewManager.EmptyInterface() {
            @Override
            public void doRetry() {
                getBanner();
                getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
            }
        });
        loan_toplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void addHeadBanner() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.heade_loan_banner, (ViewGroup) mRecyclerView.getParent(), false);
        mAmtv_notice = (AutoVerticalScrollTextView) headView.findViewById(R.id.tv_loan_notice);
        convenientBanner = (ConvenientBanner) headView.findViewById(R.id.loan_banner);
        convenientBanner.setPageIndicator(new int[]{R.mipmap.ic_indicator_nor, R.mipmap.ic_indicator_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        loanAdapter.addHeaderView(headView);
        HttpUtil.GetNotice(getActivity().getPackageName(), "200", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                    int code = jsonObject.get("ErrorCode").getAsInt();
                    if (code == 0) {
                        JsonElement jsonElement = jsonObject.get("data");
                        Gson gson = new Gson();
                        notice = gson.fromJson(jsonElement.toString(), String[].class);
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void addHeadOption() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.heade_loan_option, (ViewGroup) mRecyclerView.getParent(), false);
        mLl_range = (LinearLayout) headView.findViewById(R.id.ll_loan_loanRange);
        mLl_cycle = (LinearLayout) headView.findViewById(R.id.ll_loan_loanCycle);
        mTv_loanRangeText = (TextView) headView.findViewById(R.id.tv_loan_loanRangeText);
        mTv_rangeText = (TextView) headView.findViewById(R.id.tv_loan_RangeText);
        mTv_loanCycleText = (TextView) headView.findViewById(R.id.tv_loan_loanCycleText);
        mTv_CycleText = (TextView) headView.findViewById(R.id.tv_loan_CycleText);
        mIv_range = (ImageView) headView.findViewById(R.id.iv_loan_Range);
        mIv_cycle = (ImageView) headView.findViewById(R.id.iv_loan_Cycle);
        mLl_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_loanRangeText.setTextColor(Color.rgb(51, 51, 51));
                mTv_rangeText.setTextColor(Color.rgb(51, 51, 51));
                mIv_range.setImageResource(R.mipmap.arrow_open);
                mTv_loanCycleText.setTextColor(Color.rgb(153, 153, 153));
                mTv_CycleText.setTextColor(Color.rgb(153, 153, 153));
                mIv_cycle.setImageResource(R.mipmap.arrow_close_no);
                // 构建一个popupwindow的布局
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_loancycle, null);
                final PopupWindow window = new PopupWindow(popupView, getActivity().getWindowManager().getDefaultDisplay().getWidth(), popupView.getMeasuredHeight());
                window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                heade_rg_sum = (RadioGroup) popupView.findViewById(R.id.heade_rg_sum);
                heade_sumrb1 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb1);
                heade_sumrb2 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb2);
                heade_sumrb3 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb3);
                heade_sumrb4 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb4);
                heade_sumrb5 = (RadioButton) popupView.findViewById(R.id.loan_heade_sumrb5);
                //设置可以获取焦点
                window.setFocusable(true);
                //设置背景颜色
//                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                window.setBackgroundDrawable(new ColorDrawable(0x00000000));
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);
                //更新popupwindow的状态
                window.update();
                //以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(mLl_range, 0, 10);
                ((RadioButton) popupView.findViewWithTag(String.valueOf(RangeIndex))).setChecked(true);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        OptionInit();
                    }
                });
                heade_rg_sum.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        switch (i) {
                            case R.id.loan_heade_sumrb1:
                                mTv_rangeText.setText(heade_sumrb1.getText());
                                mTv_topRange.setText(heade_sumrb1.getText());
                                window.dismiss();
                                RangeIndex = 0;
                                break;
                            case R.id.loan_heade_sumrb2:
                                mTv_rangeText.setText(heade_sumrb2.getText());
                                mTv_topRange.setText(heade_sumrb2.getText());
                                window.dismiss();
                                RangeIndex = 1;
                                break;
                            case R.id.loan_heade_sumrb3:
                                mTv_rangeText.setText(heade_sumrb3.getText());
                                mTv_topRange.setText(heade_sumrb3.getText());
                                window.dismiss();
                                RangeIndex = 2;
                                break;
                            case R.id.loan_heade_sumrb4:
                                mTv_rangeText.setText(heade_sumrb4.getText());
                                mTv_topRange.setText(heade_sumrb4.getText());
                                window.dismiss();
                                RangeIndex = 3;
                                break;
                            case R.id.loan_heade_sumrb5:
                                mTv_rangeText.setText(heade_sumrb5.getText());
                                mTv_topRange.setText(heade_sumrb5.getText());
                                window.dismiss();
                                RangeIndex = 4;
                                break;
                        }
                        getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
                    }
                });
            }
        });
        mLl_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_loanRangeText.setTextColor(Color.rgb(153, 153, 153));
                mTv_rangeText.setTextColor(Color.rgb(153, 153, 153));
                mIv_range.setImageResource(R.mipmap.arrow_close_no);
                mTv_loanCycleText.setTextColor(Color.rgb(51, 51, 51));
                mTv_CycleText.setTextColor(Color.rgb(51, 51, 51));
                mIv_cycle.setImageResource(R.mipmap.arrow_open);
                // 构建一个popupwindow的布局
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_loanrange, null);
                heade_rg_data = (RadioGroup) popupView.findViewById(R.id.heade_rg_data);
                heade_datarb1 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb1);
                heade_datarb2 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb2);
                heade_datarb3 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb3);
                heade_datarb4 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb4);
                heade_datarb5 = (RadioButton) popupView.findViewById(R.id.loan_heade_datarb5);
                final PopupWindow window = new PopupWindow(popupView, getActivity().getWindowManager().getDefaultDisplay().getWidth(), popupView.getMeasuredHeight());
                window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                //设置可以获取焦点
                window.setFocusable(true);
                //设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);
                //更新popupwindow的状态
                window.update();
                //以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(mLl_range, 0, 10);
                ((RadioButton) popupView.findViewWithTag(String.valueOf(CycleIndex))).setChecked(true);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        OptionInit();
                    }
                });
                heade_rg_data.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        switch (i) {
                            case R.id.loan_heade_datarb1:
                                mTv_CycleText.setText(heade_datarb1.getText());
                                mTv_topCycle.setText(heade_datarb1.getText());
                                window.dismiss();
                                CycleIndex = 0;
                                break;
                            case R.id.loan_heade_datarb2:
                                mTv_CycleText.setText(heade_datarb2.getText());
                                mTv_topCycle.setText(heade_datarb2.getText());
                                window.dismiss();
                                CycleIndex = 1;
                                break;
                            case R.id.loan_heade_datarb3:
                                mTv_CycleText.setText(heade_datarb3.getText());
                                mTv_topCycle.setText(heade_datarb3.getText());
                                window.dismiss();
                                CycleIndex = 2;
                                break;
                            case R.id.loan_heade_datarb4:
                                mTv_CycleText.setText(heade_datarb4.getText());
                                mTv_topCycle.setText(heade_datarb4.getText());
                                window.dismiss();
                                CycleIndex = 3;
                                break;
                            case R.id.loan_heade_datarb5:
                                mTv_CycleText.setText(heade_datarb5.getText());
                                mTv_topCycle.setText(heade_datarb5.getText());
                                window.dismiss();
                                CycleIndex = 4;
                                break;
                        }
                        getData(loanCycle[CycleIndex], loanRange[RangeIndex]);
                    }
                });
            }
        });
        loanAdapter.addHeaderView(headView);
    }

    //初始化下拉弹窗
    private void OptionInit() {
        mTv_loanRangeText.setTextColor(Color.rgb(153, 153, 153));
        mTv_loanCycleText.setTextColor(Color.rgb(153, 153, 153));
        mTv_rangeText.setTextColor(Color.rgb(51, 51, 51));
        mTv_CycleText.setTextColor(Color.rgb(51, 51, 51));
        mIv_range.setImageResource(R.mipmap.arrow_close);
        mIv_cycle.setImageResource(R.mipmap.arrow_close);
        mTv_topRangeText.setTextColor(Color.rgb(153, 153, 153));
        mTv_topCycleText.setTextColor(Color.rgb(153, 153, 153));
        mTv_topRange.setTextColor(Color.rgb(51, 51, 51));
        mTv_topCycle.setTextColor(Color.rgb(51, 51, 51));
        mIv_topRange.setImageResource(R.mipmap.arrow_close);
        mIv_topCycle.setImageResource(R.mipmap.arrow_close);
    }

    private void getBanner() {

        HttpUtil.getBannerAd(HttpUtil.BANNERAD_TOP, Tools.getBannerAdtopVersio(0) + "", new Callback() {
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
                int localVersion = Tools.getBannerAdtopVersio(0);
                if (localVersion < currentVersion) {
                    Tools.writeBannerAdtopVersion(currentVersion);
                    Tools.writeFile(getActivity(), PublicDef.WEB_BANNERADTOP_FILE, ads.toString());
                } else {
                    String json = Tools.readFile(getActivity(), PublicDef.WEB_BANNERADTOP_FILE);
                    ads = new JsonParser().parse(json).getAsJsonArray();
                }
                Gson gson = new Gson();
                bannerInfos.clear();
                for (int i = 0; i < ads.size(); i++) {
                    BannerInfo productInfo = gson.fromJson(ads.get(i).getAsJsonObject(), BannerInfo.class);
                    bannerInfos.add(productInfo);
                }
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        convenientBanner.setPages(new CBViewHolderCreator() {
                            @Override
                            public Object createHolder() {
                                return new LocalImageHolderView();
                            }
                        }, bannerInfos).setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                BaseActivityWeb.start(getActivity(), bannerInfos.get(position).getAccessUrl(), "");
                            }
                        });
                        if (bannerInfos.size() > 1) {
                            convenientBanner.setCanLoop(true);
                            convenientBanner.setPointViewVisible(true);
                        } else {
                            convenientBanner.setCanLoop(false);
                            convenientBanner.setPointViewVisible(false);
                        }
                    }
                });
            }
        });

    }

    private void getData(String loanCycle, String loanRange) {
        pageIndex = 1;
        loanAdapter.loadMoreEnd(false);
        progressBar.setVisibility(View.VISIBLE);
        if (loanAdapter.getData().size() == 0) {

            emptyViewManager.setLoading();
        } else {
            emptyViewManager.setNormal();
        }

        HttpUtil.getProductList(loanCycle, loanRange, pageIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        emptyViewManager.setRetry();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("HomeFragment", "onResponse: "+str);
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                Log.d("............", "onResponse: "+jsonObject.toString());
                int code = jsonObject.get("ErrorCode").getAsInt();
                if (code == 0) {
                    Gson gson = new Gson();
                    final ArrayList<ProductInfo> arrayList = new ArrayList<ProductInfo>();
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    int totalCount = data.get("totalCount").getAsInt();
                    JsonArray jsonArray = data.get("products").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ProductInfo productInfo = gson.fromJson(jsonArray.get(i).getAsJsonObject(), ProductInfo.class);
                        arrayList.add(productInfo);
                    }
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            loanAdapter.setNewData(arrayList);
                            progressBar.setVisibility(View.GONE);
                            if (arrayList.size() == 0) {
                                emptyViewManager.setNodata();
                            } else {
                                emptyViewManager.setNormal();
                            }

                        }
                    });
                }
            }
        });
    }

    private void getNextData(String loanCycle, String loanRange) {
        pageIndex++;
        HttpUtil.getProductList(loanCycle, loanRange, pageIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        loanAdapter.loadMoreFail();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                Log.d("HomeFragment", "onResponse: "+jsonObject.toString());
                int code = jsonObject.get("ErrorCode").getAsInt();
                if (code == 0) {
                    Gson gson = new Gson();
                    final ArrayList<ProductInfo> arrayList = new ArrayList<ProductInfo>();
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    JsonArray jsonArray = data.get("products").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ProductInfo productInfo = gson.fromJson(jsonArray.get(i).getAsJsonObject(), ProductInfo.class);
                        arrayList.add(productInfo);
                    }
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (arrayList.size() != 0) {
                                loanAdapter.addData(arrayList);
                                loanAdapter.loadMoreComplete();
                            } else {
                                loanAdapter.loadMoreEnd(false);
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        getNextData(loanCycle[CycleIndex], loanRange[RangeIndex]);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }
}
