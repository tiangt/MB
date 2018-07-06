package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.LiveShowListInfo;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;


public class HomeLiveAdapter<T> extends CommonAdapter<LiveShowListInfo>{

    public HomeLiveAdapter(Context context, int layoutId, List<LiveShowListInfo> datas) {
        super(context, layoutId, datas);
    }
    @Override
    protected void convert(ViewHolder holder, LiveShowListInfo info, int position) {
        if (info.equals("T")){
            GlideImageLoader.getInstace().displayImage(mContext, R.drawable.ic_home_live_middle,holder.getView(R.id.home_rvitem_status));
        }
        GlideImageLoader.getInstace().displayImage(mContext,info.getCover(),holder.getView(R.id.home_rvitem_cover));
        holder.setText(R.id.home_rvitem_anchorNickname,info.getAnchorNickname());
        holder.setText(R.id.home_rvitem_roomUserCount,info.getRoomUserCount()+"");
    }
}
