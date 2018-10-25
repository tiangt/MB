package com.whzl.mengbi.util;

import android.text.TextUtils;
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

    /**
     * @param text 要打印的字符串
     */
    public static void e2(String text) {
        if (!isDebuggable())
            return;
        if (!TextUtils.isEmpty(text)) {
            int logcatMaxLength = 1000;
            int lineCount = text.length() / logcatMaxLength;
            if (text.length() % logcatMaxLength > 0) {
                lineCount += 1;
            }
            if (lineCount > 1) {
                Log.e("<<<<<<<<", "总共" + lineCount + "行开始打印");
            }
            for (int i = 0; i < lineCount; i++) {
                String subString = text.substring(i * logcatMaxLength, Math.min((i + 1) * logcatMaxLength, text.length()));
//                Log.d("<<<<<<<<", "当前第" + (i+1) + "行：" + subString);
                Log.e("<<<<<<<<", subString);
            }
            if (lineCount > 1) {
                Log.e("<<<<<<<<", "总共" + lineCount + "行结束打印");
            }
        }
    }

    /**
     * @param text 要打印的字符串
     */
    public static void d2(String text) {
        if (!isDebuggable())
            return;
        if (!TextUtils.isEmpty(text)) {
            int logcatMaxLength = 1000;
            int lineCount = text.length() / logcatMaxLength;
            if (text.length() % logcatMaxLength > 0) {
                lineCount += 1;
            }
            if (lineCount > 1) {
                Log.d("<<<<<<<<", "总共" + lineCount + "行开始打印");
            }
            for (int i = 0; i < lineCount; i++) {
                String subString = text.substring(i * logcatMaxLength, Math.min((i + 1) * logcatMaxLength, text.length()));
//                Log.d("<<<<<<<<", "当前第" + (i+1) + "行：" + subString);
                Log.d("<<<<<<<<", subString);
            }
            if (lineCount > 1) {
                Log.d("<<<<<<<<", "总共" + lineCount + "行结束打印");
            }
        }
    }
}
