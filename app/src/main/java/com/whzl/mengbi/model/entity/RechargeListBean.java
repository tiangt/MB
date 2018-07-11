package com.whzl.mengbi.model.entity;

import java.util.List;

public class RechargeListBean {
    private String channelCat;
    private String type;
    private String flag;
    private String name;
    private String pic;
    private List<RechargeRuleListBean> ruleList;

    public String getChannelCat() {
        return channelCat;
    }

    public void setChannelCat(String channelCat) {
        this.channelCat = channelCat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<RechargeRuleListBean> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<RechargeRuleListBean> ruleList) {
        this.ruleList = ruleList;
    }
}
