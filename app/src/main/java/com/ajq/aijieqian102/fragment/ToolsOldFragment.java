package com.ajq.aijieqian102.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.base.BaseFragment;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolsOldFragment extends BaseFragment {
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tools_old;
    }

    @Override
    public void initView(View rootView) {
        final EditText et_wages = (EditText) rootView.findViewById(R.id.et_wages);
        final EditText et_sum = (EditText) rootView.findViewById(R.id.et_sum);
        TextView tv_submit = (TextView) rootView.findViewById(R.id.btn_submit);
        final TextView tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_sum.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "累计的养老金额不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                int wages = Integer.parseInt(et_wages.getText().toString());
                int sum = Integer.parseInt(et_sum.getText().toString());
                // Math.round((document.getElementById("pjgz").value*0.2 + document.getElementById("ljylj").value/120)*100)/100;
                DecimalFormat df = new DecimalFormat("#.00");
                Double reult = wages * 0.2 + sum / 120;
                tv_result.setText(df.format(reult));
            }
        });
    }
}
