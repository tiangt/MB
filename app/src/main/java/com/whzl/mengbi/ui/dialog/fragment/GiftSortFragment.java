package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.LiveHouseGiftAdapter;
import com.whzl.mengbi.ui.fragment.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/9
 */
public class GiftSortFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private LiveHouseGiftAdapter giftAdapter;


    public static GiftSortFragment newInstance(ArrayList<GiftInfo.GiftDetailInfoBean> giftList) {
        GiftSortFragment fragment = new GiftSortFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("gifts", giftList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_sort;
    }

    @Override
    public void init() {
        ArrayList<GiftInfo.GiftDetailInfoBean> giftList = getArguments().getParcelableArrayList("gifts");
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        giftAdapter = new LiveHouseGiftAdapter(getContext(), R.layout.item_live_house_gift, giftList);
        giftAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                giftAdapter.setSelectedPosition(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setAdapter(giftAdapter);
    }
}
