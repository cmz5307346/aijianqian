package com.ajq.aijieqian102.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.adapter.HistoryAdapter;
import com.ajq.aijieqian102.base.BaseActivity;
import com.ajq.aijieqian102.bean.HistoryInfo;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HistoryActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private LinearLayout mLl_back;
    private Map<String, List<ProductInfo>> mDatas;
    private List<HistoryInfo> mInfos = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private HistoryAdapter historyAdapter;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //更新View
                    historyAdapter = new HistoryAdapter(HistoryActivity.this, mInfos);
                    mRecyclerView.setAdapter(historyAdapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_history;
    }

    @Override
    public void init() {
        mDatas = Tools.getDataMap();
        Set<String> keys = mDatas.keySet();
        for (String key : keys) {
            mInfos.add(new HistoryInfo(key, mDatas.get(key)));
        }

    }


    @Override
    public void initView() {
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDatas();

    }

    @Override
    public void initListener() {
        mLl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {

    }

    public void getDatas() {
        final StringBuilder finalStr = new StringBuilder();
        HttpUtil.getProductList("", "", 1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Collections.reverse(mInfos); // 倒序排列
                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                finalStr.append(response.body().string());
                filter(finalStr, mInfos);
                Collections.reverse(mInfos); // 倒序排列
                myHandler.sendEmptyMessage(1);
            }
        });
    }

    //过滤下线产品
    private void filter(StringBuilder str, List<HistoryInfo> mDatas) {
        String string = str.toString();
        for (HistoryInfo i : mDatas) {
            Iterator<ProductInfo> ip = i.getmProducts().iterator();
            while (ip.hasNext()) {
                ProductInfo p = ip.next();
                if (!string.contains(p.getName())) {
                    ip.remove();
                }
            }
            if (i.getmProducts().size() == 0) {
                mDatas.remove(i);
            }
        }
    }
}
