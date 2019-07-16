package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.LoginDialog;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.ExpValueLayout;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.ClipboardUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    LinearLayout tvFollowState;
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
    @BindView(R.id.tv_rank_name)
    TextView tvRankName;

    private long mUserId, mVisitorId;
    private PersonalInfoBean.DataBean userBean;
    private int levelValue;
    private String levelType;
    private BaseListAdapter medalAdapter;
    private int fansCount;
    private int mProgramId;
    private List<PersonalInfoBean.DataBean.LevelListBean> levelList;
    private List<PersonalInfoBean.DataBean.GoodsListBean> goodsList = new ArrayList<>();

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void setStatusBar() {
        View mViewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mUserId = bundle.getLong("userId", 0); //被访者
//        mVisitorId = bundle.getLong("visitorId", 0); //访问者
        mProgramId = bundle.getInt("programId", 0);
        mVisitorId = Long.parseLong(SPUtils.get(this, "userId", 0L).toString());
        getHomePageInfo(mUserId, mVisitorId);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.tv_follow_state, R.id.tv_copy_num, R.id.tv_live_state,
            R.id.ib_anchor_note_personinfo, R.id.ib_rich_note_personinfo, R.id.ib_royal_note_personinfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_follow_state:
                if (mVisitorId == 0) {
                    login();
                    return;
                }
                follow(mProgramId, mVisitorId);
                break;
            case R.id.tv_copy_num:
                ClipboardUtils.putTextIntoClip(this, mUserId + "");
                break;
            case R.id.tv_live_state:
                Intent intent = new Intent(this, LiveDisplayActivity.class);
                intent.putExtra(BundleConfig.PROGRAM_ID, mProgramId);
                startActivity(intent);
                break;
            case R.id.ib_anchor_note_personinfo:
                startActivity(new Intent(PersonalInfoActivity.this, JsBridgeActivity.class)
                        .putExtra("title", "主播等级")
                        .putExtra("url", SPUtils.get(PersonalInfoActivity.this, SpConfig.ANCHORGRADEURL, "").toString()));
                break;
            case R.id.ib_rich_note_personinfo:
                startActivity(new Intent(PersonalInfoActivity.this, JsBridgeActivity.class)
                        .putExtra("title", "富豪等级")
                        .putExtra("url", SPUtils.get(PersonalInfoActivity.this, SpConfig.USERGRADEURL, "").toString()));
                break;
            case R.id.ib_royal_note_personinfo:
                startActivity(new Intent(PersonalInfoActivity.this, JsBridgeActivity.class)
                        .putExtra("title", "royal"));
                break;
            default:
                break;
        }
    }

    public void login() {
        LoginDialog.newInstance()
                .setLoginSuccessListener(new LoginDialog.LoginSuccessListener() {
                    @Override
                    public void onLoginSuccessListener() {
                        mVisitorId = (long) SPUtils.get(PersonalInfoActivity.this, "userId", 0L);
//                        tvFollowState.setVisibility(View.GONE);
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0.7f)
                .show(getSupportFragmentManager());
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
        tvNickName.setText(userBean.getNickname());
        String introduce = userBean.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            tvSign.setText(introduce);
        }

        if (userBean.getRank() < 0) {
            if ("ANCHOR".equals(userBean.getUserType())) {
                tvRank.setText("万名之外");
                tvRankName.setText("主播排名");
            } else {
                tvRank.setText("万名之外");
                tvRankName.setText("富豪排名");
            }
        } else {
            if ("ANCHOR".equals(userBean.getUserType())) {
                fansCount = userBean.getRank();
                tvRank.setText(fansCount + "");
                tvRankName.setText("主播排名");
            } else {
                tvRank.setText(userBean.getRank() + "");
                tvRankName.setText("富豪排名");
            }
        }
        tvFansCount.setText(userBean.programSubUserCount + "");
        tvFollow.setText(userBean.userSubProgramCount + "");

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
                        LinearLayout.LayoutParams royalParams = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 45), UIUtil.dip2px(PersonalInfoActivity.this, 15));
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
//                        tvFollowState.setVisibility(View.VISIBLE);
                    }
                    tvLiveState.setVisibility(View.VISIBLE);
                    if ("T".equals(userBean.getLiveStatus())) {
                        tvLiveState.setText(R.string.live);
                        tvLiveState.setTextColor(Color.rgb(255, 43, 63));
                    } else {
                        tvLiveState.setText(R.string.rest);
                        tvLiveState.setTextColor(Color.WHITE);
                    }
                    if ("ANCHOR_LEVEL".equals(levelType)) {
                        imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                    }
                } else if ("USER_LEVEL".equals(levelType)) {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                    break;
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(PersonalInfoActivity.this, 39.17f), UIUtil.dip2px(PersonalInfoActivity.this, 15));
            params.leftMargin = UIUtil.dip2px(PersonalInfoActivity.this, 3);
            linearLayout.addView(imageView, params);
        }

        //守护，VIP
        if (userBean.getGoodsList() != null) {
            for (int i = 0; i < userBean.getGoodsList().size(); i++) {
                PersonalInfoBean.DataBean.GoodsListBean goodsListBean = userBean.getGoodsList().get(i);

                if ("VIP".equals(goodsListBean.getGoodsType())) {
                    ImageView vipImage = new ImageView(this);
                    vipImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_vip));
                    LinearLayout.LayoutParams vip = new LinearLayout.LayoutParams(UIUtil.dip2px(this, 15), UIUtil.dip2px(this, 15));
                    vip.leftMargin = UIUtil.dip2px(this, 3);
                    linearLayout.addView(vipImage, vip);
                }

                if ("PRETTY_NUM".equals(goodsListBean.getGoodsType())) {
                    tvPrettyNum.setVisibility(View.VISIBLE);
                    tvPrettyNum.setPrettyTextSize(12);
                    if ("A".equals(goodsListBean.getGoodsColor())) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("A");
                    } else if ("B".equals(goodsListBean.getGoodsColor())) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("B");
                    } else if ("C".equals(goodsListBean.getGoodsColor())) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("C");
                    } else if ("D".equals(goodsListBean.getGoodsColor())) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("D");
                    } else if ("E".equals(goodsListBean.getGoodsColor())) {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("E");
                    } else {
                        tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                        tvPrettyNum.setPrettyType("");
                    }
                }
            }
        }
    }

    private void follow(long programId, long followingUserId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", followingUserId + "");
        ApiFactory.getInstance().getApi(Api.class)
                .addSub(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement jsonElement) {
//                        tvFollowState.setVisibility(View.GONE);
                        showToast("关注成功");
                        tvFansCount.setText(getString(R.string.fans_count, userBean.getFansNum() + 1));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mVisitorId = (long) SPUtils.get(this, "userId", 0L);
//                tvFollowState.setVisibility(View.GONE);
            }
        }
    }
}
