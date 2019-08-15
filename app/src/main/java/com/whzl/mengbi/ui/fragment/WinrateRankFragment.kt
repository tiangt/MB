package com.whzl.mengbi.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PkVictoryRankListBean
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.PkQualifyingLevelUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_win_rate.view.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-08-14
 */

class WinrateRankFragment : BasePullListFragment<PkVictoryRankListBean.ListBean, BasePresenter<BaseView>>() {
    override fun init() {
        super.init()
        hideDividerShawdow(null)
        val titleView = LayoutInflater.from(activity).inflate(R.layout.head_win_rate, pullView, false)
        addHeadTips(titleView)
        pullView.setBackgroundResource(R.drawable.bg_power_rank)
    }

    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun setShouldRefresh(): Boolean {
        return false
    }

    override fun loadData(action: Int, mPage: Int) {
        ApiFactory.getInstance().getApi(Api::class.java)
                .pkVictoryRankList(ParamsUtils.getSignPramsMap(HashMap()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<PkVictoryRankListBean>() {
                    override fun onSuccess(t: PkVictoryRankListBean?) {
                        loadSuccess(t?.list)
                    }
                })
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_win_rate, parent, false)
        return ViewHolder(inflate)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]
            itemView.tv_rank_win_rate.text = (position + 1).toString()
            when (position) {
                0 -> {
                    itemView.tv_rank_win_rate.text = ""
                    itemView.tv_rank_win_rate.setBackgroundResource(R.drawable.ic_first_qualifying)
                }
                1 -> {
                    itemView.tv_rank_win_rate.text = ""
                    itemView.tv_rank_win_rate.setBackgroundResource(R.drawable.ic_second_qualifying)
                }
                2 -> {
                    itemView.tv_rank_win_rate.text = ""
                    itemView.tv_rank_win_rate.setBackgroundResource(R.drawable.ic_third_qualifying)
                }
            }

            GlideImageLoader.getInstace().displayCircleAvatar(activity, listBean.anchorAvatar, itemView.iv_avatar_win_rate)
            itemView.tv_name_win_rate.text = listBean.anchorNickname
            GlideImageLoader.getInstace().displayImage(activity,
                    PkQualifyingLevelUtils.getInstance().getUserLevelIcon(listBean.rankInfo.rankId), itemView.iv_level_win_rate)
            PkQualifyingLevelUtils.getInstance().measureImage(itemView.iv_level_win_rate, listBean.rankInfo.rankId, itemView.container_rank)

            itemView.tv_level_win_rate.text = listBean.rankInfo.rankName
            itemView.tv_rate_win_rate.text = "${String.format("%.2f", listBean.rankInfo.anchorVictoryRatio)}%"
        }

    }

}