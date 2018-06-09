package com.whzl.mengbi.util;

import android.util.Log;

import com.whzl.mengbi.BuildConfig;

public class LogUtils {
    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数
    private static int LOG_MAXLENGTH = 2000;
    private LogUtils() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message) {
        if (!isDebuggable())
            return;

        int strLength = message.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        getMethodNames(new Throwable().getStackTrace());
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.e(className + i, createLog(message.substring(start, end)));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(className + i, createLog(message.substring(start, strLength)));
                break;
            }
        }
    }
    public static void i(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}
