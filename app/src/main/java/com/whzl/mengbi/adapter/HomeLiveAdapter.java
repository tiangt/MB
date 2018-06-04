package com.whzl.mengbi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

public class HomeLiveAdapter extends RecyclerView.Adapter<HomeLiveAdapter.ViewHolder>{

        private List<ResultBean.LiveBean> mData;

        public HomeLiveAdapter (List<ResultBean.LiveBean> mData){
            this.mData = mData;
        }

    public void updateData(List<ResultBean.LiveBean> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_rvitem_layout, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView item_status_iv;
            ImageView item_cover_iv;
            TextView item_anchorNickname_tv;
            TextView item_roomUserCount_tv;

            public ViewHolder(View itemView) {
                super(itemView);
                item_status_iv = (ImageView) itemView.findViewById(R.id.home_rvitem_status);
                item_cover_iv = (ImageView) itemView.findViewById(R.id.home_rvitem_cover);
                item_anchorNickname_tv  = (TextView) itemView.findViewById(R.id.home_rvitem_anchorNickname);
                item_roomUserCount_tv  = (TextView) itemView.findViewById(R.id.home_rvitem_roomUserCount);
            }
        }
}
