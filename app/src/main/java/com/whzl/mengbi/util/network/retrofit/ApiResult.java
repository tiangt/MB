package com.whzl.mengbi.util.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 *
 * @author shaw
 * @date 2017/8/28 0028
 */

public class ApiResult<T> {
    public static final int REQUEST_SUCCESS = 200;

    public int code;

    public String msg;

    public boolean success;

    public JsonElement data;

    public JsonElement getResult() {
        return data;
    }

    public void setResult(JsonElement result) {
        this.data = result;
    }

    public T getResultBean(Class<T> clazz) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getResultBean(Type type) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
