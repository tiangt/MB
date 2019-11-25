package com.whzl.mengbi.ui.dialog.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.JsonElement
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.message.events.RobBigLuckyEvent
import com.whzl.mengbi.chat.room.message.events.RobLuckChangeEvent
import com.whzl.mengbi.chat.room.message.events.RobNoPrizeEvent
import com.whzl.mengbi.chat.room.message.events.RobPrizeEvent
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.model.entity.GiftBetPeriodList
import com.whzl.mengbi.model.entity.GiftBetRecordsBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.LogUtils
import com.whzl.mengbi.util.ToastUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_snatch_his.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.HashMap
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * @author nobody
 * @date 2019/3/6
 */
class SnatchDialog : BaseAwesomeDialog() {
    private var requireGiftNum = 0
    private var disposable: Disposable? = null
    private val LUCKBET = "LuckBet"
    private val ONEINHUNDRED = "OneInHundred"

    private lateinit var tvHisPrize: TextView
    private lateinit var tvDaojishi: TextView
    private lateinit var tvPrizeName: TextView
    private lateinit var tvPrizePoolNum: TextView
    private lateinit var tvTips1: TextView
    private lateinit var tvTips2: TextView
    private lateinit var tvLimit: TextView
    private lateinit var tvTimeLimit: TextView
    private lateinit var ivDaojishi: ImageView
    private lateinit var tvSecond: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivGift: ImageView
    private lateinit var ivFangpao: ImageView
    private lateinit var llStateNormal: LinearLayout
    private lateinit var llStateProcess: LinearLayout
    private lateinit var llStateEnd: RelativeLayout
    private lateinit var ibReduce: ImageButton
    private lateinit var ibAdd: ImageButton
    private lateinit var tvWant: TextView


    private lateinit var tvHisPrizeOne: TextView
    private lateinit var tvDaojishiOne: TextView
    private lateinit var tvPrizeNameOne: TextView
    private lateinit var tvPrizePoolNumOne: TextView
    private lateinit var tvLimitOne: TextView
    private lateinit var tvTimeLimitOne: TextView
    private lateinit var ivDaojishiOne: ImageView
    private lateinit var tvSecondOne: TextView
    private lateinit var tvDateOne: TextView
    private lateinit var ivGiftOne: ImageView
    private lateinit var ivFangpaoOne: ImageView
    private lateinit var llStateNormalOne: LinearLayout
    private lateinit var llStateProcessOne: LinearLayout
    private lateinit var llStateEndOne: RelativeLayout
    private lateinit var ibReduceOne: ImageButton
    private lateinit var ibAddOne: ImageButton
    private lateinit var tvWantOne: TextView

