package com.whzl.mengbi.ui.dialog.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.eventbus.event.MengdouChangeEvent
import com.whzl.mengbi.model.entity.GiftBetPeriodInfo
import com.whzl.mengbi.model.entity.GiftBetRecordsBean
import com.whzl.mengbi.model.entity.UserInfo
import com.whzl.mengbi.ui.activity.me.ShopActivity
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
import io.reactivex.functions.Consumer
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
    private lateinit var disposable: Disposable
    private lateinit var tvHisPrize: TextView
    private lateinit var tvPrizePoolNum: TextView
    private lateinit var tvLimit: TextView
    private lateinit var tvSecond: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivGift: ImageView
    private lateinit var llStateNormal: LinearLayout
    private lateinit var llStateProcess: LinearLayout
    private lateinit var llStateEnd: LinearLayout
    private var ibReduce: ImageButton? = null
    private lateinit var ibAdd: ImageButton
    private var tvWant: TextView? = null
    private var mUserId: Long = 0
    private var tvMengdou: TextView? = null
    private lateinit var hisAdapter: BaseListAdapter
    private var hisDatas = ArrayList<GiftBetRecordsBean.ListBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        disposable.dispose()
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_snatch
    }

    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
        mUserId = arguments!!.getLong("mUserId")

        tvHisPrize = holder.getView(R.id.tv_his_prize)
        tvPrizePoolNum = holder.getView(R.id.tv_prize_pool_num)
        tvSecond = holder.getView(R.id.tv_second)
        tvDate = holder.getView(R.id.tv_date_snatch)
        tvLimit = holder.getView(R.id.tv_limit)
        ivGift = holder.getView(R.id.iv_gift_snatch)
        tvHisPrize!!.paint.flags = Paint.UNDERLINE_TEXT_FLAG
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
        ibAdd!!.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant!!.text.toString()) >= 10) {
                return@OnClickListener
            }
            tvWant?.text = (Integer.parseInt(tvWant!!.text.toString()) + 1).toString()
        })
        holder.setOnClickListener(R.id.tv_five) { tvWant?.text = "5" }
        holder.setOnClickListener(R.id.tv_ten) { tvWant?.text = "10" }
        holder.setOnClickListener(R.id.iv_mengdou_snatch) {
            if (ClickUtil.isFastClick()) {
                startActivity(Intent(activity, ShopActivity::class.java))
            }
        }
        tvHisPrize.setOnClickListener { showHisDialog(mUserId) }
        loadData()
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
                return hisDatas.size
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
            tvGood = itemView.findViewById<TextView>(R.id.tv_good_item_snatch)
            val bean = hisDatas[position]
            itemView.tv_nick_item_snatch.text = bean.nickName
            itemView.tv_date_item_snatch.text = bean.periodNumber
            itemView.tv_count_item_snatch.text = bean.robNumber.toString()

            tvGood.text = bean.goodsName
            tvGood.append(LightSpanString.getLightString(" ×${bean.prizeNumber}", Color.parseColor("#FFFF2323")))
        }

    }

    private fun loadData() {
        getData()
        getMengdou()
    }

    private fun getData() {
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
                            llStateProcess.visibility = View.VISIBLE
                            llStateNormal.visibility = View.GONE
                            llStateEnd.visibility = View.GONE
                            return
                        }
                        tvDate.text = "${bean?.periodNumber}期"
                        GlideImageLoader.getInstace().displayImage(activity, bean?.goodsPic, ivGift)
                        tvPrizePoolNum.text = bean?.prizePoolNumber?.toString()
                        tvLimit.text = "每次${bean?.uRobGame?.amount}萌豆，已参与 "
                        tvLimit.append(LightSpanString.getLightString(bean?.userBetCount?.toString(),
                                Color.parseColor("#FFFF416E")))
                        tvLimit.append(" / ${bean?.uRobGame?.limit}次")

                        tvSecond.text = DateUtils.translateLastSecond(bean!!.surplusSecond)
                        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                            LogUtils.e("sssssssssss  "+t)
                            if (t == bean.surplusSecond.toLong() + 1) {
                                disposable.dispose()
                                return@subscribe
                            }
                            tvSecond.text = DateUtils.translateLastSecond(bean.surplusSecond - t!!.toInt())
                        }
                    }

                    override fun onError(code: Int) {
                    }
                })
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
}
