package com.whzl.mengbi.model.entity;

/**
 * @author cliang
 * @date 2018.12.28
 */
public class RankBeyondInfo extends ResponseInfo {

    /**
     * data : {"goodsNum":2,"mengCoin":200,"rank":46}
     */
    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * "rank": -1,
         * "selfScore": 0,
         * "topScore": 0
         */
        public int rank;
        public int selfScore;
        public int topScore;
    }
}
