package com.whzl.mengbi.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/30
 */
public abstract class BaseListActivity<T> extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    public static final int ACTION_PULL_REFRESH = 100;
    public static final int ACTION_LOAD_MORE = 200;
    public static final int ACTION_FIRST_LOAD = 300;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    protected int mPager = 1;
    protected ArrayList<T> mDatas = new ArrayList<>();
    private BaseListAdapter adapter;

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_common_list, getActivityTitle(), true);
    }

    protected abstract String getActivityTitle();

    @Override
    protected void setupView() {
        refresh.setEnableLoadMore(setEnableLoadMore());
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mDatas == null ? 0 : mDatas.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                return setViewHolder(parent, viewType);
            }
        };
        recycler.setAdapter(adapter);
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder setViewHolder(ViewGroup parent, int viewType);

    @Override
    protected void loadData() {
        onLoadAction(ACTION_FIRST_LOAD);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (setEnableLoadMore()) {
            refresh.setEnableLoadMore(true);
        }
        onLoadAction(ACTION_PULL_REFRESH);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onLoadAction(ACTION_LOAD_MORE);
    }

    protected abstract void onLoadAction(int action);

    protected boolean setEnableLoadMore() {
        return true;
    }

    protected void loadSuccess(ArrayList<T> datas) {
        if (datas != null) {
            if (mPager == 2 || mPager == 1) {
                mDatas.clear();
                refresh.finishRefresh();
            } else {
                refresh.finishLoadMore();
            }
            mDatas.addAll(datas);
            if (!setEnableLoadMore()) {
                adapter.notifyDataSetChanged();
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                if (datas.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                    adapter.notifyDataSetChanged();
                    if (mDatas.size() > 0) {
                        adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                    } else {
                        adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                    }
                    refresh.postDelayed(() -> refresh.setEnableLoadMore(false), 300);
                } else {
                    refresh.setEnableLoadMore(true);
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            if (mDatas.size() > 0) {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            }
            refresh.postDelayed(() -> refresh.setEnableLoadMore(false), 300);
        }
    }

    protected void loadFail() {
        mPager--;
        if (mPager == 1) {
            refresh.setEnableLoadMore(false);
        }
        refresh.finishRefresh();
        refresh.finishLoadMore();
    }
}
