package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

public class LiveGiftLuxuryAdapter<T> extends CommonAdapter<GiftInfo.DataBean.豪华Bean>{

    public LiveGiftLuxuryAdapter(Context context, int layoutId, List<GiftInfo.DataBean.豪华Bean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.DataBean.豪华Bean luxury, int position) {
        GlideImageLoader.getInstace().displayImage(mContext,luxury.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
        holder.setText(R.id.live_display_rvitem_gift_name,luxury.getGoodsName());
        holder.setText(R.id.live_display_rvitem_gift_rent,luxury.getRent()+"");
    }
}