    private var mUserId: Long = 0
    private lateinit var hisAdapter: BaseListAdapter
    private var hisDatas = ArrayList<GiftBetRecordsBean.ListBean>()
    private var gameId: Int = 0
    private var gameIdOne: Int = 0
    private var userBetCount: Int = 0
    private var userBetCountOne: Int = 0
    private var totalLimit: Int = 0
    private var totalLimitOne: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }


    override fun intLayoutId(): Int {
        return R.layout.dialog_snatch
    }

    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
        mUserId = arguments!!.getLong("mUserId")
        initSnatch(holder)
        initOne(holder)
        loadData()
    }

    private fun initSnatch(holder: ViewHolder) {
        ivDaojishi = holder.getView(R.id.iv_daojishi)
        ivFangpao = holder.getView(R.id.iv_fangpao)
        tvDaojishi = holder.getView(R.id.tv_daojishi)
        tvTips1 = holder.getView(R.id.tv_tips_1)
        tvTips2 = holder.getView(R.id.tv_tips_2)
        tvPrizeName = holder.getView(R.id.tv_prize_name)
        tvHisPrize = holder.getView(R.id.tv_his_prize)
        tvPrizePoolNum = holder.getView(R.id.tv_prize_pool_num)
        tvSecond = holder.getView(R.id.tv_second)
        tvDate = holder.getView(R.id.tv_date_snatch)
        tvLimit = holder.getView(R.id.tv_price_limit)
        tvTimeLimit = holder.getView(R.id.tv_time_limit)
        ivGift = holder.getView(R.id.iv_gift_snatch)
        tvWant = holder.getView(R.id.tv_want_join)
        ibReduce = holder.getView(R.id.ib_reduce_want)
        ibAdd = holder.getView(R.id.ib_add_want)
        llStateNormal = holder.getView(R.id.ll_state_normal)
        llStateProcess = holder.getView(R.id.ll_state_process)
        llStateEnd = holder.getView(R.id.ll_state_end)
        ibReduce.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant.text.toString()) <= 1) {
                return@OnClickListener
            }
            tvWant.text = (Integer.parseInt(tvWant.text.toString()) - 1).toString()
        })
        ibAdd.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant.text.toString()) >= 10) {
                return@OnClickListener
            }
            tvWant.text = (Integer.parseInt(tvWant.text.toString()) + 1).toString()
        })
        holder.setOnClickListener(R.id.tv_five) { tvWant.text = "5" }
        holder.setOnClickListener(R.id.tv_ten) { tvWant.text = "10" }
        tvHisPrize.setOnClickListener { showHisDialog(LUCKBET) }
        holder.setOnClickListener(R.id.tv_snatch) {
            if (userBetCount == totalLimit) {
                return@setOnClickListener
            }
            snatch(mUserId.toString(), gameId.toString(), tvWant.text.toString(), LUCKBET)
        }

        tvTips1.text = getString(R.string.snatch_tips)
    }

    private fun initOne(holder: ViewHolder) {
        ivDaojishiOne = holder.getView(R.id.iv_daojishi_one)
        ivFangpaoOne = holder.getView(R.id.iv_fangpao_one)
        tvDaojishiOne = holder.getView(R.id.tv_daojishi_one)
        tvPrizeNameOne = holder.getView(R.id.tv_prize_name_one)
        tvHisPrizeOne = holder.getView(R.id.tv_his_one)
        tvPrizePoolNumOne = holder.getView(R.id.tv_prize_pool_num_one)
        tvSecondOne = holder.getView(R.id.tv_second_one)
        tvDateOne = holder.getView(R.id.tv_date_one)
        tvLimitOne = holder.getView(R.id.tv_price_limit_one)
        tvTimeLimitOne = holder.getView(R.id.tv_time_limit_one)
        ivGiftOne = holder.getView(R.id.iv_gift_one)
        tvWantOne = holder.getView(R.id.tv_want_join_one)
        ibReduceOne = holder.getView(R.id.ib_reduce_want_one)
        ibAddOne = holder.getView(R.id.ib_add_want_one)
        llStateNormalOne = holder.getView(R.id.ll_state_normal_one)
        llStateProcessOne = holder.getView(R.id.ll_state_process_one)
        llStateEndOne = holder.getView(R.id.ll_state_end_one)
        ibReduceOne.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWantOne.text.toString()) <= 1) {
                return@OnClickListener
            }
            tvWantOne.text = (Integer.parseInt(tvWantOne.text.toString()) - 1).toString()
        })
        ibAddOne.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWantOne.text.toString()) >= 10) {
                return@OnClickListener
            }
            tvWantOne.text = (Integer.parseInt(tvWantOne.text.toString()) + 1).toString()
        })
        holder.setOnClickListener(R.id.tv_five_one) { tvWantOne.text = "5" }
        holder.setOnClickListener(R.id.tv_ten_one) { tvWantOne.text = "10" }
        tvHisPrizeOne.setOnClickListener { showHisDialog(ONEINHUNDRED) }
        holder.setOnClickListener(R.id.tv_snatch_one) {
            if (userBetCountOne == totalLimitOne) {
                return@setOnClickListener
            }
            snatch(mUserId.toString(), gameIdOne.toString(), tvWantOne.text.toString(), ONEINHUNDRED)
        }

    }

    private fun snatch(userid: String, gameid: String, tvwant: String, type: String) {
        val paramsMap = HashMap<String, String>()
        paramsMap["userId"] = userid
        paramsMap["gameId"] = gameid
        paramsMap["robCount"] = tvwant
        paramsMap["betType"] = type
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .giftBet(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>(this) {
                    override fun onSuccess(bean: JsonElement?) {
                        ToastUtils.showToastUnify(activity, "夺宝成功")
                        if (type == LUCKBET) {
                            tvWant.text = "1"
                            val get = bean?.asJsonObject?.get("betCount")
                            tvTimeLimit.text = "已参与 "
                            tvTimeLimit.append(LightSpanString.getLightString("$get/$totalLimit",
                                    Color.parseColor("#FFFFED25")))
                            tvTimeLimit.append(" 次")
                        } else {
                            tvWantOne.text = "1"
                            val get = bean?.asJsonObject?.get("betCount")
                            tvTimeLimitOne.text = "已参与 "
                            tvTimeLimitOne.append(LightSpanString.getLightString("$get/$totalLimitOne",
                                    Color.parseColor("#FFFFED25")))
                            tvTimeLimitOne.append(" 次")
                        }
//                        loadData()
                    }

                    override fun onError(code: Int) {

                    }
                })
    }

    private fun showHisDialog(type: String) {
        dismiss()
        AwesomeDialog.init().setLayoutId(R.layout.dialog_snatch_his).setConvertListener(object : ViewConvertListener() {
            override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                val recyclerView = holder?.getView<RecyclerView>(R.id.rv_snatch_his)
                initRV(recyclerView)
                getHisData(type)
            }

        }).show(fragmentManager)
    }

    private fun getHisData(type: String) {
        val paramsMap = HashMap<String, String>()
        paramsMap["betType"] = type
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .giftBetRecords(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GiftBetRecordsBean>(this) {
                    override fun onSuccess(bean: GiftBetRecordsBean?) {
                        if (bean?.list == null) {
                            return
                        }
                        hisDatas.clear()
                        hisDatas.addAll(bean.list!!)
                        hisAdapter.notifyDataSetChanged()
                    }

                    override fun onError(code: Int) {
                    }
                })
    }

    private fun initRV(recyclerView: RecyclerView?) {
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        hisAdapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return 10
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_snatch_his, parent, false)
                return HisViewHolder(view)
            }

        }
        recyclerView.adapter = hisAdapter
    }

    internal inner class HisViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private lateinit var tvGood: TextView

        override fun onBindViewHolder(position: Int) {
            if (position >= hisDatas.size) {
                return
            }
            tvGood = itemView.findViewById<TextView>(R.id.tv_good_item_snatch)
            val bean = hisDatas[position]
            itemView.tv_nick_item_snatch.text = bean.nickName
            itemView.tv_date_item_snatch.text = bean.periodNumber
            itemView.tv_count_item_snatch.text = bean.robNumber.toString()

            tvGood.text = "${bean.goodsName}×${bean.prizeNumber}"
        }

    }

    private fun loadData() {
        getData()
    }

    private fun getData() {
        userBetCount = 0
        userBetCountOne = 0
        val paramsMap = HashMap<String, String>()
        paramsMap["userId"] = mUserId.toString()
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .giftBetPeriodList(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GiftBetPeriodList>(this) {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(bean: GiftBetPeriodList?) {
                        if ("PRIZEING" == bean?.LuckBet?.robStatus) {
                            prizing(bean.LuckBet)
                        } else if ("BETTING" == bean?.LuckBet?.robStatus) {
                            betting(bean.LuckBet)
                        }

                        bettingOne(bean?.OneInHundred)

                    }

                    override fun onError(code: Int) {
                    }
                })
    }

    private fun bettingOne(bean: GiftBetPeriodList.OneInHundredBean?) {
        llStateProcessOne.visibility = View.GONE
        llStateNormalOne.visibility = View.VISIBLE
        llStateEndOne.visibility = View.GONE
        gameIdOne = bean?.uRobGame?.id!!.toInt()
        tvDateOne.text = "${bean?.periodNumber}期"

        GlideImageLoader.getInstace().displayImage(activity, bean.goodsPic, ivGiftOne)

        tvPrizePoolNumOne.text = bean.prizePoolNumber.toString()

        tvLimitOne.text = "每次 "
        tvLimitOne.append(LightSpanString.getLightString("${bean.uRobGame?.amount}",
                Color.parseColor("#FFFFED25")))
        tvLimitOne.append(" 萌豆")

        tvTimeLimitOne.text = "已参与 "
        tvTimeLimitOne.append(LightSpanString.getLightString("${bean.userBetCount}/${bean.uRobGame?.limit}",
                Color.parseColor("#FFFFED25")))
        tvTimeLimitOne.append(" 次")
        userBetCountOne = bean.userBetCount.toInt()
        totalLimitOne = bean.uRobGame?.limit?.toInt() ?: 0
        requireGiftNum = bean.requireGiftNum.toInt()
        tvSecondOne.text = (requireGiftNum - bean.prizePoolNumber).toString()
    }

    @SuppressLint("SetTextI18n")
    private fun betting(bean: GiftBetPeriodList.LuckBetBean?) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        llStateProcess.visibility = View.GONE
        llStateNormal.visibility = View.VISIBLE
        llStateEnd.visibility = View.GONE
        gameId = bean?.uRobGame?.id!!.toInt()
        tvDate.text = "${bean.periodNumber}期"

        tvTips2.movementMethod = LinkMovementMethod.getInstance()
        tvTips2.text = "每个玩家每期最多可夺宝"
        tvTips2.append(LightSpanString.getLightString("${bean.uRobGame.limit}", Color.rgb(255, 237, 37)))
        tvTips2.append("次，夺宝次数越多中奖概率越大。")
        tvTips2.append(LightSpanString.getClickSpan(context, "中奖概率查看", Color.rgb(255, 237, 37), true, 10) {
            ToastUtils.showToast("sda")
        })

        GlideImageLoader.getInstace().displayImage(activity, bean.goodsPic, ivGift)

        tvPrizePoolNum.text = bean.prizePoolNumber.toString()

        tvLimit.text = "每次 "
        tvLimit.append(LightSpanString.getLightString("${bean.uRobGame?.amount}",
                Color.parseColor("#FFFFED25")))
        tvLimit.append(" 萌豆")

        tvTimeLimit.text = "已参与 "
        tvTimeLimit.append(LightSpanString.getLightString("${bean.userBetCount}/${bean.uRobGame?.limit}",
                Color.parseColor("#FFFFED25")))
        tvTimeLimit.append(" 次")
        userBetCount = bean.userBetCount.toInt()
        totalLimit = bean.uRobGame?.limit?.toInt() ?: 0
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                    LogUtils.e("sssssssssss  $t")
                    if (t == bean.surplusSecond.toLong() + 1) {
                        disposable!!.dispose()
                        prizing(bean)
                        return@subscribe
                    }
                    tvSecond.text = DateUtils.translateLastSecond((bean.surplusSecond - t!!.toInt()).toInt())
                }
    }

    private fun prizing(bean: GiftBetPeriodList.LuckBetBean) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        llStateProcess.visibility = View.VISIBLE
        llStateNormal.visibility = View.GONE
        llStateEnd.visibility = View.GONE
        Glide.with(this).asGif().load(R.drawable.daojishi).into(ivDaojishi)
        if (bean.surplusSecond == 0L) {
            return
        }
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                    if (t == 11.toLong()) {
                        disposable!!.dispose()
                        return@subscribe
                    }
                    tvDaojishi.text = DateUtils.translateLastSecond(10 - t!!.toInt())
                }
    }

    companion object {
        fun newInstance(mUserId: Long): SnatchDialog {
            val dialog = SnatchDialog()
            val bundle = Bundle()
            bundle.putLong("mUserId", mUserId)
            dialog.arguments = bundle
            return dialog
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RobLuckChangeEvent) {
        tvPrizePoolNum.text = event.robLuckJson.context.prizePoolNumber.toString()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RobNoPrizeEvent) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        getData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RobPrizeEvent) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        llStateNormal.visibility = View.GONE
        llStateProcess.visibility = View.GONE
        llStateEnd.visibility = View.VISIBLE
        tvPrizeName.text = event.robLuckJson.context.userNickName
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                    LogUtils.e("sssssssssss  $t")
                    if (t == 4.toLong()) {
                        disposable!!.dispose()
                        getData()
                        return@subscribe
                    }
                    tvDaojishi.text = DateUtils.translateLastSecond(3 - t!!.toInt())
                }
        Glide.with(this).asGif().load(R.drawable.fangpao).into(ivFangpao)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RobBigLuckyEvent) {
        when (event.robBigLuckyJson.context.busiCode) {
            "big_prize_pool_change" -> {
                tvPrizePoolNumOne.text = event.robBigLuckyJson.context.prizePoolNumber.toString()
                tvSecondOne.text = (requireGiftNum - event.robBigLuckyJson.context.prizePoolNumber).toString()
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        if (disposable != null) {
            disposable!!.dispose()
        }
        super.onDestroy()
    }
}
