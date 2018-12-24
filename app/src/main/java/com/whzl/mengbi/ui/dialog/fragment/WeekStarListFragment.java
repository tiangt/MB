package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 周星榜本周，上周
 *
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarListFragment extends BaseFragment {


    @BindView(R.id.rl_week_star)
    RelativeLayout relativeLayout;
    @BindView(R.id.rv_week_star)
    RecyclerView rvWeekRank;
    @BindView(R.id.rl_gift)
    RelativeLayout rlGift;

    private static final int TYPE_RICH = 0xa01;
    private static final int TYPE_ANCHOR = 0xa02;
    private String mType;
    private BaseListAdapter userAdapter;
    private RecyclerView rvWeekGift;
    private RecyclerView rvAnchor;
    private BaseListAdapter anchorAdapter;
    private BaseListAdapter giftAdapter;
    private int[] gifts = {R.drawable.shape_gradient_magenta, R.drawable.shape_gradient_red, R.drawable.shape_gradient_yellow};

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
        if ("T".equals(mType)) {
            rlGift.setVisibility(View.VISIBLE);
        } else {
            rlGift.setVisibility(View.GONE);
        }
        initUserRecycler();
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
                return 5;
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
                return 5;
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
                return 3;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_star, null);
                return new UserViewHolder(itemView, TYPE_ANCHOR);
            }
        };
        rvAnchor.setAdapter(anchorAdapter);
    }

    class UserViewHolder extends BaseViewHolder {

        private final int type;

        public UserViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            switch (type) {
                case TYPE_ANCHOR:
                    break;
                case TYPE_RICH:
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
        }
    }
}
