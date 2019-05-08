package com.whzl.mengbi.ui.dialog

import android.animation.ObjectAnimator
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.model.entity.SignInfoBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_sign.*
import kotlinx.android.synthetic.main.item_sign_dialog.view.*

/**
 * @author nobody
 * @date 2019/5/8
 */
class SignDialog : BaseAwesomeDialog() {

    var mData = arrayListOf<SignInfoBean.ListBean>()
    lateinit var adapter: BaseListAdapter
    private val dates = intArrayOf(R.drawable.ic_monday_sign_dialog, R.drawable.ic_tuesday_sign_dialog
            , R.drawable.ic_wednsday_sign_dialog, R.drawable.ic_thursday_sign_dialog
            , R.drawable.ic_friday_sign_dialog, R.drawable.ic_saturday_sign_dialog, R.drawable.ic_sunday_sign_dialog
            , R.drawable.ic_ba_sign_dialog)

    private var curAwardId: Int = 0
    private var animation: ObjectAnimator? = null

    override fun intLayoutId(): Int {
        return R.layout.dialog_sign
    }

    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
//        animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f)
//        animation?.duration = 3000
//        animation?.repeatCount = -1
//        animation?.interpolator = LinearInterpolator()

        iv_close_sign_dialog.setOnClickListener {
            dismissDialog()
        }
        init(rv_sign_dialog)
        loadData()

    }

    private fun loadData() {
        getSignInfo()
    }

    private fun init(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = GridLayoutManager(activity, 4)
        recyclerView?.overScrollMode = View.OVER_SCROLL_NEVER
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return mData.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val itemView = LayoutInflater.from(activity).inflate(R.layout.item_sign_dialog, parent, false)
                return SignViewHolder(itemView)
            }

        }
        recyclerView?.adapter = adapter
    }

    inner class SignViewHolder(itemView: View?) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            val bean = mData[position]
            GlideImageLoader.getInstace().displayImage(activity, dates[position], itemView.iv_date_item_sign_dialog)
            itemView.tv_name_item_sign_dialog.text = bean.awardGoods
            GlideImageLoader.getInstace().displayImage(activity, bean.picUrl, itemView.iv_gift_item_sign_dialog)
            when (bean.signStatus) {
                "signed" -> {
                    itemView.ll_signed_item_sign_dialog.visibility = View.VISIBLE
                    itemView.ll_retro_item_sign_dialog.visibility = View.GONE
                }
                "notsign" -> {
                    itemView.ll_signed_item_sign_dialog.visibility = View.GONE
                    itemView.ll_retro_item_sign_dialog.visibility = View.GONE
                }
                "retroactive" -> {
                    itemView.ll_signed_item_sign_dialog.visibility = View.GONE
                    itemView.ll_retro_item_sign_dialog.visibility = View.VISIBLE
                }
                else -> {
                    itemView.ll_signed_item_sign_dialog.visibility = View.GONE
                    itemView.ll_retro_item_sign_dialog.visibility = View.GONE
                }

            }

            if (bean.awardId == curAwardId && bean.signStatus == "notsign") {
                itemView.rl_item_sign_dialog.setBackgroundResource(R.drawable.bg_select_sign_dialog)
                itemView.iv_select_item_sign_dialog.visibility = View.VISIBLE
//                itemView.iv_select_item_sign_dialog.startAnimation(animation)
                animation = ObjectAnimator.ofFloat(itemView.iv_select_item_sign_dialog, "rotation", 0f, 360f)
                animation?.repeatCount = 1000
                animation?.duration = 3000
                animation?.interpolator = LinearInterpolator()
                animation?.start()
            } else {
                itemView.rl_item_sign_dialog.setBackgroundResource(R.drawable.bg_normal_sign_dialog)
                itemView.iv_select_item_sign_dialog.visibility = View.GONE
                itemView.iv_select_item_sign_dialog.clearAnimation()
            }


            if (position == 7) {
                itemView.tv_name_item_sign_dialog.setTextColor(Color.parseColor("#ffff00"))
                itemView.rl_item_sign_dialog.setBackgroundResource(R.drawable.bg_secret_sign_dialog)
            }
        }
    }


    private fun getSignInfo() {
        val param = HashMap<String, String>()
        param["userId"] = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L).toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .signInfo(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<SignInfoBean>() {
                    override fun onSuccess(signInfoBean: SignInfoBean) {
                        mData.addAll(signInfoBean.list)
                        curAwardId = signInfoBean.curAwardId
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    companion object {
        fun newInstance(): SignDialog {
            return SignDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (animation != null) {
            animation?.cancel()
            animation?.end()
            animation = null
        }
    }
}

