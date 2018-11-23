package com.whzl.mengbi.ui.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.RoyalLevel;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;


/**
 * @author cliang
 * @date 2018.10.26
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoViewHolder> {

    private final Context mContext;
    private final ArrayList<AudienceListBean.AudienceInfoBean> mData;
    protected OnItemClickListener mOnItemClickListener;

    private View mView;
    private CircleImageView mCircleHead;
    private ImageView mRoyalLevel;
    private int mUserRoyalLevel;

    public AutoPollAdapter(Context context, ArrayList<AudienceListBean.AudienceInfoBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public AutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_auto_poll, parent, false);
        AutoViewHolder holder = new AutoViewHolder(mView) {
            @Override
            public void onBindViewHolder(int position) {

            }
        };
        return holder;
    }

    @Override
    public void onBindViewHolder(final AutoViewHolder holder, int position) {
        mCircleHead = holder.itemView.findViewById(R.id.iv_circle_head);
        mRoyalLevel = holder.itemView.findViewById(R.id.iv_royal_level);
        mUserRoyalLevel = mData.get(position % mData.size()).getLevelMap().getROYAL_LEVEL();
        if (mUserRoyalLevel > 0) {
            mView.setBackgroundResource(R.drawable.shape_online_head_royal);
            setRoyalTag(mUserRoyalLevel);
        } else {
            mView.setBackgroundResource(R.drawable.shape_online_head_civilian);
        }
        GlideImageLoader.getInstace().displayImage(mContext, mData.get(position % mData.size()).getAvatar(), mCircleHead);


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

    /**
     * 用户贵族等级
     *
     * @param level
     */
    private void setRoyalTag(int level) {
        switch (level) {
            case RoyalLevel.ROYAL_BRONZE:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_1, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_SILVER:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_2, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_GOLD:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_3, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_PLATINUM:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_4, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_DIAMOND:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_5, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_STAR:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_6, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_KING:
                GlideImageLoader.getInstace().displayImage(mContext, R.drawable.royal_7, mRoyalLevel);
                break;

        }
    }

}
