package com.whzl.mengbi.model.entity;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/11
 */
public class QueryBagByGoodsTypeBean {

    public List<ListBean> list;

    public static class ListBean implements IPickerViewData {


        /**
         * goodsId : 94644
         * goodsType : BLACK_CARD
         * goodsName : 1小时小黑屋解救卡
         * goodsSum : 15
         * addMultiple : null
         * goodsPic : http://localtest-img.mengbitv.com/default/000/00/05/91.jpg
         * effSecond : 3600
         */

        public int goodsId;
        public String goodsType;
        public String goodsName;
        public int goodsSum;
        public Object addMultiple;
        public String goodsPic;
        public int effSecond;

        @Override
        public String getPickerViewText() {
            return goodsName;
        }
    }
}
