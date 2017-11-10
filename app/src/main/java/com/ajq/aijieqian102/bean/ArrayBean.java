package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class ArrayBean implements Serializable{

    public ArrayBean(ScriptAndroidBean script_android, ScriptIosBean script_ios, String webName, String webSite, WebUrlBean webUrl) {
        this.script_android = script_android;
        this.script_ios = script_ios;
        this.webName = webName;
        this.webSite = webSite;
        this.webUrl = webUrl;
    }

    public ArrayBean() {


    }

    private String webName;
        private String webSite;
        private WebUrlBean webUrl;
        private ScriptAndroidBean script_android;
        private ScriptIosBean script_ios;

        public String getWebName() {
            return webName;
        }

        public void setWebName(String webName) {
            this.webName = webName;
        }

        public String getWebSite() {
            return webSite;
        }

        public void setWebSite(String webSite) {
            this.webSite = webSite;
        }

        public WebUrlBean getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(WebUrlBean webUrl) {
            this.webUrl = webUrl;
        }

        public ScriptAndroidBean getScript_android() {
            return script_android;
        }

        public void setScript_android(ScriptAndroidBean script_android) {
            this.script_android = script_android;
        }

        public ScriptIosBean getScript_ios() {
            return script_ios;
        }

        public void setScript_ios(ScriptIosBean script_ios) {
            this.script_ios = script_ios;
        }


    @Override
    public String toString() {
        return "ArrayBean{" +
                "script_android=" + script_android +
                ", webName='" + webName + '\'' +
                ", webSite='" + webSite + '\'' +
                ", webUrl=" + webUrl +
                ", script_ios=" + script_ios +
                '}';
    }
}
