package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.ImageUrl
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PkQualifyingBean
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import kotlinx.android.synthetic.main.item_best_team.view.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-08-15
 */
class BestTeamFragment : BasePullListFragment<PkQualifyingBean.RankAnchorInfoBean.BestTeamBean, BasePresenter<BaseView>>() {

    override fun init() {
        super.init()
        hideDividerShawdow(null)
    }

    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun setShouldRefresh(): Boolean {
        return false
    }

    override fun loadData(action: Int, mPage: Int) {
        var list = arguments?.getParcelableArrayList<PkQualifyingBean.RankAnchorInfoBean.BestTeamBean>("data")
        if (list == null) {
            list = ArrayList<PkQualifyingBean.RankAnchorInfoBean.BestTeamBean>()
        }
        if (list.size < 3) {
            list.add(null)
            list.add(null)
            list.add(null)
        }
        val subList = list.subList(0, 3)
        loadSuccess(subList)
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_best_team, parent, false)
        return ViewHolder(inflate)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            if (position > 2) {
                return
            }
            when (position) {
                0 -> GlideImageLoader.getInstace().displayImage(activity, R.drawable.ic_first_qualifying, itemView.iv_rank_best_team)
                1 -> GlideImageLoader.getInstace().displayImage(activity, R.drawable.ic_second_qualifying, itemView.iv_rank_best_team)
                2 -> GlideImageLoader.getInstace().displayImage(activity, R.drawable.ic_third_qualifying, itemView.iv_rank_best_team)
            }
            val bestTeamBean = mDatas[position]
            if (bestTeamBean == null) {
                GlideImageLoader.getInstace().circleCropImage(activity, R.drawable.ic_empty_qualifying, itemView.iv_avatar_best_team)
                itemView.tv_name_best_team.setTextColor(Color.parseColor("#B3FFFFFF"))
                itemView.tv_name_best_team.text = "虚位以待"
                itemView.tv_tips_best_team.visibility = View.GONE
                itemView.tv_score_best_team.visibility = View.GONE
            } else {
                GlideImageLoader.getInstace().circleCropImage(activity,
                        ImageUrl.getAvatarUrl(bestTeamBean.userId.toLong(), "jpg", DateUtils.dateStrToMillis(bestTeamBean.lastUpdateTime, "yyyy-MM-dd HH:mm:ss")), itemView.iv_avatar_best_team)
                itemView.tv_name_best_team.setTextColor(Color.parseColor("#FFFFFF"))
                itemView.tv_name_best_team.text = bestTeamBean.nickname
                itemView.tv_tips_best_team.visibility = View.VISIBLE
                itemView.tv_score_best_team.visibility = View.VISIBLE
                itemView.tv_score_best_team.text = bestTeamBean.contributeScore.toString()
            }
        }

    }

    companion object {
        fun newInstance(bestTeam: ArrayList<PkQualifyingBean.RankAnchorInfoBean.BestTeamBean>?): BestTeamFragment {
            val anchorRankFragment = BestTeamFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("data", bestTeam)
            anchorRankFragment.arguments = bundle
            return anchorRankFragment
        }
    }
}