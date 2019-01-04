package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
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
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.dialog.UserListDialog;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author cliang
 * @date 2018.12.19
 */
public class UserListFragment extends BasePullListFragment<AudienceListBean.AudienceInfoBean, BasePresenter> {

    private int mProgramId;
    private Disposable disposable;

    public static UserListFragment newInstance(int programId) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        super.init();
        getPullView().setShouldRefresh(false);
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_follow_sort, getPullView(), false);
        TextView content = view.findViewById(R.id.tv_content);
        content.setText("暂无玩家");
        content.setTextColor(Color.parseColor("#70ffffff"));
        setEmptyView(view);

        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    @Override
    protected void loadData(int action, int mPage) {
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
                                UserListDialog userListDialog = (UserListDialog) getParentFragment();
                                userListDialog.setUserTitle(dataBean.total);
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

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_user, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.ll_level_container)
        LinearLayout levelLayout;
        @BindView(R.id.tv_pretty_num)
        PrettyNumText tvPrettyNum;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_car)
        ImageView ivCar;
        @BindView(R.id.ll_medal_container)
        LinearLayout medalLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AudienceListBean.AudienceInfoBean audienceInfoBean = mDatas.get(position);
            levelLayout.removeAllViews();
            medalLayout.removeAllViews();
            ivCar.setVisibility(View.GONE);
            tvName.setText(audienceInfoBean.getName());
            GlideImageLoader.getInstace().displayImage(getContext(), audienceInfoBean.getAvatar(), ivAvatar);
            int identity = audienceInfoBean.getIdentity();
            if (audienceInfoBean.getLevelMap().getROYAL_LEVEL() > 0) {
                ImageView royalImg = new ImageView(getContext());
                Glide.with(getMyActivity()).asGif().load(ResourceMap.getResourceMap().
                        getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL())).into(royalImg);
                LinearLayout.LayoutParams rparams = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 40), UIUtil.dip2px(getMyActivity(), 16));
                levelLayout.addView(royalImg, rparams);
            }

            ImageView imageView = new ImageView(getContext());
            if (identity == 10) {
                imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(audienceInfoBean.getLevelMap().getANCHOR_LEVEL()));
            } else {
                imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(audienceInfoBean.getLevelMap().getUSER_LEVEL()));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 38), UIUtil.dip2px(getMyActivity(), 16));
            params.leftMargin = UIUtil.dip2px(getContext(), 1);
            medalLayout.addView(imageView, params);

            if (audienceInfoBean.getMedal() != null) {
                for (int i = 0; i < audienceInfoBean.getMedal().size(); i++) {
                    AudienceListBean.MedalBean medalBean = audienceInfoBean.getMedal().get(i);
                    if ("VIP".equals(medalBean.getGoodsType())) {
                        ImageView vipImage = new ImageView(getContext());
                        vipImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_vip));
                        LinearLayout.LayoutParams vip = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 15), UIUtil.dip2px(getMyActivity(), 15));
                        vip.leftMargin = UIUtil.dip2px(getContext(), 3);
                        levelLayout.addView(vipImage, vip);
                    }
                    if ("GUARD".equals(medalBean.getGoodsType())) {
                        ImageView guardImage = new ImageView(getContext());
                        guardImage.setImageDrawable(getResources().getDrawable(R.drawable.guard));
                        LinearLayout.LayoutParams guard = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 15), UIUtil.dip2px(getMyActivity(), 15));
                        guard.leftMargin = UIUtil.dip2px(getContext(), 3);
                        levelLayout.addView(guardImage, guard);
                    }
                    if ("BADGE".equals(medalBean.getGoodsType())) {
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
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 18), UIUtil.dip2px(getMyActivity(), 16));
                                        params.leftMargin = UIUtil.dip2px(getContext(), 3);
                                        medalLayout.addView(imageView, params);
                                    }
                                });
                    }
                    //座驾
//                    if ("CAR".equals(medalBean.getGoodsType())) {
//                        ivCar.setVisibility(View.VISIBLE);
//                        GlideImageLoader.getInstace().displayImage(getMyActivity(), medalBean.getGoodsIcon(), ivCar);
//                    }

                    if ("PRETTY_NUM".equals(medalBean.getGoodsType())) {
                        tvPrettyNum.setVisibility(View.VISIBLE);
                        tvPrettyNum.setPrettyTextSize(10);
                        if ("A".equals(medalBean.getGoodsColor())) {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("A");
                        } else if ("B".equals(medalBean.getGoodsColor())) {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("B");
                        } else if ("C".equals(medalBean.getGoodsColor())) {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("C");
                        } else if ("D".equals(medalBean.getGoodsColor())) {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("D");
                        } else if ("E".equals(medalBean.getGoodsColor())) {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("E");
                        } else {
                            tvPrettyNum.setPrettyNum(medalBean.getGoodsName());
                            tvPrettyNum.setPrettyType("");
                        }
                    }
                }
            }

            if (identity == UserIdentity.ROOM_MANAGER) {
                ImageView mgrView = new ImageView(getContext());
                mgrView.setImageResource(R.drawable.room_manager);
                LinearLayout.LayoutParams mgrViewParams = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 15), UIUtil.dip2px(getMyActivity(), 15));
                mgrViewParams.leftMargin = UIUtil.dip2px(getContext(), 3);
                medalLayout.addView(mgrView, mgrViewParams);
            }

        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AudienceListBean.AudienceInfoBean audienceInfoBean = mDatas.get(position);
            if (getActivity() != null) {
                ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(audienceInfoBean.getUserid(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
