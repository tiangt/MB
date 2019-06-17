package com.whzl.mengbi.ui.fragment.me

import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import kotlinx.android.synthetic.main.fragment_myguess.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-06-17
 */
class MyGuessFragment : BaseFragment<BasePresenter<BaseView>>() {
    override fun getLayoutId() = R.layout.fragment_myguess

    override fun init() {
        (activity as FrgActivity).setTitle("我的竞猜")

        val titles = ArrayList<String>()
        titles.add("正在竞猜")
        titles.add("历史竞猜")
        val fragments = ArrayList<Fragment>()
        fragments.add(MyGuessSortFragment.newInstance("ONGOING"))
        fragments.add(MyGuessSortFragment.newInstance("HISTORY"))
        val fragmentPagerAdaper = FragmentPagerAdaper(fragmentManager, fragments, titles)
        vp_my_guess.adapter = fragmentPagerAdaper
        vp_my_guess.offscreenPageLimit = 2
        tab_my_guess.tabMode = TabLayout.MODE_FIXED
        tab_my_guess.tabGravity = TabLayout.GRAVITY_FILL
        tab_my_guess.isNeedSwitchAnimation = true
        tab_my_guess.selectedTabIndicatorWidth = com.whzl.mengbi.util.UIUtil.dip2px(activity, 25f)
        tab_my_guess.setupWithViewPager(vp_my_guess)
    }
}