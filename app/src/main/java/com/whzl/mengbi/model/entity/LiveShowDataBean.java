package com.whzl.mengbi.model.entity;

import java.util.List;

public class LiveShowDataBean {

    private int mark;

    private List<LiveShowListBean> list;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public List<LiveShowListBean> getList() {
        return list;
    }

    public void setList(List<LiveShowListBean> list) {
        this.list = list;
    }

}
