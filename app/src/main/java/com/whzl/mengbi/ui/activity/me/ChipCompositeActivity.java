package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.model.entity.CompositionListInfo;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 合成碎片
 *
 * @author cliang
 * @date 2019.1.10
 */
public class ChipCompositeActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_menu_text)
    TextView tvMenu;
    @BindView(R.id.rv_parent_chip)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private ArrayList<CompositionListInfo.DataBean.ListBean> mParentList = new ArrayList<>();
    private ArrayList<CompositionListInfo.DataBean.ListBean.DetailDtosBean> mChildList = new ArrayList<>();
    private BaseListAdapter adapter;
    private BaseListAdapter childAdapter;
    private int mCurrentPager = 1;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_chip_composite);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
        tvTitle.setText("物品合成");
        tvMenu.setText("我的碎片");
        rlBack.setOnClickListener((v -> finish()));
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        getCompositeList(mCurrentPager++);
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.tv_menu_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_menu_text:
                Intent intent = new Intent(ChipCompositeActivity.this, MyChipActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

//    @Override
//    protected void onToolbarMenuClick() {
//        super.onToolbarMenuClick();
//        Intent intent = new Intent(ChipCompositeActivity.this, MyChipActivity.class);
//        startActivity(intent);
//    }

    private void initRecycler() {
        recycler.setNestedScrollingEnabled(false);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mParentList == null ? 0 : mParentList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(ChipCompositeActivity.this).inflate(R.layout.item_parent_chip, parent, false);
                return new ChipParentViewHolder(itemView);
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
        getCompositeList(mCurrentPager++);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPager = 1;
        getCompositeList(mCurrentPager++);
    }

    class ChipParentViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_chip_name)
        TextView chipName;
        @BindView(R.id.iv_chip_pic)
        ImageView ivChipPic;
        @BindView(R.id.rv_chip_list)
        RecyclerView rvChipList;
        @BindView(R.id.btn_composite)
        Button btnComposite;

        public ChipParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (mParentList != null && mParentList.get(position).goodsDetails != null) {
                mChildList.clear();
                for (int i = 0; i < mParentList.get(position).goodsDetails.size(); i++) {
                    chipName.setText(mParentList.get(position).goodsDetails.get(i).goodsName);
                    String chipPic = mParentList.get(position).goodsDetails.get(i).goodsPic;
                    GlideImageLoader.getInstace().displayImage(ChipCompositeActivity.this, chipPic, ivChipPic);
                    if (mParentList.get(position).isComposition == 0) {
                        btnComposite.setEnabled(false);
                    } else {
                        btnComposite.setEnabled(true);
                    }
                    mChildList.addAll(mParentList.get(position).detailDtos);
                }

                initChildRecycler(rvChipList);
                compositeChip(btnComposite, mParentList.get(position).compositionId);
            }
        }
    }

    /**
     * 获取用户的碎片合成列表
     *
     * @param pager
     */
    public void getCompositeList(int pager) {
        HashMap hashMap = new HashMap();
        hashMap.put("page", pager);
        hashMap.put("pageSize", 20);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.COMPOSITE_LIST, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        CompositionListInfo listInfo = JSON.parseObject(result.toString(), CompositionListInfo.class);
                        if (listInfo.getCode() == 200) {
                            loadSuccess(listInfo);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            mCurrentPager--;
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        ToastUtils.showToast(errorMsg);
                        mCurrentPager--;
                    }
                });
    }

    /**
     * 用户的碎片合成列表
     *
     * @param listInfo
     */
    private void loadSuccess(CompositionListInfo listInfo) {
        if (listInfo != null && listInfo.data != null && listInfo.data.list != null) {
            rlEmpty.setVisibility(View.GONE);
            if (mCurrentPager == 2) {
                mParentList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mParentList.addAll(listInfo.data.list);
            if (listInfo.data.list == null || listInfo.data.list.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                adapter.notifyDataSetChanged();
                if (mParentList.size() > 0) {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                } else {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                }
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }, 300);
            } else {
                refreshLayout.setEnableLoadMore(true);
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                adapter.notifyDataSetChanged();
            }
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
            if (mParentList.size() > 0) {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            }
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setEnableLoadMore(false);
                }
            }, 300);
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

    /**
     * 物品碎片列表
     *
     * @param childRecycler
     */
    private void initChildRecycler(RecyclerView childRecycler) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        childRecycler.setLayoutManager(layoutManager);
        childAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mChildList == null ? 0 : mChildList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(ChipCompositeActivity.this).inflate(R.layout.item_child_chip, parent, false);
                return new ChildListViewHolder(itemView);
            }
        };
        childRecycler.setAdapter(childAdapter);
    }

    class ChildListViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_child_chip)
        ImageView ivChildChip;
        @BindView(R.id.tv_good_count)
        TextView tvGoodCount;

        public ChildListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            String childPic = ImageUrl.getImageUrl(mChildList.get(position).picId, "jpg");
            GlideImageLoader.getInstace().displayImage(ChipCompositeActivity.this, childPic, ivChildChip);
            tvGoodCount.setText(getString(R.string.chip_count, mChildList.get(position).goodsNum, mChildList.get(position).num));
        }
    }

    /**
     * 合成碎片
     *
     * @param compositionId
     */
    private void compositeChip(Button button, int compositionId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("compositionId", compositionId);
                RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.COMPOSITE_CHIP, RequestManager.TYPE_POST_JSON, hashMap,
                        new RequestManager.ReqCallBack<Object>() {
                            @Override
                            public void onReqSuccess(Object result) {
                                ResponseInfo reportBean = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                                if (reportBean.getCode() == 200) {
                                    showToast("合成成功");
                                    mCurrentPager = 1;
                                    getCompositeList(mCurrentPager++);
                                } else {
                                    showToast(reportBean.getMsg());
                                }
                            }

                            @Override
                            public void onReqFailed(String errorMsg) {
                                showToast(errorMsg);
                            }
                        });
            }
        });
    }
}
