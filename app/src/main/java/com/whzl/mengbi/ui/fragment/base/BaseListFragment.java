package com.whzl.mengbi.ui.fragment.base;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/8/3
 */
public abstract class BaseListFragment<T> extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_empty_text)
    TextView tvEmptyText;
    protected ArrayList<T> mData = new ArrayList<>();
    private BaseListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.base_list;
    }

    @Override
    public void init() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mData == null ? 0 : mData.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                return setViewHolder(parent, viewType);
            }
        };
        recycler.setAdapter(mAdapter);
        loadData();
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder setViewHolder(ViewGroup parent, int viewType);

    /**
     * 获取数据
     */
    protected abstract void loadData();

    protected void loadSuccess(ArrayList<T> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        if (data == null || data.size() == 0) {
            showEmpty(true);
        } else {
            showEmpty(false);
        }
    }

    private void showEmpty(boolean isEmpty) {
        recycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvEmptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    protected void setEmptyText(CharSequence text){
        tvEmptyText.setText(text);
    }

    protected void setEmptyText(int textRes){
        tvEmptyText.setText(textRes);
    }

}
