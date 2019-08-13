package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorFollowedDataBean;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2019/1/9
 */
public class PlayNotifyActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.switch_play)
    Switch switchPlay;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    private BaseListAdapter adapter;
    private ArrayList<AnchorFollowedDataBean.AnchorInfoBean> mAnchorList = new ArrayList<>();
    private long userId;
    private int mCurrentPager = 1;

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_play_notify, "开播提醒", true);
    }

    @Override
    protected void setupView() {
        userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        initSmart();
        initRecycler();
        smartRefresh.setVisibility(switchPlay.isChecked() ? View.VISIBLE : View.GONE);
        tvTips.setText(switchPlay.isChecked() ? getString(R.string.play_notify_on) : getString(R.string.play_notify_off));
        switchPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvTips.setText(switchPlay.isChecked() ? getString(R.string.play_notify_on) : getString(R.string.play_notify_off));
                if (isChecked) {
                    smartRefresh.setVisibility(View.VISIBLE);
                    smartRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            smartRefresh.setEnableLoadMore(true);
                        }
                    });
                    addUserSet("1");
                } else {
                    smartRefresh.setVisibility(View.GONE);
                    smartRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            smartRefresh.setEnableLoadMore(false);
                        }
                    });
                    addUserSet("0");
                }
            }
        });
    }

    private void addUserSet(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("setType", "subscribe");
        hashMap.put("setValue", s);
        ApiFactory.getInstance().getApi(Api.class)
                .addUserSet(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {

                    }

                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                    }
                });
    }

    private void initSmart() {
        smartRefresh.setOnLoadMoreListener(this);
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(PlayNotifyActivity.this));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mAnchorList == null ? 0 : mAnchorList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(PlayNotifyActivity.this).inflate(R.layout.item_play_notify, parent, false);
                return new AnchorViewHolder(itemView);
            }

            @Override
            protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more_end, parent, false);
                TextView tvFoot = view.findViewById(R.id.tv_foot);
                tvFoot.setText("没有更多了~");
                return new LoadMoreFooterViewHolder(view);
            }
        };
        recycler.setAdapter(adapter);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getAnchorList(mCurrentPager++);
    }

    public void getAnchorList(int pager) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("page", pager);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ANCHOR_FOLLOWED, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        AnchorFollowedDataBean anchorFollowedDataBean = GsonUtils.GsonToBean(jsonStr, AnchorFollowedDataBean.class);
                        loadSuccess(anchorFollowedDataBean);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        smartRefresh.finishRefresh();
                        smartRefresh.finishLoadMore();
                        ToastUtils.showToast(errorMsg);
                        mCurrentPager--;
                    }
                });
    }

    private void loadSuccess(AnchorFollowedDataBean anchorFollowedDataBean) {
        if (anchorFollowedDataBean != null && anchorFollowedDataBean.data != null && anchorFollowedDataBean.data.list != null) {
            if (mCurrentPager == 2) {
                mAnchorList.clear();
                smartRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        smartRefresh.finishRefresh();
                    }
                });
            } else {
                smartRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        smartRefresh.finishLoadMore();
                    }
                });
            }
            mAnchorList.addAll(anchorFollowedDataBean.data.list);
            if (anchorFollowedDataBean.data.list == null || anchorFollowedDataBean.data.list.size() == 0/*< NetConfig.DEFAULT_PAGER_SIZE*/) {
                adapter.notifyDataSetChanged();
                if (mAnchorList.size() > 0) {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                } else {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                }
                smartRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefresh.setEnableLoadMore(false);
                    }
                }, 300);
            } else {
                smartRefresh.setEnableLoadMore(true);
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                adapter.notifyDataSetChanged();
            }
        } else {
            if (mAnchorList.size() > 0) {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            }
            smartRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefresh.setEnableLoadMore(false);
                }
            }, 300);
        }
    }


    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_last_time)
        TextView tvLastTime;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            GlideImageLoader.getInstace().displayRoundAvatar(PlayNotifyActivity.this, anchorInfoBean.avatar, ivAvatar, 5);
            if ("T".equals(anchorInfoBean.status)) {
                tvStatus.setVisibility(View.VISIBLE);
                tvLastTime.setVisibility(View.GONE);
            } else {
                tvStatus.setVisibility(View.GONE);
                tvLastTime.setVisibility(View.VISIBLE);
                tvLastTime.setText("上次直播:");
                if (!TextUtils.isEmpty(anchorInfoBean.lastShowBeginTime)) {
                    String timeRange = DateUtils.getTimeRange(anchorInfoBean.lastShowBeginTime);
                    tvLastTime.append(timeRange);
                } else {
                    tvLastTime.append("无");
                }
            }
            tvAnchorName.setText(anchorInfoBean.anchorNickname);
            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(anchorInfoBean.anchorLevelValue));
        }


        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            Intent intent = new Intent(PlayNotifyActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, anchorInfoBean.programId);
            startActivity(intent);
        }

    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {

        public LoadMoreFooterViewHolder(View view) {
            super(view);
        }

        @Override
        public void onBindViewHolder(int position) {
            // 强制关闭复用，刷新动画
            this.setIsRecyclable(false);
            // 设置自定义加载中和到底了效果
        }

        @Override
        public void onItemClick(View view, int position) {
            // 设置点击效果，比如加载失败，点击重试
        }
    }


    @Override
    protected void loadData() {
        getAnchorList(mCurrentPager++);
        getUserSet();
    }

    private void getUserSet() {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        ApiFactory.getInstance().getApi(Api.class)
                .getUserSet(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetUserSetBean>(this) {

                    @Override
                    public void onSuccess(GetUserSetBean bean) {
                        if (bean.list == null || bean.list.size() == 0) {
                            switchPlay.setChecked(false);
                        } else {
                            for (int i = 0; i < bean.list.size(); i++) {
                                if (bean.list.get(i).setType.equals("subscribe")) {
                                    switchPlay.setChecked(bean.list.get(i).setValue.equals("1"));
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ApiResult<GetUserSetBean> body) {
                    }
                });
    }

}
