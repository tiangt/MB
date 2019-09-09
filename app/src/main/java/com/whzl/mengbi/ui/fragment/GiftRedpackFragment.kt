package com.whzl.mengbi.ui.fragment

import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.clickDelay
import kotlinx.android.synthetic.main.fragment_gift_redpack.*
import kotlinx.android.synthetic.main.item_condition_redpack.view.*
import kotlinx.android.synthetic.main.pop_condition_gift_reapack.view.*

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class GiftRedpackFragment : BaseFragment<BasePresenter<BaseView>>() {
    private lateinit var conditionPop: PopupWindow
    private var conditionList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_gift_redpack
    }

    override fun init() {
        container_condotion_gift.clickDelay {
            showConditionPopWindow()
        }
    }

    private fun showConditionPopWindow() {
        conditionList.add("aaaaa")
        conditionList.add("bb")
        conditionList.add("dd")
        conditionList.add("f")
        conditionList.add("fw")
        conditionList.add("aaaaa")
        val popView = LayoutInflater.from(activity).inflate(R.layout.pop_condition_gift_reapack, null)
        popView.rv_pop_condition_gift_redpack.layoutManager = LinearLayoutManager(activity)
        popView.rv_pop_condition_gift_redpack.adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return conditionList.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_condition_redpack, parent, false)
                return ConditionHolder(inflate)
            }

        }
        conditionPop = PopupWindow(popView, UIUtil.dip2px(activity, 290f),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        conditionPop.setBackgroundDrawable(BitmapDrawable())
        conditionPop.isOutsideTouchable = true
        conditionPop.isFocusable = true
        conditionPop.showAsDropDown(container_condotion_gift, 0, 8)
    }

    inner class ConditionHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            itemView.tv_item_condition.text = conditionList[position]
        }

    }

}