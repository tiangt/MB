package com.whzl.mengbi.ui.fragment.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.widget.view.PullRecycler;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/8/22
 */
public abstract class BasePullListFragment<T> extends BaseFragment {
    @BindView(R.id.pull_recycler)
    PullRecycler pullRecycler;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;
    protected ArrayList<T> mDatas = new ArrayList<>();
    private BaseListAdapter mAdapter;
    private boolean mIsViewCreate;
    private boolean hasLoadData;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && mIsViewCreate && !hasLoadData) {
            loadData();
        }
    }

    private void loadData() {
        pullRecycler.setRecyclerAction(PullRecycler.ACTION_LOAD_DATA);
        loadData(PullRecycler.ACTION_LOAD_DATA);
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

        pullRecycler.setOnActionListener(action -> loadData(action));

        if (getUserVisibleHint()) {
            pullRecycler.setRecyclerAction(PullRecycler.ACTION_LOAD_DATA);
            loadData(PullRecycler.ACTION_LOAD_DATA);
        }

        mIsViewCreate = true;

    }

    protected int getViewType(int position) {
        return 0;
    }

    protected boolean setShouldLoadMore() {
        return false;
    }

    public void loadSuccess(ArrayList<T> data) {
        hasLoadData = true;
        mDatas.clear();
        if (data == null || data.size() == 0) {
            mAdapter.notifyDataSetChanged();
            pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_EMPTY);
            return;
        }
        mDatas.addAll(data);
        mAdapter.notifyDataSetChanged();
        if (!setShouldLoadMore()) {
            mAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_LOAD_MORE_END);
        } else {
            // TODO: 2018/8/22
        }
    }

    public void loadFail() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        pullRecycler.OnActionComplete(PullRecycler.LOAD_RESULT_ERROR);
    }

    protected abstract void loadData(int action);

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
}
