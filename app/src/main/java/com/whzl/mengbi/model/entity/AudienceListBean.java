package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class AudienceListBean {

    /**
     * code : 200
     * msg : success
     * data : {"list":[{"userid":30000537,"name":"萌友30000537","identity":41,"avatar":"http://dev.img.mengbitv.com/avatar/030/00/05/30.jpg?1528880125","medal":[{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100079},{"goodsId":94313,"goodsName":"第一次","goodsType":"BADGE","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/09.jpg","bindProgramId":0},{"goodsId":94314,"goodsName":"首充座驾","goodsType":"CAR","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/10.jpg","bindProgramId":0},{"goodsId":94321,"goodsName":"新人勋章","goodsType":"BADGE","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/08.jpg","bindProgramId":0}],"levelMap":{"USER_LEVEL":6,"ROYAL_LEVEL":5,"ANCHOR_LEVEL":1},"platform":"PC_WEB"}]}
     */

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
        private List<AudienceInfoBean> list;

        public List<AudienceInfoBean> getList() {
            return list;
        }

        public void setList(List<AudienceInfoBean> list) {
            this.list = list;
        }

        public static class AudienceInfoBean {
            /**
             * userid : 30000537
             * name : 萌友30000537
             * identity : 41
             * avatar : http://dev.img.mengbitv.com/avatar/030/00/05/30.jpg?1528880125
             * medal : [{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100079},{"goodsId":94313,"goodsName":"第一次","goodsType":"BADGE","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/09.jpg","bindProgramId":0},{"goodsId":94314,"goodsName":"首充座驾","goodsType":"CAR","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/10.jpg","bindProgramId":0},{"goodsId":94321,"goodsName":"新人勋章","goodsType":"BADGE","goodsIcon":"http://dev.img.mengbitv.com/default/000/00/01/08.jpg","bindProgramId":0}]
             * levelMap : {"USER_LEVEL":6,"ROYAL_LEVEL":5,"ANCHOR_LEVEL":1}
             * platform : PC_WEB
             */

            private int userid;
            private String name;
            private int identity;
            private String avatar;
            private LevelMapBean levelMap;
            private String platform;
            private List<MedalBean> medal;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIdentity() {
                return identity;
            }

            public void setIdentity(int identity) {
                this.identity = identity;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public LevelMapBean getLevelMap() {
                return levelMap;
            }

            public void setLevelMap(LevelMapBean levelMap) {
                this.levelMap = levelMap;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public List<MedalBean> getMedal() {
                return medal;
            }

            public void setMedal(List<MedalBean> medal) {
                this.medal = medal;
            }

            public static class LevelMapBean {
                /**
                 * USER_LEVEL : 6
                 * ROYAL_LEVEL : 5
                 * ANCHOR_LEVEL : 1
                 */

                private int USER_LEVEL;
                private int ROYAL_LEVEL;
                private int ANCHOR_LEVEL;

                public int getUSER_LEVEL() {
                    return USER_LEVEL;
                }

                public void setUSER_LEVEL(int USER_LEVEL) {
                    this.USER_LEVEL = USER_LEVEL;
                }

                public int getROYAL_LEVEL() {
                    return ROYAL_LEVEL;
                }

                public void setROYAL_LEVEL(int ROYAL_LEVEL) {
                    this.ROYAL_LEVEL = ROYAL_LEVEL;
                }

                public int getANCHOR_LEVEL() {
                    return ANCHOR_LEVEL;
                }

                public void setANCHOR_LEVEL(int ANCHOR_LEVEL) {
                    this.ANCHOR_LEVEL = ANCHOR_LEVEL;
                }
            }

            public static class MedalBean {
                /**
                 * goodsId : 300
                 * goodsName : 守护
                 * goodsType : GUARD
                 * goodsIcon : http://dev.img.mengbitv.com/default/000/00/01/39.jpg
                 * bindProgramId : 100079
                 */

                private int goodsId;
                private String goodsName;
                private String goodsType;
                private String goodsIcon;
                private int bindProgramId;

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

                public String getGoodsIcon() {
                    return goodsIcon;
                }

                public void setGoodsIcon(String goodsIcon) {
                    this.goodsIcon = goodsIcon;
                }

                public int getBindProgramId() {
                    return bindProgramId;
                }

                public void setBindProgramId(int bindProgramId) {
                    this.bindProgramId = bindProgramId;
                }
            }
        }
    }
}
