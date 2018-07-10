package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class LiveGiftLuckyAdapter<T> extends CommonAdapter<GiftInfo.DataBean.幸运Bean> {

    public LiveGiftLuckyAdapter(Context context, int layoutId, List<GiftInfo.DataBean.幸运Bean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.DataBean.幸运Bean lucky, int position) {
        GlideImageLoader.getInstace().displayImage(mContext,lucky.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
        holder.setText(R.id.live_display_rvitem_gift_name,lucky.getGoodsName());
        holder.setText(R.id.live_display_rvitem_gift_rent,lucky.getRent()+"");
    }
}
