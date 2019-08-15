package com.whzl.mengbi.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PkQualifyingBean
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.widget.tablayout.TabLayout
import com.whzl.mengbi.util.PkQualifyingLevelUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.glide.GlideImageLoader
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
        val qualifyingBean = arguments?.getParcelable<PkQualifyingBean>("bean")

        val titles = ArrayList<String>()
        titles.add("最佳队友")
        titles.add("最近排位")
        val fragments = ArrayList<Fragment>()
        fragments.add(BestTeamFragment.newInstance(qualifyingBean?.rankAnchorInfo?.bestTeam))
        fragments.add(LastPkRecordFragment.newInstance(qualifyingBean?.lastestPkRecord))
        val fragmentPagerAdaper = FragmentPagerAdaper(childFragmentManager, fragments, titles)
        vp_anchor_rank.adapter = fragmentPagerAdaper
        vp_anchor_rank.offscreenPageLimit = 2
        tab_anchor_rank.tabMode = TabLayout.MODE_FIXED
        tab_anchor_rank.tabGravity = TabLayout.GRAVITY_FILL
        tab_anchor_rank.isNeedSwitchAnimation = true
        tab_anchor_rank.selectedTabIndicatorWidth = UIUtil.dip2px(activity, 25f)
        tab_anchor_rank.setupWithViewPager(vp_anchor_rank)

        initView(qualifyingBean)
    }

    private fun initView(qualifyingBean: PkQualifyingBean?) {
        val rankAnchorInfo = qualifyingBean?.rankAnchorInfo
        GlideImageLoader.getInstace().displayImage(activity,
                rankAnchorInfo?.rankId?.let { PkQualifyingLevelUtils.getInstance().getUserLevelIcon(it) }, iv_level_anchor_rank)
        rankAnchorInfo?.rankId?.let { PkQualifyingLevelUtils.getInstance().measureImage(iv_level_anchor_rank, it, container_rank) }
        tv_level_anchor_rank.text = rankAnchorInfo?.rankName
        tv_current_rank.text = rankAnchorInfo?.currentRank.toString()
        tv_rank_pk_time.text = rankAnchorInfo?.rankPkTime.toString()
        tv_victory_time_anchor_rank.text = rankAnchorInfo?.victoryTime.toString()
        tv_victory_ratio.text = String.format("%.2f", rankAnchorInfo?.victoryRatio?.times(100))
        tv_continue_victory_time.text = rankAnchorInfo?.continueVictoryTime.toString()
        val plus = rankAnchorInfo?.haveValue?.plus(rankAnchorInfo.nextRankNeedValue)
        tv_progress_anchor_rank.text = "${rankAnchorInfo?.haveValue}/$plus"
        if (rankAnchorInfo?.haveValue != null && plus != 0) {
            progress_anchor_rank.progress = rankAnchorInfo.haveValue * 100 / plus!!
        }
    }

    companion object {
        fun newInstance(bean: PkQualifyingBean?): AnchorRankFragment {
            val anchorRankFragment = AnchorRankFragment()
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            anchorRankFragment.arguments = bundle
            return anchorRankFragment
        }
    }
}