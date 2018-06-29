package com.whzl.mengbi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.bean.LiveShowBean;
import com.whzl.mengbi.bean.LiveShowListBean;
import com.whzl.mengbi.glide.GlideImageLoader;

import java.util.List;

public class HomeLiveAdapter extends RecyclerView.Adapter<HomeLiveAdapter.ViewHolder>{

        private List<LiveShowListBean> mData;
        private Context mContext;

        public HomeLiveAdapter ( Context mContext,List<LiveShowListBean> mData){
            this.mContext = mContext;
            this.mData = mData;
        }

        public void updateData(List<LiveShowListBean> data) {
                this.mData = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_show_rvitem_layout, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mData.get(position).getStatus().equals("T")){
                GlideImageLoader.getInstace().displayImage(mContext,R.drawable.ic_home_live_middle,holder.item_status_iv);
            }
             GlideImageLoader.getInstace().displayImage(mContext,mData.get(position).getCover(),holder.item_cover_iv);
             holder.item_anchorNickname_tv.setText(mData.get(position).getAnchorLevelName());
             holder.item_roomUserCount_tv.setText(mData.get(position).getRoomUserCount()+"");

        }


        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView item_status_iv;
            ImageView item_cover_iv;
            TextView item_anchorNickname_tv;
            TextView item_roomUserCount_tv;

            public ViewHolder(View itemView) {
                super(itemView);
                item_status_iv = itemView.findViewById(R.id.home_rvitem_status);
                item_cover_iv = itemView.findViewById(R.id.home_rvitem_cover);
                item_anchorNickname_tv  = itemView.findViewById(R.id.home_rvitem_anchorNickname);
                item_roomUserCount_tv  = itemView.findViewById(R.id.home_rvitem_roomUserCount);
            }
        }
}
