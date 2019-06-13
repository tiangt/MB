package com.whzl.mengbi.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;

/**
 * @author nobody
 * @date 2019-06-13
 */
public abstract class GuessAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateNormalViewHolder(parent, viewType);
    }

    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return getDataCount();
    }

    protected abstract int getDataCount();
}
