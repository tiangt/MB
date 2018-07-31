package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.GiftSelectedEvent;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.LiveHouseGiftAdapter;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private boolean flagOnMessageEvent = true;


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
        EventBus.getDefault().register(this);
        ArrayList<GiftInfo.GiftDetailInfoBean> giftList = getArguments().getParcelableArrayList("gifts");
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        giftAdapter = new LiveHouseGiftAdapter(getContext(), R.layout.item_live_house_gift, giftList);
        giftAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                giftAdapter.setSelectedPosition(position);
                GiftInfo.GiftDetailInfoBean giftDetailInfoBean = giftList.get(position);
                flagOnMessageEvent = false;
                EventBus.getDefault().post(new GiftSelectedEvent(giftDetailInfoBean));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setAdapter(giftAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        if(!flagOnMessageEvent){
            flagOnMessageEvent = true;
            return;
        }
        flagOnMessageEvent = false;
        giftAdapter.setSelectedPosition(-1);
    }
}
