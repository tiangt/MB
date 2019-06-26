package com.whzl.mengbi.ui.activity.me

import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.glide.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_nobility_sort.*

/**
 *
 * @author nobody
 * @date 2019/3/21
 */
class NobilitySortFragment : BaseFragment<BasePresenter<BaseView>>() {
    companion object {
        fun newInstance(img: Int): NobilitySortFragment {
            val fragment = NobilitySortFragment()
            val bundle = Bundle()
            bundle.putInt("img", img)
            fragment.arguments = bundle
            return fragment
        }


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nobility_sort
    }

    override fun init() {
        val img = arguments?.get("img")
        GlideImageLoader.getInstace().displayImageNoCache(activity, img, iv_nobility_sort)
    }
}