package com.whzl.mengbi.ui.dialog.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.eventbus.event.ClearAllAnim;
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
    private int currentPosition = -1;
    private AnimatorSet animatorSetsuofang;


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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), AppConfig.NUM_SPAN_GIFT_DIALOG);
        recycler.setLayoutManager(gridLayoutManager);
        giftAdapter = new LiveHouseGiftAdapter(getContext(), R.layout.item_live_house_gift, giftList);
        giftAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (currentPosition == position) {
                    return;
                }
                EventBus.getDefault().post(new ClearAllAnim());
                try {
//                    giftAdapter.setSelectedPosition(position);
                    currentPosition = position;
                    for (int i = 0; i < gridLayoutManager.getChildCount(); i++) {
                        gridLayoutManager.getChildAt(i).findViewById(R.id.rl).setBackground(null);
                        gridLayoutManager.getChildAt(i).findViewById(R.id.iv_gift).getAnimation();
                    }
//                    if (recycler.getChildCount() > 0) {
//                        for (int i = 0; i < recycler.getChildCount(); i++) {
//                            recycler.getChildAt(i).findViewById(R.id.rl).setBackground(null);
//                        }
//                    }
//                    GifDrawable drawable = new GifDrawable(getResources(), R.drawable.bg_live_house_select);
                    (view.findViewById(R.id.rl)).setBackgroundResource(R.drawable.bg_live_house_gift_bg);
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
        animatorSetsuofang = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f, 1f, 0.8f, 1f);
        scaleX.setRepeatCount(-1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f, 1f, 0.8f, 1f);
        scaleY.setRepeatCount(-1);

        animatorSetsuofang.setDuration(1500);
        animatorSetsuofang.setInterpolator(new LinearInterpolator());
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
        if (animatorSetsuofang != null && animatorSetsuofang.isRunning()) {
            animatorSetsuofang.cancel();
            animatorSetsuofang.end();
            animatorSetsuofang = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        if (!flagOnMessageEvent) {
            flagOnMessageEvent = true;
            return;
        }
        flagOnMessageEvent = false;
        currentPosition = -1;
        giftAdapter.setSelectedPosition(-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClearAllAnim event) {
        if (animatorSetsuofang != null) {
            animatorSetsuofang.end();
            animatorSetsuofang = null;
        }
    }
}
