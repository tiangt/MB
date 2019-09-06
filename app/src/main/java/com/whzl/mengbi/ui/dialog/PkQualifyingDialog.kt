package com.whzl.mengbi.ui.dialog

import android.content.Intent
import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PkQualifyingBean
import com.whzl.mengbi.ui.activity.JsBridgeActivity
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.AnchorRankFragment
import com.whzl.mengbi.ui.fragment.PowerRankFragment
import com.whzl.mengbi.ui.fragment.WinrateRankFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.clickDelay
import kotlinx.android.synthetic.main.dialog_pk_qualifying.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-08-14
 */
class PkQualifyingDialog : BaseActivity<BasePresenter<BaseView>>() {
    override fun setupContentView() {
        setContentView(R.layout.dialog_pk_qualifying)
    }

    override fun setupView() {
        val bean = intent.getParcelableExtra<PkQualifyingBean>("bean")

        iv_note_qualifying.clickDelay {
            startActivity(Intent(this@PkQualifyingDialog, JsBridgeActivity::class.java)
                    .putExtra("title", "排位赛帮助说明")
                    .putExtra("url", SPUtils.get(this@PkQualifyingDialog, SpConfig.PKQUALIFYINGHELPURL, "").toString()))
        }

        val titles = ArrayList<String>()
        titles.add("主播段位")
        titles.add("战力排行")
        titles.add("胜率排行")
        val fragments = ArrayList<Fragment>()
        fragments.add(AnchorRankFragment.newInstance(bean))
        fragments.add(PowerRankFragment())
        fragments.add(WinrateRankFragment())
        val fragmentPagerAdaper = FragmentPagerAdaper(supportFragmentManager, fragments, titles)
        viewpager_qualifying.adapter = fragmentPagerAdaper
        viewpager_qualifying.offscreenPageLimit = 3
        tab_layout_qualifying.tabMode = TabLayout.MODE_FIXED
        tab_layout_qualifying.tabGravity = TabLayout.GRAVITY_FILL
        tab_layout_qualifying.isNeedSwitchAnimation = true
        tab_layout_qualifying.selectedTabIndicatorWidth = UIUtil.dip2px(this, 25f)
        tab_layout_qualifying.setupWithViewPager(viewpager_qualifying)

        initView(bean)
    }

    override fun loadData() {
    }


    private fun initView(qualifyingBean: PkQualifyingBean) {
        tv_season_name.text = qualifyingBean?.seasonInfo?.seasonName
        tv_season_time.text = "距本赛季结束剩 ${qualifyingBean?.seasonInfo?.seasonLeftSec?.let { DateUtils.translateLastSecond2(it) }}"
    }

}