package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class BannerInfo implements Serializable {

    /**
     * accessUrl : www.baidu.com
     * content : test
     * id : 8
     * position : market-top
     * showImg : http://120.27.131.97/image/v2bannerad/2017-07-12/1499851385742.png
     * sort : 1
     */

    private String accessUrl;
    private String content;
    private String id;
    private String position;
    private String showImg;
    private String sort;

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
