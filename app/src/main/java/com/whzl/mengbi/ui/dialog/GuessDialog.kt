package com.whzl.mengbi.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.model.entity.AllGuessBean
import com.whzl.mengbi.model.entity.GameGuessBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.LogUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import com.whzl.mengbi.util.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_guess.*
import kotlinx.android.synthetic.main.item_empty_guess.view.*
import kotlinx.android.synthetic.main.item_list_guess.view.*
import java.util.concurrent.TimeUnit

/**
 *
 * @author nobody
 * @date 2019-06-12
 */
class GuessDialog : BaseAwesomeDialog() {
    private val emptyData = ArrayList<AllGuessBean.ListBean>()
    private val guessData = ArrayList<GameGuessBean.ListBean>()
    private lateinit var emptyAdapter: BaseListAdapter
    private lateinit var guessAdapter: BaseListAdapter
    private val compositeDisposable = CompositeDisposable()


    override fun intLayoutId(): Int {
        return R.layout.dialog_guess
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val userId = arguments?.getLong("userId")
        val programId = arguments?.getInt("programId")
        val anchorId = arguments?.getInt("anchorId")
        initDataRv(recycler_data_guess)
        initEmptyRv(recycler_empty_guess)
        getEmptyData()
        getGuessList(anchorId)
    }

    private fun initDataRv(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.setItemViewCacheSize(10)
        guessAdapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return guessData.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_list_guess, parent, false)
                return GuessHolder(inflate)
            }
        }
        recyclerView?.adapter = guessAdapter
    }

    inner class GuessHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            itemView.ll_square_select.isEnabled = false
            itemView.ll_counter_select.isEnabled = false

            val listBean = guessData[position]
            itemView.tv_theme_guess.text = listBean.guessTheme

            itemView.tv_square_argument.text = listBean.squareArgument
            itemView.tv_square_odds.text = getString(R.string.tv_odd_guess, listBean.squareOdds)

            itemView.tv_counter_argument.text = listBean.counterArgument
            itemView.tv_counter_odds.text = getString(R.string.tv_odd_guess, listBean.counterOdds)

            val totalFee = listBean.squareArgumentFee + listBean.counterArgumentFee
            if (totalFee.toInt() == 0) {
                itemView.progress_guess.progress = 50
            } else {
                val d = listBean.squareArgumentFee / totalFee * 100
                itemView.progress_guess.progress = d.toInt()
            }

            when (listBean.status) {
                "SEALED_DISK" -> itemView.tv_status_guess.text = "已封盘"
                "SETTLEMENT", "FINISH" -> {
                    itemView.tv_status_guess.text = "已结束"
                    when {
                        listBean.successArgument == "squareArgument" -> {
                            itemView.iv_square_outcome.setImageResource(R.drawable.ic_win_guess)
                            itemView.iv_counter_outcome.setImageResource(R.drawable.ic_lose_guess)
                        }
                        listBean.successArgument == "counterArgument" -> {
                            itemView.iv_square_outcome.setImageResource(R.drawable.ic_lose_guess)
                            itemView.iv_counter_outcome.setImageResource(R.drawable.ic_win_guess)
                        }
                        else -> {

                        }
                    }
                }
                "FLOW_BUREAU" -> {
                    itemView.tv_status_guess.text = "流局"
                }
                "BET" -> {
                    val dateStrToMillis = DateUtils.dateStrToMillis(listBean.closingTime, "yyyy-MM-dd HH:mm:ss")
                    val total = ((dateStrToMillis - System.currentTimeMillis()) / 1000).toInt()
                    val disposable = Observable.interval(1, 1, TimeUnit.SECONDS)
                            .take(total.toLong() + 1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                LogUtils.e("ssssssssssssssss    $it")
                                itemView.tv_status_guess.text =
                                        "${DateUtils.translateLastSecond((total - it).toInt())}封盘"
                                if (it == total.toLong()) {
                                    itemView.tv_status_guess.text = "已封盘"
                                    itemView.ll_square_select.isEnabled = false
                                    itemView.ll_counter_select.isEnabled = false
                                }
                            }
                    compositeDisposable.add(disposable)

                    itemView.ll_square_select.isEnabled = true
                    itemView.ll_counter_select.isEnabled = true
                }
            }

            itemView.ll_square_select.setOnClickListener {
                toast(activity, "ll_square_select")
            }

            itemView.ll_counter_select.setOnClickListener {
                toast(activity, "ll_counter_select")
            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun initEmptyRv(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        emptyAdapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return emptyData.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_empty_guess, parent, false)
                return EmptyHolder(inflate)
            }
        }
        recyclerView.adapter = emptyAdapter
    }

    inner class EmptyHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            val allGuessBean = emptyData[position]
            GlideImageLoader.getInstace().displayCircleAvatar(context, allGuessBean.anchorAvatar, itemView.iv_avatar_item_empty_guess)
            itemView.tv_nick_item_empty_guess.text = allGuessBean.anchorNickname
            itemView.tv_people_item_empty_guess.text = getString(R.string.tv_empty_guess, allGuessBean.roomUserCount)
        }
    }

    private fun getEmptyData() {
        val param = HashMap<String, String>()
        ApiFactory.getInstance().getApi(Api::class.java)
                .allGuessList(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<AllGuessBean>() {
                    override fun onSuccess(t: AllGuessBean?) {
                        if (t?.list != null) {
                            emptyData.clear()
                            emptyData.addAll(t.list)
                            emptyAdapter.notifyDataSetChanged()
                        }
                    }
                })

    }

    private fun getGuessList(anchorId: Int?) {
        val param = HashMap<String, String>()
        param["userId"] = anchorId.toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .gameGuessList(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GameGuessBean>() {
                    override fun onSuccess(t: GameGuessBean?) {
                        if (t?.list != null && t.list.isNotEmpty()) {
                            container_empty.visibility = View.GONE
                            container_data.visibility = View.VISIBLE
                            guessData.clear()
                            guessData.addAll(t.list)
                            guessAdapter.notifyDataSetChanged()
                        } else {
                            container_empty.visibility = View.VISIBLE
                            container_data.visibility = View.GONE
                        }
                    }
                })
    }


    companion object {
        fun newInstance(mUserId: Long, mProgramId: Int, mAnchorId: Int): GuessDialog {
            val guessDialog = GuessDialog()
            val bundle = Bundle()
            bundle.putLong("userId", mUserId)
            bundle.putInt("programId", mProgramId)
            bundle.putInt("anchorId", mAnchorId)
            guessDialog.arguments = bundle
            return guessDialog
        }
    }
}