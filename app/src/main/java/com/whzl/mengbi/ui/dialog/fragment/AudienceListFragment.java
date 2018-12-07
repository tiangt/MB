package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;
import com.whzl.mengbi.ui.dialog.GuardListDialog;
import com.whzl.mengbi.ui.fragment.base.BaseListFragment;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class AudienceListFragment extends BaseListFragment<AudienceListBean.AudienceInfoBean> {

    private int mProgramId;
    private Disposable disposable;

    public static AudienceListFragment newInstance(int programId) {
        AudienceListFragment fragment = new AudienceListFragment();
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_audience, parent, false);
        return new AudienceViewHolder(itemView);
    }

    @Override
    protected void loadData() {
        disposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mProgramId = getArguments().getInt("programId");
            HashMap paramsMap = new HashMap();
            paramsMap.put("programId", mProgramId);
            HashMap signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
            ApiFactory.getInstance().getApi(Api.class)
                    .getAudienceList(signPramsMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<AudienceListBean.DataBean>() {
                        @Override
                        public void onSuccess(AudienceListBean.DataBean dataBean) {
                            if (dataBean != null) {
                                loadSuccess(dataBean.getList());
                            } else {
                                loadSuccess(null);
                            }
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        });

    }

    class AudienceViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_level_container)
        LinearLayout linearLayout;

        public AudienceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AudienceListBean.AudienceInfoBean audienceInfoBean = mData.get(position);
            linearLayout.removeAllViews();
            tvName.setText(audienceInfoBean.getName());
            GlideImageLoader.getInstace().displayImage(getContext(), audienceInfoBean.getAvatar(), ivAvatar);
            int identity = audienceInfoBean.getIdentity();
            if ( audienceInfoBean.getLevelMap().getROYAL_LEVEL() > 0) {
                ImageView royalImg = new ImageView(getContext());
                Glide.with(getMyActivity()).asGif().load(ResourceMap.getResourceMap().
                        getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL())).into(royalImg);
//                royalImg.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL()));
                LinearLayout.LayoutParams rparams = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(),30), UIUtil.dip2px(getMyActivity(),11));
                linearLayout.addView(royalImg, rparams);
            }

            ImageView imageView = new ImageView(getContext());
            if (identity == 10) {
                imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(audienceInfoBean.getLevelMap().getANCHOR_LEVEL()));
            } else {
                imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(audienceInfoBean.getLevelMap().getUSER_LEVEL()));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(),28), UIUtil.dip2px(getMyActivity(),15));
            linearLayout.addView(imageView, params);
            if (identity == UserIdentity.ROOM_MANAGER) {
                ImageView mgrView = new ImageView(getContext());
                mgrView.setImageResource(R.drawable.room_manager);
                LinearLayout.LayoutParams mgrViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mgrViewParams.leftMargin = UIUtil.dip2px(getContext(), 6);
                linearLayout.addView(mgrView, mgrViewParams);
            }
            if (audienceInfoBean.getMedal() != null) {
                for (int i = 0; i < audienceInfoBean.getMedal().size(); i++) {
                    AudienceListBean.MedalBean medalBean = audienceInfoBean.getMedal().get(i);
                    if ("BADGE".equals(medalBean.getGoodsType()) || "GUARD".equals(medalBean.getGoodsType())|| "VIP".equals(medalBean.getGoodsType())) {
                        Glide.with(getContext())
                                .load(medalBean.getGoodsIcon())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        if (getContext() == null) {
                                            return;
                                        }
                                        int intrinsicHeight = resource.getIntrinsicHeight();
                                        int intrinsicWidth = resource.getIntrinsicWidth();
                                        ImageView imageView = new ImageView(getContext());
                                        imageView.setImageDrawable(resource);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 15), UIUtil.dip2px(getContext(), 15));
                                        params.leftMargin = UIUtil.dip2px(getContext(), 6);
                                        linearLayout.addView(imageView, params);
                                    }
                                });
                    }
                }
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AudienceListBean.AudienceInfoBean audienceInfoBean = mData.get(position);
            if (getActivity() != null) {
                ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(audienceInfoBean.getUserid(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}
