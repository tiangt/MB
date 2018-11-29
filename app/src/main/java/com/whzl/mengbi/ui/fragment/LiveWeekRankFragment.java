package com.whzl.mengbi.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.WeekRankBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/11/28
 */
public class LiveWeekRankFragment extends BaseFragment {
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.tv_name_1)
    TextView tvName1;
    @BindView(R.id.tv_rank_1)
    TextView tvRank1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_name_2)
    TextView tvName2;
    @BindView(R.id.tv_rank_2)
    TextView tvRank2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.tv_name_3)
    TextView tvName3;
    @BindView(R.id.tv_rank_3)
    TextView tvRank3;

    public static LiveWeekRankFragment newInstance(WeekRankBean bean) {
        LiveWeekRankFragment weekRankFragment = new LiveWeekRankFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", bean);
        weekRankFragment.setArguments(bundle);
        return weekRankFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_week_rank;
    }

    @Override
    public void init() {
        WeekRankBean bean = getArguments().getParcelable("bean");
        if (bean.list != null && bean.list.size() > 0) {
            tvName1.setText(bean.list.get(0).goodsName);
            tvName2.setText(bean.list.get(1).goodsName);
            tvName3.setText(bean.list.get(2).goodsName);

            tvRank1.setText(bean.list.get(0).rankValue < 0 ? "未上榜" : "第" + bean.list.get(0).rankValue + "名");
            tvRank2.setText(bean.list.get(1).rankValue < 0 ? "未上榜" : "第" + bean.list.get(1).rankValue + "名");
            tvRank3.setText(bean.list.get(2).rankValue < 0 ? "未上榜" : "第" + bean.list.get(2).rankValue + "名");

            tvRank1.setTextColor(bean.list.get(0).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));
            tvRank2.setTextColor(bean.list.get(1).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));
            tvRank3.setTextColor(bean.list.get(2).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));

            Glide.with(this).load(bean.list.get(0).goodsPic).into(iv1);
            Glide.with(this).load(bean.list.get(1).goodsPic).into(iv2);
            Glide.with(this).load(bean.list.get(2).goodsPic).into(iv3);
        }
    }
}
