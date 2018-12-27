package com.whzl.mengbi.model.entity;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarListInfo {
    public WeekStarRankListBean anchor; //主播周星
    public WeekStarRankListBean user; //富豪周星

    public WeekStarRankListBean getAnchor() {
        return anchor;
    }

    public void setAnchor(WeekStarRankListBean anchor) {
        this.anchor = anchor;
    }

    public WeekStarRankListBean getUser() {
        return user;
    }

    public void setUser(WeekStarRankListBean user) {
        this.user = user;
    }
}
