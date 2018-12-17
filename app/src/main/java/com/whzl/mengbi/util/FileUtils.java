package com.whzl.mengbi.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.whzl.mengbi.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    static HashMap<String, Integer> hashMap = new HashMap();

    static {
        hashMap.put("0", R.drawable.usergrade);
        hashMap.put("1", R.drawable.usergrade1);
        hashMap.put("2", R.drawable.usergrade2);
        hashMap.put("3", R.drawable.usergrade3);
        hashMap.put("4", R.drawable.usergrade4);
        hashMap.put("5", R.drawable.usergrade5);
        hashMap.put("6", R.drawable.usergrade6);
        hashMap.put("7", R.drawable.usergrade7);
        hashMap.put("8", R.drawable.usergrade8);
        hashMap.put("9", R.drawable.usergrade9);
        hashMap.put("10", R.drawable.usergrade10);
        hashMap.put("11", R.drawable.usergrade11);
        hashMap.put("12", R.drawable.usergrade12);
        hashMap.put("13", R.drawable.usergrade13);
        hashMap.put("14", R.drawable.usergrade14);
        hashMap.put("15", R.drawable.usergrade15);
        hashMap.put("16", R.drawable.usergrade16);
        hashMap.put("17", R.drawable.usergrade17);
        hashMap.put("18", R.drawable.usergrade18);
        hashMap.put("19", R.drawable.usergrade19);
        hashMap.put("20", R.drawable.usergrade20);
        hashMap.put("21", R.drawable.usergrade21);
        hashMap.put("22", R.drawable.usergrade22);
        hashMap.put("23", R.drawable.usergrade23);
        hashMap.put("24", R.drawable.usergrade24);
        hashMap.put("25", R.drawable.usergrade25);
        hashMap.put("26", R.drawable.usergrade26);
        hashMap.put("27", R.drawable.usergrade27);
        hashMap.put("28", R.drawable.usergrade28);
        hashMap.put("29", R.drawable.usergrade29);
        hashMap.put("30", R.drawable.usergrade30);
        hashMap.put("31", R.drawable.usergrade31);
        hashMap.put("32", R.drawable.usergrade32);
        hashMap.put("33", R.drawable.usergrade33);
        hashMap.put("34", R.drawable.usergrade34);
        hashMap.put("35", R.drawable.usergrade35);
        hashMap.put("36", R.drawable.usergrade36);
        hashMap.put("37", R.drawable.usergrade36);

    }


    /**
     * 读取Assets文件夹中的json
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 读取Assets文件夹中的图片资源
     *
     * @param fileName 图片名称
     * @param context
     * @return
     */
    public static Bitmap readBitmapFromAssetsFile(String fileName, Context context) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 用户等级图片
     *
     * @param levelVal 等级名称
     */

    public static int userLevelDrawable(String levelVal) {
        int resId = -1;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if (entry.getKey().equals(levelVal)) {
                resId = entry.getValue();
            }
        }
        return resId;
    }
}
