package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.message.events.AnchorWishChangeEvent
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.AnchorWishBean
import com.whzl.mengbi.ui.activity.LiveDisplayActivity
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.LogUtils
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.util.glide.GlideImageLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_anchor_wish.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit


/**
 *
 * @author nobody
 * @date 2019/3/26
 */
class AnchorWishFragment : BaseFragment<BasePresenter<BaseView>>() {
    lateinit var mOnclick: OnclickListner
    private var sendGiftPrice: Int = 0
    private var totalWishCard: Int = 0

    companion object {
        fun newInstance(bean: AnchorWishBean.DataBean): AnchorWishFragment {
            val anchorWishFragment = AnchorWishFragment()
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            anchorWishFragment.arguments = bundle
            return anchorWishFragment
        }
    }

    override fun initEnv() {
        super.initEnv()
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_anchor_wish
    }

    private var disposable: Disposable? = null

    override fun init() {
        val bean = arguments?.get("bean") as AnchorWishBean.DataBean
        sendGiftPrice = bean.sendGiftPrice
        totalWishCard = bean.totalWishCard
        ll_anchor_wish.clickDelay {
            mOnclick.onCLick()
        }
        GlideImageLoader.getInstace().displayRoundAvatar(activity, bean.giftPicUrl, iv_gift, 2)
        tv_total.text = "需 "
        tv_total.append(LightSpanString.getLightString(bean.totalWishCard.toString(), Color.parseColor("#FFFE4D87")))
        tv_total.append(" 个${bean.sendGiftName}")
        tv_unfinish.text = "还差 "
        tv_unfinish.append(LightSpanString.getLightString((bean.totalWishCard - bean.finishedWishCard).toString(), Color.parseColor("#FFFE4D87")))
        tv_unfinish.append(" 个")
        tv_support.text = "共有 "
        tv_support.append(LightSpanString.getLightString(bean.supportPeopleNum.toString(), Color.parseColor("#FF732EFF")))
        tv_support.append(" 人支持")
        if (bean.remainTime <= 0) {
            tv_time.text = DateUtils.translateLastSecond(0)
        } else {
            disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                        tv_time.text = DateUtils.translateLastSecond(bean.remainTime - t!!.toInt())
                        LogUtils.e("ssssssssss  $t")
                        if (t == bean.remainTime.toLong()) {
                            disposable!!.dispose()
                            (activity as LiveDisplayActivity).removeAnchorWish()
                            return@subscribe
                        }
                    }
        }
    }

    /**
     * 主播心愿改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(anchorWishEndEvent: AnchorWishChangeEvent) {
        tv_support.text = "共有 "
        tv_support.append(LightSpanString.getLightString(anchorWishEndEvent.anchorWishJson.context.rankPeopleNum.toString(), Color.parseColor("#FF732EFF")))
        tv_support.append(" 人支持")
        val i = (totalWishCard * sendGiftPrice - anchorWishEndEvent.anchorWishJson.context.totalScore) / sendGiftPrice
        tv_unfinish.text = "还差 "
        tv_unfinish.append(LightSpanString.getLightString(i.toString(), Color.parseColor("#FFFE4D87")))
        tv_unfinish.append(" 个")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        disposable?.dispose()
    }

    interface OnclickListner {
        fun onCLick()
    }
}