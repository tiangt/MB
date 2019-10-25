package com.whzl.mengbi.ui.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.model.entity.JumpRandomRoomListBean
import com.whzl.mengbi.ui.activity.LiveDisplayActivity
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_live_stop.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019/3/20
 */
class LiveStopDialog : BaseAwesomeDialog() {
    private var mprogramId = 0
    private var mprogramId2 = 0

    companion object {
        fun newInstance(mAnchorName: String, mAnchorAvatar: String, lastUpdateTime: String): LiveStopDialog {
            val dialog = LiveStopDialog()
            val bundle = Bundle()
            bundle.putString("anchorName", mAnchorName)
            bundle.putString("anchorAvatar", mAnchorAvatar)
            bundle.putString("lastTime", lastUpdateTime)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_live_stop
    }


    override fun onDestroyView() {
        super.onDestroyView()
        iv_change_live_stop.clearAnimation()
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val fromAvatar = arguments?.get("anchorAvatar")
        val fromName = arguments?.get("anchorName")
        val lastTime = arguments?.getString("lastTime")
        val loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.rotate_live_stop)
        loadAnimation.interpolator = AccelerateDecelerateInterpolator()
        loadAnimation.fillAfter = true

        holder?.setOnClickListener(R.id.ib_close_live_stop) { dismiss() }
        if (TextUtils.isEmpty(lastTime)) holder?.setText(R.id.tv_last_live, "上次开播：刚刚")
        else holder?.setText(R.id.tv_last_live, "上次开播：" + DateUtils.getTimeRange(lastTime))

        GlideImageLoader.getInstace().displayCircleAvatar(activity, fromAvatar, holder?.getView(R.id.iv_avatar_from))
        holder?.setText(R.id.tv_nick_from, fromName.toString())
        holder?.setOnClickListener(R.id.tv_change_live_stop) {
            iv_change_live_stop.clearAnimation()
            if (loadAnimation != null) {
                iv_change_live_stop.startAnimation(loadAnimation)
            }
            getOther(holder, dialog)
        }
        holder?.setOnClickListener(R.id.iv_cover_to) {
            if (mprogramId == 0) {
                return@setOnClickListener
            }
            val liveDisplayActivity = activity as LiveDisplayActivity
            liveDisplayActivity.jumpToLive(mprogramId)
            dismiss()
        }

        holder?.setOnClickListener(R.id.iv_cover_to_2) {
            if (mprogramId2 == 0) {
                return@setOnClickListener
            }
            val liveDisplayActivity = activity as LiveDisplayActivity
            liveDisplayActivity.jumpToLive(mprogramId2)
            dismiss()
        }
        getOther(holder, dialog)
    }

    fun getOther(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val paramsMap = mutableMapOf<String, String>()
        paramsMap["count"] = "2"
        ApiFactory.getInstance().getApi(Api::class.java)
                .randomList(ParamsUtils.getSignPramsMap(paramsMap as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JumpRandomRoomListBean>(this) {
                    override fun onSuccess(t: JumpRandomRoomListBean?) {
                        GlideImageLoader.getInstace().loadRoundImage(activity, t?.list?.get(0)?.cover, holder?.getView(R.id.iv_cover_to), 5)
                        mprogramId = t?.list?.get(0)?.programId ?: 0
                        GlideImageLoader.getInstace().loadRoundImage(activity, t?.list?.get(1)?.cover, holder?.getView(R.id.iv_cover_to_2), 5)
                        mprogramId2 = t?.list?.get(1)?.programId ?: 0
                    }

                })

    }

}