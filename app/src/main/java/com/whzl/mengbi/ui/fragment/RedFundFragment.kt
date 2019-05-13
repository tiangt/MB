package com.whzl.mengbi.ui.fragment

import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.fragment.base.BaseFragment

/**
 *
 * @author nobody
 * @date 2019/5/13
 */
class RedFundFragment : BaseFragment<BasePresenter<BaseView>>() {
    companion object {
        fun newInstance(): RedFundFragment {
            val redFundFragment = RedFundFragment()
            return redFundFragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_red_fund
    }

    override fun init() {
    }
}