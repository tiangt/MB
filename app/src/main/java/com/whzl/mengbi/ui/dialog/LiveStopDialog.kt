package com.whzl.mengbi.ui.dialog

import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.model.entity.AnchorInfo
import com.whzl.mengbi.model.entity.JumpRandomRoomBean
import com.whzl.mengbi.ui.activity.LiveDisplayActivity
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

/**
 *
 * @author nobody
 * @date 2019/3/20
 */
class LiveStopDialog : BaseAwesomeDialog() {
    private var mprogramId = 0

    companion object {
        fun newInstance(mAnchorName: String, mAnchorAvatar: String): LiveStopDialog {
            val dialog = LiveStopDialog()
            val bundle = Bundle()
            bundle.putString("anchorName", mAnchorName)
            bundle.putString("anchorAvatar", mAnchorAvatar)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_live_stop
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val fromAvatar = arguments?.get("anchorAvatar")
        val fromName = arguments?.get("anchorName")
        holder?.setOnClickListener(R.id.ib_close_live_stop) { dismiss() }
        GlideImageLoader.getInstace().circleCropImage(activity, fromAvatar, holder?.getView(R.id.iv_avatar_from))
        holder?.setText(R.id.tv_nick_from, fromName.toString())
        holder?.setOnClickListener(R.id.tv_change_live_stop) {
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
        getOther(holder, dialog)
    }

    fun getOther(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val paramsMap = mutableMapOf<String, String>()
        ApiFactory.getInstance().getApi(Api::class.java)
                .random(ParamsUtils.getSignPramsMap(paramsMap as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JumpRandomRoomBean>(this) {
                    override fun onSuccess(t: JumpRandomRoomBean?) {
                        getOtherById(t?.programId, holder, dialog)
                    }

                })

    }

    fun getOtherById(programId: Int?, holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val paramsMap = mutableMapOf<String, String>()
        paramsMap.put("programId", programId.toString())
        ApiFactory.getInstance().getApi(Api::class.java)
                .getRoomInfo(ParamsUtils.getSignPramsMap(paramsMap as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<AnchorInfo>(this) {
                    override fun onSuccess(t: AnchorInfo?) {
                        mprogramId = programId!!
                        GlideImageLoader.getInstace().loadRoundImage(activity, t?.cover, holder?.getView(R.id.iv_cover_to), 5)
                        holder?.setText(R.id.tv_nick_to, t?.anchor?.name)
                        GlideImageLoader.getInstace().circleCropImage(activity, t?.anchor?.avatar, holder?.getView(R.id.iv_avatar_to))
                    }

                })

    }
}