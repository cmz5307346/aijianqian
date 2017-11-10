package com.ajq.aijieqian102.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.activity.MainActivity;
import com.ajq.aijieqian102.base.BaseFragment;
import com.ajq.aijieqian102.util.Tools;


/**
 * Created by Administrator on 2017/7/3.
 */

public class GuideFragment extends BaseFragment {

    private int resid;
    private int index;
    private ImageView imageView;

    public static GuideFragment newInstance(int resid, int index) {
        GuideFragment fragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resid", resid);
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_guide;
    }

    @Override
    public void init() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            resid = arguments.getInt("resid");
            index = arguments.getInt("index");
        }
    }

    @Override
    public void initView(View rootView) {
        imageView = (ImageView) rootView.findViewById(R.id.iv_guide);
        imageView.setImageResource(resid);
    }

    @Override
    public void initListener() {
        if (index == 0) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Tools.writFirst(false);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}
