package com.whzl.mengbi.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by shaw on 2017/9/18 0018.
 */

public class AsyncRun {
    public static void run(Runnable r) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(r);
    }

}
