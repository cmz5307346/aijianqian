package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/2.
 */

public class ProductInfoBean implements Serializable {
    private String subTitle;
    private String applyBaseCount;
    private String applyCondition;
    private String applyFlow;
    private String applyIncreaseRate;
    private String applySuccessRate;
    private String auditCycle;
    private String auditGrantTime;
    private String auditReturnWay;
    private String auditWay;
    private String[] importantRemind;
    private String loanCycle;
    private String loanRate;
    private String loanRateType;
    private String loanRange;
    private String productName;
    private String icon;

    public ProductInfoBean() {
    }

    public ProductInfoBean(String subTitle, String applyBaseCount, String applyCondition, String applyFlow, String applyIncreaseRate, String applySuccessRate, String auditCycle, String auditGrantTime, String auditReturnWay, String auditWay, String[] importantRemind, String loanCycle, String loanRate, String loanRateType, String loanRange, String productName, String icon) {
        this.subTitle = subTitle;
        this.applyBaseCount = applyBaseCount;
        this.applyCondition = applyCondition;
        this.applyFlow = applyFlow;
        this.applyIncreaseRate = applyIncreaseRate;
        this.applySuccessRate = applySuccessRate;
        this.auditCycle = auditCycle;
        this.auditGrantTime = auditGrantTime;
        this.auditReturnWay = auditReturnWay;
        this.auditWay = auditWay;
        this.importantRemind = importantRemind;
        this.loanCycle = loanCycle;
        this.loanRate = loanRate;
        this.loanRateType = loanRateType;
        this.loanRange = loanRange;
        this.productName = productName;
        this.icon = icon;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getApplyBaseCount() {
        return applyBaseCount;
    }

    public void setApplyBaseCount(String applyBaseCount) {
        this.applyBaseCount = applyBaseCount;
    }

    public String getApplyCondition() {
        return applyCondition;
    }

    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition;
    }

    public String getApplyFlow() {
        return applyFlow;
    }

    public void setApplyFlow(String applyFlow) {
        this.applyFlow = applyFlow;
    }

    public String getApplyIncreaseRate() {
        return applyIncreaseRate;
    }

    public void setApplyIncreaseRate(String applyIncreaseRate) {
        this.applyIncreaseRate = applyIncreaseRate;
    }

    public String getApplySuccessRate() {
        return applySuccessRate;
    }

    public void setApplySuccessRate(String applySuccessRate) {
        this.applySuccessRate = applySuccessRate;
    }

    public String getAuditCycle() {
        return auditCycle;
    }

    public void setAuditCycle(String auditCycle) {
        this.auditCycle = auditCycle;
    }

    public String getAuditGrantTime() {
        return auditGrantTime;
    }

    public void setAuditGrantTime(String auditGrantTime) {
        this.auditGrantTime = auditGrantTime;
    }

    public String getAuditReturnWay() {
        return auditReturnWay;
    }

    public void setAuditReturnWay(String auditReturnWay) {
        this.auditReturnWay = auditReturnWay;
    }

    public String getAuditWay() {
        return auditWay;
    }

    public void setAuditWay(String auditWay) {
        this.auditWay = auditWay;
    }

    public String[] getImportantRemind() {
        return importantRemind;
    }

    public void setImportantRemind(String[] importantRemind) {
        this.importantRemind = importantRemind;
    }

    public String getLoanCycle() {
        return loanCycle;
    }

    public void setLoanCycle(String loanCycle) {
        this.loanCycle = loanCycle;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getLoanRateType() {
        return loanRateType;
    }

    public void setLoanRateType(String loanRateType) {
        this.loanRateType = loanRateType;
    }

    public String getLoanRange() {
        return loanRange;
    }

    public void setLoanRange(String loanRange) {
        this.loanRange = loanRange;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
