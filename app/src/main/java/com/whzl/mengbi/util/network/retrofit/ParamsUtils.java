package com.whzl.mengbi.util.network.retrofit;

import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.SPUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author shaw
 * @date 2018/7/27
 */
public class ParamsUtils {
    public static HashMap<String, String> getSignPramsMap(HashMap<String, String> paramsMap) {
        String sessionId = (String) SPUtils.get(BaseApplication.getInstance(), "sessionId", "");
        long timeDiff = (long) SPUtils.get(BaseApplication.getInstance(), SpConfig.TIME_DIFF, 0L);
        paramsMap.put("appKey", NetConfig.APPKEY);
        paramsMap.put("timestamp", System.currentTimeMillis() / 1000 + timeDiff + "");
        paramsMap.put("appSecret", NetConfig.APPSECRET);
        paramsMap.put("clientType", NetConfig.CLIENTTYPE);
        paramsMap.put("clientVersion", BuildConfig.VERSION_CODE + "");
        paramsMap.put("sessionId", sessionId);
        String sign = getSign(paramsMap);
        paramsMap.put("sign", sign);
        paramsMap.remove("appSecret");
        return paramsMap;
    }

    public static HashMap<String, RequestBody> getMultiParamsMap(HashMap<String, String> paramsMap) {
        String sessionId = (String) SPUtils.get(BaseApplication.getInstance(), "sessionId", "");
        long timeDiff = (long) SPUtils.get(BaseApplication.getInstance(), SpConfig.TIME_DIFF, 0L);
        paramsMap.put("appKey", NetConfig.APPKEY);
        paramsMap.put("timestamp", System.currentTimeMillis() / 1000 + timeDiff + "");
        paramsMap.put("appSecret", NetConfig.APPSECRET);
        paramsMap.put("clientType", NetConfig.CLIENTTYPE);
        paramsMap.put("clientVersion", BuildConfig.VERSION_CODE + "");
        paramsMap.put("sessionId", sessionId);
        String sign = getSign(paramsMap);
        paramsMap.put("sign", sign);
        paramsMap.remove("appSecret");
        HashMap<String, RequestBody> map = new HashMap<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            map.put(entry.getKey(), RequestBody.create(MediaType.parse("text/plain"), entry.getValue()));
        }
        return map;
    }

    private static String getSign(Map<String, String> params_map) {
        List<String> key_array = new ArrayList<String>(params_map.keySet());
        Collections.sort(key_array);
        String params = "";
        String sign = "";

        for (int i = 0; i < key_array.size(); i++) {
            String this_key = key_array.get(i);
            String this_value = String.valueOf(params_map.get(this_key));
            String sub_param = this_key + "=" + this_value;
            if (i != key_array.size() - 1) {
                sub_param = sub_param + "&";
            }
            params = params + sub_param;
        }
        try {
            String encodeParams = URLEncoder.encode(params, "UTF-8");
            //Log.e("http", "params=" + params + ",encodeParams=" + encodeParams);
            sign = EncryptUtils.md5Hex(encodeParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }
}
