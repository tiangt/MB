package com.whzl.mengbi.chat.room.util;

import com.whzl.mengbi.util.network.URLContentUtils;

public class ImageUrl {
    //imageType: gif, jpg
    //根据时间戳获取url
    public static String getImageUrl(int imageId, String imageType, long timestamp) {
        String strImageId = String.format("%09d", imageId);
        String url = URLContentUtils.BASE_IMAGE_URL +strImageId.substring(0, 3) + "/" +
                strImageId.substring(3,5) + "/" + strImageId.substring(5, 7) +
                "/" + strImageId.substring(7, 9) + "." + imageType;
        if (timestamp >= 0) {
            url += Long.toString(timestamp);
        }
        return url;
    }

    public static  String getImageUrl(int imageId, String imageType) {
        return getImageUrl(imageId, imageType, -1);
    }
}
