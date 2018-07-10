package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class LiveGiftRecommendAdapter<T> extends CommonAdapter<GiftInfo.DataBean.推荐Bean> {

    public LiveGiftRecommendAdapter(Context context, int layoutId, List<GiftInfo.DataBean.推荐Bean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.DataBean.推荐Bean recommend, int position) {
        GlideImageLoader.getInstace().displayImage(mContext,recommend.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
        holder.setText(R.id.live_display_rvitem_gift_name,recommend.getGoodsName());
        holder.setText(R.id.live_display_rvitem_gift_rent,recommend.getRent()+"");
    }
}
