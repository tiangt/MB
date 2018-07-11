package com.whzl.mengbi.util.alipay;

import android.text.TextUtils;

import java.util.Map;

public class PayResult {

    public static final String STATUS_CODE ="9000";

    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return 结果状态
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return 备忘录
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return 结果
     */
    public String getResult() {
        return result;
    }
}
