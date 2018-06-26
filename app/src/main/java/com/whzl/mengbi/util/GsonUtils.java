package com.whzl.mengbi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
    //不用创建对象,直接使用Gson.就可以调用方法
    private static Gson gson = null;
    //判断gson对象是否存在了,不存在则创建对象
    static {
        if (gson == null) {
            //gson = new Gson();
            //当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
    }
    //无参的私有构造方法
    private GsonUtils() {

    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            //传入json对象和对象类型,将json转成对象
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }
}
