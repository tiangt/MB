package com.whzl.mengbi.ui.fragment.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.widget.view.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/8/22
 */
public abstract class BasePullListFragment<K,T extends BasePresenter> extends BaseFragment<T> {
    @BindView(R.id.pull_recycler)
    PullRecycler pullRecycler;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;
    protected ArrayList<K> mDatas = new ArrayList<>();
    private BaseListAdapter mAdapter;
    private boolean mIsViewCreate;
    private boolean hasLoadData;

    protected int mPage = 1;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && mIsViewCreate && !hasLoadData) {
            loadData();
        }
    }

    private void loadData() {
        pullRecycler.setRecyclerAction(PullRecycler.ACTION_LOAD_DATA);
        loadData(PullRecycler.ACTION_LOAD_DATA, 1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pull_refresh;
    }

    @Override
    public void init() {
        pullRecycler.setShouldLoadMore(setShouldLoadMore());
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mDatas == null ? 0 : mDatas.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                return setViewHolder(parent, viewType);
            }

            @Override
            protected int getDataViewType(int position) {
                return getViewType(position);
            }
        };
        pullRecycler.setAdapter(mAdapter);

//        pullRecycler.setOnActionListener(action -> loadData(action));
        pullRecycler.setOnActionListener(new PullRecycler.OnRecyclerRefreshListener() {
            @Override
            public void OnLoadAction(int action) {
                if (action == PullRecycler.ACTION_LOAD_MORE) {
                    mPage += 1;
                    loadData(PullRecycler.ACTION_LOAD_DATA, mPage);
                    return;
                }
                mPage = 1;
                loadData(action, 1);
            }
        });

        if (getUserVisibleHint()) {
            pullRecycler.setRecyclerAction(PullRecycler.ACTION_LOAD_DATA);
            loadData(PullRecycler.ACTION_LOAD_DATA, 1);
        }

        mIsViewCreate = true;

    }

    protected int getViewType(int position) {
        return 0;
    }

    protected boolean setShouldLoadMore() {
        return false;
    }

    protected boolean setLoadMoreEndShow() {
        return true;
    }

    public void loadSuccess(List<K> data) {
        hasLoadData = true;
        if (mPage == 1) {
            mDatas.clear();
        }
        if (mPage == 1) {
            if (data == null || data.size() == 0) {
                mAdapter.notifyDataSetChanged();
                pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_EMPTY);
                return;
            }
        }
        mDatas.addAll(data);
        mAdapter.notifyDataSetChanged();
        if (!setShouldLoadMore()) {
            mAdapter.onLoadMoreStateChanged(setLoadMoreEndShow() ? BaseListAdapter.LOAD_MORE_STATE_END_SHOW : BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_LOAD_MORE_END);
        } else {
            if (data.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                mAdapter.onLoadMoreStateChanged(setLoadMoreEndShow() ? BaseListAdapter.LOAD_MORE_STATE_END_SHOW : BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_LOAD_MORE_END);
            } else {
                mAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_SUCCESS);
            }
        }
    }

    public void loadFail() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_ERROR);
    }

    protected abstract void loadData(int action, int mPage);

    protected abstract BaseViewHolder setViewHolder(ViewGroup parent, int viewType);

    public BaseListAdapter getAdapter() {
        return mAdapter;
    }

    public void addHeadTips(View view) {
        flContain.setVisibility(View.VISIBLE);
        flContain.addView(view);
    }

    public void setEmptyView(View view) {
        pullRecycler.setEmptyView(view);
    }

    public PullRecycler getPullView() {
        return pullRecycler;
    }

}
