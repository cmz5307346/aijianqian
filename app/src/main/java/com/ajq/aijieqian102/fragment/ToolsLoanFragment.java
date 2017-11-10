package com.ajq.aijieqian102.fragment;


import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseFragment;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolsLoanFragment extends BaseFragment {
    private EditText et_busloan;
    private EditText et_busrate;
    private EditText et_pubrate;
    private EditText et_publoan;
    private TextView tv_paytotalshow;
    private TextView tv_interestshow;
    private TextView tv_monthshow;
    private TextView tv_loantotalshow;
    private TextView tv_monthnumshow;
    private LinearLayout ll_month;
    private Button btn_start;
    private Spinner spn_year, spn_loantype, spn_paytype, spn_rate;
    private ScrollView sc_loancounter;
    //贷款类型
    int loantype_id = 0;
    String loantype = "";
    //还款方式
    int paytype_id = 0;
    String paytype = "";
    //贷款总额
    String busloan = "";
    String publoan = "";
    //按揭年数
    int year_id = 0;
    String year = "";
    //贷款利率
    int rate_id;
    String busratemonth = "";
    String pubratemonth = "";
    //年利率
    double busrate_year = 0;
    double pubrate_year = 0;
    //月利率
    double busrate_month = 0;
    double pubrate_month = 0;
    //商业贷款与公积金贷款利率数组
    double[] arr_busrate = {4.78, 3.7, 3.05, 4.35, 5.6, 3.91, 3.22, 4.6, 5.34, 4.12, 3.4, 4.85,
            5.61, 4.34, 3.57, 5.1, 5.89, 4.55, 3.74, 5.35, 6.16, 4.76, 3.92, 5.6};
    double[] arr_pubrate = {3.03, 2.34, 1.93, 2.75, 3.03, 2.34, 1.93, 2.75, 3.3, 2.55, 2.1, 3,
            3.58, 2.76, 2.28, 3.25, 3.85, 2.97, 2.45, 3.5, 3.75, 3.75, 3.75, 3.75};
    double[] arr_buspubrate = {4.78, 3.7, 3.05, 4.35, 5.6, 3.91, 3.22, 4.6, 5.34, 4.12, 3.4, 4.85,
            5.61, 4.34, 3.57, 5.1, 5.89, 4.55, 3.74, 5.35, 6.16, 4.76, 3.92, 5.6,
            3.03, 2.34, 1.93, 2.75, 3.03, 2.34, 1.93, 2.75, 3.3, 2.55, 2.1, 3,
            3.58, 2.76, 2.28, 3.25, 3.85, 2.97, 2.45, 3.5, 3.75, 3.75, 3.75, 3.75};
    double[] arr_rate = new double[48];

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tools_loan;
    }

    @Override
    public void initView(View rootView) {
        sc_loancounter = (ScrollView) rootView.findViewById(R.id.sc_loancounter);
        btn_start = (Button) rootView.findViewById(R.id.btn_start);
        et_busloan = (EditText) rootView.findViewById(R.id.et_busloan);
        et_busrate = (EditText) rootView.findViewById(R.id.et_busrate);
        et_pubrate = (EditText) rootView.findViewById(R.id.et_pubrate);
        et_publoan = (EditText) rootView.findViewById(R.id.et_publoan);
        tv_paytotalshow = (TextView) rootView.findViewById(R.id.tv_paytotalshow);
        tv_interestshow = (TextView) rootView.findViewById(R.id.tv_interestshow);
        tv_monthshow = (TextView) rootView.findViewById(R.id.tv_monthshow);
        tv_loantotalshow = (TextView) rootView.findViewById(R.id.tv_loantotalshow);
        tv_monthnumshow = (TextView) rootView.findViewById(R.id.tv_monthnumshow);
        ll_month = (LinearLayout) rootView.findViewById(R.id.ll_month);
        //获取下拉列表对象
        spn_year = (Spinner) rootView.findViewById(R.id.spn_year);
        spn_loantype = (Spinner) rootView.findViewById(R.id.spn_loantype);
        spn_paytype = (Spinner) rootView.findViewById(R.id.spn_paytype);
        spn_rate = (Spinner) rootView.findViewById(R.id.spn_rate);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.spnyear, R.layout.loancounter_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.spnloantype, R.layout.loancounter_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.spnpaytype, R.layout.loancounter_spinner_item);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(), R.array.spnrate, R.layout.loancounter_spinner_item);

        //设置下拉列表的风格 
        adapter1.setDropDownViewResource(R.layout.loancounter_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.loancounter_spinner_dropdown_item);
        adapter3.setDropDownViewResource(R.layout.loancounter_spinner_dropdown_item);
        adapter4.setDropDownViewResource(R.layout.loancounter_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spn_year.setAdapter(adapter1);
        spn_loantype.setAdapter(adapter2);
        spn_paytype.setAdapter(adapter3);
        spn_rate.setAdapter(adapter4);

        //设置默认值
        spn_year.setVisibility(View.VISIBLE);
        spn_loantype.setVisibility(View.VISIBLE);
        spn_paytype.setVisibility(View.VISIBLE);
        spn_rate.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //还款方式
                paytype_id = (int) spn_paytype.getSelectedItemId();
                paytype = Integer.toString(paytype_id);
                //贷款总额
                busloan = et_busloan.getText().toString();
                publoan = et_publoan.getText().toString();
                //异常判断
                if (busloan.equals("") | publoan.equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("输入错误！请重新输入！")
                            .setPositiveButton("确定", null)
                            .show();
                    return;
                }
                //按揭年数
                year_id = (int) spn_year.getSelectedItemId();
                year = Integer.toString(year_id);
                //异常判断
                if (et_busrate.getText().toString().equals("") | et_pubrate.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("输入错误！请重新输入！")
                            .setPositiveButton("确定", null)
                            .show();
                    return;
                }
                //月利率
                busrate_year = Double.valueOf(et_busrate.getText().toString());
                pubrate_year = Double.valueOf(et_pubrate.getText().toString());
                busrate_month = busrate_year / 12;
                pubrate_month = pubrate_year / 12;
                busratemonth = Double.toString(busrate_month);
                pubratemonth = Double.toString(pubrate_month);

                //异常判断
                if (busloan.equals("") | publoan.equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("输入错误！请重新输入！")
                            .setPositiveButton("确定", null)
                            .show();
                    return;
                }
                if (loantype_id == 0) {
                    if (busloan.startsWith("0")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("错误提示")
                                .setMessage("金额不能为0 ！")
                                .setPositiveButton("确定", null)
                                .show();
                        return;
                    }
                } else if (loantype_id == 1) {
                    if (publoan.startsWith("0")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("错误提示")
                                .setMessage("金额不能为0 ！")
                                .setPositiveButton("确定", null)
                                .show();
                        return;
                    }
                } else {
                    if (busloan.startsWith("0") || publoan.startsWith("0")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("错误提示")
                                .setMessage("金额不能为0 ！")
                                .setPositiveButton("确定", null)
                                .show();
                        return;
                    }
                }

//                spn_year.setVisibility(View.VISIBLE);
//                spn_loantype.setVisibility(View.VISIBLE);
//                spn_paytype.setVisibility(View.VISIBLE);
//                spn_rate.setVisibility(View.VISIBLE);
//                et_busloan.setText("");
//                et_publoan.setText("");
//                et_busrate.setText("");
//                et_pubrate.setText("");
                counter();
                sc_loancounter.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        et_busloan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!ed_values22.equals(et_busloan.getText().toString().trim().replaceAll(",", ""))) {
                    et_busloan.setText(addComma(et_busloan.getText().toString().trim().replaceAll(",", ""), et_busloan));
                    et_busloan.setSelection(addComma(et_busloan.getText().toString().trim().replaceAll(",", ""), et_busloan).length());
                }
            }
        });
    }

    //计算
    private void counter() {
        //还款总额
        double totalpay = 0;
        double buspay = 0;
        double pubpay = 0;

        //月还款总额
        double monthpay = 0;
        double busmonthpay = 0;
        double pubmonthpay = 0;
        //还款方式
        int paytype_id = Integer.valueOf(paytype).intValue();
        //贷款总额
        long busloan_double = Long.valueOf(busloan.replaceAll(",", ""));
        long publoan_double = Long.valueOf(publoan.replaceAll(",", ""));
        long loantotal_double = busloan_double + publoan_double;
        //贷款年数
        int year_id = Integer.valueOf(year).intValue();
        //月利率

        double busratemonth_double = Double.valueOf(busratemonth).doubleValue();
        double pubratemonth_double = Double.valueOf(pubratemonth).doubleValue();

        //等额本息
        if (paytype_id == 0) {
            //贷款总额、贷款月数
            tv_loantotalshow.setText(addComma(loantotal_double + "", null));
            tv_monthnumshow.setText(Integer.toString((12 * (year_id + 1))));

            ll_month.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tv_monthnumshow.getHeight()));
            //商业贷款
            if (busloan_double != 0 && publoan_double == 0) {
                busmonthpay = (busloan_double * busratemonth_double / 100 * Math.pow((1 + busratemonth_double / 100), (12 * (year_id + 1))))
                        / (Math.pow((1 + busratemonth_double / 100), (12 * (year_id + 1))) - 1);
                pubmonthpay = 0;
            }
            //公积金贷款
            if (busloan_double == 0 && publoan_double != 0) {
                busmonthpay = 0;
                pubmonthpay = (publoan_double * pubratemonth_double / 100 * Math.pow((1 + pubratemonth_double / 100), (12 * (year_id + 1))))
                        / (Math.pow((1 + pubratemonth_double / 100), (12 * (year_id + 1))) - 1);
            }
            //组合型
            if (busloan_double != 0 && publoan_double != 0) {
                busmonthpay = (busloan_double * busratemonth_double / 100 * Math.pow((1 + busratemonth_double / 100), (12 * (year_id + 1))))
                        / (Math.pow((1 + busratemonth_double / 100), (12 * (year_id + 1))) - 1);
                pubmonthpay = (publoan_double * pubratemonth_double / 100 * Math.pow((1 + pubratemonth_double / 100), (12 * (year_id + 1))))
                        / (Math.pow((1 + pubratemonth_double / 100), (12 * (year_id + 1))) - 1);
            }
            monthpay = busmonthpay + pubmonthpay;
            //设定输出格式，保留小数点后两位
            DecimalFormat df = new DecimalFormat("#0.00");
            tv_monthshow.setText(addComma(df.format(monthpay), null));
            tv_paytotalshow.setText(addComma(df.format(monthpay * 12 * (year_id + 1)), null));
            tv_interestshow.setText(addComma(df.format(monthpay * 12 * (year_id + 1) - loantotal_double), null));
        }
        //等额本金
        else if (paytype_id == 1) {
            //贷款总额、贷款月数
            tv_loantotalshow.setText(addComma(loantotal_double + "", null));
            tv_monthnumshow.setText(Integer.toString((12 * (year_id + 1))));
            ll_month.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //商业贷款
            if (busloan_double != 0 && publoan_double == 0) {
                //设定输出格式，保留小数点后四位
                DecimalFormat df = new DecimalFormat("#.00");
                buspay = (12 * (year_id + 1) + 1) * busloan_double * busratemonth_double / 200 + busloan_double;
                pubpay = 0;

                //月还款
                double[] arr_monthpay = new double[6];
                arr_monthpay[0] = (busloan_double / (12 * (year_id + 1))) + (busloan_double - 0) * busratemonth_double / 100;
                tv_monthshow.setText("第1月：" + df.format(arr_monthpay[0]) + "\n");

                for (int i = 1; i < 6; i++) {
                    arr_monthpay[i] = (busloan_double / (12 * (year_id + 1))) + busratemonth_double / 100 * (busloan_double - ((i * ((12 * (year_id + 1)) / 6) - 1) * (busloan_double / (12 * (year_id + 1)))));
                    if (i == 5) {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]));
                    } else {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]) + "\n");
                    }

                }
            }
            //公积金贷款
            if (busloan_double == 0 && publoan_double != 0) {
                //设定输出格式，保留小数点后两位
                DecimalFormat df = new DecimalFormat("#.00");
                buspay = 0;
                pubpay = (12 * (year_id + 1) + 1) * publoan_double * pubratemonth_double / 200 + publoan_double;
                //月还款
                double[] arr_monthpay = new double[6];
                arr_monthpay[0] = (publoan_double / (12 * (year_id + 1))) + (publoan_double - 0) * pubratemonth_double / 100;
                tv_monthshow.setText("第1月：" + df.format(arr_monthpay[0]) + "\n");

                for (int i = 1; i < 6; i++) {
                    arr_monthpay[i] = (publoan_double / (12 * (year_id + 1))) + pubratemonth_double / 100 * (publoan_double - ((i * ((12 * (year_id + 1)) / 6) - 1) * (publoan_double / (12 * (year_id + 1)))));
                    if (i == 5) {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]));
                    } else {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]) + "\n");
                    }
                }
            }
            //组合型
            if (busloan_double != 0 && publoan_double != 0) {
                //设定输出格式，保留小数点后四位
                DecimalFormat df = new DecimalFormat("#.00");
                buspay = (12 * (year_id + 1) + 1) * busloan_double * busratemonth_double / 200 + busloan_double;
                pubpay = (12 * (year_id + 1) + 1) * publoan_double * pubratemonth_double / 200 + publoan_double;
                //月还款
                double[] arr_monthpay = new double[6];
                arr_monthpay[0] = ((busloan_double / (12 * (year_id + 1))) + (busloan_double - 0) * busratemonth_double / 100)
                        + ((publoan_double / (12 * (year_id + 1))) + (publoan_double - 0) * pubratemonth_double / 100);
                tv_monthshow.setText("第1月：" + df.format(arr_monthpay[0]) + "\n");

                for (int i = 1; i < 6; i++) {
                    arr_monthpay[i] = ((busloan_double / (12 * (year_id + 1))) + busratemonth_double / 100 * (busloan_double - ((i * ((12 * (year_id + 1)) / 6) - 1) * (busloan_double / (12 * (year_id + 1))))))
                            + ((publoan_double / (12 * (year_id + 1))) + pubratemonth_double / 100 * (publoan_double - ((i * ((12 * (year_id + 1)) / 6) - 1) * (publoan_double / (12 * (year_id + 1))))));
                    if (i == 5) {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]));
                    } else {
                        tv_monthshow.append("第" + i * ((12 * (year_id + 1)) / 6) + "月：" + df.format(arr_monthpay[i]) + "\n");
                    }
                }
            }
            //设定输出格式，保留小数点后四位
            DecimalFormat df = new DecimalFormat("#.00");
            //还款总额
            totalpay = buspay + pubpay;
            tv_paytotalshow.setText(addComma(df.format(totalpay), null));
            //支付利息款
            tv_interestshow.setText(addComma(df.format(totalpay - loantotal_double), null));

        }
    }

    class rateSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
            //贷款利率
            rate_id = (int) arg0.getSelectedItemId();
            et_busrate.setText(Double.toString(arr_rate[arg2]));
            et_pubrate.setText(Double.toString(arr_rate[arg2 + 24]));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class loantypeSelectedListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
            //贷款类型
            loantype_id = (int) arg0.getSelectedItemId();
            switch (loantype_id) {
                case 0:
                    busrate_month = 4.78 / 12;
                    pubrate_month = 0;
                    et_busloan.setEnabled(true);
                    et_publoan.setText("0");
                    et_busloan.setText("");
                    et_publoan.setEnabled(false);
                    for (int i = 0; i < 24; i++) {
                        arr_rate[i] = arr_busrate[i];
                    }
                    for (int i = 24; i < 48; i++) {
                        arr_rate[i] = 0;
                    }
                    et_pubrate.setText("0");
                    et_pubrate.setEnabled(false);
                    et_busrate.setEnabled(true);
                    et_busrate.setText(Double.toString(arr_rate[0]));
                    break;
                case 1:
                    busrate_month = 0;
                    pubrate_month = 3.03 / 12;
                    et_publoan.setEnabled(true);
                    et_publoan.setText("");
                    et_busloan.setText("0");
                    et_busloan.setEnabled(false);
                    for (int i = 0; i < 24; i++) {
                        arr_rate[i] = 0;
                    }
                    for (int i = 24; i < 48; i++) {
                        arr_rate[i] = arr_pubrate[i - 24];
                    }
                    et_busrate.setText("0");
                    et_busrate.setEnabled(false);
                    et_pubrate.setEnabled(true);
                    et_pubrate.setText(Double.toString(arr_rate[24]));
                    break;
                case 2:
                    busrate_month = 4.78 / 12;
                    pubrate_month = 3.03 / 12;
                    et_publoan.setText("");
                    et_busloan.setText("");
                    et_publoan.setEnabled(true);
                    et_busloan.setEnabled(true);
                    et_pubrate.setEnabled(true);
                    et_busrate.setEnabled(true);
                    for (int i = 0; i < 48; i++) {
                        arr_rate[i] = arr_buspubrate[i];
                    }
                    et_busrate.setText(Double.toString(arr_rate[0]));
                    et_pubrate.setText(Double.toString(arr_rate[24]));
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * 在数字型字符串千分位加逗号
     *
     * @param str
     * @param edtext
     * @return sb.toString()
     */
    private String ed_values22 = "";

    private String addComma(String str, EditText edtext) {


        if (edtext != null) {
            ed_values22 = edtext.getText().toString().trim().replaceAll(",", "");
        }
//        if (str.startsWith("0")) {
//            return ed_values22;
//        }
        boolean neg = false;
        if (str.startsWith("-")) {  //处理负数
            str = str.substring(1);
            neg = true;
        }
        String tail = null;
        if (str.indexOf('.') != -1) { //处理小数点
            tail = str.substring(str.indexOf('.'));
            str = str.substring(0, str.indexOf('.'));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ',');
        }
        sb.reverse();
        if (neg) {
            sb.insert(0, '-');
        }
        if (tail != null) {
            sb.append(tail);
        }
        return sb.toString();
    }
}
