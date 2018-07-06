package com.whzl.mengbi.chat.room.util;

import com.whzl.mengbi.util.network.URLContentUtils;

public class ImageUrl {
    //imageType: gif, jpg
    public static String getImageUrl(int imageId, String imageType) {
        String strImageId = String.format("%09d", imageId);
        String url = URLContentUtils.BASE_IMAGE_URL +strImageId.substring(0, 3) + "/" +
                strImageId.substring(3,5) + "/" + strImageId.substring(5, 7) +
                "/" + strImageId.substring(7, 9) + "." + imageType;
        return url;
    }
}
