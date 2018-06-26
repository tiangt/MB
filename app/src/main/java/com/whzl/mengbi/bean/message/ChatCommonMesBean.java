package com.whzl.mengbi.bean.message;

import java.util.List;

public class ChatCommonMesBean {
    private String content;
    private String create_time;
    private FromJsonBean from_json;
    private String from_nickname;
    private String from_uid;
    private Object to_json;
    private String to_nickname;
    private String to_uid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public FromJsonBean getFrom_json() {
        return from_json;
    }

    public void setFrom_json(FromJsonBean from_json) {
        this.from_json = from_json;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public Object getTo_json() {
        return to_json;
    }

    public void setTo_json(Object to_json) {
        this.to_json = to_json;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public static class FromJsonBean {
        private List<GoodsListBean> goodsList;
        private List<LevelListBean> levelList;

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public List<LevelListBean> getLevelList() {
            return levelList;
        }

        public void setLevelList(List<LevelListBean> levelList) {
            this.levelList = levelList;
        }

        public static class GoodsListBean {

            private Object bindProgramId;
            private boolean colorSpeak;
            private int goodsIcon;
            private int goodsId;
            private String goodsName;
            private String goodsType;

            public Object getBindProgramId() {
                return bindProgramId;
            }

            public void setBindProgramId(Object bindProgramId) {
                this.bindProgramId = bindProgramId;
            }

            public boolean isColorSpeak() {
                return colorSpeak;
            }

            public void setColorSpeak(boolean colorSpeak) {
                this.colorSpeak = colorSpeak;
            }

            public int getGoodsIcon() {
                return goodsIcon;
            }

            public void setGoodsIcon(int goodsIcon) {
                this.goodsIcon = goodsIcon;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(String goodsType) {
                this.goodsType = goodsType;
            }
        }

        public static class LevelListBean {
            private String levelType;
            private int levelValue;

            public String getLevelType() {
                return levelType;
            }

            public void setLevelType(String levelType) {
                this.levelType = levelType;
            }

            public int getLevelValue() {
                return levelValue;
            }

            public void setLevelValue(int levelValue) {
                this.levelValue = levelValue;
            }
        }
    }
}
