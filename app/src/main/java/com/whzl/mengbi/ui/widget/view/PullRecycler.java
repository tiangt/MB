package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;

/**
 * @author shaw
 * @date 2018/8/22
 */
public class PullRecycler extends RelativeLayout implements OnRefreshListener, OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recycler;
    private TextView tvEmpty;
    private LinearLayout llLoad;
    private RelativeLayout rlEmpty;

    public void setShouldLoadMore(boolean mShouldLoadMore) {
        this.mShouldLoadMore = mShouldLoadMore;
        refreshLayout.setEnableLoadMore(mShouldLoadMore);
    }

    public void setShouldRefresh(boolean mShouldRefresh) {
        this.mShouldRefresh = mShouldRefresh;
        refreshLayout.setEnableLoadMore(mShouldRefresh);
    }

    private boolean mShouldLoadMore = false;
    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE = 2;
    public static final int ACTION_LOAD_DATA = 4;
    public static final int LOAD_RESULT_ERROR = 100;
    public static final int LOAD_RESULT_SUCCESS = 200;
    public static final int LOAD_RESULT_EMPTY = 300;
    public static final int LOAD_RESULT_LOAD_MORE_END = 400;

    public int mRecyclerAction = ACTION_LOAD_DATA;

    public void setOnActionListener(OnRecyclerRefreshListener listener) {
        this.listener = listener;
    }

    private OnRecyclerRefreshListener listener;
    private boolean mShouldRefresh = true;

    public PullRecycler(Context context) {
        this(context, null);
    }

    public PullRecycler(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.pull_refresh_layout, this, true);
        refreshLayout = findViewById(R.id.smart_refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tvEmpty = findViewById(R.id.tv_empty);
        llLoad = findViewById(R.id.ll_load_layout);
        rlEmpty = findViewById(R.id.rl_empty);
    }

    public void setAdapter(BaseListAdapter adapter) {
        recycler.setAdapter(adapter);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (listener != null) {
            setRecyclerAction(ACTION_PULL_TO_REFRESH);
            listener.OnLoadAction(ACTION_PULL_TO_REFRESH);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (listener != null) {
            setRecyclerAction(ACTION_LOAD_MORE);
            listener.OnLoadAction(ACTION_LOAD_MORE);
        }
    }

    public interface OnRecyclerRefreshListener {
        void OnLoadAction(int action);
    }

    public void setRecyclerAction(int recyclerAction) {
        mRecyclerAction = recyclerAction;
        if (recyclerAction == ACTION_LOAD_DATA) {
            refreshLayout.setEnableRefresh(false);
            refreshLayout.setEnableLoadMore(false);
            llLoad.setVisibility(VISIBLE);
            rlEmpty.setVisibility(GONE);
            recycler.setVisibility(GONE);
        }
    }

    public void OnActionComplete(int loadResult) {
        switch (mRecyclerAction) {
            case ACTION_PULL_TO_REFRESH:
                refreshLayout.finishRefresh();
                break;
            case ACTION_LOAD_MORE:
                refreshLayout.finishLoadMore();
                break;
            default:
                break;
        }
        switch (loadResult) {
            case LOAD_RESULT_EMPTY:
                recycler.setVisibility(GONE);
                llLoad.setVisibility(GONE);
//                tvEmpty.setVisibility(VISIBLE);
                tvEmpty.setText("当前列表为空");
//                tvEmpty.setOnClickListener(null);
                rlEmpty.setVisibility(VISIBLE);
                refreshLayout.setEnableRefresh(mShouldRefresh);
                refreshLayout.setEnableLoadMore(false);
                break;
            case LOAD_RESULT_ERROR:
                recycler.setVisibility(GONE);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableLoadMore(false);
                llLoad.setVisibility(GONE);
//                tvEmpty.setVisibility(VISIBLE);
                tvEmpty.setText("出错了，点我重试");
                tvEmpty.setOnClickListener(v -> {
                    if (listener != null) {
                        setRecyclerAction(ACTION_LOAD_DATA);
                        listener.OnLoadAction(ACTION_LOAD_DATA);
                    }
                });
                rlEmpty.setVisibility(VISIBLE);
                break;
            case LOAD_RESULT_SUCCESS:
                recycler.setVisibility(VISIBLE);
                llLoad.setVisibility(GONE);
//                tvEmpty.setVisibility(GONE);
                rlEmpty.setVisibility(GONE);
                refreshLayout.setEnableRefresh(mShouldRefresh);
                refreshLayout.setEnableLoadMore(mShouldLoadMore);
                break;
            case LOAD_RESULT_LOAD_MORE_END:
                recycler.setVisibility(VISIBLE);
                llLoad.setVisibility(GONE);
//                tvEmpty.setVisibility(GONE);
                rlEmpty.setVisibility(GONE);
                refreshLayout.setEnableRefresh(mShouldRefresh);
                refreshLayout.setEnableLoadMore(false);
                break;
            default:
                break;
        }
    }


    public void setEmptyView(View view) {
        tvEmpty.setVisibility(GONE);
        rlEmpty.addView(view);
    }

}
