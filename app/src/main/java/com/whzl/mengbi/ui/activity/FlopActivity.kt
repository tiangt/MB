package com.whzl.mengbi.ui.activity

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.PopupWindow
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.FlopContract
import com.whzl.mengbi.model.FlopPriceBean
import com.whzl.mengbi.model.entity.FlopAwardRecordBean
import com.whzl.mengbi.model.entity.FlopCardBean
import com.whzl.mengbi.model.entity.UserFlopInfoBean
import com.whzl.mengbi.presenter.FlopPresenter
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.UIUtil
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_flop.*
import kotlinx.android.synthetic.main.item_benlun.view.*
import kotlinx.android.synthetic.main.item_flop.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * @author nobody
 * @date 2019-06-18
 */
class FlopActivity : BaseActivity<FlopPresenter>(), FlopContract.View {

    private var priceList: MutableList<FlopPriceBean.ListBean>? = null
    private var disposable: Disposable? = null
    private lateinit var adapter: BaseListAdapter
    private var roomId = 0
    private val mData = ArrayList<UserFlopInfoBean.ListBean>()

    init {
        mPresenter = FlopPresenter()
        mPresenter.attachView(this)
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_flop, "超级翻牌", true)
    }

    override fun setupView() {
        roomId = intent.getIntExtra("roomId", 0)
        initRecyclerView(recycler_flop)
        btn_flop_card.setOnClickListener {
            transformAll()
        }

        tv_note_flop.setOnClickListener {
            showNotePopwindow()
        }

        tv_benlun_flop.setOnClickListener {
            showBenlunPopwindow()
        }

        tv_start_flop.setOnClickListener {
            if (recyclerIsAnim()) {
                return@setOnClickListener
            }
            var count = 0
            var index = 0
            for (i in 0 until recycler_flop.childCount) {
                if (recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                    count += 1
                }
            }
            for (i in 0 until recycler_flop.childCount) {
                if (recycler_flop.getChildAt(i).rotateview.anim.isOpen) {
                    recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener {
                        index += 1
                        recycler_flop.getChildAt(i).rotateview.anim.setCloseAnimEndListener(null)
                        if (index == count) {
                            mPresenter.startFlop(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString())
                        }
                    }
                    recycler_flop.getChildAt(i).rotateview.transform()
                }
            }
        }
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
            override fun getDataCount() = mData.size

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_flop, parent, false)
                return FlopHolder(inflate)
            }

        }
        recycler.adapter = adapter
    }

    internal inner class FlopHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
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
            if (itemView.rotateview.anim.isOpen) {
                return
            }
            if (recyclerIsAnim()) return
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
                            tv_luck_flop.text = t?.userLuckVal.toString()

                            itemView.rotateview.anim.setOpenAnimEndListener {
                                for (i in 0 until mData.size) {
                                    if (mData[i].index == 0) {
                                        recycler_flop.getChildAt(i).rotateview.transform()
                                        disposable = Observable.timer(2, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe {
                                                    recycler_flop.getChildAt(i).rotateview.transform()
                                                }
                                    }
                                }
                                itemView.rotateview.anim.setOpenAnimEndListener(null)
                            }

                            for (i in 0 until mData.size) {
                                if (mData[i].flag == t?.flag) {
                                    val listBean = mData[i]
                                    val listBean1 = mData[position]
                                    listBean.index = position + 1
                                    mData[position] = listBean
                                    mData[i] = listBean1
                                    recycler_flop.getChildAt(i).rotateview.setIvPic(listBean1?.pic)
                                    recycler_flop.getChildAt(i).rotateview.setTvName("${listBean1?.name} ×${listBean1?.num}")
                                    break
                                }
                            }

                            itemView.rotateview.transform()
                        }
                    })

        }
    }

    private fun recyclerIsAnim(): Boolean {
        for (i in 0 until recycler_flop.childCount) {
            if (recycler_flop.getChildAt(i).rotateview.anim.isOpenPlay) {
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
        mData.clear()
        mData.addAll(userFlopInfoBean?.list!!)
        adapter.notifyDataSetChanged()
        tv_luck_flop.text = userFlopInfoBean.userLuckVal.toString()
        var dex = 0
        for (i in 0 until mData.size) {
            if (mData[i].index > 0) {
                dex += 1
            }
        }
        tv_price_flop.text = priceList?.get(dex)?.number.toString()
    }

    override fun onFlopCardSuccess(flopCardBean: FlopCardBean?) {

    }

    override fun onStartFlopSuccess(userFlopInfoBean: UserFlopInfoBean?) {
        mData.clear()
        mData.addAll(userFlopInfoBean?.list!!)
        adapter.notifyDataSetChanged()
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
                                recycler_flop.getChildAt(i).rotateview.transform()
                            }
//                    }
                    recycler_flop.getChildAt(i).rotateview.anim.setOpenAnimEndListener(null)
                }
                recycler_flop.getChildAt(i).rotateview.transform()
            }
        })
    }

    override fun onFlopAwardRecordSuccess(flopAwardRecordBean: FlopAwardRecordBean?) {

    }

    override fun onFlopPriceSuccess(flopPriceBean: FlopPriceBean?) {
        priceList = flopPriceBean?.list
        mPresenter.userFlopInfo(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
