package com.whzl.mengbi.model.entity;

public class RecommendAnchorInfoBean {

    private Integer programId;

    private String programName;

    private String status;

    private Integer roomUserCount;

    private String cover;

    private long anchorId;

    private String anchorNickname;

    private String anchorLevelName;

    private ShowStreamDataInfo showStreamData;

    private String isPk;

    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRoomUserCount() {
        return roomUserCount;
    }

    public void setRoomUserCount(Integer roomUserCount) {
        this.roomUserCount = roomUserCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Integer anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorNickname() {
        return anchorNickname;
    }

    public void setAnchorNickname(String anchorNickname) {
        this.anchorNickname = anchorNickname;
    }

    public String getAnchorLevelName() {
        return anchorLevelName;
    }

    public void setAnchorLevelName(String anchorLevelName) {
        this.anchorLevelName = anchorLevelName;
    }

    public ShowStreamDataInfo getShowStreamData() {
        return showStreamData;
    }

    public void setShowStreamData(ShowStreamDataInfo showStreamData) {
        this.showStreamData = showStreamData;
    }
}
