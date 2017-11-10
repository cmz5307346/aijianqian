package com.ajq.aijieqian102.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class DataBean implements Serializable{

    public DataBean(List<ArrayBean> array, int version) {
        this.array = array;
        this.version = version;
    }

    public DataBean() {
    }

        private int version;
        private List<ArrayBean> array;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

}
