package com.whzl.mengbi.model.entity;

import java.util.List;

public class RechargeChannelListBean {
    private int channelId;
    private String channelName;
    private String channelFlag;
    private String status;
    private String platform;
    private String pic;
    private int sortValue;
    private List<RechargeListBean> list;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelFlag() {
        return channelFlag;
    }

    public void setChannelFlag(String channelFlag) {
        this.channelFlag = channelFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }

    public List<RechargeListBean> getList() {
        return list;
    }

    public void setList(List<RechargeListBean> list) {
        this.list = list;
    }
}
