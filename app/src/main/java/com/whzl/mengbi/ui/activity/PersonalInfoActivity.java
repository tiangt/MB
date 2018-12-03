package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.TextProgressBar;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.30
 */
public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tp_anchor_level)
    TextProgressBar tpAnchorLevel;
    @BindView(R.id.tp_user_level)
    TextProgressBar tpUserLevel;
    @BindView(R.id.tp_royal_level)
    TextProgressBar tpRoyalLevel;
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
    @BindView(R.id.ll_level_container)
    LinearLayout linearLayout;
    @BindView(R.id.tv_live_state)
    TextView tvLiveState;
    @BindView(R.id.tv_follow_state)
    TextView tvFollowState;

    private int mAnchorGrade;
    private int mUserGrade;
    private int mRoyalGrade;
    private long mUserId, mCurrentUserId;
    private int mProgramId;
    private RoomUserInfo.DataBean userBean;
    private List<RoomUserInfo.LevelMapBean> levelMapBeans;
    private int levelValue;
    private String levelType;
    private boolean mIsSubs;


    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void setupView() {
        mUserId = getIntent().getLongExtra("userId", 0);
        mProgramId = getIntent().getIntExtra("programId", 0);
        mCurrentUserId = getIntent().getLongExtra("currentUserId", 0);
        mIsSubs = getIntent().getBooleanExtra("isSubs", false);
        getUserInfo(mUserId, mProgramId);
        tpAnchorLevel.setText(getString(R.string.anchor_grade, mAnchorGrade));
        tpAnchorLevel.setMaxCount(100);
        tpAnchorLevel.setCurrentCount(50);
        tpAnchorLevel.setPercentColor(Color.rgb(236, 81, 227));
        tpUserLevel.setText(getString(R.string.user_grade, mUserGrade));
        tpUserLevel.setMaxCount(100);
        tpUserLevel.setCurrentCount(50);
        tpUserLevel.setPercentColor(Color.rgb(236, 194, 56));
        tpRoyalLevel.setText(getString(R.string.royal_grade, mRoyalGrade));
        tpRoyalLevel.setMaxCount(100);
        tpRoyalLevel.setCurrentCount(50);
        tpRoyalLevel.setPercentColor(Color.rgb(246, 55, 73));

        tvRank.setText(getString(R.string.user_rank, 0));
        tvFansCount.setText(getString(R.string.fans_count, 0));
        tvFollow.setText(getString(R.string.follow, 0));
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.tv_follow_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_follow_state:
                follow();
                break;
            default:
                break;
        }
    }

    private void getUserInfo(long userId, long programId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomUserInfo roomUserInfoData = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfoData.getCode() == 200) {
                    if (roomUserInfoData.getData() != null) {
                        userBean = roomUserInfoData.getData();
                        setView(userBean);
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setView(RoomUserInfo.DataBean userBean) {
        GlideImageLoader.getInstace().displayImage(this, userBean.getAvatar(), ivAvatar);
        tvNickName.setText(userBean.getNickname());
        String introduce = userBean.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            tvSign.setText(introduce);
        }

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

    }

    private void setLevelIcon(RoomUserInfo.DataBean userBean) {
        int identityId = userBean.getIdentityId();
        linearLayout.removeAllViews();
        ImageView imageView = new ImageView(this);
        levelMapBeans = new ArrayList<>();
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
                        LinearLayout.LayoutParams royalParams = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 20), UIUtil.dip2px(PersonalInfoActivity.this, 20));
                        royalParams.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
                        royalParams.gravity = UIUtil.dip2px(PersonalInfoActivity.this, 6);
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
                    if (!mIsSubs) {
                        tvFollowState.setVisibility(View.VISIBLE);
                    }
                    tvLiveState.setVisibility(View.VISIBLE);
                    imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                } else {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 20), UIUtil.dip2px(PersonalInfoActivity.this, 20));
            params.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
            params.gravity = UIUtil.dip2px(PersonalInfoActivity.this, 6);
            linearLayout.addView(imageView, params);
        }

        //守护，VIP
        if (userBean.getGoodsList() != null) {
            for (int i = 0; i < userBean.getGoodsList().size(); i++) {
                RoomUserInfo.GoodsListBean goodsListBean = userBean.getGoodsList().get(i);
                if ("BADGE".equals(goodsListBean.getGoodsType()) || "GUARD".equals(goodsListBean.getGoodsType()) || "VIP".equals(goodsListBean.getGoodsType())) {
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
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this
                                            , intrinsicWidth / 4f * 3)
                                            , UIUtil.dip2px(PersonalInfoActivity.this, intrinsicHeight / 4f * 3));
                                    params.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 6);
                                    params.gravity = UIUtil.dip2px(PersonalInfoActivity.this, 6);
                                    linearLayout.addView(goodImage, params);
                                }
                            });
                }
            }
        }

        //房管
        if (identityId == UserIdentity.ROOM_MANAGER) {
            ImageView mgrView = new ImageView(this);
            mgrView.setImageResource(R.drawable.room_manager);
            LinearLayout.LayoutParams mgrViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mgrViewParams.leftMargin = UIUtil.dip2px(this, 6);
            mgrViewParams.gravity = UIUtil.dip2px(this, 6);
            linearLayout.addView(mgrView, mgrViewParams);
        }
    }

    private void follow() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", mProgramId + "");
        paramsMap.put("userId", mCurrentUserId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FELLOW_HOST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    ToastUtils.showToast("关注成功");
                    tvFollowState.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

}
