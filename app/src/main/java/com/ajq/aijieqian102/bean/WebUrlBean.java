package com.ajq.aijieqian102.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class WebUrlBean implements Serializable{

    public WebUrlBean(List<String> getUrl, List<String> setUrl) {
        this.getUrl = getUrl;
        this.setUrl = setUrl;
    }

    public WebUrlBean() {
    }

    private List<String> getUrl;
        private List<String> setUrl;

        public List<String> getGetUrl() {
            return getUrl;
        }

        public void setGetUrl(List<String> getUrl) {
            this.getUrl = getUrl;
        }

        public List<String> getSetUrl() {
            return setUrl;
        }

        public void setSetUrl(List<String> setUrl) {
            this.setUrl = setUrl;
        }
}
