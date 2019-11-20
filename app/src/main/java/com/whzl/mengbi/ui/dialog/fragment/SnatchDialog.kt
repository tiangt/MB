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
import com.whzl.mengbi.chat.room.message.events.RobLuckChangeEvent
import com.whzl.mengbi.chat.room.message.events.RobNoPrizeEvent
import com.whzl.mengbi.chat.room.message.events.RobPrizeEvent
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.eventbus.event.MengdouChangeEvent
import com.whzl.mengbi.model.entity.GiftBetPeriodInfo
import com.whzl.mengbi.model.entity.GiftBetRecordsBean
import com.whzl.mengbi.model.entity.UserInfo
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.*
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
    private var disposable: Disposable? = null
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
    private var ibReduce: ImageButton? = null
    private lateinit var ibAdd: ImageButton
    private var tvWant: TextView? = null
    private var mUserId: Long = 0
    private var tvMengdou: TextView? = null
    private lateinit var hisAdapter: BaseListAdapter
    private var hisDatas = ArrayList<GiftBetRecordsBean.ListBean>()
    private var gameId: Int = 0
    private var limitTimes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }


    override fun intLayoutId(): Int {
        return R.layout.dialog_snatch
    }

    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
        mUserId = arguments!!.getLong("mUserId")
        initId(holder)
        loadData()
    }

    private fun initId(holder: ViewHolder) {
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
        tvMengdou = holder.getView(R.id.tv_mengdou)
        ibReduce = holder.getView(R.id.ib_reduce_want)
        ibAdd = holder.getView(R.id.ib_add_want)
        llStateNormal = holder.getView(R.id.ll_state_normal)
        llStateProcess = holder.getView(R.id.ll_state_process)
        llStateEnd = holder.getView(R.id.ll_state_end)
        ibReduce?.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant!!.text.toString()) <= 1) {
                return@OnClickListener
            }
            tvWant?.text = (Integer.parseInt(tvWant!!.text.toString()) - 1).toString()
        })
        ibAdd.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant!!.text.toString()) >= 10) {
                return@OnClickListener
            }
            tvWant?.text = (Integer.parseInt(tvWant!!.text.toString()) + 1).toString()
        })
        holder.setOnClickListener(R.id.tv_five) { tvWant?.text = "5" }
        holder.setOnClickListener(R.id.tv_ten) { tvWant?.text = "10" }
//        holder.setOnClickListener(R.id.iv_mengdou_snatch) {
//            if (ClickUtil.isFastClick()) {
//                startActivity(Intent(activity, ShopActivity::class.java))
//            }
//        }
        tvHisPrize.setOnClickListener { showHisDialog(mUserId) }
        holder.setOnClickListener(R.id.tv_snatch) {
            if (limitTimes == 10) {
                return@setOnClickListener
            }
            snatch(mUserId.toString(), gameId.toString(), tvWant!!.text.toString())
        }

        tvTips1.text = getString(R.string.snatch_tips)
    }

    private fun snatch(userid: String, gameid: String, tvwant: String) {
        val paramsMap = HashMap<String, String>()
        paramsMap.put("userId", userid)
        paramsMap.put("gameId", gameid)
        paramsMap.put("robCount", tvwant)
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .giftBet(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>(this) {
                    override fun onSuccess(bean: JsonElement?) {
                        ToastUtils.showToastUnify(activity, "夺宝成功")
                        tvWant!!.text = "1"
                        loadData()
                    }

                    override fun onError(code: Int) {

                    }
                })
    }

    private fun showHisDialog(mUserId: Long) {
        dismiss()
        AwesomeDialog.init().setLayoutId(R.layout.dialog_snatch_his).setConvertListener(object : ViewConvertListener() {
            override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                val recyclerView = holder?.getView<RecyclerView>(R.id.rv_snatch_his)
                initRV(recyclerView)
                getHisData()
            }

        }).show(fragmentManager)
    }

    private fun getHisData() {
        val paramsMap = HashMap<String, String>()
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
        getMengdou()
    }

    private fun getData() {
        limitTimes = 0
        val paramsMap = HashMap<String, String>()
        paramsMap.put("userId", mUserId.toString())
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .giftBetPeriodInfo(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GiftBetPeriodInfo>(this) {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(bean: GiftBetPeriodInfo?) {
                        if ("PRIZEING" == bean?.robStatus) {
                            prizing(bean)
                            return
                        }
                        betting(bean)
                    }

                    override fun onError(code: Int) {
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    private fun betting(bean: GiftBetPeriodInfo?) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        llStateProcess.visibility = View.GONE
        llStateNormal.visibility = View.VISIBLE
        llStateEnd.visibility = View.GONE
        gameId = bean?.uRobGame?.id!!
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
        limitTimes = bean.userBetCount
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                    LogUtils.e("sssssssssss  $t")
                    if (t == bean.surplusSecond.toLong() + 1) {
                        disposable!!.dispose()
                        prizing(bean)
                        return@subscribe
                    }
                    tvSecond.text = DateUtils.translateLastSecond(bean.surplusSecond - t!!.toInt())
                }
    }

    private fun prizing(bean: GiftBetPeriodInfo?) {
        if (disposable != null) {
            disposable!!.dispose()
        }
        llStateProcess.visibility = View.VISIBLE
        llStateNormal.visibility = View.GONE
        llStateEnd.visibility = View.GONE
        Glide.with(this).asGif().load(R.drawable.daojishi).into(ivDaojishi)
        if (bean!!.surplusSecond == 0) {
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

    private fun getMengdou() {
        BusinessUtils.getUserInfo(activity, mUserId.toString() + "", object : BusinessUtils.UserInfoListener {
            override fun onSuccess(bean: UserInfo.DataBean?) {
                if (bean != null && bean.wealth != null) {
                    val mengDou = bean.wealth.mengDou
                    tvMengdou?.text = AmountConversionUitls.amountConversionFormat(mengDou)
                }
            }

            override fun onError(code: Int) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MengdouChangeEvent) {
        getMengdou()
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
//        ApngDrawable.getFromView(ivDaojishi).stop()
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
                    LogUtils.e("sssssssssss  " + t)
                    if (t == 4.toLong()) {
                        disposable!!.dispose()
                        getData()
                        return@subscribe
                    }
                    tvDaojishi.text = DateUtils.translateLastSecond(3 - t!!.toInt())
                }
        Glide.with(this).asGif().load(R.drawable.fangpao).into(ivFangpao)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
