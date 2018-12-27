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
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.HeadlineListBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
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

    private BaseListAdapter mAdapter;
    private String mType = "F"; //"F"本轮头条，"T"上轮头条，默认本轮
    private Disposable disposable;
    private int TOP_RANK = 0;
    private int OTHER_RANK = 1;
    private int[] rankIcons = new int[]{R.drawable.ic_headline_rank1, R.drawable.ic_headline_rank2, R.drawable.ic_headline_rank3};
    private Long userId;
    private List<HeadlineListBean.DataBean.ListBean> mListData = new ArrayList<>();
    private int mLeftTime;

    public static HeadlineListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("lineType", type);
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
        if ("F".equals(mType)) {
            rlGift.setVisibility(View.VISIBLE);
        } else {
            rlGift.setVisibility(View.GONE);
        }
        getData(mType);
        initRecycler();
        tvNeedValue.setText(getString(R.string.beyond_first, StringUtils.formatNumber(999999)));
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
                    OneClickDialog.newInstance()
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
                            initCountdown(mLeftTime);
                        }
                    } else {
                        rlEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getMyActivity(), "空", Toast.LENGTH_SHORT).show();
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
        mAdapter.notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_ranking)
        TextView tvRank;
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
                    tvRank.setBackgroundResource(rankIcons[position]);
                    tvRank.setText("");
                } else {
                    tvRank.setText(String.valueOf(position + 1));
                }
            } else {
                tvRank.setText(String.valueOf(position + 1));
            }

            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListData.get(position).anchorAvatar, ivAvatar);
            tvNickName.setText(mListData.get(position).anchorNickname);
            tvCharm.setText(StringUtils.formatNumber(mListData.get(position).score) + "魅力");
            int anchorLevelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(mListData.get(position).anchorLevelValue);
            ivLevel.setImageResource(anchorLevelIcon);
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
