package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.ExpValueLayout;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.TextProgressBar;
import com.whzl.mengbi.util.ClipboardUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.30
 */
public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.iv_personal_cover)
    ImageView ivPersonalCover;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_ranking)
    TextView tvRank;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_basic_nick_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_constellation)
    TextView tvConstellation;
    @BindView(R.id.tv_pretty_num)
    PrettyNumText tvPrettyNum;
    @BindView(R.id.ll_level_container)
    LinearLayout linearLayout;
    @BindView(R.id.tv_live_state)
    TextView tvLiveState;
    @BindView(R.id.tv_follow_state)
    TextView tvFollowState;
    @BindView(R.id.rv_medal_wall)
    RecyclerView rvMedalWall;
    @BindView(R.id.tv_copy_num)
    TextView tvCopyNum;
    @BindView(R.id.ll_anchor_level)
    LinearLayout llAnchorLevel;
    @BindView(R.id.evl_anchor_level)
    ExpValueLayout evlAnchorLevel;
    @BindView(R.id.evl_user_level)
    ExpValueLayout evlUserLevel;
    @BindView(R.id.evl_royal_level)
    ExpValueLayout evlRoyalLevel;

    private long mUserId, mVisitorId;
    private PersonalInfoBean.DataBean userBean;
    private int levelValue;
    private String levelType;
    private String mLiveState;
    private BaseListAdapter medalAdapter;
    private int fansCount;
    private List<PersonalInfoBean.DataBean.LevelListBean> levelList;
    private List<PersonalInfoBean.DataBean.GoodsListBean> goodsList = new ArrayList<>();

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mUserId = bundle.getLong("userId", 0); //被访者
        mVisitorId = bundle.getLong("visitorId", 0); //访问者
        mLiveState = bundle.getString("liveState", "");
        getHomePageInfo(mUserId, mVisitorId);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.tv_follow_state, R.id.tv_copy_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_follow_state:
                follow(mVisitorId, mUserId);
                break;
            case R.id.tv_copy_num:
                ClipboardUtils.putTextIntoClip(this, mUserId + "");
                break;
            default:
                break;
        }
    }

    private void getHomePageInfo(long userId, long visitorId) {
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("visitorId", visitorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.HOME_PAGE, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                PersonalInfoBean personalInfoBean = GsonUtils.GsonToBean(result.toString(), PersonalInfoBean.class);
                if (personalInfoBean.getCode() == 200) {
                    if (personalInfoBean.getData() != null) {
                        userBean = personalInfoBean.getData();
                        setView(userBean);
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setView(PersonalInfoBean.DataBean userBean) {
        GlideImageLoader.getInstace().displayImage(this, userBean.getAvatar(), ivAvatar);
        GlideImageLoader.getInstace().displayImage(this, userBean.getAvatar(), ivPersonalCover);
        tvNickName.setText(userBean.getNickname());
        String introduce = userBean.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            tvSign.setText(introduce);
        }

        if (userBean.getRank() < 0) {
            if ("ANCHOR".equals(userBean.getUserType())) {
                tvRank.setText(getString(R.string.anchor_rank_out, "万名之外"));
            } else {
                tvRank.setText(getString(R.string.user_rank_out, "万名之外"));
            }
        } else {
            if ("ANCHOR".equals(userBean.getUserType())) {
                fansCount = userBean.getRank();
                tvRank.setText(getString(R.string.anchor_rank, fansCount));
            } else {
                tvRank.setText(getString(R.string.user_rank, userBean.getRank()));
            }
        }
        tvFansCount.setText(getString(R.string.fans_count, userBean.getFansNum()));
        tvFollow.setText(getString(R.string.follow, userBean.getMyFollowNum()));

        tvUserName.setText(userBean.getNickname());
        tvUserId.setText(userBean.getUserId() + "");

        if (!TextUtils.isEmpty(userBean.getCity())) {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(userBean.getCity());
        }
        if (!TextUtils.isEmpty(userBean.getBirthday())) {
            tvAge.setVisibility(View.VISIBLE);
            tvAge.setText(DateUtils.getAge(userBean.getBirthday()) + "岁");
            tvConstellation.setVisibility(View.VISIBLE);
            tvConstellation.setText(DateUtils.getConstellations(userBean.getBirthday()));
        }

        setLevelIcon(userBean);

        setLevel(userBean);

        if (userBean.getGoodsList() != null) {
            for (int i = 0; i < userBean.getGoodsList().size(); i++) {
                String type = userBean.getGoodsList().get(i).getGoodsType();
                if ("BADGE".equals(type)) {
                    goodsList.add(userBean.getGoodsList().get(i));
                }
            }
            initRecy();
        }
    }

    private void setLevelIcon(PersonalInfoBean.DataBean userBean) {
        linearLayout.removeAllViews();
        ImageView imageView = new ImageView(this);
        //贵族等级
        if (userBean.getLevelList() != null) {
            for (int i = 0; i < userBean.getLevelList().size(); i++) {
                levelType = userBean.getLevelList().get(i).getLevelType();
                levelValue = userBean.getLevelList().get(i).getLevelValue();
                if ("ROYAL_LEVEL".equals(levelType)) {
                    if (levelValue == 0) {
                        break;
                    } else {
                        ImageView royalImage = new ImageView(PersonalInfoActivity.this);
                        royalImage.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
                        LinearLayout.LayoutParams royalParams = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 30), UIUtil.dip2px(PersonalInfoActivity.this, 11));
                        royalParams.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
                        linearLayout.addView(royalImage, royalParams);
                    }
                }
            }
        }

        //主播用户等级
        if (userBean.getLevelList() != null) {
            for (int i = 0; i < userBean.getLevelList().size(); i++) {
                levelType = userBean.getLevelList().get(i).getLevelType();
                levelValue = userBean.getLevelList().get(i).getLevelValue();
                if ("ANCHOR".equals(userBean.getUserType())) {
                    if ("F".equals(userBean.getIsFollowed())) {
                        tvFollowState.setVisibility(View.VISIBLE);
                    }
                    tvLiveState.setVisibility(View.VISIBLE);
                    if ("T".equals(mLiveState)) {
                        tvLiveState.setText(R.string.live);
                        tvLiveState.setTextColor(Color.RED);
                    } else {
                        tvLiveState.setText(R.string.rest);
                        tvLiveState.setTextColor(Color.GRAY);
                    }

                    imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                } else {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 28), UIUtil.dip2px(PersonalInfoActivity.this, 15));
            params.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
            linearLayout.addView(imageView, params);
        }

        //守护，VIP
        if (userBean.getGoodsList() != null) {
            for (int i = 0; i < userBean.getGoodsList().size(); i++) {
                PersonalInfoBean.DataBean.GoodsListBean goodsListBean = userBean.getGoodsList().get(i);
                if ("BADGE".equals(goodsListBean.getGoodsType()) || "VIP".equals(goodsListBean.getGoodsType())) {
                    Glide.with(this)
                            .load(goodsListBean.getGoodsIcon())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    if (this == null) {
                                        return;
                                    }
                                    int intrinsicHeight = resource.getIntrinsicHeight();
                                    int intrinsicWidth = resource.getIntrinsicWidth();
                                    ImageView goodImage = new ImageView(PersonalInfoActivity.this);
                                    goodImage.setImageDrawable(resource);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 15), UIUtil.dip2px(PersonalInfoActivity.this, 15));
                                    params.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
                                    linearLayout.addView(goodImage, params);
                                }
                            });
                }

                if ("PRETTY_NUM".equals(goodsListBean.getGoodsType())) {
                    tvPrettyNum.setVisibility(View.VISIBLE);
                    if (goodsListBean.getGoodsName().length() == 5) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setNumColor(Color.rgb(255, 43, 63));
                        tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_five);
                        tvPrettyNum.setNumber();
                    } else if (goodsListBean.getGoodsName().length() == 6) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setNumColor(Color.rgb(255, 165, 0));
                        tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_six);
                        tvPrettyNum.setNumber();
                    } else if (goodsListBean.getGoodsName().length() == 7) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setNumColor(Color.rgb(49, 161, 255));
                        tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_seven);
                        tvPrettyNum.setNumber();
                    }

                }
            }
        }
    }

    private void follow(long userId, long followingUserId) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("followingUserId", followingUserId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SOCIAL_FOLLOW, RequestManager.TYPE_POST_JSON, map,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
                            tvFollowState.setVisibility(View.GONE);
                            showToast("关注成功");
                            tvRank.setText(getString(R.string.anchor_rank, fansCount + 1));
                        } else {
                            showToast(responseInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        ToastUtils.showToast(errorMsg);
                    }
                });
    }

    private void initRecy() {
        rvMedalWall.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvMedalWall.setFocusableInTouchMode(false);
        rvMedalWall.setHasFixedSize(true);
        rvMedalWall.setLayoutManager(new GridLayoutManager(this, 4));
        rvMedalWall.addItemDecoration(new SpacesItemDecoration(10));
        medalAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return goodsList == null ? 0 : goodsList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.item_medal_wall, parent, false);
                return new MedalViewHolder(itemView);
            }
        };
        rvMedalWall.setAdapter(medalAdapter);
    }

    class MedalViewHolder extends BaseViewHolder {
        ImageView ivMedal;
        TextView tvMedal;

        public MedalViewHolder(View itemView) {
            super(itemView);
            ivMedal = itemView.findViewById(R.id.iv_medal);
            tvMedal = itemView.findViewById(R.id.tv_medal);
        }

        @Override
        public void onBindViewHolder(int position) {
            String goodsIcon = goodsList.get(position).getGoodsIcon();
            String goodsName = goodsList.get(position).getGoodsName();
            String goodsType = goodsList.get(position).getGoodsType();
            if ("BADGE".equals(goodsType)) {
                GlideImageLoader.getInstace().displayImage(PersonalInfoActivity.this, goodsIcon, ivMedal);
                tvMedal.setText(goodsName);
            }
        }
    }

    private void setLevel(PersonalInfoBean.DataBean dataBean) {
        levelList = dataBean.getLevelList();
        if (levelList != null) {
            for (int i = 0; i < levelList.size(); i++) {
                if ("ANCHOR_LEVEL".equals(levelList.get(i).getLevelType())) {
                    llAnchorLevel.setVisibility(View.VISIBLE);
                    evlAnchorLevel.setLevelValue(levelList.get(i).getLevelType(), levelList.get(i).getLevelValue(),
                            levelList.get(i).getLevelName(), levelList.get(i).getExpList());
                    evlAnchorLevel.initView();
                } else if ("USER_LEVEL".equals(levelList.get(i).getLevelType())) {
                    evlUserLevel.setLevelValue(levelList.get(i).getLevelType(), levelList.get(i).getLevelValue(),
                            levelList.get(i).getLevelName(), levelList.get(i).getExpList());
                    evlUserLevel.initView();
                } else if ("ROYAL_LEVEL".equals(levelList.get(i).getLevelType())) {
                    evlRoyalLevel.setLevelValue(levelList.get(i).getLevelType(), levelList.get(i).getLevelValue(),
                            levelList.get(i).getLevelName(), levelList.get(i).getExpList());
                    evlRoyalLevel.initView();
                }
            }
        }
    }

}