package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class LiveHouseGiftAdapter extends CommonAdapter<GiftInfo.GiftDetailInfoBean> {
    private int selectedPosition = -1;
    private Context context;


    public LiveHouseGiftAdapter(Context context, int layoutId, List<GiftInfo.GiftDetailInfoBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, GiftInfo.GiftDetailInfoBean giftDetailInfoBean, int position) {
        GlideImageLoader.getInstace().displayImage(mContext, giftDetailInfoBean.getGoodPic(), holder.getView(R.id.iv_gift));
        holder.setText(R.id.tv_gift_name, giftDetailInfoBean.getGoodsName());
        holder.setText(R.id.tv_cost, giftDetailInfoBean.getRent() + "萌币");
//        holder.getView(R.id.view_select_mark).setSelected(position == selectedPosition);
        if (position == selectedPosition) {
            try {
                GifDrawable drawable = new GifDrawable(context.getResources(),R.drawable.bg_live_house_select);
                ( holder.getView(R.id.rl)).setBackground(drawable);
//                ((ImageView) holder.getView(R.id.iv)).setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            holder.getView(R.id.rl).setBackground();
        } else {
            ( holder.getView(R.id.rl)).setBackground(null);
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
