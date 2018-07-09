package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

public class LiveChatFaceAdapter<T> extends CommonAdapter<EmjoyInfo.FaceBean.PublicBean>{

    public LiveChatFaceAdapter(Context context, int layoutId, List<EmjoyInfo.FaceBean.PublicBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, EmjoyInfo.FaceBean.PublicBean publicBean, int position) {
        Bitmap path = FileUtils.readBitmapFromAssetsFile(publicBean.getIcon(),mContext);
        GlideImageLoader.getInstace().displayImage(mContext,path,holder.getView(R.id.iv_emoji));
    }
}
