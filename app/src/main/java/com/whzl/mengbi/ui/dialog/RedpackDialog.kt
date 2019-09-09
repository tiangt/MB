package com.whzl.mengbi.ui.dialog

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.GiftRedpackFragment
import com.whzl.mengbi.ui.fragment.MengbiRedpackFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.UIUtil
import kotlinx.android.synthetic.main.dialog_redpack.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class RedpackDialog : BaseAwesomeDialog() {
    override fun intLayoutId() = R.layout.dialog_redpack

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val titles = ArrayList<String>()
        titles.add("礼物红包")
        titles.add("萌币红包")
        val giftRedpackFragment = GiftRedpackFragment()
        val mengbiRedpackFragment = MengbiRedpackFragment()
        val fragments = ArrayList<Fragment>()
        fragments.add(giftRedpackFragment)
        fragments.add(mengbiRedpackFragment)
        viewpager_redpack.offscreenPageLimit = titles.size
        viewpager_redpack.adapter = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        tab_redpack.clearOnTabSelectedListeners()
        tab_redpack.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager_redpack.setCurrentItem(tab.position, true)
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.tab_item_red_bag)
                }
                val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(tab_redpack.tabTextColors)
                textView.typeface = Typeface.DEFAULT_BOLD
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.tab_item_red_bag)
                }
                val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.typeface = Typeface.DEFAULT
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tab_redpack.setupWithViewPager(viewpager_redpack)
        tab_redpack?.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 28f)
        tab_redpack.isNeedSwitchAnimation = true
    }

    companion object {
        @JvmStatic
        fun newInstance(): RedpackDialog {
            val redpackDialog = RedpackDialog()
            val bundle = Bundle()
            redpackDialog.arguments = bundle
            return redpackDialog
        }
    }
}