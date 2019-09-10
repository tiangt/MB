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
import kotlinx.android.synthetic.main.fragment_gift_redpack.*
import kotlinx.android.synthetic.main.item_condition_redpack.view.*
import kotlinx.android.synthetic.main.item_goods_redpack.view.*
import kotlinx.android.synthetic.main.pop_condition_gift_reapack.view.*
import kotlinx.android.synthetic.main.pop_good_gift_reapack.view.*
import java.util.concurrent.TimeUnit

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class GiftRedpackFragment : BaseFragment<BasePresenter<BaseView>>() {
    private var currentGood: RedpackGoodInfoBean.PrizeGoodsListBean? = null
    //参与条件
    private var conditionGoodList = ArrayList<RedpackGoodInfoBean.ConditionGoodListBean>()
    //礼物配置
    private var prizeGoodsList = ArrayList<RedpackGoodInfoBean.PrizeGoodsListBean>()

    private lateinit var goodsPop: PopupWindow
    private lateinit var conditionPop: PopupWindow

    override fun getLayoutId(): Int {
        return R.layout.fragment_gift_redpack
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun init() {
        val goodInfoBean = arguments?.getParcelable<RedpackGoodInfoBean>("data")
        prizeGoodsList.addAll(goodInfoBean?.prizeGoodsList!!)
        conditionGoodList.addAll(goodInfoBean.conditionGoodList)

        if (prizeGoodsList.isNotEmpty()) {
            currentGood = prizeGoodsList[0]
            tv_prize_good_gift.text = "${prizeGoodsList[0].goodsName}（${prizeGoodsList[0].goodsPrice}萌币）"
            et_goods_num_gift.setText(prizeGoodsList[0].minNum.toString(), TextView.BufferType.NORMAL)
            tv_amount_gift.text = AmountConversionUitls.amountConversionFormat(
                    prizeGoodsList[0].goodsPrice * prizeGoodsList[0].minNum.toLong()
            )
        }

        if (conditionGoodList.isNotEmpty()) {
            tv_condition_gift.text = conditionGoodList[0].goodsName
        }

        container_good_gift.clickDelay {
            showGoodsPopWindow()
        }

        container_condition_gift.clickDelay {
            showConditionPopWindow()
        }

        et_goods_num_gift.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_goods_num_gift.text.isEmpty() || et_goods_num_gift.text.toString().toInt() < currentGood?.minNum!!) {
                et_goods_num_gift.setText(currentGood?.minNum.toString(), TextView.BufferType.NORMAL)
            }
            if (et_goods_num_gift.text.toString().toInt() % currentGood?.multipleNum!! != 0) {
                val i = et_goods_num_gift.text.toString().toInt() / currentGood?.multipleNum!!
                et_goods_num_gift.setText((i * currentGood?.multipleNum!!).toString(), TextView.BufferType.NORMAL)
            } else {
                tv_amount_gift.text = AmountConversionUitls.amountConversionFormat(
                        currentGood?.goodsPrice!!.times(et_goods_num_gift.text.toString().toInt()))
            }

            if (et_people_gift.text.toString().toInt() > et_goods_num_gift.text.toString().toInt()) {
                et_people_gift.setText(et_goods_num_gift.text.toString(), TextView.BufferType.NORMAL)
            }
        }

        et_people_gift.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_people_gift.text.isEmpty() || et_people_gift.text.toString().toInt() < 1) {
                et_people_gift.setText("1", TextView.BufferType.NORMAL)
            }
            if (et_people_gift.text.toString().toInt() > et_goods_num_gift.text.toString().toInt()) {
                et_people_gift.setText(et_goods_num_gift.text.toString(), TextView.BufferType.NORMAL)
            }
        }

        et_condition_num_gift.afterTextChangeEvents().debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (et_condition_num_gift.text.isEmpty() || et_condition_num_gift.text.toString().toInt() < 1) {
                et_condition_num_gift.setText("1", TextView.BufferType.NORMAL)
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
        conditionPop.showAsDropDown(container_condition_gift, 0, 8)
    }

    inner class ConditionHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            itemView.tv_item_condition.text = conditionGoodList[position].goodsName
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            conditionPop.dismiss()
            tv_condition_gift.text = conditionGoodList[position].goodsName
        }
    }

    @SuppressLint("InflateParams")
    private fun showGoodsPopWindow() {
        val popView = LayoutInflater.from(activity).inflate(R.layout.pop_good_gift_reapack, null)
        popView.rv_good_gift_redpack.layoutManager = LinearLayoutManager(activity)
        popView.rv_good_gift_redpack.adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return prizeGoodsList.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_goods_redpack, parent, false)
                return GoodsHolder(inflate)
            }

        }
        goodsPop = PopupWindow(popView, UIUtil.dip2px(activity, 290f),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        goodsPop.setBackgroundDrawable(BitmapDrawable())
        goodsPop.isOutsideTouchable = true
        goodsPop.isFocusable = true
        goodsPop.showAsDropDown(container_good_gift, 0, 8)
    }

    inner class GoodsHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            itemView.tv_item_goods.text = prizeGoodsList[position].goodsName
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            currentGood = prizeGoodsList[position]
            goodsPop.dismiss()
            tv_prize_good_gift.text = "${prizeGoodsList[position].goodsName}（${prizeGoodsList[position].goodsPrice}萌币）"
            et_goods_num_gift.setText(prizeGoodsList[position].minNum.toString(), TextView.BufferType.NORMAL)
            tv_amount_gift.text = AmountConversionUitls.amountConversionFormat(
                    prizeGoodsList[position].goodsPrice * prizeGoodsList[position].minNum
            )
        }
    }

    companion object {
        fun newInstance(t: RedpackGoodInfoBean): GiftRedpackFragment {
            val giftRedpackFragment = GiftRedpackFragment()
            val bundle = Bundle()
            bundle.putParcelable("data", t)
            giftRedpackFragment.arguments = bundle
            return giftRedpackFragment
        }
    }

}