package com.whzl.mengbi.util;

import android.graphics.Bitmap;

/**
 * @author cliang
 * @date 2018.12.26
 */
public interface HttpCallBackListener {
    void onFinish(Bitmap bitmap);
    void onError(Exception e);
}
