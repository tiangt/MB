package com.whzl.mengbi.ui.fragment

import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.ImageUrl
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
                        tv_nick_fund.text = t?.nickname
                        tv_fenhong_fund.text = t?.userPoolCount?.toString()
                        GlideImageLoader.getInstace().circleCropImage(activity,
                                ImageUrl.getAvatarUrl(t?.userId!!, "jpg", t.lastUpdateTime), iv_avatar_fund)

                    }

                })
    }
}