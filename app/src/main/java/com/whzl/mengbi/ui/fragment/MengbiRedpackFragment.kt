package com.whzl.mengbi.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.google.gson.JsonElement
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.model.entity.RedpackGoodInfoBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.SendRedpacketListener
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.*
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import com.whzl.mengbi.wxapi.WXPayEntryActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_mengbi_redpack.*
import kotlinx.android.synthetic.main.item_condition_redpack.view.*
import kotlinx.android.synthetic.main.pop_condition_gift_reapack.view.*

/**
 *
 * @author nobody
 * @date 2019-09-09
 */
class MengbiRedpackFragment : BaseFragment<BasePresenter<BaseView>>() {
    private var errorDialog: AwesomeDialog = AwesomeDialog()
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

        et_mengbi.afterTextChangeEvents().observeOn(AndroidSchedulers.mainThread()).subscribe {
            //            if (TextUtils.isEmpty(et_mengbi.text) || et_mengbi.text.toString().toLong() < minCoinNum) {
//                et_mengbi.setText(minCoinNum.toString())
//            }
            if (TextUtils.isEmpty(et_mengbi.text)) {
                tv_amount_mengbi.text = "0"
                return@subscribe
            }
            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
                    et_mengbi.text.toString().toLong())
        }

//        et_people_mengbi.afterTextChangeEvents().debounce(interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            if (TextUtils.isEmpty(et_people_mengbi.text) || et_people_mengbi.text.toString().toLong() < 1) {
//                et_people_mengbi.setText("1", TextView.BufferType.NORMAL)
//            }
//            if (TextUtils.isEmpty(et_mengbi.text) || et_people_mengbi.text.isEmpty()) {
//                return@subscribe
//            }
////            tv_amount_mengbi.text = AmountConversionUitls.amountConversionFormat(
////                    et_mengbi.text.toString().toLong().times(et_people_mengbi.text.toString().toInt()))
//        }
//
//        et_condition_num_mengbi.afterTextChangeEvents().debounce(interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            if (TextUtils.isEmpty(et_condition_num_mengbi.text) || et_people_mengbi.text.toString().toLong() < 1) {
//                et_condition_num_mengbi.setText("1", TextView.BufferType.NORMAL)
//            }
//        }

        btn_mengbi.clickDelay {
            if (checkSend()) {
                return@clickDelay
            }
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

    private fun checkSend(): Boolean {
        if (TextUtils.isEmpty(et_mengbi.text) || TextUtils.isEmpty(et_people_mengbi.text) || TextUtils.isEmpty(et_condition_num_mengbi.text)) {
            return true
        }
        if (et_mengbi.text.toString().toLong() < 10000) {
            toast(activity, "礼物数量不能小于10000")
            return true
        }

        if ((et_mengbi.text.toString().toLong() % 10000).toInt() != 0) {
            toast(activity, "礼物数量需为10000的整数倍")
            return true
        }

        if (et_people_mengbi.text.toString().toLong() > 50) {
            toast(activity, "中奖人数不能超过50人")
            return true
        }

        if (currentCondition?.goodsType == "GOODS" && et_condition_num_mengbi.text.toString().toLong() > 99) {
            toast(activity, "${currentCondition?.goodsName}数量不能超过99")
            return true
        }

        if (currentCondition?.goodsType == "COIN" && (et_mengbi.text.toString().toLong()) / 5 < et_condition_num_mengbi.text.toString().toLong()) {
            toast(activity, "参与资格不能大于奖品总萌币的五分之一")
            return true
        }

        return false
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

                    override fun onError(body: ApiResult<JsonElement>?) {
                        when (body?.code) {
                            //余额不足
                            -1211 -> {
                                if (errorDialog.isAdded) {
                                    return
                                }
                                errorDialog = AwesomeDialog.init()
                                errorDialog.setLayoutId(R.layout.dialog_simple).setConvertListener(object : ViewConvertListener() {
                                    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
                                        holder.setText(R.id.tv_content_simple_dialog, "您的萌币余额不足，是否现在充值？")
                                        holder.setText(R.id.btn_confirm_simple_dialog, "充值")
                                        holder.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                                            dialog.dismissDialog()
                                            val intent = Intent(activity, WXPayEntryActivity::class.java)
                                            activity?.startActivity(intent)
                                        }
                                        holder.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                                            dialog.dismissDialog()
                                        }
                                    }
                                }).setOutCancel(false).show(fragmentManager)
                            }
                            //节目未开播
                            -1135 -> {
                                if (errorDialog.isAdded) {
                                    return
                                }
                                errorDialog = AwesomeDialog.init()
                                errorDialog.setLayoutId(R.layout.dialog_simple).setConvertListener(object : ViewConvertListener() {
                                    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
                                        holder.setText(R.id.tv_content_simple_dialog, "当前主播未开播，无法发起红包抽奖")
                                        holder.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                                            dialog.dismissDialog()
                                        }
                                        holder.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                                            dialog.dismissDialog()
                                        }
                                    }
                                }).setOutCancel(false).show(fragmentManager)
                            }
                            //抽奖红包存在
                            -1282 -> {
                                if (errorDialog.isAdded) {
                                    return
                                }
                                errorDialog = AwesomeDialog.init()
                                errorDialog.setLayoutId(R.layout.dialog_simple).setConvertListener(object : ViewConvertListener() {
                                    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
                                        holder.setText(R.id.tv_content_simple_dialog, "当前房间已经有红包抽奖，等结束后再次发起")
                                        holder.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                                            dialog.dismissDialog()
                                        }
                                        holder.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                                            dialog.dismissDialog()
                                        }
                                    }
                                }).setOutCancel(false).show(fragmentManager)
                            }
                        }
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