package com.whzl.mengbi.ui.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2018.10.26
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoViewHolder> {

    private final Context mContext;
    //    private final ArrayList<GuardListBean.GuardDetailBean> mData;
    private final List<String> mData;
    protected OnItemClickListener mOnItemClickListener;

    private CircleImageView mCircleHead;
    private ImageView mRoyalLevel;

    public AutoPollAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public AutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_auto_poll, parent, false);
        AutoViewHolder holder = new AutoViewHolder(view) {
            @Override
            public void onBindViewHolder(int position) {

            }
        };
        return holder;
    }

    @Override
    public void onBindViewHolder(final AutoViewHolder holder, int position) {
//        String data = mData.get(position % mData.size()).;
//        holder.itemView.setText(R.id.tv_content, data);
        mCircleHead = holder.itemView.findViewById(R.id.iv_circle_head);
        mRoyalLevel = holder.itemView.findViewById(R.id.iv_royal_level);
//        GlideImageLoader.getInstace().displayImage(mContext, mData.get(position % mData.size()).avatar, mCircleHead);
        GlideImageLoader.getInstace().displayImage(mContext, R.drawable.anchor_level1, mCircleHead);
        GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_7, mRoyalLevel);
//        circleImageView.setTooltipText(data);
        // 如果设置了回调，则设置点击事件/长按事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView.findViewById(R.id.circle_head), pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView.findViewById(R.id.circle_head), pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {

        void onItemClick(View viewById, int pos);

        void onItemLongClick(View viewById, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
