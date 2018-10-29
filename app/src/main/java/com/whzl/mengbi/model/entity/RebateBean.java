package com.whzl.mengbi.model.entity;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/29
 */
public class RebateBean {

    public List<ListBean> list;

    public static class ListBean implements IPickerViewData {
        /**
         * couponId : 9
         * scale : 3
         * goodsSn : 2148
         * goodsName : 3%返利券
         * identifyCode : DGUW1O
         * goodsSum : 1
         */

        public long couponId;
        public int scale;
        public long goodsSn;
        public String goodsName;
        public String identifyCode;
        public int goodsSum;

        @Override
        public String getPickerViewText() {
            return goodsName + "(" + identifyCode + ")";
        }
    }

}
