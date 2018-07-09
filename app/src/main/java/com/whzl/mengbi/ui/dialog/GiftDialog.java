package com.whzl.mengbi.ui.dialog;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.GiftSortMotherFragment;
import com.whzl.mengbi.ui.widget.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;

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
        if(mGiftInfo.getData().get推荐() != null){
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get推荐()));
            titles.add("推荐");
        }
        if(mGiftInfo.getData().get幸运() != null){
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get幸运()));
            titles.add("幸运");
        }
        if(mGiftInfo.getData().get普通() != null){
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get普通()));
            titles.add("普通");
        }
        if(mGiftInfo.getData().get豪华() != null){
            fragments.add(GiftSortMotherFragment.newInstance(mGiftInfo.getData().get豪华()));
            titles.add("豪华");
        }
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
    }

}
