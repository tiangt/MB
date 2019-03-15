package com.whzl.mengbi.ui.fragment.main

import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment

/**
 * 消息
 *
 * @author cliang
 * @date 2019.2.14
 */
class MessageFragment : BasePullListFragment<Any,BasePresenter<BaseView>>() {
    override fun loadData(action: Int, mPage: Int) {

    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
