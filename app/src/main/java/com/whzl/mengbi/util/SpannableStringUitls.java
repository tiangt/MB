package com.whzl.mengbi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.message.ChatCommonMesBean;

import java.util.List;

public class SpannableStringUitls {
    public static  SpannableStringBuilder faceReplace(Context context,ChatCommonMesBean commonMesBean, List<EmjoyInfo.FaceBean.PublicBean> mFaceData){
        String contentVal = commonMesBean.getContent();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(contentVal);
        //循环替换聊天信息表情图片
        for(int i = 0; i < mFaceData.size(); ++i) {
            String faceVal = mFaceData.get(i).getValue();
            String tmpValue = contentVal;
            int pos = tmpValue.indexOf(faceVal);
            int spanPos = 0;
            while (pos>=0){
                String faceIcon = mFaceData.get(i).getIcon();
                Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(faceIcon,context);
                ImageSpan imageSpan = new ImageSpan(bitmap);
                stringBuilder.setSpan(imageSpan,spanPos, spanPos + faceVal.length(),SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                tmpValue = tmpValue.substring(pos + faceVal.length());
                pos = tmpValue.indexOf(faceVal);
                spanPos += faceVal.length();
            }
        }
        return stringBuilder;
    }
}
