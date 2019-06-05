package com.whzl.mengbi.ui.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.WeekRankBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/11/28
 */
public class LiveWeekRankFragment extends BaseFragment {
    @BindView(R.id.iv_1)
    ImageView iv1;
    //    @BindView(R.id.tv_name_1)
//    TextView tvName1;
    @BindView(R.id.tv_rank_1)
    TextView tvRank1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    //    @BindView(R.id.tv_name_2)
//    TextView tvName2;
    @BindView(R.id.tv_rank_2)
    TextView tvRank2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    //    @BindView(R.id.tv_name_3)
//    TextView tvName3;
    @BindView(R.id.tv_rank_3)
    TextView tvRank3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    //    @BindView(R.id.tv_name_4)
//    TextView tvName4;
    @BindView(R.id.tv_rank_4)
    TextView tvRank4;

    @BindView(R.id.ll_week_rank)
    LinearLayout llWeekRank;
    private int mAnchorId;
    private Disposable disposable;

    public static LiveWeekRankFragment newInstance(int mProgramId, int mAnchorId) {
        LiveWeekRankFragment weekRankFragment = new LiveWeekRankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mProgramId", mProgramId);
        bundle.putInt("mAnchorId", mAnchorId);
        weekRankFragment.setArguments(bundle);
        return weekRankFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_week_rank;
    }

    @Override
    public void init() {
        mAnchorId = getArguments().getInt("mAnchorId");
        disposable = Observable.interval(0, 10, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            loaddata();
        });
        llWeekRank.setOnClickListener(v -> {
            ((LiveDisplayActivity) getActivity()).jumpToWeekRank();
        });
    }

    public void loaddata() {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        ApiFactory.getInstance().getApi(Api.class)
                .getWeekRank(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<WeekRankBean>() {
                    @Override
                    public void onSuccess(WeekRankBean bean) {
                        if (bean.list != null && bean.list.size() > 0) {
//                            tvName1.setText(bean.list.get(0).goodsName);
//                            tvName2.setText(bean.list.get(1).goodsName);
//                            tvName3.setText(bean.list.get(2).goodsName);
//                            tvName4.setText(bean.list.get(3).goodsName);

                            tvRank1.setText(bean.list.get(0).rankValue < 0 ? "未上榜" : "第" + bean.list.get(0).rankValue + "名");
                            tvRank2.setText(bean.list.get(1).rankValue < 0 ? "未上榜" : "第" + bean.list.get(1).rankValue + "名");
                            tvRank3.setText(bean.list.get(2).rankValue < 0 ? "未上榜" : "第" + bean.list.get(2).rankValue + "名");
                            tvRank4.setText(bean.list.get(3).rankValue < 0 ? "未上榜" : "第" + bean.list.get(3).rankValue + "名");

//                            tvRank1.setTextColor(bean.list.get(0).rankValue < 0 ? Color.parseColor("#f9f9f9") : Color.parseColor("#ec5b03"));
//                            tvRank2.setTextColor(bean.list.get(1).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));
//                            tvRank3.setTextColor(bean.list.get(2).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));
//                            tvRank4.setTextColor(bean.list.get(3).rankValue < 0 ? Color.parseColor("#505050") : Color.parseColor("#ec5b03"));

                            Glide.with(LiveWeekRankFragment.this).load(bean.list.get(0).goodsPic).into(iv1);
                            Glide.with(LiveWeekRankFragment.this).load(bean.list.get(1).goodsPic).into(iv2);
                            Glide.with(LiveWeekRankFragment.this).load(bean.list.get(2).goodsPic).into(iv3);
                            Glide.with(LiveWeekRankFragment.this).load(bean.list.get(3).goodsPic).into(iv4);
                        }
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
