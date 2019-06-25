package com.whzl.mengbi.ui.fragment

import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.fragment.base.BaseFragment

/**
 *
 * @author nobody
 * @date 2019-06-25
 */
class PkRankSortFragment : BaseFragment<BasePresenter<BaseView>>() {
    override fun getLayoutId() = R.layout.fragment_pk_rank_sort

    override fun init() {

    }

    companion object {
        fun newInstance(): PkRankSortFragment {
            val pkRankSortFragment = PkRankSortFragment()
            return pkRankSortFragment
        }
    }
}