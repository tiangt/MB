package com.whzl.mengbi.greendao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author nobody
 * @date 2019/1/10
 */
public class CommonGift_Converter implements PropertyConverter<List<CommonGiftBean>, String> {
    @Override
    public List<CommonGiftBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        Type type = new TypeToken<List<CommonGiftBean>>() {
        }.getType();
        List<CommonGiftBean> list = new Gson().fromJson("[" + databaseValue + "]", type);

        return list;
    }

    @Override
    public String convertToDatabaseValue(List<CommonGiftBean> arrays) {
        if (arrays == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arrays.size(); i++) {
                CommonGiftBean commonGiftBean = arrays.get(i);
                String s = new Gson().toJson(commonGiftBean);
                if (i == arrays.size() - 1) {
                    sb.append(s);
                } else {
                    sb.append(s + ",");
                }
            }

            return sb.toString();

        }
    }
}
