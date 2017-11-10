package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by zhoushicheng on 2017/2/23.
 */

public class WebSetJsonData implements Serializable {

    private String ErrorCode;
    private String ErrorMsg;
    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }
}
