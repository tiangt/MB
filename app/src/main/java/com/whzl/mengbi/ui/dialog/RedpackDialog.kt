package com.whzl.mengbi.ui.dialog

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.model.entity.RedpackGoodInfoBean
import com.whzl.mengbi.ui.activity.JsBridgeActivity
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.GiftRedpackFragment
import com.whzl.mengbi.ui.fragment.MengbiRedpackFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_redpack.*
import java.util.*
import kotlin.collections.HashMap

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class RedpackDialog : BaseAwesomeDialog() {
    private var programId: Int = 0

    override fun intLayoutId() = R.layout.dialog_redpack

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        programId = arguments?.getInt("programId")!!
        getRedpackGoodsInfo()
        iv_close_redpack.clickDelay {
            dismissDialog()
        }

        iv_note_redpack.clickDelay {
            startActivity(Intent(activity, JsBridgeActivity::class.java)
                    .putExtra("title", "红包说明")
                    .putExtra("url", SPUtils.get(activity, SpConfig.REDPACKETHELPURL, "")!!.toString()))
        }
    }

    private fun initVp(t: RedpackGoodInfoBean) {
        val titles = ArrayList<String>()
        titles.add("礼物红包")
        titles.add("萌币红包")
        val giftRedpackFragment = GiftRedpackFragment.newInstance(t, programId)
        giftRedpackFragment.setListener(SendRedpacketListener { dismissDialog() })

        val mengbiRedpackFragment = MengbiRedpackFragment.newInstance(t, programId)
        mengbiRedpackFragment.setListener(SendRedpacketListener { dismissDialog() })
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

    private fun getRedpackGoodsInfo() {
        ApiFactory.getInstance().getApi(Api::class.java)
                .redpackGoodsInfo(ParamsUtils.getSignPramsMap(HashMap()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<RedpackGoodInfoBean>() {
                    override fun onSuccess(t: RedpackGoodInfoBean?) {
                        if (t?.conditionGoodList == null || t.prizeGoodsList == null) {
                            return
                        }
                        initVp(t)
                    }
                })
    }

    companion object {
        @JvmStatic
        fun newInstance(mProgramId: Int): RedpackDialog {
            val redpackDialog = RedpackDialog()
            val bundle = Bundle()
            bundle.putInt("programId", mProgramId)
            redpackDialog.arguments = bundle
            return redpackDialog
        }
    }
}