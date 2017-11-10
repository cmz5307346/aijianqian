package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class ScriptIosBean implements Serializable{

    public ScriptIosBean(String after_js, String name, String phone) {
        this.after_js = after_js;
        this.name = name;
        this.phone = phone;
    }

    public ScriptIosBean() {
    }

    private String name;
        private String phone;
        private String after_js;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAfter_js() {
            return after_js;
        }

        public void setAfter_js(String after_js) {
            this.after_js = after_js;
        }
}
