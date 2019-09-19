package com.whzl.mengbi.ui.dialog

import android.os.Bundle
import android.view.View
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.model.entity.RoomRedpacketBean
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_room_redpacket.*
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 *
 * @author nobody
 * @date 2019-09-11
 */
class RoomRedpacketDialog : BaseAwesomeDialog() {
    private var roomGameRedpackDispose: Disposable? = null

    override fun intLayoutId(): Int {
        return R.layout.dialog_room_redpacket
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        iv_close.clickDelay { dismissDialog() }
        val userId = arguments?.getLong("userid")
        val programId = arguments?.getInt("programid")
        loadData(userId, programId)
    }

    private fun loadData(id: Long?, programId: Int?) {
        val map = HashMap<String, String>()
        map["userId"] = id.toString()
        map["programId"] = programId.toString()
        val signPramsMap = ParamsUtils.getSignPramsMap(map)
        ApiFactory.getInstance().getApi(Api::class.java)
                .roomGameRedpacket(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<RoomRedpacketBean>() {
                    override fun onSuccess(jsonElement: RoomRedpacketBean) {
                        initView(jsonElement)
                    }

                    override fun onError(body: ApiResult<RoomRedpacketBean>) {

                    }
                })
    }

    private fun initView(redpacketBean: RoomRedpacketBean?) {
        tv_name_game_packet.text = redpacketBean?.list?.nickname
        if (redpacketBean?.list?.userIsSatisfied == "T") {
            tv_is_satisfied.visibility = View.VISIBLE
        } else {
            tv_is_satisfied.visibility = View.GONE
        }

        val time = (System.currentTimeMillis() - DateUtils.dateStrToMillis(redpacketBean?.list?.startTime, "yyyy-MM-dd HH:mm:ss")) / 1000
        val interval = (DateUtils.dateStrToMillis(redpacketBean?.list?.closeTime, "yyyy-MM-dd HH:mm:ss")
                - DateUtils.dateStrToMillis(redpacketBean?.list?.startTime, "yyyy-MM-dd HH:mm:ss")) / 1000
        roomGameRedpackDispose = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { aLong: Long ->
                    if (aLong > interval - time) {
                        tv_close_time.text = "00:00 后开奖"
                        roomGameRedpackDispose?.dispose()
                        return@subscribe
                    }
                    tv_close_time.text = String.format("%s 后开奖", DateUtils.translateLastSecond3((interval - time - aLong).toInt()))
                }

        tv_total_valid_people.text = String.format("共%d人参与", redpacketBean?.list?.totalValidPeople)

        tv_award_people_num.text = String.format("%d人瓜分", redpacketBean?.list?.awardPeopleNum)

        if (redpacketBean?.list?.conditionGoodsNum!! > 0) {
            tv_condition_good.text = String.format("单笔送礼 ≥%d%s", redpacketBean?.list?.conditionGoodsNum, redpacketBean?.list?.conditionGoodsName)
        } else {
            tv_condition_good.text = String.format("单笔送礼 ≥%d萌币", redpacketBean?.list?.conditionPrice)
        }
        if ("COIN" == redpacketBean?.list?.awardType) {
            tv_goods_name.text = String.format("萌币 %d", redpacketBean.list?.awardTotalPrice)
        } else {
            tv_goods_name.text = String.format("%s %d", redpacketBean?.list?.awardGoodsName, redpacketBean?.list?.awardGoodsNum)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (roomGameRedpackDispose != null) {
            roomGameRedpackDispose!!.dispose()
        }
    }

    companion object {
        fun newInstance(mUserId: Long, mProgramId: Int): RoomRedpacketDialog {
            val roomRedpacketDialog = RoomRedpacketDialog()
            val bundle = Bundle()
            bundle.putLong("userid", mUserId)
            bundle.putInt("programid", mProgramId)
            roomRedpacketDialog.arguments = bundle
            return roomRedpacketDialog
        }
    }
}