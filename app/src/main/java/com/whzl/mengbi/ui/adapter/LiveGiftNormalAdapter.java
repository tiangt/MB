package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

public class LiveGiftNormalAdapter<T> extends CommonAdapter<GiftInfo.DataBean.普通Bean>{

    public LiveGiftNormalAdapter(Context context, int layoutId, List<GiftInfo.DataBean.普通Bean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.DataBean.普通Bean normal, int position) {
        GlideImageLoader.getInstace().displayImage(mContext,normal.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
        holder.setText(R.id.live_display_rvitem_gift_name,normal.getGoodsName());
        holder.setText(R.id.live_display_rvitem_gift_rent,normal.getRent()+"");
    }
}
