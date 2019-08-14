package com.whzl.mengbi.ui.dialog

import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.AnchorRankFragment
import com.whzl.mengbi.ui.fragment.PowerRankFragment
import com.whzl.mengbi.ui.fragment.WinrateRankFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.UIUtil
import kotlinx.android.synthetic.main.dialog_pk_qualifying.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-08-14
 */
class PkQualifyingDialog : BaseAwesomeDialog() {
    override fun intLayoutId() = R.layout.dialog_pk_qualifying

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
//        holder?.setOnClickListener(R.id.view_out_qualifying) { dismissDialog() }

        val titles = ArrayList<String>()
        titles.add("主播段位")
        titles.add("战力排行")
        titles.add("胜率排行")
        val fragments = ArrayList<Fragment>()
        fragments.add(AnchorRankFragment())
        fragments.add(PowerRankFragment())
        fragments.add(WinrateRankFragment())
        val fragmentPagerAdaper = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        viewpager_qualifying.adapter = fragmentPagerAdaper
        viewpager_qualifying.offscreenPageLimit = 3
        tab_layout_qualifying.tabMode = TabLayout.MODE_FIXED
        tab_layout_qualifying.tabGravity = TabLayout.GRAVITY_FILL
        tab_layout_qualifying.isNeedSwitchAnimation = true
        tab_layout_qualifying.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 25f)
        tab_layout_qualifying.setupWithViewPager(viewpager_qualifying)
    }

    companion object {
        fun newInstance(): PkQualifyingDialog {
            return PkQualifyingDialog()
        }
    }
}