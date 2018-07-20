package com.whzl.mengbi.model.pay;

import com.google.gson.annotations.SerializedName;

/**
 * @author shaw
 * @date 2017/10/23
 */

public class WXPayOrder {

    /**
     * appid : wxb77b8975f149005e
     * noncestr : 8Vet1YZ2Dm1srYXh
     * package : Sign=WXPay
     * partnerid : 1487522502
     * prepayid : wx201710231908502adb4b76040495523136
     * sign : 64F647B066F224E354A93F10021B7E5C
     * timestamp : 1508756931
     */

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
