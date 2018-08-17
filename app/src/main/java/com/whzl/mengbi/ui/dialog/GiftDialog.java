package com.whzl.mengbi.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.GiftSelectedEvent;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.GiftCountInfoBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.BackpackFragment;
import com.whzl.mengbi.ui.dialog.fragment.BackpackMotherFragment;
import com.whzl.mengbi.ui.dialog.fragment.GiftSortMotherFragment;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class GiftDialog extends BaseAwesomeDialog {

    private GiftInfo mGiftInfo;
    private long coin;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    //    @BindView(R.id.viewpager)
//    NoScrollViewPager viewpager;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.btn_send_gift)
    Button btnSendGift;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_top_up)
    TextView tvTopUp;
    @BindView(R.id.rl_send_container)
    RelativeLayout rlSendContainer;
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.ll_count_custom_container)
    LinearLayout llCountCustomContainer;
    private ArrayList<GiftCountInfoBean> giftCountInfoList;
    private CommonAdapter<GiftCountInfoBean> adapter;
    private PopupWindow popupWindow;
    private GiftInfo.GiftDetailInfoBean giftDetailInfoBean;
    private int currentSelectedIndex;
    private ArrayList<Fragment> fragments;

    public static BaseAwesomeDialog newInstance(GiftInfo giftInfo, long coin) {
        Bundle args = new Bundle();
        args.putParcelable("gift_info", giftInfo);
        args.putLong("coin", coin);
        BaseAwesomeDialog dialog = new GiftDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_gift;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        coin = getArguments().getLong("coin");
        mGiftInfo = getArguments().getParcelable("gift_info");
        fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        if (mGiftInfo.getData().getRecommend() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().getRecommend()));
            titles.add("推荐");
            tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        }
        if (mGiftInfo.getData().getLucky() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().getLucky()));
            titles.add("幸运");
            tabLayout.addTab(tabLayout.newTab().setText("幸运"));
        }
        if (mGiftInfo.getData().getCommon() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().getCommon()));
            titles.add("普通");
            tabLayout.addTab(tabLayout.newTab().setText("普通"));
        }
        if (mGiftInfo.getData().getLuxury() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().getLuxury()));
            titles.add("豪华");
            tabLayout.addTab(tabLayout.newTab().setText("豪华"));
        }
        fragments.add(BackpackMotherFragment.newInstance());
        tabLayout.addTab(tabLayout.newTab().setText("背包"));
//        viewpager.setOffscreenPageLimit(titles.size());
//        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        tvAmount.setText(coin + "");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabChange(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragments.get(0));
        fragmentTransaction.commit();
    }

    private void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments.get(currentSelectedIndex));
        if (fragments.get(index).isAdded()) {
            fragmentTransaction.show(fragments.get(index));
        } else {
            fragmentTransaction.add(R.id.container, fragments.get(index));
        }
        fragmentTransaction.commit();
        currentSelectedIndex = index;
    }

    @OnClick({R.id.tv_count, R.id.btn_send_gift, R.id.btn_count_confirm, R.id.tv_top_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_count:
                showCountSelectPopWindow();
                break;
            case R.id.btn_send_gift:
                String countStr = tvCount.getText().toString().trim();
                int giftCount = 0;
                try {
                    giftCount = Integer.parseInt(countStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (giftDetailInfoBean == null) {
                    ToastUtils.showToast("请选择礼物");
                    return;
                }
                if (giftCount == 0) {
                    ToastUtils.showToast("礼物数量不能为0");
                    return;
                }
                ((LiveDisplayActivity) getActivity()).sendGift(giftCount, giftDetailInfoBean.getGoodsId(), giftDetailInfoBean.isBackpack());
                break;
            case R.id.btn_count_confirm:
                KeyBoardUtil.closeKeybord(etCount, getContext());
                String trim = etCount.getText().toString().trim();
                int count = 0;
                try {
                    count = Integer.parseInt(trim);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                etCount.getText().clear();
                tvCount.setText(count + "");
                llCountCustomContainer.setVisibility(View.GONE);
                rlSendContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_top_up:
                jumpRechargeActivity();
                break;
        }

    }

    private void jumpRechargeActivity() {
        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
        startActivity(intent);
        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int[] countArr = getResources().getIntArray(R.array.gift_count_select);
        String[] countDescArr = getResources().getStringArray(R.array.gift_count_desc_select);
        giftCountInfoList = new ArrayList<>();
        for (int i = 0; i < countArr.length; i++) {
            GiftCountInfoBean giftCountInfoBean = new GiftCountInfoBean(countArr[i], countDescArr[i]);
            giftCountInfoList.add(giftCountInfoBean);
        }
    }

    private void showCountSelectPopWindow() {
        if (popupWindow == null) {
            View popView = getLayoutInflater().inflate(R.layout.pop_window_gift_count_select, null);
            TextView tvCustom = popView.findViewById(R.id.tv_custom);
            RecyclerView recyclerView = popView.findViewById(R.id.recycler);
            tvCustom.setOnClickListener(v -> {
                popupWindow.dismiss();
                llCountCustomContainer.setVisibility(View.VISIBLE);
                rlSendContainer.setVisibility(View.GONE);
                etCount.requestFocus();
                KeyBoardUtil.openKeybord(etCount, getContext());
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CommonAdapter<GiftCountInfoBean>(getContext(), R.layout.item_gift_count, giftCountInfoList) {
                @Override
                protected void convert(com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder holder, GiftCountInfoBean giftCountInfoBean, int position) {
                    holder.setText(R.id.tv_count, giftCountInfoBean.count + "");
                    holder.setText(R.id.tv_count_desc, giftCountInfoBean.countDesc);
                    if (position == giftCountInfoList.size() - 1) {
                        holder.setVisible(R.id.view_divider, false);
                    }
                }
            };
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    tvCount.setText(giftCountInfoList.get(position).count + "");
                    popupWindow.dismiss();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            recyclerView.setAdapter(adapter);
            popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }
        popupWindow.showAtLocation(btnSendGift, Gravity.BOTTOM | Gravity.END, UIUtil.dip2px(getContext(), 28.5f), UIUtil.dip2px(getContext(), 33.5f));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        giftDetailInfoBean = event.giftDetailInfoBean;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveHouseUserInfoUpdateEvent event) {
        tvAmount.setText(event.coin + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGiftInfo = null;
        fragments = null;
    }
}
