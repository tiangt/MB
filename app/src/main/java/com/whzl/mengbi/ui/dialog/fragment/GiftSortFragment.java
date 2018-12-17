package com.whzl.mengbi.ui.dialog.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

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
import pl.droidsonroids.gif.GifDrawable;

/**
 * @author shaw
 * @date 2018/7/9
 */
public class GiftSortFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private LiveHouseGiftAdapter giftAdapter;
    private boolean flagOnMessageEvent = true;
    private int currentPosition = -1;


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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recycler.setLayoutManager(gridLayoutManager);
        giftAdapter = new LiveHouseGiftAdapter(getContext(), R.layout.item_live_house_gift, giftList);
        giftAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (currentPosition == position) {
                    return;
                }
                try {
//                    giftAdapter.setSelectedPosition(position);
                    currentPosition = position;
                    if (recycler.getChildCount() > 0) {
                        for (int i = 0; i < recycler.getChildCount(); i++) {
                            recycler.getChildAt(i).findViewById(R.id.rl).setBackground(null);
                        }
                    }
                    GifDrawable drawable = new GifDrawable(getResources(), R.drawable.bg_live_house_select);
                    (view.findViewById(R.id.rl)).setBackground(drawable);
                    startAnimal(view.findViewById(R.id.iv_gift), position);
                    GiftInfo.GiftDetailInfoBean giftDetailInfoBean = giftList.get(position);
                    flagOnMessageEvent = false;
                    EventBus.getDefault().post(new GiftSelectedEvent(giftDetailInfoBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setAdapter(giftAdapter);
    }

    public void startAnimal(View imageView, int position) {

        AnimatorSet animatorSetsuofang = new AnimatorSet();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f);

        animatorSetsuofang.setDuration(300);
        animatorSetsuofang.setInterpolator(new OvershootInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                giftAdapter.setSelectedPosition(position);
            }
        });
        animatorSetsuofang.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        if (!flagOnMessageEvent) {
            flagOnMessageEvent = true;
            return;
        }
        flagOnMessageEvent = false;
        giftAdapter.setSelectedPosition(-1);
    }
}
