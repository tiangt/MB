package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.ContributeDataBean;
import com.whzl.mengbi.model.entity.RoomRankBean;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

/**
 * @author shaw
 */
public class AudienceContributeListAdapter<T> extends CommonAdapter<RoomRankBean.DataBean.ListBean> {

    private int[] rankIcons = new int[]{R.drawable.contribute_rank_1, R.drawable.contribute_rank_2, R.drawable.contribute_rank_3, R.drawable.contribute_rank_4, R.drawable.contribute_rank_5};

    public AudienceContributeListAdapter(Context context, int layoutId, List<RoomRankBean.DataBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    public void setDatas(List<RoomRankBean.DataBean.ListBean> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, RoomRankBean.DataBean.ListBean userInfoBean, int position) {
        ImageView ivRank = holder.getView(R.id.iv_rank);
        ivRank.setVisibility(position < 5 ? View.VISIBLE : View.INVISIBLE);
        if (position < 5) {
            ivRank.setImageResource(rankIcons[position]);
        }
        holder.setText(R.id.tv_name, userInfoBean.nickname);
        holder.setText(R.id.tv_amount, StringUtils.formatNumber(userInfoBean.value) + "");
        GlideImageLoader.getInstace().displayImage(mContext, userInfoBean.avatar, holder.getView(R.id.iv_avatar));
        int userLevelIcon = ResourceMap.getResourceMap().getUserLevelIcon(userInfoBean.level);
        holder.setImageResource(R.id.iv_level_icon, userLevelIcon);

    }
}
