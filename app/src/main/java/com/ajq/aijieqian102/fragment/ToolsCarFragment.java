package com.ajq.aijieqian102.fragment;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.creditapp.util.ToolsCredit;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolsCarFragment extends BaseFragment {
    private int AutoPrices;//裸车价格
    private int traffic;//交强险
    private int ThirdParty;//第三方责任险
    private int scratch;//车身划痕险
    private int glass;//玻璃破碎险
    private int lose;//车俩损失险
    private int robbery;//全车盗抢车险
    private int autoignition;//自燃损失险
    private int disregard;//不计免赔特约险
    private int noliability;//无过责任险
    private int personnel;//车上人员责任险
    private int total;
    private String unit = "元";

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tools_car;
    }

    @Override
    public void initView(View rootView) {
        TextView btn_submit = (TextView) rootView.findViewById(R.id.btn_submit);
        final View ll_reckon = rootView.findViewById(R.id.ll_reckon);
        final View ll_result = rootView.findViewById(R.id.ll_result);
        final EditText et_autoprices = (EditText) rootView.findViewById(R.id.et_autoprices);
        final Spinner chandi_spinner = (Spinner) rootView.findViewById(R.id.chandi_spinner);
        final Spinner chezuo_spinner = (Spinner) rootView.findViewById(R.id.chezuo_spinner);
        final Spinner disanf_spinner = (Spinner) rootView.findViewById(R.id.disanf_spinner);
        final TextView tv_traffic = (TextView) rootView.findViewById(R.id.tv_traffic);
        final TextView tv_thirdparty = (TextView) rootView.findViewById(R.id.tv_thirdparty);
        final TextView tv_scratch = (TextView) rootView.findViewById(R.id.tv_scratch);
        final TextView tv_glass = (TextView) rootView.findViewById(R.id.tv_glass);
        final TextView tv_lose = (TextView) rootView.findViewById(R.id.tv_lose);
        final TextView tv_robbery = (TextView) rootView.findViewById(R.id.tv_robbery);
        final TextView tv_autoignition = (TextView) rootView.findViewById(R.id.tv_autoignition);
        final TextView tv_disregard = (TextView) rootView.findViewById(R.id.tv_disregard);
        final TextView tv_noliability = (TextView) rootView.findViewById(R.id.tv_noliability);
        final TextView tv_personnel = (TextView) rootView.findViewById(R.id.tv_personnel);
        final TextView tv_total = (TextView) rootView.findViewById(R.id.tv_total);
        final Spinner huahen_spinner = (Spinner) rootView.findViewById(R.id.huahen_spinner);
        String[] chandiItems = new String[]{"国产", "进口"};
        ArrayAdapter<String> chandiadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, chandiItems);
        chandiadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        chandi_spinner.setAdapter(chandiadapter);
        String[] chezuoItems = new String[]{"家用6座以下", "家用6座以上"};
        ArrayAdapter<String> chezuoadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, chezuoItems);
        chezuoadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        chezuo_spinner.setAdapter(chezuoadapter);
        String[] disanfItems = new String[]{"5万", "10万", "20万", "50万", "100万"};
        ArrayAdapter<String> disanfdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, disanfItems);
        disanfdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        disanf_spinner.setAdapter(disanfdapter);
        String[] huahenItems = new String[]{"2千", "5千", "1万", "2万"};
        ArrayAdapter<String> huahendapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, huahenItems);
        huahendapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        huahen_spinner.setAdapter(huahendapter);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_autoprices.getText().toString().trim().equals("")) {
                    ToolsCredit.NormalDialog("请输入裸车价格", getActivity());
                    return;
                }
                AutoPrices = Integer.parseInt(et_autoprices.getText().toString());
                if (chezuo_spinner.getSelectedItem().equals("家用6座以下")) {
                    traffic = 950;
                    personnel = 50 * 5;
                } else if (chezuo_spinner.getSelectedItem().equals("家用6座以上")) {
                    traffic = 1100;
                    personnel = 50 * 7;
                }

                String disanf = (String) disanf_spinner.getSelectedItem();
                switch (disanf) {
                    case "5万":
                        ThirdParty = 516;
                        break;
                    case "10万":
                        ThirdParty = 746;
                        break;
                    case "20万":
                        ThirdParty = 924;
                        break;
                    case "50万":
                        ThirdParty = 1252;
                        break;
                    case "100万":
                        ThirdParty = 1630;
                        break;
                }
                String huahen = (String) huahen_spinner.getSelectedItem();
                switch (huahen) {
                    case "2千":
                        scratch = 850;
                        break;
                    case "5千":
                        scratch = 1100;
                        break;
                    case "1万":
                        scratch = 1500;
                        break;
                    case "2万":
                        scratch = 2250;
                        break;
                }
                String chandi = (String) chandi_spinner.getSelectedItem();
                if (chandi.equals("进口")) {
                    glass = (int) (AutoPrices * (0.0025));
                } else {
                    glass = (int) (AutoPrices * (0.0015));
                }
                //车俩损失险=基础保费+裸车价格×1.0880%
                lose = (int) (459 + AutoPrices * 1.08880 / 100);
                //全车盗抢险=基础保费+裸车价格×0.43
                robbery = (int) (120 + AutoPrices * 0.43 / 100);
                //自燃损失险
                autoignition = (int) (AutoPrices * 0.15 / 100);
                //不计免赔特约险
                disregard = (lose + ThirdParty) * 20 / 100;
                //无过责任险
                noliability = ThirdParty * 20 / 100;
                total = lose + robbery + autoignition + disregard + noliability + traffic + ThirdParty + scratch + glass + personnel;
                tv_lose.setText(lose + unit);
                tv_robbery.setText(robbery + unit);
                tv_autoignition.setText(autoignition + unit);
                tv_disregard.setText(disregard + unit);
                tv_noliability.setText(noliability + unit);
                tv_traffic.setText(traffic + unit);
                tv_thirdparty.setText(ThirdParty + unit);
                tv_scratch.setText(scratch + unit);
                tv_glass.setText(glass + unit);
                tv_personnel.setText(personnel + unit);
                String hint = "新车保险指导价%1$s元";
                hint = String.format(hint, String.valueOf(total));
                SpannableString spannableString = new SpannableString(hint);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5c96e0")), spannableString.length() - String.valueOf(total).length() - 1, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_total.setText(spannableString);

                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

                TranslateAnimation animation = new TranslateAnimation(-wm.getDefaultDisplay().getWidth(), 0, 0, 0);
                animation.setDuration(800);
                ll_result.startAnimation(animation);
                ll_result.setVisibility(View.VISIBLE);
            }
        });

    }
}
