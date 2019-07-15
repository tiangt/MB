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
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.eventbus.event.AudienceEvent;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.RoyalCarListBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.dialog.UserListDialog;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cliang
 * @date 2018.12.19
 */
public class UserListFragment extends BasePullListFragment<AudienceListBean.AudienceInfoBean, BasePresenter> {

    private AudienceListBean.DataBean audienceBean;
    private RoyalCarListBean royalList;

    public static UserListFragment newInstance(AudienceListBean.DataBean audienceBean, RoyalCarListBean listBean) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putParcelable("audienceBean", audienceBean);
        args.putParcelable("royalList", listBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        EventBus.getDefault().register(this);
    }

    @Override
    protected boolean setShouldRefresh() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        royalList = getArguments().getParcelable("royalList");
        hideDividerShawdow(null);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AudienceEvent event) {
        if (event.bean != null) {
            loadSuccess(event.bean.getList());
            UserListDialog userListDialog = (UserListDialog) getParentFragment();
            if (event.bean.getList() == null || event.bean.getList().isEmpty()) {
                userListDialog.setUserTitle(0);
            } else {
                userListDialog.setUserTitle(event.bean.total);
            }
        } else {
            loadSuccess(null);
        }
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (getArguments() != null) {
            audienceBean = getArguments().getParcelable("audienceBean");
        }
        if (audienceBean != null) {
            loadSuccess(audienceBean.getList());
//            UserListDialog userListDialog = (UserListDialog) getParentFragment();
//            userListDialog.setUserTitle(audienceBean.total);
        } else {
            loadSuccess(null);
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_user, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
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
            tvPrettyNum.setVisibility(View.GONE);
            tvName.setText(audienceInfoBean.getName());
            GlideImageLoader.getInstace().displayCircleAvatar(getContext(), audienceInfoBean.getAvatar(), ivAvatar);
            int identity = audienceInfoBean.getIdentity();
            if (audienceInfoBean.getLevelMap().getROYAL_LEVEL() > 0) {
                ImageView royalImg = new ImageView(getContext());
//                if (BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
//                    Glide.with(getMyActivity()).asGif().load(ResourceMap.getResourceMap().
//                            getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL())).into(royalImg);
                GlideImageLoader.getInstace().displayGift(getMyActivity(), ResourceMap.getResourceMap().
                        getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL()), royalImg);
//                } else {
//                    Glide.with(getMyActivity()).asBitmap().load(ResourceMap.getResourceMap().
//                            getRoyalLevelIcon(audienceInfoBean.getLevelMap().getROYAL_LEVEL())).into(royalImg);
//                }
                LinearLayout.LayoutParams rparams = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 48), UIUtil.dip2px(getMyActivity(), 16));
                levelLayout.addView(royalImg, rparams);
            }

            ImageView imageView = new ImageView(getContext());
            if (identity == 10) {
                imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(audienceInfoBean.getLevelMap().getANCHOR_LEVEL()));
            } else {
                imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(audienceInfoBean.getLevelMap().getUSER_LEVEL()));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getMyActivity(), 36), UIUtil.dip2px(getMyActivity(), 14));
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
                    if ("DEMON_CARD".equals(medalBean.getGoodsType())) {
                        ImageView vipImage = new ImageView(getContext());
                        vipImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_succubus));
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

            GlideImageLoader.getInstace().displayImage(getContext(), null, ivCar);
            if (audienceInfoBean.getLevelMap() != null && royalList.getList() != null) {
                boolean b = hasCar(audienceInfoBean);
                if (!b) {
                    if (audienceInfoBean.getLevelMap().getROYAL_LEVEL() != 0 && royalList.getList().get(audienceInfoBean.getLevelMap().getROYAL_LEVEL() - 1) != null) {
                        GlideImageLoader.getInstace().displayImage(getContext()
                                , royalList.getList().get(audienceInfoBean.getLevelMap().getROYAL_LEVEL() - 1).getCarImageUrl(), ivCar);
                    }
                }
            }

        }

        private boolean hasCar(AudienceListBean.AudienceInfoBean audienceInfoBean) {
            for (int i = 0; i < audienceInfoBean.getMedal().size(); i++) {
                AudienceListBean.MedalBean medalBean = audienceInfoBean.getMedal().get(i);
                if ("CAR".equals(medalBean.getGoodsType())) {
                    GlideImageLoader.getInstace().displayImage(getContext()
                            , medalBean.getGoodsIcon(), ivCar);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AudienceListBean.AudienceInfoBean audienceInfoBean = mDatas.get(position);
            if (ClickUtil.isFastClick()) {
                if (getActivity() != null) {
                    ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(audienceInfoBean.getUserid(), true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
