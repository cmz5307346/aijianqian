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

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/29.
 */

public class HotAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<ProductInfo> arrayList = new ArrayList<>();

    public HotAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ProductInfo getItem(int i) {
        if (arrayList.size() == 0) {
            return null;
        } else {
            return arrayList.get(i);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDatas(ArrayList<ProductInfo> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home_gridview, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_home_icon);
            holder.name = (TextView) convertView.findViewById(R.id.item_home_name);
            holder.describe = (TextView) convertView.findViewById(R.id.item_home_describe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductInfo productInfo = arrayList.get(position);
        ImageLoader.loadCenterCrop(mContext, productInfo.getIcon(), holder.icon, R.mipmap.logo);
        holder.name.setText(productInfo.getName());
        holder.describe.setText(productInfo.getTitle());
        return convertView;
    }


    class ViewHolder {
        private TextView name;
        private TextView describe;
        private ImageView icon;
    }
}
