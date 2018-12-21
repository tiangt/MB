package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarRuleFragment extends BaseFragment {

    @BindView(R.id.tv_anchor_top_condition)
    TextView tvAnchorCon;
    @BindView(R.id.tv_anchor_top_award_1)
    TextView tvAnchor1;
    @BindView(R.id.tv_rich_top_1)
    TextView tvRich1;
    @BindView(R.id.tv_rich_top_2)
    TextView tvRich2;

    public static WeekStarRuleFragment newInstance(){
        WeekStarRuleFragment fragment = new WeekStarRuleFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_satr_rule;
    }

    @Override
    public void init() {
        tvAnchorCon.setText(LightSpanString.getLightString("每周收到周星礼物总数量 ",Color.parseColor("#70000000")));
        tvAnchorCon.append(LightSpanString.getLightString("第一名 ",Color.parseColor("#FF2B3F")));
        tvAnchorCon.append(LightSpanString.getLightString(" 的主播",Color.parseColor("#70000000")));

        tvAnchor1.setText(LightSpanString.getLightString("网页端首页6图推荐 ",Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7",Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、手机端小编推荐 ",Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7",Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、星光红人勋章 ",Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("7",Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 天、 ",Color.parseColor("#70000000")));
        tvAnchor1.append(LightSpanString.getLightString("20",Color.parseColor("#FF2B3F")));
        tvAnchor1.append(LightSpanString.getLightString(" 万萌币",Color.parseColor("#70000000")));

        tvRich1.setText(LightSpanString.getLightString("每周送出周星礼物总数量 ",Color.parseColor("#70000000")));
        tvRich1.append(LightSpanString.getLightString("第一名 ",Color.parseColor("#FF2B3F")));
        tvRich1.append(LightSpanString.getLightString(" 的用户",Color.parseColor("#70000000")));

        tvRich2.setText(LightSpanString.getLightString("星光达人勋章 ",Color.parseColor("#70000000")));
        tvRich2.append(LightSpanString.getLightString("7",Color.parseColor("#FF2B3F")));
        tvRich2.append(LightSpanString.getLightString(" 天、VIP ",Color.parseColor("#70000000")));
        tvRich2.append(LightSpanString.getLightString("30",Color.parseColor("#FF2B3F")));
        tvRich2.append(LightSpanString.getLightString(" 天",Color.parseColor("#70000000")));

    }
}
