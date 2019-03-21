package com.whzl.mengbi.ui.activity.me

import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.fragment.base.BaseFragment

/**
 *
 * @author nobody
 * @date 2019/3/21
 */
class NobilitySortFragment : BaseFragment<BasePresenter<BaseView>>() {
    companion object {
        fun newInstance(): NobilitySortFragment {
            val fragment = NobilitySortFragment()
            return fragment
        }


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nobility_sort
    }

    override fun init() {
    }
}