package com.ajq.aijieqian102.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseActivity;
import com.ajq.aijieqian102.base.BaseActivityWeb;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.bean.ProductInfoBean;
import com.ajq.aijieqian102.component.AutoVerticalScrollTextView;
import com.ajq.aijieqian102.component.EmptyViewManager;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.ImageLoader;
import com.ajq.aijieqian102.util.UmengUtils;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/2.
 */
/*
    产品详情页面
 */
public class ProductInfoActivity extends BaseActivity {
    private ImageView mIv_icon;
    private TextView mTv_name, mTv_subTitle, mTv_loanCycle, mTv_loanRange, mTv_loanDayRate, mTv_loanDayRate2;
    private TextView mTv_auditWay, mTv_auditCycle, mTv_grantTime, mTv_returnWay;
    private TextView mTv_applyFlow, mTv_applyCondition;
    private TextView mTv_loanDayRateTitle, mTv_calc_date;
    private EditText mEt_money, mEt_date;
    private TextView mTv_dayMoney, mTv_grossInterest;
    private LinearLayout mLl_importantRemind, mLl_loanIntroduction, mLl_applyFlow, mLl_applyCondition;
    private LinearLayout mLl_auditWay, mLl_auditGrantTime, mLl_auditCycle, mLl_auditReturnWay;
    private GridLayout mGl_importantRemind;
    private ScrollView mSv_info;
    private LinearLayout mLl_back, mLl_i;
    private TextView mTv_title;
    private LinearLayout mLl_submit;
    private ProductInfo mProductInfo;
    private ProductInfoBean mProductInfoBean = null;
    private AutoVerticalScrollTextView mAmtv_notice;
    private ProgressBar progressBar;
    private EmptyViewManager emptyViewManager;
    private boolean isRunning = true;
    private int number = 0;
    private String[] notice;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    progressBar.setVisibility(View.GONE);
                    emptyViewManager.setRetry();
                    emptyViewManager.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    progressBar.setVisibility(View.GONE);
                    if (mProductInfoBean == null) {
                        emptyViewManager.setNodata();
                        emptyViewManager.setVisibility(View.VISIBLE);
                    } else {
                        emptyViewManager.setNormal();
                        mSv_info.setVisibility(View.VISIBLE);
                    }
                    InputData();
                    break;
                case 2:

