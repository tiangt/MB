package com.whzl.mengbi.ui.activity

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.message.events.FlopCardEvent
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.FlopContract
import com.whzl.mengbi.model.FlopPriceBean
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.model.entity.FlopAwardRecordBean
import com.whzl.mengbi.model.entity.FlopCardBean
import com.whzl.mengbi.model.entity.UserFlopInfoBean
import com.whzl.mengbi.presenter.FlopPresenter
import com.whzl.mengbi.ui.activity.base.BaseActivity
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
import kotlinx.android.synthetic.main.activity_flop.*
import kotlinx.android.synthetic.main.flipper_flop.view.*
import kotlinx.android.synthetic.main.item_benlun.view.*
import kotlinx.android.synthetic.main.item_flop.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @author nobody
 * @date 2019-06-18
 */
class FlopActivity : BaseActivity<FlopPresenter>(), FlopContract.View {

    private lateinit var rotateAnimation: ObjectAnimator
    private var awesomeDialog: AwesomeDialog? = null
    private lateinit var subList: MutableList<FlopAwardRecordBean.ListBean>
    private var recordList = java.util.ArrayList<FlopAwardRecordBean.ListBean>()
    private var maxFlopTimes: Int = 0
    private var shufflePrice = 0
    private var priceList: MutableList<FlopPriceBean.ListBean>? = null
    private var disposable: Disposable? = null
    private lateinit var adapter: BaseListAdapter
    private var roomId = 0
    private val mData = ArrayList<UserFlopInfoBean.ListBean>()
    private var showShuffle = true
    private var canClick = true


