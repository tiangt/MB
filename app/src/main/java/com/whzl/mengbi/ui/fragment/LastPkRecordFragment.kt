package com.whzl.mengbi.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment

/**
 *
 * @author nobody
 * @date 2019-08-15
 */
class LastPkRecordFragment : BasePullListFragment<Any, BasePresenter<BaseView>>() {
    override fun init() {
        super.init()
        hideDividerShawdow(null)
    }

    override fun loadData(action: Int, mPage: Int) {

    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_last_pk_record, parent, false)
        return ViewHolder(inflate)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {

        }

    }
}