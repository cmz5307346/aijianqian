package com.ajq.aijieqian102.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.activity.ToolsBaseActivity;
import com.ajq.aijieqian102.bean.ToolItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ToolItem> mDatas;

    public RecyclerViewAdapter(Context context, List<ToolItem> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView mTxt;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.item_personal_tools,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.tv_tools);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.iv_tools);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mImg.setImageResource(mDatas.get(i).getImageId());
        viewHolder.mTxt.setText(mDatas.get(i).getText());
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ToolsBaseActivity.class);
                intent.putExtra("title", mDatas.get(i).getText());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

