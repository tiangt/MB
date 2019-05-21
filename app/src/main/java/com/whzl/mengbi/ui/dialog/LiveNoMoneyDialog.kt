package com.whzl.mengbi.ui.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SDKConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.eventbus.event.QuickPayEvent
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.model.entity.QuickChannelBean
import com.whzl.mengbi.model.entity.RechargeOrderBean
import com.whzl.mengbi.model.entity.UserInfo
import com.whzl.mengbi.model.pay.WXPayOrder
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.AmountConversionUitls
import com.whzl.mengbi.util.GsonUtils
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import com.whzl.mengbi.util.toast
import com.whzl.mengbi.wxapi.WXPayEntryActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_live_no_money.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author nobody
 * @date 2019/4/9
 */
class LiveNoMoneyDialog : BaseAwesomeDialog() {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(quickPayEvent: QuickPayEvent) {
        onResp(quickPayEvent.baseResp)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UserInfoUpdateEvent) {
        getUserInfo()
    }

    private fun onResp(baseResp: BaseResp?) {

        when (baseResp?.errCode) {
            NetConfig.CODE_WE_CHAT_PAY_SUCCESS -> {
                toast(activity, getString(R.string.pay_success))
                EventBus.getDefault().post(UserInfoUpdateEvent())
                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, true)
            }
            NetConfig.CODE_WE_CHAT_PAY_CANCEL -> {
                toast(activity, getString(R.string.user_cancel))
            }
            NetConfig.CODE_WE_CHAT_PAY_FAIL -> {
                toast(activity, getString(R.string.pay_fail))
            }
            else -> {
                toast(activity, getString(R.string.pay_fail))
            }
        }

    }


    private var channelMap = HashMap<Int, Int>()
    private var ruleList = ArrayList<QuickChannelBean.RuleListBean>()
    private lateinit var adapter: BaseListAdapter
    private var rulePosition: Int = 0
    private var wxApi: IWXAPI? = null


    companion object {
        fun newInstance(): LiveNoMoneyDialog {
            val liveNoMoneyDialog = LiveNoMoneyDialog()
            val bundle = Bundle()
            liveNoMoneyDialog.arguments = bundle
            return liveNoMoneyDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_live_no_money
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        wxApi = WXAPIFactory.createWXAPI(activity, SDKConfig.KEY_WEIXIN)

        initRv(rv_no_money)
        initEvent(holder)
        loadData()
    }

    private fun initEvent(holder: ViewHolder?) {
        holder?.setOnClickListener(R.id.ib_wechat_no_money, {
            val findRuleId = findRuleId(NetConfig.FLAG_WECHAT_PAY)
            if (findRuleId == -1) {
                jumpToWXActivity()
            } else {
                getOrderInfo(R.id.ib_wechat_no_money, channelMap[R.id.ib_wechat_no_money].toString(), findRuleId.toString())
            }
        })

        holder?.setOnClickListener(R.id.ib_alipay_no_money, {
            val findRuleId = findRuleId(NetConfig.FLAG_ALI_PAY)
            if (findRuleId == -1) {
                jumpToWXActivity()
            } else {
                getOrderInfo(R.id.ib_alipay_no_money, channelMap[R.id.ib_alipay_no_money].toString(), findRuleId.toString())
            }
        })
    }

    private fun getOrderInfo(id: Int, channelId: String, ruleId: String) {
        val mUserId = SPUtils.get(activity, "userId", 0.toLong())!!.toString()
        val paramsMap = mutableMapOf<String, String>()
        paramsMap["channelId"] = channelId
        paramsMap["ruleId"] = ruleId
        paramsMap["userId"] = mUserId
        paramsMap["toUserId"] = mUserId
        paramsMap["proxyUserId"] = "0"
        paramsMap["quickRecharge"] = "T"
        ApiFactory.getInstance().getApi(Api::class.java)
                .rechargeOrder(ParamsUtils.getSignPramsMap(paramsMap as java.util.HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<RechargeOrderBean>(this) {
                    override fun onSuccess(t: RechargeOrderBean?) {
                        if (id == R.id.ib_alipay_no_money) {
                            aliPayAuth(t?.token)
                        } else {
                            val wxPayOrder = GsonUtils.GsonToBean(t?.token, WXPayOrder::class.java)
                            val payReq = PayReq()
                            payReq.appId = wxPayOrder!!.appid
                            payReq.partnerId = wxPayOrder.partnerid
                            payReq.prepayId = wxPayOrder.prepayid
                            payReq.packageValue = wxPayOrder.packageX
                            payReq.nonceStr = wxPayOrder.noncestr
                            payReq.timeStamp = wxPayOrder.timestamp
                            payReq.sign = wxPayOrder.sign
                            payReq.extData = "quick"
                            wxApi?.sendReq(payReq)
                        }
                    }

                })
    }

    /**
     * aliPay
     */
    private fun aliPayAuth(token: String?) {
        Observable.just(token)
                .map { s ->
                    val paytask = PayTask(activity)
                    paytask.payV2(s, true)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resultMap ->
                    val resultCode = Integer.parseInt(resultMap["resultStatus"])
                    when (resultCode) {
                        NetConfig.CODE_ALI_PAY_SUCCESS -> {
                            toast(activity, getString(R.string.pay_success))
                            EventBus.getDefault().post(UserInfoUpdateEvent())
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, true)
                        }
                        NetConfig.CODE_ALI_PAY_CANCEL -> {
                            toast(activity, getString(R.string.user_cancel))
                        }
                        else -> {
                            toast(activity, getString(R.string.pay_fail))
                        }
                    }
                }
    }

    private fun findRuleId(Flag: String): Int {
        val list = ruleList[rulePosition].list
        for (it in list) {
            if (it.channelFlag == Flag) {
                return it.ruleId
            }
        }
        return -1
    }


    private fun initRv(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = GridLayoutManager(activity, 4)
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return ruleList.size + 1
            }

            override fun getItemViewType(position: Int): Int {
                return if (position == ruleList.size) {
                    2
                } else {
                    1
                }
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                return if (viewType == 1) {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.item_rule_no_money, parent, false)
                    ListViewHolder(inflate)
                } else {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.foot_rule_no_money, parent, false)
                    FootViewHolder(inflate)
                }
            }

        }
        recyclerView?.adapter = adapter
    }


    internal inner class ListViewHolder(view: View) : BaseViewHolder(view) {
        private var tvDes: TextView? = null
        private var tvCost: TextView? = null
        private var llRuleContain: LinearLayout? = null

        init {
            tvDes = view.findViewById(R.id.tv_desc_no_money)
            tvCost = view.findViewById(R.id.tv_cost_no_money)
            llRuleContain = view.findViewById(R.id.ll_rule_container)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            val ruleListBean = ruleList[position]
            tvDes?.text = AmountConversionUitls.amountConversionFormat(ruleListBean.chengCount.toDouble())
                    tvDes ?. append (LightSpanString.getLightString(getString(R.string.mengbi_unit), Color.parseColor("#000000")))
                    tvCost ?. text = "${ruleListBean.chargeCount / 100}元"

            llRuleContain?.isSelected = rulePosition == position
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            rulePosition = position
            adapter.notifyDataSetChanged()
        }
    }

    internal inner class FootViewHolder(view: View) : BaseViewHolder(view) {
        override fun onBindViewHolder(position: Int) {
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            jumpToWXActivity()
        }
    }

    private fun jumpToWXActivity() {
        startActivity(Intent(activity, WXPayEntryActivity::class.java))
        dismissDialog()
    }

    private fun loadData() {
        getUserInfo()
        getChannelInfo()
    }

    private fun getUserInfo() {
        val paramsMap = mutableMapOf<String, String>()
        paramsMap["userId"] = SPUtils.get(activity, "userId",
                0.toLong())!!.toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .getUserInfo(ParamsUtils.getSignPramsMap(paramsMap as java.util.HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<UserInfo.DataBean>() {
                    override fun onSuccess(bean: UserInfo.DataBean) {
                        val coin = bean.wealth?.coin
                        tv_yue_no_money.text = LightSpanString.getLightString(coin?.toString(), Color.parseColor("#f4545a"))
                        tv_yue_no_money.append("萌币")
                    }

                    override fun onError(body: ApiResult<UserInfo.DataBean>) {
                    }

                })
    }

    private fun getChannelInfo() {
        val paramsMap = mutableMapOf<String, String>()
        ApiFactory.getInstance().getApi(Api::class.java)
                .quickChannel(ParamsUtils.getSignPramsMap(paramsMap as java.util.HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<QuickChannelBean>(this) {
                    override fun onSuccess(t: QuickChannelBean?) {
                        if (t?.channelList == null || t.ruleList == null) return
                        ruleList.addAll(t.ruleList!!)
                        adapter.notifyDataSetChanged()

                        for (i in t.channelList!!) {
                            if (i.channelFlag == NetConfig.FLAG_WECHAT_PAY) {
                                ib_wechat_no_money.visibility = View.VISIBLE
                                channelMap[R.id.ib_wechat_no_money] = i.channelId
                            }
                            if (i.channelFlag == NetConfig.FLAG_ALI_PAY) {
                                ib_alipay_no_money.visibility = View.VISIBLE
                                channelMap[R.id.ib_alipay_no_money] = i.channelId
                            }
                        }
                    }

                })
    }


    override fun onDestroy() {
        super.onDestroy()
        wxApi?.unregisterApp()
        EventBus.getDefault().unregister(this)
    }

}