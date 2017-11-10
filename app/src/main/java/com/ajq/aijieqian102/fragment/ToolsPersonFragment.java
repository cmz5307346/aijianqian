package com.ajq.aijieqian102.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseFragment;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolsPersonFragment extends BaseFragment {
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tools_person;
    }

    @Override
    public void initView(View rootView) {
        // 社保与住房公积金缴费金额
        final TextView tvYanglaoNum = (TextView) rootView.findViewById(R.id.tvYanglaoNum);
        final TextView tvYiliaoNum = (TextView) rootView.findViewById(R.id.tvYiliaoNum);
        final TextView tvShiyeNum = (TextView) rootView.findViewById(R.id.tvShiyeNum);
        final TextView tvZhufangNum = (TextView) rootView.findViewById(R.id.tvZhufangNum);
        // 税前税后以及个人所得税金额
        final EditText etShuiqianNum = (EditText) rootView.findViewById(R.id.etShuiqianNum);
        final EditText etShuihouNum = (EditText) rootView.findViewById(R.id.etShuihouNum);
        final EditText etGeshui = (EditText) rootView.findViewById(R.id.etGeshui);
        // 社保与住房公积金缴费比例
        final EditText etYanglao = (EditText) rootView.findViewById(R.id.etYanglao);
        final EditText etYiliao = (EditText) rootView.findViewById(R.id.etYiliao);
        final EditText etShiye = (EditText) rootView.findViewById(R.id.etShiye);
        final EditText etZhufang = (EditText) rootView.findViewById(R.id.etZhufang);
        // 社保与住房公积金缴费基数
        final EditText etShebaoBase = (EditText) rootView.findViewById(R.id.etShebaoBase);
        final EditText etYiliaoBase = (EditText) rootView.findViewById(R.id.etYiliaoBase);
        final EditText etZhufangBase = (EditText) rootView.findViewById(R.id.etZhufangBase);
        final EditText etQizhengdianBase = (EditText) rootView.findViewById(R.id.etQizhengdianBase);

// 计算
        Button btnJisuan = (Button) rootView.findViewById(R.id.btnJisuan);
        etShuiqianNum.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // 把输入内容同步显示在社保基数，医疗基数，住房公积金基数中
                etShebaoBase.setText(etShuiqianNum.getText());
                etYiliaoBase.setText(etShuiqianNum.getText());
                etZhufangBase.setText(etShuiqianNum.getText());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }


            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

        });
        btnJisuan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 计算个税
                String strShuiqian = etShuiqianNum.getText().toString();
                if (strShuiqian.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入税前收入！")
                            .setPositiveButton("确定", null)
                            .show();
                    etShuiqianNum.requestFocus();
                    return;
                }

                String strYanglao = etYanglao.getText().toString();
                if (strYanglao.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入养老缴费比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    etYanglao.requestFocus();
                    return;
                }
                String strYiliao = etYiliao.getText().toString();
                if (strYiliao.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入医疗缴费比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    etYiliao.requestFocus();
                    return;
                }
                String strShiye = etShiye.getText().toString().trim();
                if (strShiye.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入失业缴费比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    etShiye.requestFocus();
                    return;
                }
                String strZhufang = etZhufang.getText().toString();
                if (strZhufang.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入住房公积金缴费比例！")
                            .setPositiveButton("确定", null)
                            .show();
                    etZhufang.requestFocus();
                    return;
                }
                String strShebaoBase = etShebaoBase.getText().toString();
                if (strShebaoBase.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入社保缴费基数！")
                            .setPositiveButton("确定", null)
                            .show();
                    etShebaoBase.requestFocus();
                    return;
                }
                String strYiliaoBase = etYiliaoBase.getText().toString();
                if (strYiliaoBase.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入医疗缴费基数！")
                            .setPositiveButton("确定", null)
                            .show();
                    etYiliaoBase.requestFocus();
                    return;
                }
                String strZhufangBase = etZhufangBase.getText().toString();
                if (strZhufangBase.length() <= 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入住房公积金缴费基数！")
                            .setPositiveButton("确定", null)
                            .show();
                    etZhufangBase.requestFocus();
                    return;
                }
                String strQizhengdianBase = etQizhengdianBase.getText().toString();
                if (strQizhengdianBase.length() <= 0) {
                    Toast.makeText(getActivity(), "请输入个人所得税起征点！",
                            Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("错误提示")
                            .setMessage("请输入个人所得税起征点！")
                            .setPositiveButton("确定", null)
                            .show();
                    etQizhengdianBase.requestFocus();
                    return;
                }

                try {
                    float shuiqianNum = Float.parseFloat(strShuiqian);
                    float yanglaoNum = Float.parseFloat(strYanglao);
                    float yiliaoNum = Float.parseFloat(strYiliao);
                    float shiyeNum = Float.parseFloat(strShiye);
                    float zhufangNum = Float.parseFloat(strZhufang);
                    float shebaoBase = Float.parseFloat(strShebaoBase);
                    float yiliaoBase = Float.parseFloat(strYiliaoBase);
                    float zhufangBase = Float.parseFloat(strZhufangBase);
                    float qizhengdianBase = Float.parseFloat(strQizhengdianBase);

                    // 养老缴纳金额
                    float yanglao = shebaoBase * yanglaoNum * 0.01f;
                    tvYanglaoNum.setText(String.valueOf(yanglao));

                    // 医疗缴纳金额
                    float yiliao = yiliaoBase * yiliaoNum * 0.01f + 3;
                    tvYiliaoNum.setText(String.valueOf(yiliao));

                    // 失业缴纳金额
                    float shiye = shebaoBase * shiyeNum * 0.01f;
                    tvShiyeNum.setText(String.valueOf(shiye));

                    // 住房公积金缴纳金额
                    float zhufang = zhufangBase * zhufangNum * 0.01f;
                    tvZhufangNum.setText(String.valueOf(zhufang));

                    // 计算个税
                    float shuihou = shuiqianNum - yanglao - yiliao - shiye - zhufang;
                    float geshui = 0f;
                    if (shuihou > qizhengdianBase) {
                        // 需要交税
                        float jiaoshuiBase = shuihou - qizhengdianBase;
                        if (jiaoshuiBase <= 500) {
                            geshui = jiaoshuiBase * 0.05f;
                        } else if (jiaoshuiBase > 500 && jiaoshuiBase <= 2000) {
                            geshui = jiaoshuiBase * 0.1f - 25;
                        } else if (jiaoshuiBase > 2000 && jiaoshuiBase <= 5000) {
                            geshui = jiaoshuiBase * 0.15f - 125;
                        } else if (jiaoshuiBase > 5000 && jiaoshuiBase <= 20000) {
                            geshui = jiaoshuiBase * 0.2f - 375;
                        } else if (jiaoshuiBase > 20000 && jiaoshuiBase <= 40000) {
                            geshui = jiaoshuiBase * 0.25f - 1375;
                        } else if (jiaoshuiBase > 40000 && jiaoshuiBase <= 60000) {
                            geshui = jiaoshuiBase * 0.3f - 3375;
                        } else if (jiaoshuiBase > 60000 && jiaoshuiBase <= 80000) {
                            geshui = jiaoshuiBase * 0.35f - 6375;
                        } else if (jiaoshuiBase > 80000 && jiaoshuiBase <= 100000) {
                            geshui = jiaoshuiBase * 0.4f - 10375;
                        } else if (jiaoshuiBase > 100000) {
                            geshui = jiaoshuiBase * 0.45f - 15375;
                        }
                    }
                    shuihou -= geshui;
                    if (shuihou < 0f) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("错误提示")
                                .setMessage("税后收入为负数，请检查输入是否正确！")
                                .setPositiveButton("确定", null)
                                .show();
                        etShuihouNum.setTextColor(Color.RED);
                    } else {
                        etShuihouNum.setTextColor(Color.BLUE);
                    }
                    etShuihouNum.setText(String.valueOf(shuihou));
                    etGeshui.setText(String.valueOf(geshui));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "parseFloat ERROR!!!");
                }
            }
        });
    }
}
