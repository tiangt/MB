package com.whzl.mengbi.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.AnchorTopBean
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.widget.view.CircleImageView
import com.whzl.mengbi.util.ResourceMap
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_peak_rank.*
import kotlinx.android.synthetic.main.item_3_peak_rank.view.*
import kotlinx.android.synthetic.main.item_normal_peak_rank.view.*

/**
 *
 * @author nobody
 * @date 2019-08-12
 */
class PeakRankActivity : BaseActivity<BasePresenter<BaseView>>() {

    private lateinit var adapter: BaseListAdapter
    private var type: Int = ANCHOR
    private var datas = ArrayList<AnchorTopBean.ListBean>()

    override fun setupContentView() {
        type = intent.getIntExtra("type", ANCHOR)
        if (type == ANCHOR)
            setContentView(R.layout.activity_peak_rank, "主播巅峰榜", true)
        else
            setContentView(R.layout.activity_peak_rank, "富豪巅峰榜", true)
    }

    override fun setupView() {
        recycler_peak.layoutManager = LinearLayoutManager(this)
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return if (datas.size <= 3) {
                    1
                } else {
                    datas.size - 2
                }
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                return if (viewType == 1) {
                    val inflate = LayoutInflater.from(this@PeakRankActivity).inflate(R.layout.item_3_peak_rank, parent, false)
                    TopViewHolder(inflate)
                } else {
                    val inflate = LayoutInflater.from(this@PeakRankActivity).inflate(R.layout.item_normal_peak_rank, parent, false)
                    NormalViewHolder(inflate)
                }
            }

            override fun getDataViewType(position: Int): Int {
                return if (position == 0) {
                    1
                } else {
                    2
                }
            }
        }
        recycler_peak.adapter = adapter
    }

    inner class NormalViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            val listBean = datas[position + 2]
            itemView.tv_num_peak.text = "${position + 3}"
            GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                    ResourceMap.getResourceMap().getAnchorLevelIcon(listBean.anchorLevelValue), itemView.iv_level_peak)
            itemView.tv_nick_peak.text = listBean.anchorNickname
            when {
                listBean.upAndDownNumber == 0L -> {
                    itemView.tv_up_down.text = ""
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            R.drawable.ic_mid_peak, itemView.iv_up_down)
                }
                listBean.upAndDownNumber > 0L -> {
                    itemView.tv_up_down.text = "${Math.abs(listBean.upAndDownNumber)}"
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            R.drawable.ic_up_peak, itemView.iv_up_down)
                }
                listBean.upAndDownNumber < 0L -> {
                    itemView.tv_up_down.text = "${Math.abs(listBean.upAndDownNumber)}"
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            R.drawable.ic_down_peak, itemView.iv_up_down)
                }
            }
        }

    }

    inner class TopViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val ivAvatars = arrayOf<CircleImageView>(itemView.iv_avatar_1_peak, itemView.iv_avatar_2_peak, itemView.iv_avatar_3_peak)
        private val tvNicks = arrayOf<TextView>(itemView.tv_nick_1_peak, itemView.tv_nick_2_peak, itemView.tv_nick_3_peak)
        private val ivLevels = arrayOf<ImageView>(itemView.iv_level_1_peak, itemView.iv_level_2_peak, itemView.iv_level_3_peak)
        override fun onBindViewHolder(position: Int) {
            for (i in 0 until datas.size) {
                if (i > 2) {
                    break
                }
                val listBean = datas[i]
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity, listBean.anchorAvatar, ivAvatars[i])
                tvNicks[i].text = listBean.anchorNickname
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                        ResourceMap.getResourceMap().getAnchorLevelIcon(listBean.anchorLevelValue), ivLevels[i])
            }
        }

    }

    override fun loadData() {
        ApiFactory.getInstance()
                .getApi(Api::class.java)
                .anchorTopRank(ParamsUtils.getSignPramsMap(HashMap<String, String>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<AnchorTopBean>() {
                    override fun onSuccess(t: AnchorTopBean?) {
                        datas.clear()
                        if (t?.list == null) {
                            return
                        }
                        datas.addAll(t.list)
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    companion object {
        const val ANCHOR = 1
        const val RICH = 2
    }


}