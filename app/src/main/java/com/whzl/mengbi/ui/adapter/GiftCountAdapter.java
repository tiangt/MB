package com.whzl.mengbi.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/9
 */
public class GiftCountAdapter extends CommonAdapter {
    public GiftCountAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {

    }
}
