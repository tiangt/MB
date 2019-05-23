package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.view.View
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.ImageUrl
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.RedFundInfoBean
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_red_fund.*

/**
 *
 * @author nobody
 * @date 2019/5/13
 */
class RedFundFragment : BaseFragment<BasePresenter<BaseView>>() {
    companion object {
        fun newInstance(): RedFundFragment {
            val redFundFragment = RedFundFragment()
            return redFundFragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_red_fund
    }

    override fun init() {
        ApiFactory.getInstance().getApi(Api::class.java)
                .redFundInfo(ParamsUtils.getSignPramsMap(HashMap()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<RedFundInfoBean>() {
                    override fun onSuccess(t: RedFundInfoBean?) {
                        tv_amount_fund.text = t?.redEnvelopePoolCount?.toString()
                        if (t?.userId == 0L) {
                            tv_nick_fund.visibility = View.GONE
                            tv_fenhong_fund.visibility = View.GONE
                            iv_avatar_fund.visibility = View.GONE
                            tv_tips_fund.visibility = View.GONE
                            return
                        }
                        tv_nick_fund.text = t?.nickname
                        tv_fenhong_fund.text = "分红 "
                        tv_fenhong_fund.append(LightSpanString.getLightString(t?.userPoolCount?.toString(), Color.rgb(255, 87, 5)))
                        tv_fenhong_fund.append(" 萌币")
                        GlideImageLoader.getInstace().displayCircleAvatar(activity,
                                ImageUrl.getAvatarUrl(t?.userId!!, "jpg", t.lastUpdateTime), iv_avatar_fund)

                    }

                })
    }
}