package com.whzl.mengbi.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment

/**
 *
 * @author nobody
 * @date 2019-08-14
 */
class PowerRankFragment : BasePullListFragment<Any, BasePresenter<BaseView>>() {
    override fun init() {
        super.init()
        hideDividerShawdow(null)
        val titleView = LayoutInflater.from(activity).inflate(R.layout.head_power_rank, pullView, false)
        addHeadTips(titleView)
        pullView.setBackgroundResource(R.drawable.bg_power_rank)
    }

    override fun loadData(action: Int, mPage: Int) {
        loadSuccess(null)
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_power_rank, parent, false)
        return ViewHolder(inflate)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {

        }

    }

}