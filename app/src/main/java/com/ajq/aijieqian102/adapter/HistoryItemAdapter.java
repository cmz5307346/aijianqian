package com.ajq.aijieqian102.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.util.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class HistoryItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProductInfo> mDatas;


    public HistoryItemAdapter(Context context, List<ProductInfo> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_history_item, null);
            holder.mIv_avatar = (ImageView) convertView.findViewById(R.id.item_loan_icon);
            holder.mTv_name = (TextView) convertView.findViewById(R.id.item_loan_name);
            holder.mTv_des = (TextView) convertView.findViewById(R.id.item_loan_title);
            holder.mTv_title = (TextView) convertView.findViewById(R.id.item_loan_describe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.loadCenterCrop(mContext, mDatas.get(position).getIcon(), holder.mIv_avatar);
        holder.mTv_name.setText(mDatas.get(position).getName());
        holder.mTv_des.setText(mDatas.get(position).getProDescribe());
        holder.mTv_title.setText(mDatas.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView mIv_avatar;
        TextView mTv_name;
        TextView mTv_des;
        TextView mTv_title;
    }

}


