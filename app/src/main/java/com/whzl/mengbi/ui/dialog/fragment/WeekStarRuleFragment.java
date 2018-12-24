package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarRuleFragment extends BaseFragment {

    @BindView(R.id.rv_rule_gift)
    RecyclerView recycler;
    @BindView(R.id.tv_anchor_top_condition)
    TextView tvAnchorCon;
    @BindView(R.id.tv_anchor_top_award_1)
    TextView tvAnchor1;
    @BindView(R.id.tv_rich_top_1)
    TextView tvRich1;
    @BindView(R.id.tv_rich_top_2)
    TextView tvRich2;

    private BaseListAdapter mAdapter;
    private int[] gifts = {R.drawable.shape_gradient_magenta, R.drawable.shape_gradient_red, R.drawable.shape_gradient_yellow};

    public static WeekStarRuleFragment newInstance() {
        WeekStarRuleFragment fragment = new WeekStarRuleFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_satr_rule;
    }

    @Override
    public void init() {
        initRule();
        initRecycler();
    }

    private void initRule() {
        tvAnchorCon.setText(LightSpanString.getLightString("每周收到周星礼物总数量 ", Color.parseColor("#70000000")));
        tvAnchorCon.append(LightSpanString.getLightString("第一名 ", Color.parseColor("#FF2B3F")));
        tvAnchorCon.append(LightSpanString.getLightString(" 的主播", Color.parseColor("#70000000")));

        tvAnchor1.setText(LightSpanString.getLightString("网页端首页6图推荐 ", Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7", Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、手机端小编推荐 ", Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7", Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、星光红人勋章 ", Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7", Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、 ", Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("20", Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 万萌币", Color.parseColor("#70000000")));

        tvRich1.setText(LightSpanString.getLightString("每周送出周星礼物总数量 ", Color.parseColor("#70000000")));
        tvRich1.append(LightSpanString.getLightString("第一名 ", Color.parseColor("#FF2B3F")));
        tvRich1.append(LightSpanString.getLightString(" 的用户", Color.parseColor("#70000000")));

        tvRich2.setText(LightSpanString.getLightString("星光达人勋章 ", Color.parseColor("#70000000")));
        tvRich2.append(LightSpanString.getLightString("7", Color.parseColor("#FF2B3F")));
        tvRich2.append(LightSpanString.getLightString(" 天、VIP ", Color.parseColor("#70000000")));
        tvRich2.append(LightSpanString.getLightString("30", Color.parseColor("#FF2B3F")));
        tvRich2.append(LightSpanString.getLightString(" 天", Color.parseColor("#70000000")));

    }

    private void initRecycler() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManage);

        mAdapter = new BaseListAdapter() {
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

        recycler.setAdapter(mAdapter);
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
        }
    }
}
