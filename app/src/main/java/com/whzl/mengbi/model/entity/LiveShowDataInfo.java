package com.whzl.mengbi.model.entity;

import java.util.List;

public class LiveShowDataInfo {

    private int mark;

    private List<LiveShowListInfo> list;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public List<LiveShowListInfo> getList() {
        return list;
    }

    public void setList(List<LiveShowListInfo> list) {
        this.list = list;
    }

}
