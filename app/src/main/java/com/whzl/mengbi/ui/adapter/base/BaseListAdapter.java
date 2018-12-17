package com.whzl.mengbi.ui.adapter.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;


/**
 * Created by shaw on 16/4/29.
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int VIEW_TYPE_HEADER = 100;
    // 已显示全部内容视图type
    protected static final int VIEW_TYPE_LOAD_MORE_END = 102;
    public static final int LOAD_MORE_STATE_END_SHOW = 250;
    public static final int LOAD_MORE_STATE_END_HIDE = 520;
    private int loadMoreFooterState = LOAD_MORE_STATE_END_HIDE;

    public View mHeaderView;
    public View mFooterView;

    private View loadMoreEndLayout;

    protected boolean isScrolling = false;

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeadView() {
        if (mHeaderView != null) {
            return mHeaderView;
        }
        return null;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        }
        if (viewType == VIEW_TYPE_LOAD_MORE_END) {
            return onCreateLoadMoreFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        // headerview
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            return;
        }

        final int realPosition = getRealPosition(holder);
        // footerview
        if (getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_END) {
            //在grid和stagger模式下，footer要占满一行而不是一个span。所以grid需要SpanSizeLookup来动态改footer所占的spanCount。而stagger，需要将viewholder中的itemView的LayoutParams中isFullSpan设置为true。
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
                return;
            }
        }
        holder.onBindViewHolder(realPosition);
    }

    @Override
    public int getItemCount() {
        if (loadMoreFooterState == LOAD_MORE_STATE_END_SHOW) {
            return getDataCount() + 1 + (mHeaderView == null ? 0 : 1);
        }
        return getDataCount() + (mHeaderView == null ? 0 : 1);
    }

    @Override

    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return VIEW_TYPE_HEADER;
        }
        if (loadMoreFooterState == LOAD_MORE_STATE_END_SHOW && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_END;
        }

        return getDataViewType(position);
    }

    /**
     * @return
     */
    protected abstract int getDataCount();

    /**
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected int getDataViewType(int position) {
        return 0;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public void onLoadMoreStateChanged(int loadMoreFooterState) {
        this.loadMoreFooterState = loadMoreFooterState;
        if (loadMoreFooterState == LOAD_MORE_STATE_END_SHOW) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isSectionHeader(int position) {
        return false;
    }


    //  -----------  RecyclerView底部Footer（加载中和"已显示全部内容"）  ------------  //
    private class HeaderViewHolder extends BaseViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }
    }

    // GridLayoutManager单独处理，当前位置是header的位置，那么该item占据2个单元格，正常情况下占据1个单元格
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_END
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    // StaggeredGridLayoutManager单独处理
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getItemViewType() == VIEW_TYPE_HEADER || holder.getItemViewType() == VIEW_TYPE_LOAD_MORE_END);
        }
    }


    protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more_end, parent, false);
        mFooterView = view;
        return new LoadMoreFooterViewHolder(view);
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

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

}
