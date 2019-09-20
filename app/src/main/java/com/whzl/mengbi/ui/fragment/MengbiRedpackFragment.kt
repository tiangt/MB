package com.whzl.mengbi.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.google.gson.JsonElement
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.RedpackGoodInfoBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.SendRedpacketListener
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.*
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private lateinit var sendRedpacketListener: SendRedpacketListener
    private var currentCondition: RedpackGoodInfoBean.ConditionGoodListBean? = null
    //参与条件
    private var conditionGoodList = ArrayList<RedpackGoodInfoBean.ConditionGoodListBean>()

    private lateinit var conditionPop: PopupWindow

    private var minCoinNum = 0L
    private var interval = 800L

    override fun getLayoutId(): Int {
        return R.layout.fragment_mengbi_redpack
    }

    @SuppressLint("CheckResult")
    override fun init() {
        val goodInfoBean = arguments?.getParcelable<RedpackGoodInfoBean>("data")
        val programId = arguments?.getInt("programId")
        minCoinNum = goodInfoBean?.minCoinNum!!
        et_mengbi.setText(minCoinNum.toString())

        conditionGoodList.addAll(goodInfoBean?.conditionGoodList!!)
        if (conditionGoodList.isNotEmpty()) {
            currentCondition = conditionGoodList[0]
            tv_condition_mengbi.text = conditionGoodList[0].goodsName
        }


        container_condition_mengbi.clickDelay {
            showConditionPopWindow()
        }

        et_mengbi.afterTextChangeEvents().debounce(interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (TextUtils.isEmpty(et_mengbi.text) || et_mengbi.text.toString().toLong() < minCoinNum) {
                et_mengbi.setText(minCoinNum.toString())
            }
            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
                    et_mengbi.text.toString().toLong())
        }

        et_people_mengbi.afterTextChangeEvents().debounce(interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (TextUtils.isEmpty(et_people_mengbi.text) || et_people_mengbi.text.toString().toLong() < 1) {
                et_people_mengbi.setText("1", TextView.BufferType.NORMAL)
            }
            if (TextUtils.isEmpty(et_mengbi.text) || et_people_mengbi.text.isEmpty()) {
                return@subscribe
            }
//            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
//                    et_mengbi.text.toString().toLong().times(et_people_mengbi.text.toString().toInt()))
        }

        et_condition_num_mengbi.afterTextChangeEvents().debounce(interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (TextUtils.isEmpty(et_condition_num_mengbi.text) || et_people_mengbi.text.toString().toLong() < 1) {
                et_condition_num_mengbi.setText("1", TextView.BufferType.NORMAL)
            }
        }

        btn_mengbi.clickDelay {
            val userId = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L) as Long
            if (currentCondition?.goodsType == "GOODS") {
                sendRedpack(userId, programId, "COIN", currentCondition?.conditionGoodsCfgId, et_mengbi.text.toString(),
                        et_condition_num_mengbi.text.toString().toLong(),
                        0, et_people_mengbi.text.toString().toLong())
            } else {
                sendRedpack(userId, programId, "COIN", currentCondition?.conditionGoodsCfgId, et_mengbi.text.toString(),
                        0,
                        et_condition_num_mengbi.text.toString().toLong(), et_people_mengbi.text.toString().toLong())
            }
        }
    }

    private fun sendRedpack(userId: Long, programId: Int?, type: String, conditionGoodsCfgId: Int?, prizeTotalPrize: String, conditionGoodsNum: Long, conditionPrize: Long, awardPeopleNum: Long) {
        val params = HashMap<String, String>()
        params.put("userId", userId.toString())
        params.put("programId", programId.toString())
        params.put("prizeGoodsType", type)
        params.put("conditionGoodsCfgId", conditionGoodsCfgId.toString())
        params.put("prizeTotalPrize", prizeTotalPrize)
        params.put("conditionGoodsNum", conditionGoodsNum.toString())
        params.put("conditionPrize", conditionPrize.toString())
        params.put("awardPeopleNum", awardPeopleNum.toString())
        ApiFactory.getInstance()
                .getApi(Api::class.java)
                .sendGameRedpack(ParamsUtils.getSignPramsMap(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>() {
                    override fun onSuccess(t: JsonElement?) {
                        toast(activity, "发起成功")
                        sendRedpacketListener.sendRedpacketSuccess()
                    }
                })
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

    fun setListener(sendRedpacketListener: SendRedpacketListener) {
        this.sendRedpacketListener = sendRedpacketListener
    }

    inner class ConditionHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            itemView.tv_item_condition.text = conditionGoodList[position].goodsName
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            currentCondition = conditionGoodList[position]
            conditionPop.dismiss()
            tv_condition_mengbi.text = conditionGoodList[position].goodsName
        }
    }

    companion object {
        fun newInstance(t: RedpackGoodInfoBean, programId: Int): MengbiRedpackFragment {
            val mengbiRedpackFragment = MengbiRedpackFragment()
            val bundle = Bundle()
            bundle.putParcelable("data", t)
            bundle.putInt("programId", programId)
            mengbiRedpackFragment.arguments = bundle
            return mengbiRedpackFragment
        }
    }

}