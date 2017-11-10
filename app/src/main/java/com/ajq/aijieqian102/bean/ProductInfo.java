package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ProductInfo implements Serializable {

    /**
     * accessUrl : https://m.ppdai.com/act/cpa/cpa_tenth.html?source=xiamenyouyu&url=
     * icon : https://www.youyunet.com/server-images/v2product/2017-07-27/1501159342595.png
     * id : 7
     * name : 曹操贷
     * proDescribe : 千元极速贷，通过率97%
     * sort : 50
     * tag : star
     * tagIcon : https://www.youyunet.com/server-images/v2poducttag/2017-07-27/1501140246436.png
     * title : 借钱不用还，100免单名额
     */

    private String accessUrl;
    private String icon;
    private String id;
    private String name;
    private String proDescribe;
    private String sort;
    private String tag;
    private String tagIcon;
    private String title;

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProDescribe() {
        return proDescribe;
    }

    public void setProDescribe(String proDescribe) {
        this.proDescribe = proDescribe;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagIcon() {
        return tagIcon;
    }

    public void setTagIcon(String tagIcon) {
        this.tagIcon = tagIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        ProductInfo reportInfo = (ProductInfo) obj;
        return id.equals(reportInfo.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
