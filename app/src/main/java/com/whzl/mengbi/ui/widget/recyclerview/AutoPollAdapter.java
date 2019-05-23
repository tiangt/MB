package com.whzl.mengbi.ui.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.RoyalLevel;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author cliang
 * @date 2018.10.26
 */
public class AutoPollAdapter extends BaseListAdapter {
    public void setmAudienceList(ArrayList<AudienceListBean.AudienceInfoBean> mAudienceList) {
        this.mAudienceList = mAudienceList;
    }

    private ArrayList<AudienceListBean.AudienceInfoBean> mAudienceList = new ArrayList<>();
    private Context context;
    private OnclickListerner listerner;

    public void setListerner(OnclickListerner listerner) {
        this.listerner = listerner;
    }

    public AutoPollAdapter(ArrayList<AudienceListBean.AudienceInfoBean> mAudienceList, Context context) {
        this.mAudienceList = mAudienceList;
        this.context = context;
    }

    @Override
    protected int getDataCount() {
        if (mAudienceList == null || mAudienceList.size() <= 1) {
            return 0;
        } else if (mAudienceList.size() > 50) {
            return 50;
        } else {
            return mAudienceList.size() - 1;
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_auto_poll, parent, false);
        return new ProtectViewHolder(itemView);
    }

    class ProtectViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_circle_head)
        ImageView ivHead;
        @BindView(R.id.iv_royal_level)
        ImageView ivRoyal;
        @BindView(R.id.circle_head)
        RelativeLayout rl;


        public ProtectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            int mUserRoyalLevel = mAudienceList.get(position + 1).getLevelMap().getROYAL_LEVEL();
            if (mUserRoyalLevel > 0) {
                rl.setBackgroundResource(R.drawable.shape_online_head_royal);
                if (BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                    setRoyalTag(mUserRoyalLevel, ivRoyal);
                } else {
                    setRoyalTagNoGif(mUserRoyalLevel, ivRoyal);
                }
            } else {
                rl.setBackgroundResource(R.drawable.shape_online_head_civilian);
                GlideImageLoader.getInstace().displayImage(context, null, ivRoyal);
            }
            GlideImageLoader.getInstace().displayCircleAvatar(context, mAudienceList.get(position + 1).getAvatar(), ivHead);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            long userId = mAudienceList.get(position + 1).getUserid();
//            showAudienceInfoDialog(userId, false);
            if (listerner != null) {
                listerner.onClick(position);
            }
        }
    }


    public interface OnclickListerner {
        void onClick(int position);
    }

    /**
     * 用户贵族等级
     *
     * @param level
     * @param mRoyalLevel
     */
    private void setRoyalTag(int level, ImageView mRoyalLevel) {
        switch (level) {
            case RoyalLevel.ROYAL_BRONZE:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_1, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_SILVER:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_2, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_GOLD:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_3, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_PLATINUM:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_4, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_DIAMOND:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_5, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_STAR:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_6, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_KING:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_7, mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_LEGENT:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.royal_8, mRoyalLevel);
                break;
        }
    }

    /**
     * 用户贵族等级
     *
     * @param level
     * @param mRoyalLevel
     */
    private void setRoyalTagNoGif(int level, ImageView mRoyalLevel) {
        switch (level) {
            case RoyalLevel.ROYAL_BRONZE:
                Glide.with(context).asBitmap().load(R.drawable.royal_1).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_SILVER:
                Glide.with(context).asBitmap().load(R.drawable.royal_2).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_GOLD:
                Glide.with(context).asBitmap().load(R.drawable.royal_3).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_PLATINUM:
                Glide.with(context).asBitmap().load(R.drawable.royal_4).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_DIAMOND:
                Glide.with(context).asBitmap().load(R.drawable.royal_5).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_STAR:
                Glide.with(context).asBitmap().load(R.drawable.royal_6).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_KING:
                Glide.with(context).asBitmap().load(R.drawable.royal_7).into(mRoyalLevel);
                break;
            case RoyalLevel.ROYAL_LEGENT:
                Glide.with(context).asBitmap().load(R.drawable.royal_8).into(mRoyalLevel);
                break;

        }
    }
}
