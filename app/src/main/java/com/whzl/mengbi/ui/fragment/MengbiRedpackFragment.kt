package com.whzl.mengbi.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.RedpackGoodInfoBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.AmountConversionUitls
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.clickDelay
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_mengbi_redpack.*
import kotlinx.android.synthetic.main.item_condition_redpack.view.*
import kotlinx.android.synthetic.main.pop_condition_gift_reapack.view.*
import java.util.concurrent.TimeUnit

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class MengbiRedpackFragment : BaseFragment<BasePresenter<BaseView>>() {
    //参与条件
    private var conditionGoodList = ArrayList<RedpackGoodInfoBean.ConditionGoodListBean>()

    private lateinit var conditionPop: PopupWindow

    override fun getLayoutId(): Int {
        return R.layout.fragment_mengbi_redpack
    }

    @SuppressLint("CheckResult")
    override fun init() {
        val goodInfoBean = arguments?.getParcelable<RedpackGoodInfoBean>("data")
        conditionGoodList.addAll(goodInfoBean?.conditionGoodList!!)
        if (conditionGoodList.isNotEmpty()) {
            tv_condition_mengbi.text = conditionGoodList[0].goodsName
        }

        container_condition_mengbi.clickDelay {
            showConditionPopWindow()
        }

        et_mengbi.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_mengbi.text.isEmpty() || et_people_mengbi.text.isEmpty()) {
                return@subscribe
            }
            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
                    et_mengbi.text.toString().toLong().times(et_people_mengbi.text.toString().toInt()))
        }

        et_people_mengbi.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_people_mengbi.text.isEmpty() || et_people_mengbi.text.toString().toInt() < 1) {
                et_people_mengbi.setText("1", TextView.BufferType.NORMAL)
            }
            if (et_mengbi.text.isEmpty() || et_people_mengbi.text.isEmpty()) {
                return@subscribe
            }
            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
                    et_mengbi.text.toString().toLong().times(et_people_mengbi.text.toString().toInt()))
        }

        et_condition_num_mengbi.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_condition_num_mengbi.text.isEmpty() || et_people_mengbi.text.toString().toInt() < 1) {
                et_condition_num_mengbi.setText("1", TextView.BufferType.NORMAL)
            }
        }
    }


    @SuppressLint("InflateParams")
    private fun showConditionPopWindow() {
        val popView = LayoutInflater.from(activity).inflate(R.layout.pop_condition_gift_reapack, null)
        popView.rv_condition_gift_redpack.layoutManager = LinearLayoutManager(activity)
        popView.rv_condition_gift_redpack.adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return conditionGoodList.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_condition_redpack, parent, false)
                return ConditionHolder(inflate)
            }

        }
        conditionPop = PopupWindow(popView, UIUtil.dip2px(activity, 205.5f),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        conditionPop.setBackgroundDrawable(BitmapDrawable())
        conditionPop.isOutsideTouchable = true
        conditionPop.isFocusable = true
        conditionPop.showAsDropDown(container_condition_mengbi, 0, 8)
    }

    inner class ConditionHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            itemView.tv_item_condition.text = conditionGoodList[position].goodsName
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            conditionPop.dismiss()
            tv_condition_mengbi.text = conditionGoodList[position].goodsName
        }
    }

    companion object {
        fun newInstance(t: RedpackGoodInfoBean): MengbiRedpackFragment {
            val mengbiRedpackFragment = MengbiRedpackFragment()
            val bundle = Bundle()
            bundle.putParcelable("data", t)
            mengbiRedpackFragment.arguments = bundle
            return mengbiRedpackFragment
        }
    }
}