package com.whzl.mengbi.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.ImageUrl
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.GuessRankBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.ui.widget.view.CircleImageView
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_guess_rank.*
import kotlinx.android.synthetic.main.item_3_guess_rank.view.*
import kotlinx.android.synthetic.main.item_normal_guess_rank.view.*

/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class GuessRankFragment : BaseFragment<BasePresenter<BaseView>>() {
    private lateinit var rankType: String
    private lateinit var sortType: String
    private lateinit var adapter: BaseListAdapter
    private var data = ArrayList<GuessRankBean.ListBean>()

    override fun getLayoutId() = R.layout.fragment_guess_rank

    override fun init() {
        sortType = arguments?.getString("sortType")!!
        rankType = arguments?.getString("rankType")!!
        initRv(recycler_guess_rank)
        if (rankType != null) {
            loadData(sortType, rankType)
        }
    }

    private fun loadData(sortType: String, rankType: String) {
        val hashMap = HashMap<String, String>()
        hashMap["rankType"] = rankType
        hashMap["sortType"] = sortType
        ApiFactory.getInstance().getApi(Api::class.java)
                .guessRank(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GuessRankBean>() {
                    override fun onSuccess(t: GuessRankBean?) {
                        if (t?.list == null) {
                            return
                        }
                        if (t.list.size > 10) {
                            data.addAll(t.list.subList(0, 10))
                        } else {
                            data.addAll(t.list!!)
                        }
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    private fun initRv(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return if (data.size <= 3) {
                    1
                } else {
                    data.size - 2
                }
            }

            override fun getDataViewType(position: Int): Int {
                return if (position < 1) {
                    1
                } else {
                    2
                }
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                return if (viewType == 1) {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.item_3_guess_rank, parent, false)
                    TopViewHolder(inflate)
                } else {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.item_normal_guess_rank, parent, false)
                    NormalViewHolder(inflate)
                }
            }
        }
        recyclerView.adapter = adapter
    }

    inner class TopViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            if (sortType == "DSC") {
                itemView.ll_center.setBackgroundResource(R.drawable.shape_ase_top_guess_rank)
                itemView.iv_color_guess_rank.setBackgroundResource(R.drawable.shape_ase_top_guess_rank)
            } else {
                itemView.ll_center.setBackgroundResource(R.drawable.shape_dse_top_guess_rank)
                itemView.iv_color_guess_rank.setBackgroundResource(R.drawable.shape_dse_top_guess_rank)
            }
            val ivs = arrayOf<CircleImageView>(itemView.iv_1_guess_rank, itemView.iv_2_guess_rank, itemView.iv_3_guess_rank)
            val tvnicks = arrayOf<TextView>(itemView.tv_nick_1_guess_rank, itemView.tv_nick_2_guess_rank, itemView.tv_nick_3_guess_rank)
            val tvScores = arrayOf<TextView>(itemView.tv_score_1_guess_rank, itemView.tv_score_2_guess_rank, itemView.tv_score_3_guess_rank)
            val containers = arrayOf<ConstraintLayout>(itemView.ll_center_real, itemView.ll_left, itemView.ll_right)

//            if (data.size < 4) {
            for (i in 0 until data.size) {
                if (i > 2) {
                    break
                }
                containers[i].visibility = View.VISIBLE
                GlideImageLoader.getInstace().displayImage(activity, ImageUrl.getAvatarUrl(data[i]?.userId,
                        "jpg", DateUtils.dateStrToMillis(data[i]?.lastUpdateTime, "yyyy-MM-dd HH:mm")), ivs[i])
                tvnicks[i].text = data[i].nickName
                if ("DSC" == sortType) {
                    if ("WEEKRANK" == rankType) {
                        tvScores[i].text = "${data[i].score}"
                        tvScores[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_up_guess_rank, 0, 0, 0)
                    } else {
                        tvScores[i].text = "${data[i].score}"
                        tvScores[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_up_guess_rank, 0, 0, 0)
                    }
                } else {
                    if ("WEEKRANK" == rankType) {
                        tvScores[i].text = "${data[i].score}"
                        tvScores[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_down_guess_rank, 0, 0, 0)
                    } else {
                        tvScores[i].text = "${data[i].score}"
                        tvScores[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_down_guess_rank, 0, 0, 0)
                    }
                }
            }
//            } else {
//                for (i in 0..2) {
//                    containers[i].visibility = View.VISIBLE
//                    GlideImageLoader.getInstace().displayImage(activity, ImageUrl.getAvatarUrl(data[i]?.userId,
//                            "jpg", DateUtils.dateStrToMillis(data[i]?.lastUpdateTime, "yyyy-MM-dd HH:mm")), ivs[i])
//                    tvnicks[i].text = data[i].nickName
//                    tvScores[i].text = data[i].score.toString()
//                    if ("DSC" == sortType) {
//                        if ("WEEKRANK" == rankType) {
//                            tvScores[i].text = "周收益${data[i].score}"
//                        } else {
//                            tvScores[i].text = "月收益${data[i].score}"
//                        }
//                    } else {
//                        if ("WEEKRANK" == rankType) {
//                            tvScores[i].text = "周亏损${data[i].score}"
//                        } else {
//                            tvScores[i].text = "月亏损${data[i].score}"
//                        }
//                    }
//                }
//            }

        }

    }

    inner class NormalViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(position: Int) {
            val listBean = data[position + 2]
            itemView.tv_num.text = (position + 3).toString()
            itemView.tv_nick.text = listBean.nickName
            itemView.tv_score.text = listBean.score.toString()
            if ("DSC" == sortType) {
                if ("WEEKRANK" == rankType) {
                    itemView.tv_score.text = "周收益${listBean.score}"
                } else {
                    itemView.tv_score.text = "月收益${listBean.score}"
                }
            } else {
                if ("WEEKRANK" == rankType) {
                    itemView.tv_score.text = "周亏损${listBean.score}"
                } else {
                    itemView.tv_score.text = "月亏损${listBean.score}"
                }
            }
        }

    }

    companion object {
        fun newInstance(sortType: String?, rankType: String): GuessRankFragment {
            val guessRankFragment = GuessRankFragment()
            val bundle = Bundle()
            bundle.putString("sortType", sortType)
            bundle.putString("rankType", rankType)
            guessRankFragment.arguments = bundle
            return guessRankFragment
        }
    }
}