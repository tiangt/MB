package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.List;

public class AudienceListAdapter<T> extends CommonAdapter<AudienceListBean.DataBean.AudienceInfoBean> {

    public AudienceListAdapter(Context context, int layoutId, List<AudienceListBean.DataBean.AudienceInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AudienceListBean.DataBean.AudienceInfoBean audienceInfoBean, int position) {
        LinearLayout llLevelContainer = holder.getView(R.id.ll_level_container);
        llLevelContainer.removeAllViews();
        holder.setText(R.id.tv_name, audienceInfoBean.getName());
        GlideImageLoader.getInstace().displayImage(mContext, audienceInfoBean.getAvatar(), holder.getView(R.id.iv_avatar));
        ImageView imageView = new ImageView(mContext);
        int identity = audienceInfoBean.getIdentity();
        if (identity == 10) {
            imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(audienceInfoBean.getLevelMap().getANCHOR_LEVEL()));
        } else {
            imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(audienceInfoBean.getLevelMap().getUSER_LEVEL()));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLevelContainer.addView(imageView, params);
        if (audienceInfoBean.getMedal() != null) {
            for (int i = 0; i < audienceInfoBean.getMedal().size(); i++) {
                AudienceListBean.DataBean.AudienceInfoBean.MedalBean medalBean = audienceInfoBean.getMedal().get(i);
                if ("BADGE".equals(medalBean.getGoodsType()) || "GUARD".equals(medalBean.getGoodsType())) {
                    Glide.with(mContext)
                            .load(medalBean.getGoodsIcon())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    int intrinsicHeight = resource.getIntrinsicHeight();  
                                    int intrinsicWidth = resource.getIntrinsicWidth();
                                    ImageView imageView = new ImageView(mContext);
                                    imageView.setImageDrawable(resource);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(mContext, intrinsicWidth / 4f * 3), UIUtil.dip2px(mContext, intrinsicHeight / 4f * 3));
                                    params.leftMargin = UIUtil.dip2px(mContext, 6);
                                    llLevelContainer.addView(imageView, params);
                                }
                            });
                }
            }
        }
    }
}
