package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.WeekStarListInfo;
import com.whzl.mengbi.model.entity.WeekStarRankListBean;
import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;
import com.whzl.mengbi.presenter.impl.WeekStarPresenterImpl;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.WeekStarListView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 周星榜本周，上周
 *
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarListFragment extends BaseFragment implements WeekStarListView {

    @BindView(R.id.rl_week_star)
    RelativeLayout relativeLayout;
    @BindView(R.id.rv_week_star)
    RecyclerView rvWeekRank;
    @BindView(R.id.rl_gift)
    RelativeLayout rlGift;

    private static final int TYPE_RICH = 0xa01;
    private static final int TYPE_ANCHOR = 0xa02;
    private WeekStarPresenterImpl mPresenterImpl;
    private String mType;
    private BaseListAdapter userAdapter;
    private RecyclerView rvWeekGift;
    private RecyclerView rvAnchor;
    private BaseListAdapter anchorAdapter;
    private BaseListAdapter giftAdapter;
    private int[] gifts = {R.drawable.shape_gradient_magenta, R.drawable.shape_gradient_red, R.drawable.shape_gradient_yellow};
    private List<WeekStarGiftInfo.DataBean.ListBean> mListBean = new ArrayList<>();
    private ArrayList<WeekStarRankListBean.RankListBean> mAnchorList = new ArrayList<>();
    private List<WeekStarRankListBean.RankListBean> mUserList = new ArrayList<>();
    private List<Integer> mRankList = new ArrayList<>();

    public static WeekStarListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("weekType", type);
        WeekStarListFragment fragment = new WeekStarListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_star_list;
    }

    @Override
    public void init() {
        mType = getArguments().getString("weekType");
        if ("F".equals(mType)) {
            rlGift.setVisibility(View.VISIBLE);
        } else {
            rlGift.setVisibility(View.GONE);
        }
        loadData();
        initUserRecycler();
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresenterImpl = new WeekStarPresenterImpl(this);
    }

    private void loadData() {
        mPresenterImpl.getGiftList();
    }

    private void initUserRecycler() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        rvWeekRank.setFocusableInTouchMode(false);
        rvWeekRank.setHasFixedSize(true);
        rvWeekRank.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvWeekRank.setLayoutManager(layoutManage);

        userAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mUserList == null ? 0 : mUserList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_star, parent, false);
                return new UserViewHolder(itemView, TYPE_RICH);
            }
        };
        rvWeekRank.setAdapter(userAdapter);

        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_week_star, relativeLayout, false);
        userAdapter.addHeaderView(view);
        initHead(view);
    }

    private void initHead(View view) {
        rvWeekGift = view.findViewById(R.id.rv_week_gift);
        rvAnchor = view.findViewById(R.id.rv_anchor);
        initWeekGift();
        initAnchorRecycler();
    }

    private void initWeekGift() {
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
                return new UserViewHolder(itemView, TYPE_ANCHOR);
            }
        };
        rvAnchor.setAdapter(anchorAdapter);
    }

    @Override
    public void showGiftList(WeekStarGiftInfo weekStarGiftInfo) {
        if (weekStarGiftInfo != null && weekStarGiftInfo.getData() != null && weekStarGiftInfo.getData().getList() != null) {
            if (weekStarGiftInfo.getData().getList() != null) {
                mListBean.addAll(weekStarGiftInfo.getData().getList());
                for (int i = 0; i < mListBean.size(); i++) {
                    if (i == 0) {
                        mRankList.add(mListBean.get(i).anchorRankId);
                        mRankList.add(mListBean.get(i).userRankId);
                        mPresenterImpl.getRankList(Arrays.toString(mRankList.toArray()), mType);
                        break;
                    }
                }
                giftAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showRankList(WeekStarRankInfo weekStarRankInfo) {
        if (weekStarRankInfo != null && weekStarRankInfo.getData() != null) {
            if (weekStarRankInfo.getData().getAnchor() != null) {
                mAnchorList.clear();
                if (weekStarRankInfo.getData().getAnchor().getRankList() != null) {
                    mAnchorList.addAll(weekStarRankInfo.getData().getAnchor().getRankList());
                    anchorAdapter.notifyDataSetChanged();
                }
            }

            if (weekStarRankInfo.getData().getUser() != null) {
                mUserList.clear();
                if (weekStarRankInfo.getData().getUser().getRankList() != null) {
                    mUserList.addAll(weekStarRankInfo.getData().getUser().getRankList());
                    userAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onError(String msg) {

    }

    class UserViewHolder extends BaseViewHolder {

        private final int type;
        @BindView(R.id.tv_ranking)
        TextView tvRank;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAcatar;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_value)
        TextView tvValue;

        public UserViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            switch (type) {
                case TYPE_ANCHOR:
                    tvNickName.setText(mAnchorList.get(position).nickname);
                    tvRank.setText(position + 1 + "");
                    tvValue.setText(StringUtils.formatNumber(mAnchorList.get(position).value)+"个");
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), mAnchorList.get(position).avatar, ivAcatar);
                    break;
                case TYPE_RICH:
                    tvNickName.setText(mUserList.get(position).nickname);
                    tvRank.setText(position + 1 + "");
                    tvValue.setText(StringUtils.formatNumber(mUserList.get(position).value)+"个");
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), mUserList.get(position).avatar, ivAcatar);
                    break;
                default:
                    break;
            }
        }
    }

    class GiftViewHolder extends BaseViewHolder {

        @BindView(R.id.rl_week_gift)
        RelativeLayout rlGift;
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
            if (0 == remainder) {
                rlGift.setBackgroundResource(gifts[0]);
            } else if (1 == remainder) {
                rlGift.setBackgroundResource(gifts[1]);
            } else {
                rlGift.setBackgroundResource(gifts[2]);
            }
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListBean.get(position).goodsPic, ivGift);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            String giftType = mListBean.get(position).giftType;
            if (ClickUtil.isFastClick()) {
//                mPresenterImpl.getRankList(giftType, mType);
            }
        }
    }
}
