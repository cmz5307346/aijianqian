package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class ScriptAndroidBean implements Serializable{

    public ScriptAndroidBean(String get, String set) {
        this.get = get;
        this.set = set;
    }

    public ScriptAndroidBean() {
    }

    private String get;
        private String set;

        public String getGet() {
            return get;
        }

        public void setGet(String get) {
            this.get = get;
        }

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }
}
