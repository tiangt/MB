package com.whzl.mengbi.adapter;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.whzl.mengbi.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.widget.recyclerview.base.ItemViewDelegate;
import com.whzl.mengbi.widget.recyclerview.base.ViewHolder;

import java.util.List;


public abstract class LiveMessageAdapter<T> extends MultiItemTypeAdapter{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public LiveMessageAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                LiveMessageAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
