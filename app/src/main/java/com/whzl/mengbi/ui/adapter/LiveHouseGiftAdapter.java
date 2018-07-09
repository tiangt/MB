package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class LiveHouseGiftAdapter extends CommonAdapter<GiftInfo.GiftDetailInfoBean> {
    private int selectedPosition = -1;


    public LiveHouseGiftAdapter(Context context, int layoutId, List<GiftInfo.GiftDetailInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.GiftDetailInfoBean giftDetailInfoBean, int position) {
        GlideImageLoader.getInstace().displayImage(mContext, giftDetailInfoBean.getGoodPic(), holder.getView(R.id.iv_gift));
        holder.setText(R.id.tv_gift_name, giftDetailInfoBean.getGoodsName());
        holder.setText(R.id.tv_cost, giftDetailInfoBean.getRent() + "");
        holder.getView(R.id.view_select_mark).setSelected(position == selectedPosition);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
