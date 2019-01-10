package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GoodsPriceBatchBean;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class GiftCommonAdapter extends CommonAdapter<GoodsPriceBatchBean.ListBean> {
    private int selectedPosition = -1;
    private Context context;


    public GiftCommonAdapter(Context context, int layoutId, ArrayList<GoodsPriceBatchBean.ListBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, GoodsPriceBatchBean.ListBean giftDetailInfoBean, int position) {
        GlideImageLoader.getInstace().displayImage(mContext, giftDetailInfoBean.goodsPic, holder.getView(R.id.iv_gift));
        holder.setText(R.id.tv_gift_name, giftDetailInfoBean.goodsName);
        holder.setText(R.id.tv_cost, giftDetailInfoBean.rent + "萌币");
        if (position == selectedPosition) {
            (holder.getView(R.id.rl)).setBackgroundResource(R.drawable.bg_live_house_gift_bg);
        } else {
            (holder.getView(R.id.rl)).setBackground(null);
        }
//        if (TextUtils.isEmpty(giftDetailInfoBean.getTagName())) {
//            holder.setVisible(R.id.tv_tag, false);
//        } else {
//            holder.setVisible(R.id.tv_tag, true);
//            holder.setText(R.id.tv_tag, giftDetailInfoBean.getTagName());
//        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
