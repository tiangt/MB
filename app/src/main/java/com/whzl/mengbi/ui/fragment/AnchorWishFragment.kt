package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.AnchorWishBean
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.LogUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_anchor_wish.*
import java.util.concurrent.TimeUnit


/**
 *
 * @author nobody
 * @date 2019/3/26
 */
class AnchorWishFragment : BaseFragment<BasePresenter<BaseView>>() {
    lateinit var mOnclick: OnclickListner

    companion object {
        fun newInstance(bean: AnchorWishBean): AnchorWishFragment {
            val anchorWishFragment = AnchorWishFragment()
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            anchorWishFragment.arguments = bundle
            return anchorWishFragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_anchor_wish
    }

    private var disposable: Disposable? = null

    override fun init() {
        val bean: AnchorWishBean = arguments?.get("bean") as AnchorWishBean
        ll_anchor_wish.setOnClickListener {
            mOnclick.onCLick()
        }
        GlideImageLoader.getInstace().loadRoundImage(activity, bean.giftPicUrl, iv_gift, 2)
        tv_total.text = "需 "
        tv_total.append(LightSpanString.getLightString(bean.totalWishCard.toString(), Color.parseColor("#FFFE4D87")))
        tv_total.append(" 张心愿卡")
        tv_unfinish.text = "还差 "
        tv_unfinish.append(LightSpanString.getLightString((bean.totalWishCard - bean.finishedWishCard).toString(), Color.parseColor("#FFFE4D87")))
        tv_unfinish.append(" 张")
        tv_support.text = "共有 "
        tv_support.append(LightSpanString.getLightString(bean.supportPeopleNum.toString(), Color.parseColor("#FF732EFF")))
        tv_support.append(" 人支持")
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
                    tv_time.text = DateUtils.translateLastSecond(bean.remainTime - t!!.toInt())
                    LogUtils.e("ssssssssss  $t")
                    if (t == bean.remainTime.toLong()) {
                        disposable!!.dispose()
                        return@subscribe
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    interface OnclickListner {
        fun onCLick()
    }
}