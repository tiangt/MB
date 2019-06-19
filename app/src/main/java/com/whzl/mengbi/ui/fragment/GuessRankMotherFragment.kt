package com.whzl.mengbi.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.UIUtil
import kotlinx.android.synthetic.main.fragment_guess_rank_mother.*
import java.util.ArrayList

/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class GuessRankMotherFragment : BaseFragment<BasePresenter<BaseView>>() {
    override fun getLayoutId() = R.layout.fragment_guess_rank_mother

    override fun init() {
        val sortType = arguments?.getString("sortType")
        val titles = ArrayList<String>()
        titles.add("周榜")
        titles.add("月榜")
        val fragments = ArrayList<Fragment>()
        fragments.add(GuessRankFragment.newInstance(sortType, "WEEKRANK"))
        fragments.add(GuessRankFragment.newInstance(sortType, "MONTHRANK"))
        vp_guess_rank.offscreenPageLimit = 2
        vp_guess_rank.adapter = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        tab_guess_rank.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vp_guess_rank.setCurrentItem(tab.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tab_guess_rank.setupWithViewPager(vp_guess_rank)



        tab_guess_rank?.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 32f)
        tab_guess_rank?.isNeedSwitchAnimation = true
    }

    companion object {
        fun newInstance(sortType: String): GuessRankMotherFragment {
            val guessRankFragment = GuessRankMotherFragment()
            val argument = Bundle()
            argument.putString("sortType", sortType)
            guessRankFragment.arguments = argument
            return guessRankFragment
        }
    }
}