                    break;
                case 3:
                    mAmtv_notice.setText(notice[0]);
                    new Thread() {
                        @Override
                        public void run() {
                            while (isRunning) {
                                SystemClock.sleep(3000);
                                handler.sendEmptyMessage(4);
                            }
                        }
                    }.start();
                    break;
                case 4:
                    NoticeUpdate();
                default:
                    break;
            }

        }
    };

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_productinfo;
    }

    @Override
    public void init() {
        super.init();
        Intent intent = getIntent();
        mProductInfo = (ProductInfo) intent.getSerializableExtra("product");
    }

    @Override
    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.pb_pi_progress);
        progressBar.setVisibility(View.VISIBLE);
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mLl_submit = (LinearLayout) findViewById(R.id.ll_pi_submit);
        mTv_name = (TextView) findViewById(R.id.tv_pi_productName);
        mTv_subTitle = (TextView) findViewById(R.id.tv_pi_subTitle);
        mTv_loanCycle = (TextView) findViewById(R.id.tv_pi_loanCycle);
        mTv_loanRange = (TextView) findViewById(R.id.tv_pi_loanRange);
        mTv_loanDayRate = (TextView) findViewById(R.id.tv_pi_loanDayRate);
        mTv_loanDayRate2 = (TextView) findViewById(R.id.tv_pi_loanDayRate2);
        mTv_auditWay = (TextView) findViewById(R.id.tv_pi_auditWay);
        mTv_auditCycle = (TextView) findViewById(R.id.tv_pi_auditCycle);
        mTv_grantTime = (TextView) findViewById(R.id.tv_pi_grantTime);
        mTv_returnWay = (TextView) findViewById(R.id.tv_pi_returnWay);
        mTv_applyFlow = (TextView) findViewById(R.id.tv_pi_applyFlow);
        mTv_applyCondition = (TextView) findViewById(R.id.tv_pi_applyCondition);
        mIv_icon = (ImageView) findViewById(R.id.iv_pi_icon);
        mGl_importantRemind = (GridLayout) findViewById(R.id.gl_pi_importantRemind);
        mEt_money = (EditText) findViewById(R.id.et_pi_money);
        mEt_date = (EditText) findViewById(R.id.et_pi_date);
        mTv_dayMoney = (TextView) findViewById(R.id.tv_pi_dayMoney);
        mTv_grossInterest = (TextView) findViewById(R.id.tv_pi_grossInterest);
        mAmtv_notice = (AutoVerticalScrollTextView) findViewById(R.id.tv_pi_notice);
        mTv_loanDayRateTitle = (TextView) findViewById(R.id.tv_pi_loanDayRate_title);
        mTv_calc_date = (TextView) findViewById(R.id.tv_calc_date);
        mLl_i = (LinearLayout) findViewById(R.id.ll_pi_i);
        mLl_importantRemind = (LinearLayout) findViewById(R.id.ll_pi_importantRemind);
        mLl_loanIntroduction = (LinearLayout) findViewById(R.id.ll_pi_loanIntroduction);
        mLl_applyFlow = (LinearLayout) findViewById(R.id.ll_pi_applyFlow);
        mLl_applyCondition = (LinearLayout) findViewById(R.id.ll_pi_applyCondition);
        mLl_auditWay = (LinearLayout) findViewById(R.id.ll_pi_auditWay);
        mLl_auditCycle = (LinearLayout) findViewById(R.id.ll_pi_auditCycle);
        mLl_auditGrantTime = (LinearLayout) findViewById(R.id.ll_pi_auditGrantTime);
        mLl_auditReturnWay = (LinearLayout) findViewById(R.id.ll_pi_auditReturnWay);
        emptyViewManager = (EmptyViewManager) findViewById(R.id.ev_pi_EmptyViewManager);
        mSv_info = (ScrollView) findViewById(R.id.sv_pi_info);
        mTv_title.setText(R.string.activity_pi_title);
        getData();
        HttpUtil.GetNotice(getPackageName(), "200", new Callback() {
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
                        handler.sendEmptyMessage(3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    @Override
    public void initListener() {
        super.initListener();
        mLl_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUtils.homeProduct(getApplicationContext(), mProductInfo.getName());
                BaseActivityWeb.start(ProductInfoActivity.this, mProductInfo.getAccessUrl(), mProductInfo.getName());
            }
        });
        mLl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEt_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAmtv_notice.setFocusable(true);
                mAmtv_notice.setFocusableInTouchMode(true);
                mAmtv_notice.requestFocus();
                mAmtv_notice.requestFocusFromTouch();
                if (MoneyOrDateIsNull()) {
                    Calc();
                }
            }
        });
        mEt_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MoneyOrDateIsNull()) {
                    Calc();
                }
            }
        });
        mAmtv_notice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mAmtv_notice.setFocusable(true);
                mAmtv_notice.setFocusableInTouchMode(true);
                mAmtv_notice.requestFocus();
                mAmtv_notice.requestFocusFromTouch();
            }
        });
        mLl_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog();
            }
        });
        emptyViewManager.setEmptyInterface(new EmptyViewManager.EmptyInterface() {
            @Override
            public void doRetry() {
                getData();
            }
        });
    }

    private void Calc() {
        int day = Integer.parseInt(mEt_date.getText().toString());
        int money = Integer.parseInt(mEt_money.getText().toString());
        float rate = Float.parseFloat(mProductInfoBean.getLoanRate());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        float grossInterest = (money * rate / 100) * day;
        float dayMoney = (money + grossInterest) / day;
        if (mProductInfoBean.getLoanRateType().equals("day")) {
            mTv_dayMoney.setText("日还款\n" + decimalFormat.format(dayMoney) + "");
        } else if (mProductInfoBean.getLoanRateType().equals("month")) {
            mTv_dayMoney.setText("月还款\n" + decimalFormat.format(dayMoney) + "");
        }
        mTv_grossInterest.setText("总利息\n" + decimalFormat.format(grossInterest) + "");
    }

    //判断金额和时间是否为空
    private boolean MoneyOrDateIsNull() {
        boolean flag = !mEt_money.getText().toString().equals("") && !mEt_date.getText().toString().equals("");
        //防止除数等于0
        if (mEt_date.getText().toString().equals("0")) {
            flag = false;
        }
        if (mProductInfoBean == null) {
            flag = false;
        }
        return flag;
    }

    //获取数据
    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        HttpUtil.GetProductInfo(mProductInfo.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                handler.sendMessage(msg);
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
                        mProductInfoBean = gson.fromJson(jsonElement.toString(), ProductInfoBean.class);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    //填充数据
    private void InputData() {
        if (mProductInfoBean.getProductName().equals("")) {
            mTv_name.setText(mProductInfo.getName());
        } else {
            mTv_name.setText(mProductInfoBean.getProductName());
        }
        if (mProductInfoBean.getSubTitle().equals("")) {
            mTv_subTitle.setText(mProductInfo.getProDescribe());
        } else {
            mTv_subTitle.setText(mProductInfoBean.getSubTitle());
        }
        mTv_loanCycle.setText(mProductInfoBean.getLoanCycle());
        mTv_loanRange.setText(mProductInfoBean.getLoanRange());
        mTv_loanDayRate.setText(mProductInfoBean.getLoanRate() + "%");
        if (mProductInfoBean.getLoanRateType().equals("day")) {
            mTv_loanDayRate2.setText("日利率\n" + mProductInfoBean.getLoanRate() + "%");
            mTv_loanDayRateTitle.setText("日利率");
            mTv_calc_date.setText("天");
            mEt_date.setText("30");
        } else if (mProductInfoBean.getLoanRateType().equals("month")) {
            mTv_loanDayRate2.setText("月利率\n" + mProductInfoBean.getLoanRate() + "%");
            mTv_loanDayRateTitle.setText("月利率");
            mTv_calc_date.setText("月");
            mEt_date.setText("24");
        }
//        if (mProductInfoBean.getLoanRateType().equals("day")) {
//            mTv_dayMoney.setText("日还款\n0.0元");
//        } else if (mProductInfoBean.getLoanRateType().equals("month")) {
//            mTv_dayMoney.setText("月还款\n0.0元");
//        }
        ImageLoader.loadCenterCrop(getApplicationContext(), mProductInfoBean.getIcon(), mIv_icon);
        //判断显示贷款说明
        if (mProductInfoBean.getAuditWay().equals("") && mProductInfoBean.getAuditCycle().equals("") && mProductInfoBean.getAuditGrantTime().equals("") && mProductInfoBean.getAuditReturnWay().equals("")) {
            mLl_loanIntroduction.setVisibility(View.GONE);
        } else {
            mLl_loanIntroduction.setVisibility(View.VISIBLE);
            if (!mProductInfoBean.getAuditWay().equals("")) {
                mLl_auditWay.setVisibility(View.VISIBLE);
                mTv_auditWay.setText("审核方式：" + mProductInfoBean.getAuditWay());
            } else {
                mLl_auditWay.setVisibility(View.GONE);
            }
            if (!mProductInfoBean.getAuditCycle().equals("")) {
                mLl_auditCycle.setVisibility(View.VISIBLE);
                mTv_auditCycle.setText("审核方式：" + mProductInfoBean.getAuditCycle());
            } else {
                mLl_auditCycle.setVisibility(View.GONE);
            }
            if (!mProductInfoBean.getAuditGrantTime().equals("")) {
                mLl_auditGrantTime.setVisibility(View.VISIBLE);
                mTv_grantTime.setText("审核方式：" + mProductInfoBean.getAuditGrantTime());
            } else {
                mLl_auditGrantTime.setVisibility(View.GONE);
            }
            if (!mProductInfoBean.getAuditReturnWay().equals("")) {
                mLl_auditReturnWay.setVisibility(View.VISIBLE);
                mTv_returnWay.setText("审核方式：" + mProductInfoBean.getAuditReturnWay());
            } else {
                mLl_auditReturnWay.setVisibility(View.GONE);
            }
        }
        //判断显示贷款流程
        if (!mProductInfoBean.getApplyFlow().equals("")) {
            mLl_applyFlow.setVisibility(View.VISIBLE);
            mTv_applyFlow.setText(mProductInfoBean.getApplyFlow());
        } else {
            mLl_applyFlow.setVisibility(View.GONE);
        }
        //判断显示贷款条件
        if (!mProductInfoBean.getApplyCondition().equals("")) {
            mLl_applyCondition.setVisibility(View.VISIBLE);
            String condition = mProductInfoBean.getApplyCondition().replace("\\n", "\n");
            String condition1 = condition.replace(" ", "");
            mTv_applyCondition.setText(condition1 + "");
        } else {
            mLl_applyCondition.setVisibility(View.GONE);
        }
        //判断显示关键字
        if (mProductInfoBean.getImportantRemind().length != 0) {
            mLl_importantRemind.setVisibility(View.VISIBLE);
            int count = 0;
            int row = mProductInfoBean.getImportantRemind().length / 3;
            for (int i = 0; i <= row; i++) {
                for (int j = 0; j < 3; j++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.item_pi_importantremind, null, false);
                    TextView mTv_item_name = (TextView) view.findViewById(R.id.tv_pi_item_name);
                    mTv_item_name.setText(mProductInfoBean.getImportantRemind()[count]);
                    GridLayout.Spec rowSpec = GridLayout.spec(i);     //设置它的行和列
                    GridLayout.Spec columnSpec = GridLayout.spec(j);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//                    params.setGravity(Gravity.LEFT);
                    WindowManager wm = this.getWindowManager();
                    int width = wm.getDefaultDisplay().getWidth();
                    int height = wm.getDefaultDisplay().getHeight();
                    params.width = (width - 70) / 3;
//                    params.height = height / 19;
                    params.setMargins(10, 10, 0, 0);
//                    view.setLayoutParams(params);
                    mGl_importantRemind.addView(view, params);
//                    mGl_importantRemind.addView(view);
                    count++;
                    if (mProductInfoBean.getImportantRemind().length == count) {
                        return;
                    }
                }
            }
        } else {
            mLl_importantRemind.setVisibility(View.GONE);
        }
        Calc();
    }

    private void NoticeUpdate() {
        mAmtv_notice.next();
        number++;
        mAmtv_notice.setText(notice[number % notice.length]);
    }

    private void MaterialDialog() {
        final MaterialDialog dialog = new MaterialDialog(ProductInfoActivity.this);
        dialog.isTitleShow(false)//
                .btnNum(1)
                .content(getString(R.string.dialog_text_pia))//
                .btnText(getString(R.string.confirm))//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }
        );
    }
}
