package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;

import butterknife.ButterKnife;

/**
 * @author cliang
 * @date 2019.1.10
 */
public class ChipParentAdapter extends BaseListAdapter {

    private Context context;

    public ChipParentAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected int getDataCount() {
        return 0;
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_parent_chip, parent, false);
        return new ChipParentViewHolder(itemView);
    }

    class ChipParentViewHolder extends BaseViewHolder {

        public ChipParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }
    }
}
