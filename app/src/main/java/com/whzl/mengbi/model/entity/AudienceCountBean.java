package com.whzl.mengbi.model.entity;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class AudienceCountBean {

    /**
     * code : 200
     * msg : success
     * data : {"roomUserNum":"1115"}
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
        /**
         * roomUserNum : 1115
         */

        private long roomUserNum;

        public long getRoomUserNum() {
            return roomUserNum;
        }

        public void setRoomUserNum(long roomUserNum) {
            this.roomUserNum = roomUserNum;
        }
    }
}
