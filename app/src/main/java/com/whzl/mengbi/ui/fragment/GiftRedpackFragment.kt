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
import kotlinx.android.synthetic.main.fragment_gift_redpack.*
import kotlinx.android.synthetic.main.item_condition_redpack.view.*
import kotlinx.android.synthetic.main.item_goods_redpack.view.*
import kotlinx.android.synthetic.main.pop_condition_gift_reapack.view.*
import kotlinx.android.synthetic.main.pop_good_gift_reapack.view.*

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class GiftRedpackFragment : BaseFragment<BasePresenter<BaseView>>() {
    //参与条件
    private var conditionGoodList = ArrayList<RedpackGoodInfoBean.ConditionGoodListBean>()
    //礼物配置
    private var prizeGoodsList = ArrayList<RedpackGoodInfoBean.PrizeGoodsListBean>()

    private lateinit var goodsPop: PopupWindow
    private lateinit var conditionPop: PopupWindow

    override fun getLayoutId(): Int {
        return R.layout.fragment_gift_redpack
    }

    @SuppressLint("SetTextI18n")
    override fun init() {
        val goodInfoBean = arguments?.getParcelable<RedpackGoodInfoBean>("data")
        prizeGoodsList.addAll(goodInfoBean?.prizeGoodsList!!)
        conditionGoodList.addAll(goodInfoBean.conditionGoodList)

        if (prizeGoodsList[0] != null) {
            tv_prize_good_gift.text = "${prizeGoodsList[0].goodsName}（${prizeGoodsList[0].goodsPrice}萌币）"
            et_goods_num_gift.setText(prizeGoodsList[0].minNum.toString(), TextView.BufferType.NORMAL)
            tv_amount_gift.text = AmountConversionUitls.amountConversionFormat(
                    prizeGoodsList[0].goodsPrice * prizeGoodsList[0].minNum.toLong()
            )
        }

        if (conditionGoodList[0] != null) {
            tv_condition_gift.text = conditionGoodList[0].goodsName
        }

        container_good_gift.clickDelay {
            showGoodsPopWindow()
        }

        container_condition_gift.clickDelay {
            showConditionPopWindow()
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