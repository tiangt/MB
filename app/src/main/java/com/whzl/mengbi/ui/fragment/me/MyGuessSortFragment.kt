package com.whzl.mengbi.ui.fragment.me

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.UserGuessListBean
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_my_guess.view.*

/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class MyGuessSortFragment : BasePullListFragment<UserGuessListBean.ListBean, BasePresenter<BaseView>>() {

    override fun loadData(action: Int, mPage: Int) {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L).toString()
        hashMap["page"] = mPage.toString()
        hashMap["pageSize"] = NetConfig.DEFAULT_PAGER_SIZE.toString()
        hashMap["status"] = arguments?.getString("status")!!
        ApiFactory.getInstance().getApi(Api::class.java)
                .userGuessList(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<UserGuessListBean>() {
                    override fun onSuccess(t: UserGuessListBean?) {
                        loadSuccess(t?.list)
                    }
                })
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(activity).inflate(R.layout.item_my_guess, parent, false)
        return GuessViewHolder(inflate)
    }

    inner class GuessViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]
            itemView.tv_id_my_guess.text = "竞猜ID：${listBean.uGameGuess.guessId}"
            itemView.tv_date_my_guess.text = listBean.uGameGuess.gmtCreated
            itemView.tv_theme_my_guess.text = listBean.uGameGuess.guessTheme
            itemView.tv_fee_my_guess.text = listBean.uGameGuessObject.fee.toString()
            if ("SQUARE_ARGUMENT" == listBean.uGameGuessObject.guessBettingScheme) {
                itemView.tv_scheme_my_guess.text = listBean.uGameGuess.squareArgument
            } else {
                itemView.tv_scheme_my_guess.text = listBean.uGameGuess.counterArgument
            }

            itemView.tv_odds_my_guess.setTextColor(Color.parseColor("#000000"))
            itemView.tv_success_my_guess.setTextColor(Color.parseColor("#000000"))
            itemView.tv_produce_my_guess.setTextColor(Color.parseColor("#000000"))


            if ("SQUARE_ARGUMENT" == listBean.uGameGuessObject.guessBettingScheme) {
                itemView.tv_odds_my_guess.text = listBean.uGameGuess.squareOdds.toString()
            } else {
                itemView.tv_odds_my_guess.text = listBean.uGameGuess.counterOdds.toString()
            }
            when (listBean.uGameGuess.status) {

                "FINISH" -> {
                    itemView.tv_odds_my_guess.setTextColor(Color.parseColor("#FF2B3F"))
                    itemView.tv_success_my_guess.setTextColor(Color.parseColor("#FF2B3F"))
                    itemView.tv_produce_my_guess.setTextColor(Color.parseColor("#FF2B3F"))

                    if (TextUtils.isEmpty(listBean.uGameGuess.successArgument)) {
                        itemView.tv_status_my_guess.text = "流局"
                    } else {
                        itemView.tv_status_my_guess.text = "已结束"
                    }

                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#757575"))

                    when {
                        listBean.uGameGuess.successArgument == "squareArgument" -> itemView.tv_success_my_guess.text = listBean.uGameGuess.squareArgument
                        listBean.uGameGuess.successArgument == "counterArgument" -> itemView.tv_success_my_guess.text = listBean.uGameGuess.counterArgument
                        else -> itemView.tv_success_my_guess.text = "--"
                    }
                    if (listBean.uGameGuessObject.produce > 0) {
                        if (listBean.uGameGuessObject.produce - listBean.uGameGuessObject.fee == 0) {
                            itemView.tv_produce_my_guess.text = "${listBean.uGameGuessObject.produce - listBean.uGameGuessObject.fee}"
                        } else {
                            itemView.tv_produce_my_guess.text = "+${listBean.uGameGuessObject.produce - listBean.uGameGuessObject.fee}"
                        }
                    } else {
                        itemView.tv_produce_my_guess.text = "${listBean.uGameGuessObject.produce - listBean.uGameGuessObject.fee}"
                    }
                }
                "FLOW_BUREAU" -> {
                    itemView.tv_status_my_guess.text = "流局"
                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#000000"))
//                    itemView.tv_odds_my_guess.text = "--"
                    itemView.tv_success_my_guess.text = "--"
                    itemView.tv_produce_my_guess.text = "--"

                }
                "BET" -> {
                    itemView.tv_status_my_guess.text = "进行中"
                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#000000"))
//                    itemView.tv_odds_my_guess.text = "--"
                    itemView.tv_success_my_guess.text = "--"
                    itemView.tv_produce_my_guess.text = "--"
                }
                "SEALED_DISK" -> {
                    itemView.tv_status_my_guess.text = "已封盘"
                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#000000"))
//                    itemView.tv_odds_my_guess.text = "--"
                    itemView.tv_success_my_guess.text = "--"
                    itemView.tv_produce_my_guess.text = "--"
                }
                "SETTLEMENT" -> {
                    itemView.tv_status_my_guess.text = "结算中"
                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#000000"))
//                    itemView.tv_odds_my_guess.text = "--"
                    itemView.tv_success_my_guess.text = "--"
                    itemView.tv_produce_my_guess.text = "--"
                }
                else -> {
                    itemView.tv_status_my_guess.text = "进行中"
                    itemView.tv_status_my_guess.setTextColor(Color.parseColor("#000000"))
//                    itemView.tv_odds_my_guess.text = "--"
                    itemView.tv_success_my_guess.text = "--"
                    itemView.tv_produce_my_guess.text = "--"
                }
            }
        }
    }

    companion object {
        fun newInstance(s: String): MyGuessSortFragment {
            val myGuessSortFragment = MyGuessSortFragment()
            val bundle = Bundle()
            bundle.putString("status", s)
            myGuessSortFragment.arguments = bundle
            return myGuessSortFragment
        }

    }

    override fun setShouldLoadMore() = true

    override fun init() {
        super.init()
        hideDividerShawdow(null)
    }
}