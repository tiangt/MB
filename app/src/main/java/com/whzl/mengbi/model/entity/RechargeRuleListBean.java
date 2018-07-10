package com.whzl.mengbi.model.entity;

public class RechargeRuleListBean {
    private int ruleId;
    private int rechargeCount;
    private String rechargeCountCode;
    private int chengCount;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public String getRechargeCountCode() {
        return rechargeCountCode;
    }

    public void setRechargeCountCode(String rechargeCountCode) {
        this.rechargeCountCode = rechargeCountCode;
    }

    public int getChengCount() {
        return chengCount;
    }

    public void setChengCount(int chengCount) {
        this.chengCount = chengCount;
    }
}
