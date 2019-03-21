package com.whzl.mengbi.ui.activity.me

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.UIUtil

/**
 *
 * @author nobody
 * @date 2019/3/21
 */
class NobilityFragment : BaseFragment<BasePresenter<BaseView>>() {

    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_nobility
    }

    override fun init() {
        viewPager = activity?.findViewById(R.id.vp_nobility)
        tabLayout = activity?.findViewById(R.id.tablayout_nobility)
        initVp()
    }

    private fun initVp() {
        val titles = arrayListOf("青铜", "白银", "黄金", "铂金", "钻石", "星耀", "王者", "传说")
        val fragments = arrayListOf<Fragment>(
                NobilitySortFragment.newInstance(R.drawable.img_nobility_1),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_2),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_3),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_4),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_5),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_6),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_7),
                NobilitySortFragment.newInstance(R.drawable.img_nobility_8)
        )
        tabLayout?.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 23f)
        tabLayout?.isNeedSwitchAnimation = true
        val adapter = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        viewPager?.offscreenPageLimit = titles.size
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }
}