    init {
        mPresenter = FlopPresenter()
        mPresenter.attachView(this)
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_flop, "超级翻牌", true)
    }

    override fun setupView() {
        GlideImageLoader.getInstace().loadGif(this, R.drawable.ic_gift_gif_luck_flop, iv_luck_man_flop, null)
        GlideImageLoader.getInstace().loadGif(this, R.drawable.ic_luck_num_flop, progress_luck, null)

        rotateAnimation = ObjectAnimator.ofFloat(bg_luck_man_flop, "rotation", 360f)
        rotateAnimation.repeatCount = -1
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.duration = 2000

        roomId = intent.getIntExtra("roomId", 0)
        initRecyclerView(recycler_flop)
        btn_flop_card.clickDelay {
            transformAll()
        }

        tv_note_flop.clickDelay {
            showNotePopwindow()
        }

        tv_benlun_flop.clickDelay {
            showBenlunPopwindow()
        }

        tv_start_flop.clickDelay {
            if (recyclerIsAnim()) {
                return@clickDelay
            }
            val count = recyclerOpenNum()
            if (count < maxFlopTimes) {
                if (showShuffle) {
                    showShuffleDialog(count)
                } else {
                    startFlop()
                }
            } else {
                var index = 0
                for (i in 0 until recycler_flop.childCount) {
                    if (recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                        recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener {
                            index += 1
                            recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener(null)
                            if (index == count) {
                                startFlop()
                            }
                        }
                        recycler_flop.getChildAt(i).rotateview.transform()
                    }
                }
            }
        }
    }

    private fun recyclerOpenNum(): Int {
        var count = 0
        for (i in 0 until recycler_flop.childCount) {
            if (recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                count += 1
            }
        }
        return count
    }

    private fun showBenlunPopwindow() {
        val popView = layoutInflater.inflate(R.layout.popwindow_benlun_flop, null)
        val recyclerView = popView.findViewById<RecyclerView>(R.id.recycler_benlun)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.top = UIUtil.dip2px(this@FlopActivity, 4f)
                outRect?.bottom = UIUtil.dip2px(this@FlopActivity, 4f)
            }
        })
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return mData.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_benlun, parent, false)
                return BenlunHolder(itemView)
            }
        }
        recyclerView.adapter = adapter
        val popupWindow = PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                UIUtil.dip2px(this, 138f))
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(tv_benlun_flop)
    }

    inner class BenlunHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            GlideImageLoader.getInstace().displayImage(this@FlopActivity, mData[position].pic, itemView.iv_benlun)
            itemView.tv_name_benlun.text = "${mData[position].name} ×${mData[position].num}"
            if (mData[position].index > 0) {
                itemView.tv_name_benlun.setTextColor(Color.parseColor("#686ba4"))
            } else {
                itemView.tv_name_benlun.setTextColor(Color.parseColor("#ffffff"))
            }
        }
    }

    private fun showNotePopwindow() {
        val popView = layoutInflater.inflate(R.layout.popwindow_note_flop, null)
        val popupWindow = PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                UIUtil.dip2px(this, 105f))
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(tv_note_flop)
    }

    private fun initRecyclerView(recycler: RecyclerView) {
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.bottom = UIUtil.dip2px(this@FlopActivity, 5.5f)
                outRect?.left = UIUtil.dip2px(this@FlopActivity, 5f)
                outRect?.right = UIUtil.dip2px(this@FlopActivity, 5f)
                outRect?.top = UIUtil.dip2px(this@FlopActivity, 5.5f)
            }
        })
        adapter = object : BaseListAdapter() {
            override fun getDataCount() = 9

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_flop, parent, false)
                return FlopHolder(inflate)
            }

        }
        recycler.adapter = adapter
    }

    internal inner class FlopHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            if (position >= mData.size) {
                return
            }
            val listBean = mData[position]
            itemView.rotateview.setIvPic(listBean.pic)
            itemView.rotateview.setTvName("${listBean.name} ×${listBean.num}")
            if (listBean.index > 0) {
                itemView.rotateview.setOpen(true)
            } else {
                itemView.rotateview.setOpen(false)
            }
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            if (!canClick) {
                return
            }
            if (mData.size == 0) {
                toast(this@FlopActivity, "请点击开始翻牌")
                return
            }
            if (position >= mData.size) {
                return
            }
            if (itemView.rotateview.anim.isOpen) {
                return
            }
            if (recyclerIsAnim()) return
            canClick = false
            val params = HashMap<String, String>()
            params["userId"] = SPUtils.get(this@FlopActivity, SpConfig.KEY_USER_ID, 0L).toString()
            params["roomId"] = roomId.toString()
            params["flopIndex"] = (position + 1).toString()

            ApiFactory.getInstance()
                    .getApi(Api::class.java)
                    .flopCard(ParamsUtils.getSignPramsMap(params))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : ApiObserver<UserFlopInfoBean.ListBean>() {
                        override fun onSuccess(t: UserFlopInfoBean.ListBean?) {
                            itemView.rotateview.setIvPic(t?.pic)
                            itemView.rotateview.setTvName("${t?.name} ×${t?.num}")
//                            tv_luck_flop.text = t?.userLuckVal.toString()
                            updatePercent(t?.userLuckRatio!!)

                            itemView.rotateview.anim.setOpenAnimEndListener {

                                var recyclerOpenNum = recyclerOpenNum()

                                if (recyclerOpenNum == maxFlopTimes) {
                                    recyclerOpenNum = 0
                                    GlideImageLoader.getInstace().displayImage(this@FlopActivity, R.drawable.ic_fanpai_flop, iv_state_flop)
                                } else {
                                    GlideImageLoader.getInstace().displayImage(this@FlopActivity, R.drawable.ic_xipai_flop, iv_state_flop)
                                }

                                tv_price_flop.text = priceList?.get(recyclerOpenNum)?.number.toString()

                                dealPosition(position)

                                adapter.notifyDataSetChanged()
                                recycler_flop.post {
                                    for (i in 0 until recycler_flop.childCount) {
                                        if (!recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                                            recycler_flop.getChildAt(i).rotateview.transform()
                                            recycler_flop.getChildAt(i).rotateview.anim.isWaitPlay = true
                                            disposable = Observable.timer(2, TimeUnit.SECONDS)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe {
                                                        recycler_flop.getChildAt(i).rotateview.anim.isWaitPlay = false
                                                        recycler_flop.getChildAt(i).rotateview.transform()
                                                    }
                                        }
                                    }
                                }
                                itemView.rotateview.anim.setOpenAnimEndListener(null)
                            }

                            itemView.rotateview.transform()

                            for (i in 0 until mData.size) {
                                if (mData[i].flag == t?.flag) {
                                    val listBean = mData[i]
                                    val listBean1 = mData[position]
                                    listBean.index = position + 1
                                    mData[position] = listBean
                                    mData[i] = listBean1
//                                    recycler_flop.getChildAt(i).rotateview.setIvPic(listBean1?.pic)
//                                    recycler_flop.getChildAt(i).rotateview.setTvName("${listBean1?.name} ×${listBean1?.num}")
//                                    adapter.notifyDataSetChanged()
                                    break
                                }
                            }

                            canClick = true
                        }

                        override fun onError(body: ApiResult<UserFlopInfoBean.ListBean>?) {
                            when (body?.code) {
                                -1281 -> {
                                    showLimitDialog()
                                }
                                -1211 -> {
                                    showEnoughDialog()
                                }
                            }
                            canClick = true
                        }
                    })

        }
    }

    private fun dealPosition(position: Int) {
        var positions = ArrayList<Int>()
        when (position) {
            0 -> positions = arrayListOf(1, 3, 4)
            1 -> positions = arrayListOf(0, 2, 3, 4, 5)
            2 -> positions = arrayListOf(1, 4, 5)
            3 -> positions = arrayListOf(0, 1, 4, 6, 7)
            4 -> positions = arrayListOf(0, 1, 2, 3, 5, 6, 7, 8)
            5 -> positions = arrayListOf(1, 2, 4, 7, 8)
            6 -> positions = arrayListOf(3, 4, 7)
            7 -> positions = arrayListOf(3, 4, 5, 6, 8)
            8 -> positions = arrayListOf(4, 5, 7)
        }
        val list = arrayListOf<UserFlopInfoBean.ListBean>()
        for (i in 0 until mData.size) {
            if (mData[i].index == 0) {
                list.add(mData[i])
            }
        }
        list.sortByDescending { it.price }
        var index = 0
        for (i in positions) {
            if (i < 0 || i > 8 || mData[i].index > 0) {
                continue
            }
            val listBean = mData[i]
            val maxBean = list[index]
            val indexOf = mData.indexOf(maxBean)
            mData[indexOf] = listBean
            mData[i] = maxBean
            index += 1
        }
    }

    /**
     * 翻牌次数超过限制dialog
     */
    private fun showLimitDialog() {
        val awesomeDialog = AwesomeDialog.init()
        awesomeDialog?.setLayoutId(R.layout.dialog_simple)?.setConvertListener(object : ViewConvertListener() {
            override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                val textView = holder?.getView<TextView>(R.id.tv_content_simple_dialog)
                textView?.text = "本轮翻牌次数已达上限"
                holder?.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                    dialog?.dismissDialog()
                }
                holder?.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                    dialog?.dismissDialog()
                }
            }

        })?.show(supportFragmentManager)
    }

    /**
     * 余额不足dialog
     */
    private fun showEnoughDialog() {
        val awesomeDialog = AwesomeDialog.init()
        awesomeDialog?.setLayoutId(R.layout.dialog_simple)?.setConvertListener(object : ViewConvertListener() {
            override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                val textView = holder?.getView<TextView>(R.id.tv_content_simple_dialog)
                textView?.text = "萌豆不足"
                holder?.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                    dialog?.dismissDialog()
//                    startActivity(Intent(this@FlopActivity, ShopActivity::class.java))
                }
                holder?.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                    dialog?.dismissDialog()
                }
            }

        })?.show(supportFragmentManager)
    }

    /**
     * 洗牌dialog
     */
    private fun showShuffleDialog(count: Int) {
        if (awesomeDialog != null && awesomeDialog!!.isAdded) {
            return
        }
        awesomeDialog = AwesomeDialog.init()
        awesomeDialog!!.setLayoutId(R.layout.dialog_simple)?.setConvertListener(object : ViewConvertListener() {
            override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                val textView = holder?.getView<TextView>(R.id.tv_content_simple_dialog)
                textView?.text = "洗牌需要花费 "
                textView?.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(shufflePrice.toLong()),
                        Color.rgb(255, 43, 63)))
                textView?.append(" 萌豆")
                holder?.setOnClickListener(R.id.btn_confirm_simple_dialog) {
                    dialog?.dismissDialog()
                    if (count == 0) {
                        startFlop()
                    } else {
                        var index = 0
                        for (i in 0 until recycler_flop.childCount) {
                            if (recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                                recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener {
                                    index += 1
                                    recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener(null)
                                    if (index == count) {
                                        startFlop()
                                    }
                                }
                                recycler_flop.getChildAt(i).rotateview.transform()
                            }
                        }
                    }
                }
                holder?.setOnClickListener(R.id.btn_cancel_simple_dialog) {
                    dialog?.dismissDialog()
                }
            }

        })?.show(supportFragmentManager)
    }

    private fun recyclerIsAnim(): Boolean {
        for (i in 0 until recycler_flop.childCount) {
            if (recycler_flop.getChildAt(i).rotateview.anim.isWaitPlay ||
                    recycler_flop.getChildAt(i).rotateview.anim.isOpenPlay || recycler_flop.getChildAt(i).rotateview.anim.isClosePlay) {
                return true
            }
        }
        return false
    }

    private fun transformAll() {
        for (i in 0 until recycler_flop.childCount) {
            recycler_flop.getChildAt(i).rotateview.transform()
        }
    }

    override fun loadData() {
        mPresenter.flopPrice()
        mPresenter.flopAwardRecord()
    }

    override fun onUserFlopInfoSuccess(userFlopInfoBean: UserFlopInfoBean?) {
        shufflePrice = userFlopInfoBean?.shufflePrice!!
        maxFlopTimes = userFlopInfoBean.maxFlopTimes
//        tv_luck_flop.text = userFlopInfoBean.userLuckVal.toString()
        updatePercent(userFlopInfoBean.userLuckRatio)
        if (userFlopInfoBean.list == null || userFlopInfoBean.list.isEmpty()) {
            GlideImageLoader.getInstace().displayImage(this, R.drawable.ic_fanpai_flop, iv_state_flop)
            showShuffle = false
            tv_price_flop.text = priceList?.get(0)?.number.toString()
        } else {
            mData.clear()
            mData.addAll(userFlopInfoBean.list!!)
            adapter.notifyDataSetChanged()
            var dex = 0
            for (i in 0 until mData.size) {
                if (mData[i].index > 0) {
                    dex += 1
                }
            }
            if (dex == maxFlopTimes) {
                dex = 0
                GlideImageLoader.getInstace().displayImage(this, R.drawable.ic_fanpai_flop, iv_state_flop)
            } else {
                GlideImageLoader.getInstace().displayImage(this, R.drawable.ic_xipai_flop, iv_state_flop)
            }
            tv_price_flop.text = priceList?.get(dex)?.number.toString()
        }
    }

    private fun startFlop() {
        tv_start_flop.isEnabled = false
        mPresenter.startFlop(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString())
    }

    override fun onFlopCardSuccess(flopCardBean: FlopCardBean?) {

    }

    override fun onStartFlopSuccess(userFlopInfoBean: UserFlopInfoBean?) {
        tv_start_flop.isEnabled = true
        showShuffle = true
        mData.clear()
        mData.addAll(userFlopInfoBean?.list!!)
        adapter.notifyDataSetChanged()

        tv_price_flop.text = priceList?.get(0)?.number.toString()

        GlideImageLoader.getInstace().displayImage(this, R.drawable.ic_xipai_flop, iv_state_flop)

        recycler_flop.post(Runnable {
            var count = 0
            for (i in 0 until recycler_flop.childCount) {
                recycler_flop.getChildAt(i).rotateview.anim.setOpenAnimEndListener {
                    //                    count += 1
//                    if (count == recycler_flop.childCount) {
                    recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener {
                        recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener(null)
                    }
                    disposable = Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                recycler_flop.getChildAt(i).rotateview.anim.isWaitPlay = false
                                recycler_flop.getChildAt(i).rotateview.transform()
                            }
//                    }
                    recycler_flop.getChildAt(i).rotateview.anim.setOpenAnimEndListener(null)
                }
                recycler_flop.getChildAt(i).rotateview.transform()
                recycler_flop.getChildAt(i).rotateview.anim.isWaitPlay = true
            }
        })
    }

    override fun onStartFlopError(code: Int) {
        tv_start_flop.isEnabled = true
        when (code) {
            -1211 -> showEnoughDialog()
        }
    }

    override fun onFlopAwardRecordSuccess(flopAwardRecordBean: FlopAwardRecordBean?) {
        recordList.addAll(flopAwardRecordBean?.list!!)
        if (recordList.size > 30) {
            subList = recordList.subList(0, 30)
        } else {
            subList = recordList
        }
        for (it in subList) {
            val view = LayoutInflater.from(this).inflate(R.layout.flipper_flop, null)
            view.tv_flipper_flop.text = "恭喜 "
            view.tv_flipper_flop.append(LightSpanString.getLightString(it.nickName, Color.rgb(254, 255, 22)))
            view.tv_flipper_flop.append(" 翻到 ")
            view.tv_flipper_flop.append(LightSpanString.getLightString("${it.name} ×${it.num}", Color.rgb(252, 41, 255)))
            flipper_flop.addView(view)
        }
        flipper_flop.setFlipInterval(4000)
        flipper_flop.startFlipping()
    }

    override fun onFlopPriceSuccess(flopPriceBean: FlopPriceBean?) {
        priceList = flopPriceBean?.list
        mPresenter.userFlopInfo(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString())
    }

    fun updatePercent(percent: Double) {
        val ivWidth = progress_layout.width
        val lp = progress_luck.layoutParams as (RelativeLayout.LayoutParams)
        val marginEnd = ((1 - percent) * ivWidth).toInt()
        lp.width = ivWidth - marginEnd
        progress_luck.layoutParams = lp
        progress_luck.postInvalidate()

        if (percent >= 1) {
            iv_luck_man_flop.visibility = View.VISIBLE
            rotateAnimation.start()
            container_luck_man_flop.visibility = View.VISIBLE
            iv_luck_normal_flop.visibility = View.GONE
            tv_note_luck_flop.text = "您的幸运值已经爆棚啦"
        } else {
            iv_luck_man_flop.visibility = View.GONE
            rotateAnimation.end()
            container_luck_man_flop.visibility = View.GONE
            iv_luck_normal_flop.visibility = View.VISIBLE
            tv_note_luck_flop.text = "幸运值越高，大奖概率越高"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        EventBus.getDefault().unregister(this)
        rotateAnimation.end()
    }

    override fun initEnv() {
        super.initEnv()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(flopCardEvent: FlopCardEvent) {
        if (flipper_flop.childCount >= 30) {
            val childAt = flipper_flop.getChildAt(0)
            childAt.tv_flipper_flop.text = "恭喜 "
            childAt.tv_flipper_flop.append(LightSpanString.getLightString(flopCardEvent.flopCardJson.context.nickName, Color.rgb(254, 255, 22)))
            childAt.tv_flipper_flop.append(" 翻到 ")
            when (flopCardEvent.flopCardJson.context.prizeType) {
                "WEALTH" -> childAt.tv_flipper_flop.append(LightSpanString.getLightString("萌豆 ×${flopCardEvent.flopCardJson.context.wealthNumber}", Color.rgb(252, 41, 255)))
                "EXP" -> childAt.tv_flipper_flop.append(LightSpanString.getLightString("经验 ×${flopCardEvent.flopCardJson.context.expNumber}", Color.rgb(252, 41, 255)))
                "GOODS" -> childAt.tv_flipper_flop.append(LightSpanString.getLightString("${flopCardEvent.flopCardJson.context.goodsName} ×${flopCardEvent.flopCardJson.context.goodsCount}", Color.rgb(252, 41, 255)))
            }
        } else {
            val view = LayoutInflater.from(this).inflate(R.layout.flipper_flop, null)
            view.tv_flipper_flop.text = "恭喜 "
            view.tv_flipper_flop.append(LightSpanString.getLightString(flopCardEvent.flopCardJson.context.nickName, Color.rgb(254, 255, 22)))
            view.tv_flipper_flop.append(" 翻到 ")
            when (flopCardEvent.flopCardJson.context.prizeType) {
                "WEALTH" -> view.tv_flipper_flop.append(LightSpanString.getLightString("萌豆 ×${flopCardEvent.flopCardJson.context.wealthNumber}", Color.rgb(252, 41, 255)))
                "EXP" -> view.tv_flipper_flop.append(LightSpanString.getLightString("经验 ×${flopCardEvent.flopCardJson.context.expNumber}", Color.rgb(252, 41, 255)))
                "GOODS" -> view.tv_flipper_flop.append(LightSpanString.getLightString("${flopCardEvent.flopCardJson.context.goodsName} ×${flopCardEvent.flopCardJson.context.goodsCount}", Color.rgb(252, 41, 255)))
            }
            flipper_flop.addView(view)
        }

    }
}
