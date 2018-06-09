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
import com.whzl.mengbi.bean.RecommendBean;
import com.whzl.mengbi.bean.RecommendListBean;
import com.whzl.mengbi.glide.GlideImageLoader;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder>{

    private List<RecommendListBean> mData;
    private Context mContext;

    public RecommendAdapter(List<RecommendListBean> mData,Context mContext){
        this.mContext = mContext;
        this.mData = mData;
    }

    public  void updataData(List<RecommendListBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recommend_rvitem_layout, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getStatus().equals("T")){
            new GlideImageLoader().displayImage(mContext,R.mipmap.ic_home_live_middle,holder.item_status_iv);
        }
        new GlideImageLoader().displayImage(mContext,mData.get(position).getCover(),holder.item_cover_iv);
        holder.item_anchorNickname_tv.setText(mData.get(position).getAnchorNickname());
        holder.item_roomUserCount_tv.setText(mData.get(position).getRoomUserCount()+"");
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

            ImageView item_status_iv;
            ImageView item_cover_iv;
            TextView item_anchorNickname_tv;
            TextView item_roomUserCount_tv;

            public ViewHolder(View itemView) {
                super(itemView);
                item_status_iv = itemView.findViewById(R.id.home_recommend_rvitem_status);
                item_cover_iv = itemView.findViewById(R.id.home_recommend_rvitem_cover);
                item_anchorNickname_tv  = itemView.findViewById(R.id.home_recommend_rvitem_anchorNickname);
                item_roomUserCount_tv  = itemView.findViewById(R.id.home_recommend_rvitem_roomUserCount);
        }
    }

}
