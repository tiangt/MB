package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.FileUtils;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class ChatEmojiAdapter extends CommonAdapter<EmjoyInfo.FaceBean.PublicBean> {
    public ChatEmojiAdapter(Context context, int layoutId, List<EmjoyInfo.FaceBean.PublicBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, EmjoyInfo.FaceBean.PublicBean publicBean, int position) {
        Bitmap emojiBitmap = FileUtils.readBitmapFromAssetsFile(publicBean.getIcon(), mContext);
        ((ImageView) holder.getView(R.id.iv_emoji)).setImageBitmap(emojiBitmap);
    }
}
