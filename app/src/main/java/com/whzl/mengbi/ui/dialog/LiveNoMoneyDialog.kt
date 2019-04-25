package com.whzl.mengbi.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.model.entity.QuickChannelBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_live_no_money.*

/**
 *
 * @author nobody
 * @date 2019/4/9
 */
class LiveNoMoneyDialog : BaseAwesomeDialog() {
    private var channelMap = HashMap<String, Int>()
    private var ruleList = ArrayList<QuickChannelBean.RuleListBean>()
    private lateinit var adapter: BaseListAdapter


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
        initRv(rv_no_money)

        loadData()
    }


    private fun initRv(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = GridLayoutManager(context, 4)
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return ruleList.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(activity).inflate(R.layout.item_rule_no_money, parent, false)
                return ListViewHolder(inflate)
            }

        }
        recyclerView?.adapter = adapter
    }

    internal inner class ListViewHolder(view: View) : BaseViewHolder(view) {
        private var tvPosition: TextView? = null
        private var tvName: TextView? = null
        private var tvNum: TextView? = null

        init {

        }

        override fun onBindViewHolder(position: Int) {

        }
    }


    private fun loadData() {
        getChannelInfo()
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
                            }
                            if (i.channelFlag == NetConfig.FLAG_ALI_PAY) {
                                ib_alipay_no_money.visibility = View.VISIBLE
                            }
                        }
                    }

                })
    }


}