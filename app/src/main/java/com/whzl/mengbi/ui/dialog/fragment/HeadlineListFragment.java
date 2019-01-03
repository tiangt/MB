package com.whzl.mengbi.ui.dialog.fragment;

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
import com.whzl.mengbi.model.entity.HeadlineListBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.RankBeyondInfo;
import com.whzl.mengbi.model.entity.RoomRankBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.OneClickDialog;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author cliang
 * @date 2018.12.20
 */
public class HeadlineListFragment extends BaseFragment {

    @BindView(R.id.rv_headline)
    RecyclerView recycler;
    @BindView(R.id.rl_gift)
    RelativeLayout rlGift;
    @BindView(R.id.tv_click_gift)
    TextView tvClickGift;
    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.tv_need_value)
    TextView tvNeedValue;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_own_ranking)
    TextView tvRank;
    @BindView(R.id.iv_own_avatar)
    CircleImageView ivOwnAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_charm_value)
    TextView tvCharmValue;

    private BaseListAdapter mAdapter;
    private String mType = "F"; //"F"本轮头条，"T"上轮头条，默认本轮
    private Disposable disposable;
    private int TOP_RANK = 0;
    private int OTHER_RANK = 1;
    private int[] rankIcons = new int[]{R.drawable.ic_headline_rank1, R.drawable.ic_headline_rank2, R.drawable.ic_headline_rank3};
    private Long userId;
    private List<HeadlineListBean.DataBean.ListBean> mListData = new ArrayList<>();
    private int mLeftTime;
    private int mGoodsId;
    private int mRankId;
    private int mAnchorId;
    private String mNickName;
    private String mAvatar;
    private int needGifts;
    private int mProgramId;
    private int goodsRent;
    private String goodsName;
    private int selfScore;
    private int topScore;
    private int needValue;

    public static HeadlineListFragment newInstance(String type, int anchorId, String nickName, String avatar, int programId) {
        Bundle args = new Bundle();
        args.putString("lineType", type);
        args.putInt("anchorId", anchorId);
        args.putString("nickName", nickName);
        args.putString("avatar", avatar);
        args.putInt("programId", programId);
        HeadlineListFragment fragment = new HeadlineListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_headline_list;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    public void init() {
        mType = getArguments().getString("lineType");
        mAnchorId = getArguments().getInt("anchorId", 0);
        mNickName = getArguments().getString("nickName");
        mAvatar = getArguments().getString("avatar");
        mProgramId = getArguments().getInt("programId");
        if ("F".equals(mType)) {
            rlGift.setVisibility(View.VISIBLE);
        } else {
            rlGift.setVisibility(View.GONE);
        }
        initRecycler();
        getData(mType);
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
                                    if (disposable != null) {
                                        disposable.dispose();
                                    }
                                    setTopAnchorInfo();
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

    private void getData(String type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("preRound", type);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.HEADLINE_LIST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                if (getContext() == null) {
                    return;
                }
                HeadlineListBean rankBean = GsonUtils.GsonToBean(result.toString(), HeadlineListBean.class);
                if (rankBean.code == 200) {
                    if (rankBean.data != null && rankBean.data.list != null) {
                        if (rankBean.data.list.size() == 0) {
                            rlEmpty.setVisibility(View.VISIBLE);
                        } else {
                            rlEmpty.setVisibility(View.GONE);
                            mListData.clear();
                            mListData.addAll(rankBean.data.list);
                            mAdapter.notifyDataSetChanged();
                        }

                        if ("F".equals(mType)) {
                            tvCountdown.setVisibility(View.VISIBLE);
                            mLeftTime = rankBean.data.leftTime;
                            mGoodsId = rankBean.data.goodsId;
                            mRankId = rankBean.data.rankId;
                            initCountdown(mLeftTime);
                        }
                    } else {
                        rlEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getMyActivity(), "空", Toast.LENGTH_SHORT).show();
                    }

                    if (rankBean.data != null) {
                        getBeyondFirst(mAnchorId, rankBean.data.goodsId, rankBean.data.rankId, "魅力");
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
            }
        });
    }

    /**
     * 本轮剩余时间
     */
    private void initCountdown(int time) {
        tvCountdown.setVisibility(View.VISIBLE);
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (time - aLong <= 0) {
                        tvCountdown.setText(getString(R.string.countdown, 0 + ""));
                        disposable.dispose();
                    } else {
                        String strCountdown = StringUtils.formatLongToTimeStr(time - aLong);
                        tvCountdown.setText(getString(R.string.countdown, strCountdown));
                    }
                });
    }

    private void initRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getMyActivity()));
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mListData == null ? 0 : mListData.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                if (viewType == TOP_RANK && "T".equals(mType)) {
                    View topView = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_top, parent, false);
                    return new TopViewHolder(topView);
                } else {
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_list, parent, false);
                    return new ViewHolder(itemView);
                }
            }

            @Override
            protected int getDataViewType(int position) {
                if (position < 1) {
                    return TOP_RANK;
                } else {
                    return OTHER_RANK;
                }
            }
        };
        recycler.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_ranking)
        TextView tvRanking;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_charm_value)
        TextView tvCharm;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if ("F".equals(mType)) {
                if (position < 3) {
                    tvRanking.setBackgroundResource(rankIcons[position]);
                    tvRanking.setText("");
                } else {
                    tvRanking.setText(String.valueOf(position + 1));
                }
            } else {
                tvRanking.setText(String.valueOf(position + 1));
            }

            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListData.get(position).anchorAvatar, ivAvatar);
            tvNickName.setText(mListData.get(position).anchorNickname);
            tvCharm.setText(StringUtils.formatNumber(mListData.get(position).score) + "魅力");
            int anchorLevelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(mListData.get(position).anchorLevelValue);
            ivLevel.setImageResource(anchorLevelIcon);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (getMyActivity() != null) {
                ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(mListData.get(position).anchorId, true);
            }
        }
    }

    class TopViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_top_avatar)
        CircleImageView ivTopAvatar;
        @BindView(R.id.tv_top_name)
        TextView tvTopName;
        @BindView(R.id.tv_top_charm)
        TextView tvTopCharm;

        public TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListData.get(0).anchorAvatar, ivTopAvatar);
            tvTopName.setText(mListData.get(0).anchorNickname);
            tvTopCharm.setText(StringUtils.formatNumber(mListData.get(0).score) + "魅力");
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (getMyActivity() != null) {
                ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(mListData.get(position).anchorId, true);
            }
        }
    }

    /**
     * 获取物品价格
     *
     * @param goodsId
     * @param diffScore
     */
    private void getNeedGoods(int goodsId, int diffScore) {
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
                        needGifts = (diffScore / (goodsRent * anchorExp / 100)) + 1;
                        //需要的魅力值
                        needValue = needGifts * (goodsRent * anchorExp / 100);
                        if (tvNeedValue != null) {
                            tvNeedValue.setText("超越第1名需要");
                            if (diffScore > 0) {
                                SpannableString ss = StringUtils.spannableStringColor(StringUtils.formatNumber(needValue), Color.parseColor("#000000"));
                                tvNeedValue.append(ss);
                                SpannableString goods = StringUtils.spannableStringColor("魅力", Color.parseColor("#70000000"));
                                tvNeedValue.append(goods);
                            } else {
                                tvNeedValue.setText(R.string.top_rank);
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
                        if (tvRank != null && tvNickName != null && tvCharmValue != null) {
                            if (rank < 0) {
                                tvRank.setText("未上榜");
                                tvRank.setTextColor(Color.BLACK);
                            } else {
                                tvRank.setText(rank + "");
                                tvRank.setTextColor(Color.RED);
                            }
                            if(rank == 1){
                                tvClickGift.setVisibility(View.GONE);
                            }
                            tvCharmValue.setText(StringUtils.formatNumber((long) selfScore) + "魅力");
                            tvNickName.setText(mNickName);
                            tvNickName.setTextColor(Color.parseColor("#ff2b3f"));
                            GlideImageLoader.getInstace().displayImage(getMyActivity(), mAvatar, ivOwnAvatar);
                            int diff = topScore - selfScore;
                            getNeedGoods(goodsId, diff);
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
            }
        });
    }

    private void setTopAnchorInfo() {
        if (tvRank != null && tvNeedValue != null && tvCharmValue != null) {
            tvClickGift.setVisibility(View.GONE);
            tvRank.setText(1 + "");
            tvRank.setTextColor(Color.RED);
            long score = (long) (needValue + selfScore);
            tvCharmValue.setText(StringUtils.formatNumber(score) + "魅力");
            tvNeedValue.setText(R.string.top_rank);
//            tvNeedValue.append(0 + "");
//            SpannableString goods = StringUtils.spannableStringColor("魅力", Color.parseColor("#70000000"));
//            tvNeedValue.append(goods);
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
