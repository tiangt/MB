package com.whzl.mengbi.ui.fragment

import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.fragment.base.TestFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.UIUtil
import kotlinx.android.synthetic.main.fragment_anchor_rank.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-08-14
 */
class AnchorRankFragment : BaseFragment<BasePresenter<BaseView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_anchor_rank
    }

    override fun init() {
        val titles = ArrayList<String>()
        titles.add("最佳队友")
        titles.add("最近排位")
        val fragments = ArrayList<Fragment>()
        fragments.add(TestFragment())
        fragments.add(TestFragment())
        val fragmentPagerAdaper = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        vp_anchor_rank.adapter = fragmentPagerAdaper
        vp_anchor_rank.offscreenPageLimit = 2
        tab_anchor_rank.tabMode = TabLayout.MODE_FIXED
        tab_anchor_rank.tabGravity = TabLayout.GRAVITY_FILL
        tab_anchor_rank.isNeedSwitchAnimation = true
        tab_anchor_rank.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 25f)
        tab_anchor_rank.setupWithViewPager(vp_anchor_rank)

    }
}