package com.whzl.mengbi.ui.dialog.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/8/6
 */
public class CommonEmojiMotherFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.pager_index_container)
    LinearLayout pagerIndexContainer;

    private EditText etContent;

    public static CommonEmojiMotherFragment newInstance() {
        return new CommonEmojiMotherFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comm_emoji;
    }

    @Override
    public void init() {
        ArrayList<Fragment> pagers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            EmojiFragment emojiFragment = EmojiFragment.newInstance(i);
            emojiFragment.setMessageEditText(etContent);
            pagers.add(emojiFragment);
        }
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), pagers));
        setUpPagerIndex();
    }

    public void setEtContent(EditText etContent) {
        this.etContent = etContent;
    }


    private void setUpPagerIndex() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 8), UIUtil.dip2px(getContext(), 8));
        for (int i = 0; i < 2; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.selector_common_pager_index);
            if (i == 0) {
                view.setSelected(true);
            } else {
                params.leftMargin = UIUtil.dip2px(getContext(), 7.5f);
            }
            pagerIndexContainer.addView(view, params);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 2; i++) {
                    if (i == position) {
                        pagerIndexContainer.getChildAt(i).setSelected(true);
                    } else {
                        pagerIndexContainer.getChildAt(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
