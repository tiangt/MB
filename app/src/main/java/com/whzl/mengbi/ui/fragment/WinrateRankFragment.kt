package com.whzl.mengbi.ui.fragment

import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.fragment.base.BaseFragment

/**
 *
 * @author nobody
 * @date 2019-08-14
 */
class WinrateRankFragment:BaseFragment<BasePresenter<BaseView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun init() {

    }
}