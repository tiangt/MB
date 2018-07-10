package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

public class AudienceListAdapter<T> extends CommonAdapter<AudienceListBean.DataBean.AudienceInfoBean> {

    public AudienceListAdapter(Context context, int layoutId, List<AudienceListBean.DataBean.AudienceInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AudienceListBean.DataBean.AudienceInfoBean audienceInfoBean, int position) {

    }
}
