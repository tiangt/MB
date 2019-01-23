package com.whzl.mengbi.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GoodsPriceInfo;
import com.whzl.mengbi.model.entity.RankBeyondInfo;
import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;
import com.whzl.mengbi.model.entity.WeekStarRankListBean;
import com.whzl.mengbi.presenter.impl.WeekStarPresenterImpl;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.OneClickDialog;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.WeekStarListView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 周星榜
 *
 * @author cliang
 * @date 2019.1.3
 */
public class WeekStarListsFragment extends BaseFragment implements WeekStarListView {

    @BindView(R.id.rv_week_gift)
    RecyclerView rvWeekGift;
    @BindView(R.id.rv_anchor)
    RecyclerView rvAnchor;
    @BindView(R.id.rv_user)
    RecyclerView rvUser;
    @BindView(R.id.rl_gift)
    RelativeLayout rlGift;
    @BindView(R.id.tv_own_ranking)
    TextView tvRank;
    @BindView(R.id.iv_own_avatar)
    CircleImageView ivOwnAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_charm_value)
    TextView tvValue;
    @BindView(R.id.tv_click_gift)
    TextView tvClickGift;
    @BindView(R.id.tv_need_value)
    TextView tvNeedValue;
    @BindView(R.id.rl_empty_anchor)
    RelativeLayout ivEmptyAnchor;
    @BindView(R.id.rl_empty_user)
    RelativeLayout ivEmptyUser;

    private WeekStarPresenterImpl mPresenterImpl;
    private String mType;
    private int mAnchorId;
    private BaseListAdapter userAdapter;
    private BaseListAdapter anchorAdapter;
    private BaseListAdapter giftAdapter;
    private int[] gifts = {R.drawable.shape_gradient_magenta, R.drawable.shape_gradient_red, R.drawable.shape_gradient_yellow};
    private ArrayList<WeekStarGiftInfo.DataBean.ListBean> mListBean = new ArrayList<>();
    private ArrayList<WeekStarRankListBean.RankListBean> mAnchorList = new ArrayList<>();
    private ArrayList<WeekStarRankListBean.RankListBean> mUserList = new ArrayList<>();
    private int mAnchorRankId;
    private int mUserRankId;
    private String mNickName;
    private String mAvatar;
    private Long userId;
    private int goodsRent;
    private String goodsName;
    private int needGifts;
    private int mProgramId;
    private int mGoodsId;
    private ArrayList<Boolean> isCheck = new ArrayList<>();
    private int selfScore;
    private int topScore;
    private int[] rankIcons = new int[]{R.drawable.ic_headline_rank1, R.drawable.ic_headline_rank2, R.drawable.ic_headline_rank3};


    public static WeekStarListsFragment newInstance(String type, int anchorId, String nickName, String avatar, int programId) {
        Bundle args = new Bundle();
        args.putString("weekType", type);
        args.putInt("anchorId", anchorId);
        args.putString("nickName", nickName);
        args.putString("avatar", avatar);
        args.putInt("programId", programId);
        WeekStarListsFragment fragment = new WeekStarListsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_star_lists;
    }

    @Override
    public void init() {
        mType = getArguments().getString("weekType");
        mAnchorId = getArguments().getInt("anchorId", 0);
        mNickName = getArguments().getString("nickName");
        mAvatar = getArguments().getString("avatar");
        mProgramId = getArguments().getInt("programId");
        if ("F".equals(mType)) {
            rlGift.setVisibility(View.VISIBLE);
        } else {
            rlGift.setVisibility(View.GONE);
        }
        loadData();
        initGift();
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresenterImpl = new WeekStarPresenterImpl(this);
    }

    private void loadData() {
        mPresenterImpl.getGiftList();
    }

    @Override
    public void showGiftList(WeekStarGiftInfo weekStarGiftInfo) {
        if (weekStarGiftInfo != null && weekStarGiftInfo.getData() != null && weekStarGiftInfo.getData().getList() != null) {
            if (weekStarGiftInfo.getData().getList() != null) {
                mListBean.clear();
                for (int j = 0; j < weekStarGiftInfo.getData().getList().size(); j++) {
                    mListBean.add(weekStarGiftInfo.getData().getList().get(j));
                    if (j == 0) {
                        isCheck.add(true);
                    } else {
                        isCheck.add(false);
                    }

                }
                for (int i = 0; i < mListBean.size(); i++) {
                    if (i == 0) {
                        mAnchorRankId = mListBean.get(i).anchorRankId;
                        mUserRankId = mListBean.get(i).userRankId;
                        loadRankList(mAnchorRankId, mUserRankId, mType);
                        mGoodsId = mListBean.get(i).goodsId;
                        if ("F".equals(mType)) {
                            getBeyondFirst(mAnchorId, mListBean.get(i).goodsId, mListBean.get(i).anchorRankId, mListBean.get(i).goodsName);
                        }
                        break;
                    }
                }
                giftAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String msg) {

    }

    @OnClick({R.id.tv_click_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_click_gift:
                userId = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
                if (ClickUtil.isFastClick()) {
                    if (userId == 0) {
                        ((LiveDisplayActivity) getActivity()).login();
                        return;
                    }
                    OneClickDialog.newInstance(mProgramId, mAnchorId, mGoodsId, needGifts, userId, goodsRent, goodsName)
                            .setListener(new OneClickDialog.OnClickListener() {
                                @Override
                                public void onSendSuccess() {
                                    setTopInfo();
                                }
                            })
                            .setOutCancel(false)
                            .show(getChildFragmentManager());
                }
                break;
            default:
                break;
        }
    }

    private void setTopInfo() {
        if (tvRank != null && tvNeedValue != null && tvValue != null) {
            tvClickGift.setVisibility(View.GONE);
            tvRank.setText(1 + "");
            tvRank.setTextColor(Color.RED);
            tvValue.setText(needGifts + selfScore + "个");
            tvNeedValue.setText(R.string.top_rank);
        }
    }

    private void initGift() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWeekGift.setFocusableInTouchMode(false);
        rvWeekGift.setHasFixedSize(true);
        rvWeekGift.setLayoutManager(layoutManage);
        giftAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mListBean == null ? 0 : mListBean.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_week_gift, parent, false);
                int parentWidth = parent.getWidth();
                GiftViewHolder viewHolder = new GiftViewHolder(itemView);
                ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
                layoutParams.width = parentWidth / 3;
                return viewHolder;
            }

        };
        rvWeekGift.setAdapter(giftAdapter);
        initUserRecycler();
        initAnchorRecycler();
    }

    private void initAnchorRecycler() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        rvAnchor.setFocusableInTouchMode(false);
        rvAnchor.setHasFixedSize(true);
        rvAnchor.setLayoutManager(layoutManage);

        anchorAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mAnchorList == null ? 0 : mAnchorList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_star, null);
                return new AnchorViewHolder(itemView);
            }
        };
        rvAnchor.setAdapter(anchorAdapter);
    }

    private void initUserRecycler() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        rvUser.setFocusableInTouchMode(false);
        rvUser.setHasFixedSize(true);
        rvUser.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvUser.setLayoutManager(layoutManage);

        userAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mUserList == null ? 0 : mUserList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_star, parent, false);
                return new UserViewHolder(itemView);
            }
        };
        rvUser.setAdapter(userAdapter);
    }

    class UserViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_ranking)
        TextView tvRanking;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_value)
        TextView tvUserValue;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvNickName.setText(mUserList.get(position).nickname);
            if (position < 3) {
                tvRanking.setBackgroundResource(rankIcons[position]);
                tvRanking.setText("");
            } else {
                tvRanking.setText(String.valueOf(position + 1));
            }
            tvUserValue.setText(StringUtils.formatNumber(mUserList.get(position).value) + "个");
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mUserList.get(position).avatar, ivAvatar);
            ivLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(mUserList.get(position).getUserLevelMap().USER_LEVEL));
        }

        @Override
        public void onItemClick(View view, int position) {
            if(ClickUtil.isFastClick()){
                if (getMyActivity() != null) {
                    ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(mUserList.get(position).userId, true);
                }
            }
        }
    }

    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_ranking)
        TextView tvRanking;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_value)
        TextView tvUserValue;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvNickName.setText(mAnchorList.get(position).nickname);
            if (position < 3) {
                tvRanking.setBackgroundResource(rankIcons[position]);
                tvRanking.setText("");
            } else {
                tvRanking.setText(String.valueOf(position + 1));
            }
            tvUserValue.setText(StringUtils.formatNumber(mAnchorList.get(position).value) + "个");
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mAnchorList.get(position).avatar, ivAvatar);
            ivLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(mAnchorList.get(position).getUserLevelMap().ANCHOR_LEVEL));
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if(ClickUtil.isFastClick()){
                if (getMyActivity() != null) {
                    ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(mAnchorList.get(position).userId, true);
                }
            }
        }
    }

    class GiftViewHolder extends BaseViewHolder {

        @BindView(R.id.rl_week_gift)
        RelativeLayout rlWeekGift;
        @BindView(R.id.iv_week_gift)
        ImageView ivGift;

        public GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            int remainder = 0;
            if (position < 3) {
                remainder = position;
            } else {
                remainder = position % 3;
            }
            if (isCheck.get(position)) {
                if (0 == remainder) {
                    rlWeekGift.setBackgroundResource(gifts[0]);
                } else if (1 == remainder) {
                    rlWeekGift.setBackgroundResource(gifts[1]);
                } else {
                    rlWeekGift.setBackgroundResource(gifts[2]);
                }
            } else {
                rlWeekGift.setBackgroundColor(Color.WHITE);
            }
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListBean.get(position).goodsPic, ivGift);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            for (int i = 0; i < isCheck.size(); i++) {
                isCheck.set(i, false);
            }
            isCheck.set(position, true);
            mAnchorRankId = mListBean.get(position).anchorRankId;
            mUserRankId = mListBean.get(position).userRankId;
            //点击更换RankList
            loadRankList(mAnchorRankId, mUserRankId, mType);
            mGoodsId = mListBean.get(position).goodsId;
            if ("F".equals(mType)) {
                getBeyondFirst(mAnchorId, mListBean.get(position).goodsId, mListBean.get(position).anchorRankId, mListBean.get(position).goodsName);
            }
            giftAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据RankId取主播及富豪RankList
     *
     * @param anchorRankId
     * @param userRankId
     * @param type
     */
    private void loadRankList(int anchorRankId, int userRankId, String type) {
        HashMap hashMap = new HashMap();
        hashMap.put("rankIdList", anchorRankId + "," + userRankId);
        hashMap.put("preRound", type);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.WEEKSTAR_RANK, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        WeekStarRankInfo weekStarRankInfo = GsonUtils.GsonToBean(result.toString(), WeekStarRankInfo.class);
                        if (weekStarRankInfo.getCode() == 200) {
                            if (weekStarRankInfo != null && weekStarRankInfo.getData() != null) {
                                if (weekStarRankInfo.getData().getList() != null) {
                                    for (int i = 0; i < weekStarRankInfo.getData().getList().size(); i++) {
                                        String rankId = weekStarRankInfo.getData().getList().get(i).rankId;
                                        if (rankId.equals(mAnchorRankId + "")) {
                                            //主播周星榜
                                            mAnchorList.clear();
                                            mAnchorList.addAll(weekStarRankInfo.getData().getList().get(i).getRankList());
                                            anchorAdapter.notifyDataSetChanged();
                                            if (ivEmptyAnchor != null) {
                                                if (mAnchorList == null || mAnchorList.size() == 0) {
                                                    ivEmptyAnchor.setVisibility(View.VISIBLE);
                                                } else {
                                                    ivEmptyAnchor.setVisibility(View.GONE);
                                                }
                                            }
                                        } else if (rankId.equals(mUserRankId + "")) {
                                            //富豪周星榜
                                            mUserList.clear();
                                            mUserList.addAll(weekStarRankInfo.getData().getList().get(i).getRankList());
                                            userAdapter.notifyDataSetChanged();
                                            if (ivEmptyUser != null) {
                                                if (mUserList == null || mUserList.size() == 0) {
                                                    ivEmptyUser.setVisibility(View.VISIBLE);
                                                } else {
                                                    ivEmptyUser.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg.toString());
                    }
                });
    }

    /**
     * 一键超越
     *
     * @param userId
     * @param goodsId
     * @param rankId
     * @param goodsName
     */
    private void getBeyondFirst(int userId, int goodsId, int rankId, String goodsName) {
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("goodsId", goodsId);
        paramsMap.put("rankId", rankId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RANK_BEYOND_FIRST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RankBeyondInfo beyondInfo = GsonUtils.GsonToBean(result.toString(), RankBeyondInfo.class);
                if (beyondInfo.getCode() == 200) {
                    if (beyondInfo.data != null) {
                        selfScore = beyondInfo.data.selfScore;
                        topScore = beyondInfo.data.topScore;
                        int rank = beyondInfo.data.rank;
                        if (tvRank != null && tvNickName != null && tvValue != null && tvClickGift != null) {
                            if (rank < 0) {
                                tvRank.setText("未上榜");
                                tvRank.setTextColor(Color.BLACK);
                            } else {
                                tvRank.setText(rank + "");
                                tvRank.setTextColor(Color.RED);
                            }
                            if (rank == 1) {
                                tvClickGift.setVisibility(View.GONE);
                            } else {
                                tvClickGift.setVisibility(View.VISIBLE);
                            }
                            tvValue.setText(selfScore + "个");
                            tvNickName.setText(mNickName);
                            tvNickName.setTextColor(Color.parseColor("#ff2b3f"));
                            GlideImageLoader.getInstace().displayImage(getMyActivity(), mAvatar, ivOwnAvatar);
                            int diff = topScore - selfScore;
                            getNeedGoods(goodsId, diff, rank);
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
            }
        });
    }

    /**
     * 获取物品价格
     *
     * @param goodsId
     * @param diffScore
     */
    private void getNeedGoods(int goodsId, int diffScore, int rank) {
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("goodsId", goodsId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GOODS_PRICE, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                GoodsPriceInfo goodsPriceInfo = GsonUtils.GsonToBean(result.toString(), GoodsPriceInfo.class);
                if (goodsPriceInfo.getCode() == 200) {
                    if (goodsPriceInfo.getData() != null) {
                        goodsRent = goodsPriceInfo.getData().rent;
                        goodsName = goodsPriceInfo.getData().goodsName;
                        int anchorExp = goodsPriceInfo.getData().anchorExp;
                        //需要的礼物个数
                        needGifts = diffScore + 1;
                        if (tvNeedValue != null) {
                            if (rank == 1) {
                                tvNeedValue.setText(R.string.top_rank);
                            } else {
                                tvNeedValue.setText("超越第1名需要");
                                SpannableString ss = StringUtils.spannableStringColor(StringUtils.formatNumber(needGifts), Color.parseColor("#000000"));
                                tvNeedValue.append(ss);
                                SpannableString goods = StringUtils.spannableStringColor("个" + goodsName, Color.parseColor("#70000000"));
                                tvNeedValue.append(goods);
                            }
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
            }
        });
    }
}
