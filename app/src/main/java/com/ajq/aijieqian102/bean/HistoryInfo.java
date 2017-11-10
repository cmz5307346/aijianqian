package com.ajq.aijieqian102.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class HistoryInfo implements Serializable {
    private String time;
    private List<ProductInfo> mProducts;

    public HistoryInfo() {
    }

    public HistoryInfo(String time, List<ProductInfo> mProducts) {
        this.time = time;
        this.mProducts = mProducts;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ProductInfo> getmProducts() {
        return mProducts;
    }

    public void setmProducts(List<ProductInfo> mProducts) {
        this.mProducts = mProducts;
    }
}
