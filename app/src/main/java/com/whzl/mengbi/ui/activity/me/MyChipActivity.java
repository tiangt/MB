package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.whzl.mengbi.model.entity.MyChipListInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cliang
 * @date 2019.1.10
 */
public class MyChipActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.rv_my_chip)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private int mCurrentPager = 1;
    private BaseListAdapter adapter;
    private ArrayList<MyChipListInfo.DataBean.ListBean> mList = new ArrayList<>();
    private ArrayList<MyChipListInfo.DataBean.ListBean.GoodsBean> mGoodsList = new ArrayList<>();

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_chip, "我的碎片", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        getMyChipList(mCurrentPager++);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getMyChipList(mCurrentPager++);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPager = 1;
        getMyChipList(mCurrentPager++);
    }

    private void initRecycler() {
        recycler.setNestedScrollingEnabled(false);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mList == null ? 0 : mList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(MyChipActivity.this).inflate(R.layout.item_my_chip, parent, false);
                return new MyChipViewHolder(itemView);
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
        View view = LayoutInflater.from(this).inflate(R.layout.head_chip, refreshLayout, false);
        adapter.addHeaderView(view);
    }

    /**
     * 获取用户有的碎片
     *
     * @param page
     */
    private void getMyChipList(int page) {
        HashMap hashMap = new HashMap();
        hashMap.put("page", page);
        hashMap.put("pageSize", 20);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.MY_DEBRIS_LIST, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        MyChipListInfo listInfo = JSON.parseObject(result.toString(), MyChipListInfo.class);
                        if (listInfo.getCode() == 200) {
                            loadSuccess(listInfo);
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

    class MyChipViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_chip_pic)
        ImageView ivChipPic;
        @BindView(R.id.tv_chip_name)
        TextView tvChipName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyChipViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvChipName.setText(mList.get(position).goods.goodsName);
            String chipPic = ImageUrl.getImageUrl(mList.get(position).goods.picId, "jpg");
            GlideImageLoader.getInstace().displayImage(MyChipActivity.this, chipPic, ivChipPic);
            tvCount.setText(mList.get(position).goodsSum + "");
            String str = mList.get(position).expDate;
            if (str.contains(" ")) {
                //截取时间
                int index = str.indexOf(" ");
                String date = str.substring(0, index);
                String time = str.substring(index + 1);
                tvDate.setText(date);
                tvTime.setText(time);
            }
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

    private void loadSuccess(MyChipListInfo listInfo) {
        if (listInfo != null && listInfo.data != null && listInfo.data.list != null) {
            if (mCurrentPager == 2) {
                mList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mList.addAll(listInfo.data.list);
            if (mList == null || mList.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                adapter.notifyDataSetChanged();
                if (mList.size() > 0) {
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
            if (mList.size() > 0) {
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
}
