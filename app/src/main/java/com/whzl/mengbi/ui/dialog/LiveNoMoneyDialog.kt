package com.whzl.mengbi.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.alibaba.fastjson.JSON
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.model.entity.RechargeInfo
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.LogUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.network.RequestManager
import com.whzl.mengbi.util.network.URLContentUtils
import com.whzl.mengbi.wxapi.WXPayEntryActivity
import kotlinx.android.synthetic.main.dialog_live_no_money.*
import kotlinx.android.synthetic.main.popwindow_payway_no_money.view.*

/**
 *
 * @author nobody
 * @date 2019/4/9
 */
class LiveNoMoneyDialog : BaseAwesomeDialog() {
    private var channelId: Int? = 0
    private var channelMap = HashMap<String, Int>()
    private val WECHAT = "wechat"
    private val ALIPAY = "alipay"

    companion object {
        fun newInstance(coin: Long): LiveNoMoneyDialog {
            val liveNoMoneyDialog = LiveNoMoneyDialog()
            val bundle = Bundle()
            bundle.putLong("coin", coin)
            liveNoMoneyDialog.arguments = bundle
            return liveNoMoneyDialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_live_no_money
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val coin = arguments?.getLong("coin")

        tv_yue_no_money.text = LightSpanString.getLightString(coin?.toString(), Color.parseColor("#f4545a"))
        tv_yue_no_money.append("萌币")

        tv_payway_no_money.setOnClickListener {
            showPatWayWindow()
        }

        tv_more_no_money.setOnClickListener {
            startActivity(Intent(activity, WXPayEntryActivity::class.java))
        }
        loadData()
    }

    private fun loadData() {
        getChannelInfo()
    }

    private fun getChannelInfo() {
        val hashMap = HashMap<String, String>()
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECHARGE_GET_CHANNEL, RequestManager.TYPE_POST_JSON, hashMap,
                object : RequestManager.ReqCallBack<Any> {
                    override fun onReqSuccess(result: Any) {
                        val rechargeInfo = JSON.parseObject(result.toString(), RechargeInfo::class.java)
                        if (rechargeInfo.code == RequestManager.RESPONSE_CODE) {
                            if (rechargeInfo?.data != null && rechargeInfo.data.channelList != null) {
                                val channelList = rechargeInfo.data.channelList
                                for (i in channelList.indices) {
                                    val rechargeChannelListBean = channelList[i]
                                    if (NetConfig.FLAG_ACTIVE != rechargeChannelListBean.status) {
                                        continue
                                    }
                                    if (NetConfig.FLAG_WECHAT_PAY == rechargeChannelListBean.channelFlag) {
                                        tv_payway_no_money.text = "微信"
                                        channelId = rechargeChannelListBean.channelId
                                        channelMap[WECHAT] = rechargeChannelListBean.channelId
                                    }
                                    if (NetConfig.FLAG_ALI_PAY == rechargeChannelListBean.channelFlag) {
                                        channelMap[ALIPAY] = rechargeChannelListBean.channelId
                                        if (TextUtils.isEmpty(tv_payway_no_money.text)) {
                                            tv_payway_no_money.text = "支付宝"
                                            channelId = rechargeChannelListBean.channelId
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onReqFailed(errorMsg: String) {
                        LogUtils.d(errorMsg)
                    }
                })
    }

    private fun showPatWayWindow() {
        val popView = layoutInflater.inflate(R.layout.popwindow_payway_no_money, null)
        val popupWindow = PopupWindow(popView, UIUtil.dip2px(activity, 85f),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        if (channelMap.containsKey(ALIPAY)) {
            popView.tv_alipay.visibility = View.VISIBLE
        }

        if (channelMap.containsKey(WECHAT)) {
            popView.tv_wechat.visibility = View.VISIBLE
        }
        popView.tv_wechat.setOnClickListener {
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
            channelId = channelMap[WECHAT]
            tv_payway_no_money.text = "微信"
        }
        popView.tv_alipay.setOnClickListener {
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
            channelId = channelMap[ALIPAY]
            tv_payway_no_money.text = "支付宝"
        }
        popupWindow.showAsDropDown(tv_payway_no_money, 0, -tv_payway_no_money.measuredHeight)
    }
}