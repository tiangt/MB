package com.whzl.mengbi.chat.room.util;

import com.whzl.mengbi.util.network.URLContentUtils;

public class ImageUrl {
    public static final float IMAGE_HIGHT = 16;
    //imageType: gif, jpg
    //根据时间戳获取url
    public static String getImageUrl(int imageId, String imageType, long timestamp) {
        return getUrlWithId(imageId, "default", imageType, timestamp);
    }

    public static  String getImageUrl(int imageId, String imageType) {
        return getImageUrl(imageId, imageType, -1);
    }

    public static String getAvatarUrl(long uid, String imageType, long timestamp) {
        return getUrlWithId(uid, "avatar", "jpg", timestamp);
    }

    private static String getUrlWithId(long id, String path, String imageType, long timestamp) {
        String strImageId = String.format("%09d", id);
        String url = URLContentUtils.getBaseImageUrl() + path + "/" + strImageId.substring(0, 3) + "/" +
                strImageId.substring(3,5) + "/" + strImageId.substring(5, 7) +
                "/" + strImageId.substring(7, 9) + "." + imageType;
        if (timestamp >= 0) {
            url += "?";
            url += Long.toString(timestamp/1000);
        }
        return url;
    }

}
