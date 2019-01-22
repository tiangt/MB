package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.GiftSelectedEvent;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.SendGiftSuccessEvent;
import com.whzl.mengbi.eventbus.event.SendSuperWordEvent;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FreeGiftBean;
import com.whzl.mengbi.model.entity.GiftCountInfoBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.RunWayValueBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.BackpackMotherFragment;
import com.whzl.mengbi.ui.dialog.fragment.CommonMotherFragment;
import com.whzl.mengbi.ui.dialog.fragment.GiftSortMotherFragment;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class GiftDialog extends BaseAwesomeDialog {

    private GiftInfo mGiftInfo;
    private long coin;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.container)
    ViewPager viewpager;
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
    @BindView(R.id.first_top_up)
    ConstraintLayout firstTopUp;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.tv_title_superrun)
    TextView tvTitleSuperRun;
    @BindView(R.id.tv_content_superrun)
    TextView tvContentSuperRun;
    @BindView(R.id.tv_add_superrun)
    TextView tvAddSuperRun;
    @BindView(R.id.view_super)
    View viewSuper;
    @BindView(R.id.ll_super)
    LinearLayout llSuper;

    private ArrayList<GiftCountInfoBean> giftCountInfoList;
    private CommonAdapter<GiftCountInfoBean> adapter;
    private PopupWindow popupWindow;
    private GiftInfo.GiftDetailInfoBean giftDetailInfoBean;
    private int currentSelectedIndex;
    private ArrayList<Fragment> fragments;
    private int REQUEST_LOGIN = 120;
    private String CAN_NOT_SUPER_RUN = "单笔超过" + AppConfig.MIN_VALUE_GIFT_DIALOG + "萌币才可以上超跑";
    private AddSendWordDialog addSendWordDialog;
    private long currentValue = 0;
    public boolean superValue = false;
    private int totalValue;

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
        getFreeGift();
    }

    private void getFreeGift() {
        if (!TextUtils.isEmpty(SPUtils.get(getContext(), SpConfig.FREE_GOODS_IDS, "").toString())) {
            return;
        }
        HashMap paramsMap = new HashMap();
        Map signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
        ApiFactory.getInstance().getApi(Api.class)
                .freeGoodsIds(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<FreeGiftBean>(this) {

                    @Override
                    public void onSuccess(FreeGiftBean freeGiftBean) {
                        if (freeGiftBean != null && !freeGiftBean.list.isEmpty()) {
                            Gson gson = new Gson();
                            String s = gson.toJson(freeGiftBean);
                            SPUtils.put(getContext(), SpConfig.FREE_GOODS_IDS, s);
                        }
                    }

                    @Override
                    public void onError(ApiResult<FreeGiftBean> body) {
                    }
                });
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
        tvContentSuperRun.setText(CAN_NOT_SUPER_RUN);
        coin = getArguments().getLong("coin");
        boolean hasTopUp = (boolean) SPUtils.get(getContext(), SpConfig.KEY_HAS_RECHARGED, false);
        firstTopUp.setVisibility(hasTopUp ? View.GONE : View.VISIBLE);
        mGiftInfo = getArguments().getParcelable("gift_info");
        fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        fragments.add(CommonMotherFragment.newInstance());
        tabLayout.addTab(tabLayout.newTab().setText("常送"));
        titles.add("常送");

        if (mGiftInfo.getData() != null && mGiftInfo.getData().getList() != null) {
            List<GiftInfo.DataBean.ListBean> listBeans = mGiftInfo.getData().getList();
            for (int i = 0; i < listBeans.size(); i++) {
                if (listBeans.get(i).getGroup() != null) {
                    fragments.add(GiftSortMotherFragment.newInstance(listBeans.get(i).getGroupList()));
                    titles.add(listBeans.get(i).getGroup());
                    tabLayout.addTab(tabLayout.newTab().setText(listBeans.get(i).getGroup()));
                }
            }
        }

        fragments.add(BackpackMotherFragment.newInstance());
        tabLayout.addTab(tabLayout.newTab().setText("背包"));
        titles.add("背包");

        viewpager.setOffscreenPageLimit(titles.size());
        viewpager.setAdapter(new GiftPagerAdapter(getChildFragmentManager(), fragments, titles));
        //设置TabLayout内容超过屏幕宽度时可以横向滑动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //关联TabLayout和ViewPager
        setTabWidth(tabLayout, UIUtil.dip2px(getActivity(), 18));
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(1);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewSuper.setVisibility(position == fragments.size() - 1 ? View.INVISIBLE : View.VISIBLE);
                llSuper.setVisibility(position == fragments.size() - 1 ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvAmount.setText(AmountConversionUitls.amountConversionFormat(coin));
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

        getRunWayGiftDetail();
    }

    private void getRunWayGiftDetail() {
        ApiFactory.getInstance().getApi(Api.class)
                .getRunWayValue(ParamsUtils.getSignPramsMap(new HashMap<>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<RunWayValueBean>() {
                    @Override
                    public void onSuccess(RunWayValueBean runWayListBean) {
                        currentValue = runWayListBean.value;
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendGiftSuccessEvent event) {
        currentValue = totalValue;
        setCheckChange();
    }

    @OnClick({R.id.tv_count, R.id.btn_send_gift, R.id.btn_count_confirm, R.id.tv_top_up, R.id.first_top_up, R.id.checkbox, R.id.tv_add_superrun})
    public void onClick(View view) {
        long mUserId = Long.parseLong(SPUtils.get(getActivity(), "userId", 0L).toString());
        switch (view.getId()) {
            case R.id.tv_count:
                showCountSelectPopWindow();
                break;
            case R.id.btn_send_gift:
                if (mUserId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    return;
                }
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
                superValue = false;
                ((LiveDisplayActivity) getActivity()).sendGift(giftCount, giftDetailInfoBean.getGoodsId(),
                        giftDetailInfoBean.isBackpack(), checkBox.isChecked() ? "T" : "F",
                        SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString());
                totalValue = giftCount * giftDetailInfoBean.getRent();
                if (totalValue > currentValue) {
                    superValue = true;
                }
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
                setCheckChange();
                break;
            case R.id.tv_top_up:
                if (mUserId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    return;
                }
                jumpRechargeActivity();
                break;
            case R.id.first_top_up:
                if (mUserId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    return;
                }
                Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                startActivity(intent);
                dismiss();
                break;
            case R.id.checkbox:
                if (mUserId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    checkBox.setChecked(false);
                    return;
                }
                String countStr2 = tvCount.getText().toString().trim();
                int giftCount2 = 0;
                try {
                    giftCount2 = Integer.parseInt(countStr2);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (giftDetailInfoBean == null) {
                    ToastUtils.showToast("请选择礼物");
                    checkBox.setChecked(false);
                    return;
                }
                if (giftCount2 == 0) {
                    ToastUtils.showToast("礼物数量不能为0");
                    checkBox.setChecked(false);
                    return;
                }
                if (giftCount2 * giftDetailInfoBean.getRent() < AppConfig.MIN_VALUE_GIFT_DIALOG) {
                    ToastUtils.showToast(CAN_NOT_SUPER_RUN);
                    tvContentSuperRun.setText(CAN_NOT_SUPER_RUN);
                    checkBox.setChecked(false);
                    return;
                }
                if (checkBox.isChecked()) {
                    if (TextUtils.isEmpty(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString())) {
                        tvTitleSuperRun.setText("请添加寄语");
                    } else {
                        tvTitleSuperRun.setText(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString());
                    }

                    if (giftCount2 * giftDetailInfoBean.getRent() > currentValue) {
                        tvContentSuperRun.setText("可以攻占当前超跑哦!");
                    } else {
                        tvContentSuperRun.setText(getString(R.string.gift_dialog_super_run,
                                currentValue - giftCount2 * giftDetailInfoBean.getRent() + 1));
                    }
                    tvAddSuperRun.setSelected(true);
                } else {
                    tvTitleSuperRun.setText("是否上超跑");
                    tvContentSuperRun.setText("放心吧！您的超跑消息不会被任何人看到");
                    tvAddSuperRun.setSelected(false);
                }
                break;
            case R.id.tv_add_superrun:
                if (mUserId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    checkBox.setChecked(false);
                    return;
                }
                if (giftDetailInfoBean == null) {
                    ToastUtils.showToast("请选择礼物");
                    checkBox.setChecked(false);
                    return;
                }
                if (!tvAddSuperRun.isSelected()) {
                    return;
                }
                String countStr3 = tvCount.getText().toString().trim();
                int giftCount3 = 0;
                try {
                    giftCount3 = Integer.parseInt(countStr3);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (giftCount3 * giftDetailInfoBean.getRent() < AppConfig.MIN_VALUE_GIFT_DIALOG) {
                    ToastUtils.showToast(CAN_NOT_SUPER_RUN);
                    return;
                }
                if (addSendWordDialog != null && addSendWordDialog.isAdded()) {
                    return;
                }
                addSendWordDialog = AddSendWordDialog.newInstance();
                addSendWordDialog.setOutCancel(false).show(getFragmentManager());
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
                    setCheckChange();
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
        setCheckChange();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendSuperWordEvent event) {
        if (checkBox.isChecked()) {
            if (TextUtils.isEmpty(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString())) {
                tvTitleSuperRun.setText("请添加寄语");
            } else {
                tvTitleSuperRun.setText(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString());
            }
        }
    }

    public void refreshPrice() {
        getRunWayGiftDetail();
        setCheckChange();
    }

    private void setCheckChange() {
        if (giftDetailInfoBean == null || giftDetailInfoBean.getRent() == 0) {
            return;
        }
        String countStr = tvCount.getText().toString().trim();
        int giftCount = 0;
        giftCount = Integer.parseInt(countStr);
        if (giftDetailInfoBean.getRent() * giftCount < AppConfig.MIN_VALUE_GIFT_DIALOG) {
            checkBox.setChecked(false);
            tvAddSuperRun.setSelected(false);
            tvTitleSuperRun.setText("是否上超跑");
        } else {
            checkBox.setChecked(true);
            tvAddSuperRun.setSelected(true);
        }
        if (!checkBox.isChecked()) {
            if (giftDetailInfoBean.getRent() * giftCount < AppConfig.MIN_VALUE_GIFT_DIALOG) {
                tvContentSuperRun.setText(CAN_NOT_SUPER_RUN);
            }
            return;
        }
        //不能上超跑
        if (giftDetailInfoBean.getRent() * giftCount < AppConfig.MIN_VALUE_GIFT_DIALOG) {
            checkBox.setChecked(false);
            tvTitleSuperRun.setText("是否上超跑");
            tvContentSuperRun.setText(CAN_NOT_SUPER_RUN);
            tvAddSuperRun.setSelected(false);
        }
        //可以上超跑
        else {
            if (TextUtils.isEmpty(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString())) {
                tvTitleSuperRun.setText("请添加寄语");
            } else {
                tvTitleSuperRun.setText(SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString());
            }
            checkBox.setChecked(true);
            if (giftCount * giftDetailInfoBean.getRent() > currentValue) {
                tvContentSuperRun.setText("可以攻占当前超跑哦!");
            } else {
                tvContentSuperRun.setText(getString(R.string.gift_dialog_super_run,
                        currentValue - giftCount * giftDetailInfoBean.getRent() + 1));
            }
            tvAddSuperRun.setSelected(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveHouseUserInfoUpdateEvent event) {
        tvAmount.setText(AmountConversionUitls.amountConversionFormat(event.coin));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGiftInfo = null;
        fragments = null;
    }

    /**
     * TabLayout+ViewPager+Fragment滑动
     */
    private class GiftPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;
        ArrayList<String> titles;

        public GiftPagerAdapter(FragmentManager fm, List<Fragment> list, ArrayList<String> titles) {
            super(fm);
            this.list = list;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    public void setTabWidth(final TabLayout tabLayout, final int padding) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("Thread.currentThread().getId() "+Thread.currentThread().getId());
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);


                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
