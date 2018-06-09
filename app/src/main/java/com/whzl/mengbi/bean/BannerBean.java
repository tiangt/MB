package com.whzl.mengbi.bean;

import java.util.List;

public class BannerBean {

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {

            private String subject;
            private int category;
            private int subCategory;
            private String piclink;
            private int sort;
            private String content;
            private String textlink;

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public int getSubCategory() {
                return subCategory;
            }

            public void setSubCategory(int subCategory) {
                this.subCategory = subCategory;
            }

            public String getPiclink() {
                return piclink;
            }

            public void setPiclink(String piclink) {
                this.piclink = piclink;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTextlink() {
                return textlink;
            }

            public void setTextlink(String textlink) {
                this.textlink = textlink;
            }
        }
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
