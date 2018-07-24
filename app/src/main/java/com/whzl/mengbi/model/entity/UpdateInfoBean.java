package com.whzl.mengbi.model.entity;

/**
 * @author shaw
 * @date 2018/7/24
 */
public class UpdateInfoBean {


    public int code;
    public String msg;
    public DataBean data;

    public class DataBean {

        public PhoneBean phone;


    }

    public class PhoneBean {
        /**
         * appUrl :
         * versionName : 1.0.0
         * versionCode : 1
         * isUpdate : false
         * updateType : TIPS
         * upgradeDetail : 测试
         */

        public String appUrl;
        public String versionName;
        public int versionCode;
        public boolean isUpdate;
        public String updateType;
        public String upgradeDetail;
    }
}
