package com.ajq.aijieqian102.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.activity.HistoryActivity;
import com.ajq.aijieqian102.activity.ProductInfoActivity;
import com.ajq.aijieqian102.bean.HistoryInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HistoryInfo> mDatas;
    private HistoryActivity activity;

    public HistoryAdapter(Context context, List<HistoryInfo> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        activity = (HistoryActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTxt;
        ListView mRv;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.item_history,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.tv_history_time);
        viewHolder.mRv = (ListView) view
                .findViewById(R.id.id_recyclerview_history_item);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.mTxt.setText(mDatas.get(i).getTime());
        HistoryItemAdapter historyItemAdapter = new HistoryItemAdapter(mContext, mDatas.get(i).getmProducts());
        viewHolder.mRv.setAdapter(historyItemAdapter);
        viewHolder.mRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ProductInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", mDatas.get(i).getmProducts().get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        setListViewHeightBasedOnChildren(viewHolder.mRv);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

