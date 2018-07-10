package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftCountInfoBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.GiftSortMotherFragment;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.ui.widget.view.NoScrollViewPager;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class GiftDialog extends BaseAwesomeDialog {

    private static GiftInfo mGiftInfo;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
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

    public static BaseAwesomeDialog newInstance(GiftInfo giftInfo) {
        mGiftInfo = giftInfo;
        return new GiftDialog();
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_gift;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        if (mGiftInfo.getData().get推荐() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get推荐()));
            titles.add("推荐");
        }
        if (mGiftInfo.getData().get幸运() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get幸运()));
            titles.add("幸运");
        }
        if (mGiftInfo.getData().get普通() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get普通()));
            titles.add("普通");
        }
        if (mGiftInfo.getData().get豪华() != null) {
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get豪华()));
            titles.add("豪华");
        }
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
    }

    @OnClick({R.id.tv_count, R.id.btn_send_gift, R.id.btn_count_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_count:
                showCountSelectPopWindow();
                break;
            case R.id.btn_send_gift:
                break;
            case R.id.btn_count_confirm:
                KeyBoardUtil.closeKeybord(etCount, getContext());
                String trim = etCount.getText().toString().trim();
                int count = 0;
                try {
                    count =Integer.parseInt(trim);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                tvCount.setText(count + "");
                llCountCustomContainer.setVisibility(View.GONE);
                rlSendContainer.setVisibility(View.VISIBLE);
                break;
        }

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

}
