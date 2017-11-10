package com.ajq.aijieqian102.fragment;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseFragment;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolsLoseFragment extends BaseFragment {
    private EditText mEt_wages, mEt_standard, mEt_percentage1, mEt_percentage2;
    private EditText mEt_money, mEt_money1, mEt_money2;
    private Button mBtn_clac;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tools_lose;
    }

    @Override
    public void initView(View rootView) {
        mEt_wages = (EditText) rootView.findViewById(R.id.et_unemployed_wages);
        mEt_standard = (EditText) rootView.findViewById(R.id.et_unemployed_standard);
        mEt_percentage1 = (EditText) rootView.findViewById(R.id.et_unemployed_percentage1);
        mEt_percentage2 = (EditText) rootView.findViewById(R.id.et_unemployed_percentage2);
        mEt_money = (EditText) rootView.findViewById(R.id.et_unemployed_money);
        mEt_money1 = (EditText) rootView.findViewById(R.id.et_unemployed_money1);
        mEt_money2 = (EditText) rootView.findViewById(R.id.et_unemployed_money2);
        mBtn_clac = (Button) rootView.findViewById(R.id.btn_clac);
    }

    @Override
    public void initListener() {
        mBtn_clac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWages = mEt_wages.getText().toString();
                if (strWages.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入上年度月平均工资！")
                            .setPositiveButton("确定", null)
                            .show();
                    mEt_wages.requestFocus();
                    return;
                }
                String strStandard = mEt_standard.getText().toString();
                if (strStandard.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入本地职工上年月平均工资！")
                            .setPositiveButton("确定", null)
                            .show();
                    mEt_standard.requestFocus();
                    return;
                }
                String strPercentage1 = mEt_percentage1.getText().toString();
                if (strPercentage1.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入单位缴存比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    mEt_percentage1.requestFocus();
                    return;
                }
                String strPercentage2 = mEt_percentage2.getText().toString();
                if (strPercentage2.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入个人缴存比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    mEt_percentage2.requestFocus();
                    return;
                }
                double wages = Double.parseDouble(mEt_wages.getText().toString().trim());
                double standard = Double.parseDouble(mEt_standard.getText().toString().trim());
                double percentage1 = Double.parseDouble(mEt_percentage1.getText().toString().trim());
                double percentage2 = Double.parseDouble(mEt_percentage2.getText().toString().trim());
                if (wages > standard * 3) {
                    wages = standard * 3;
                }
                if (wages < standard * 0.6) {
                    wages = standard * 0.6;
                }
                DecimalFormat df = new java.text.DecimalFormat("#.00");
                double money_gs = Double.parseDouble(df.format(wages * (percentage1) / 100));
                double money_gr = Double.parseDouble(df.format(wages * (percentage2) / 100));
                mEt_money1.setText(String.valueOf(money_gs));
                mEt_money2.setText(String.valueOf(money_gr));
                mEt_money.setText(String.valueOf(df.format(money_gs + money_gr)));
            }
        });
    }
}
