package com.whzl.mengbi.ui.adapter;



import android.content.Context;

import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;

import java.util.List;


public  class LiveMessageAdapter extends CommonAdapter<List>{


    public LiveMessageAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, List data, int position) {
//        if(data!=null){
//            GlideImageLoader.getInstace().displayImage(mContext,data.get(position),holder.getView(R.id.live_display_message_item_userlevel_img));
//            GlideImageLoader.getInstace().displayImage(mContext,data.get(position),holder.getView(R.id.live_display_message_item_usermedal_img));
//            GlideImageLoader.getInstace().displayImage(mContext,data.get(position),holder.getView(R.id.live_display_message_item_usermedal2_img));
//            GlideImageLoader.getInstace().displayImage(mContext,data.get(position),holder.getView(R.id.live_display_message_item_usermedal3_img));
//            holder.getView(R.id.live_display_message_item_usermedal_img).setVisibility(View.VISIBLE);
//            holder.getView(R.id.live_display_message_item_usermedal2_img).setVisibility(View.VISIBLE);
//            holder.getView(R.id.live_display_message_item_usermedal3_img).setVisibility(View.VISIBLE);
//        }else{
//            holder.getView(R.id.live_display_message_item_usermedal_img).setVisibility(View.GONE);
//            holder.getView(R.id.live_display_message_item_usermedal2_img).setVisibility(View.GONE);
//            holder.getView(R.id.live_display_message_item_usermedal3_img).setVisibility(View.GONE);
//        }
    }

}
