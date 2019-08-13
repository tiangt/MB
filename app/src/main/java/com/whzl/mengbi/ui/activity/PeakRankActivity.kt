package com.whzl.mengbi.ui.activity

import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.ImageUrl
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.AnchorTopBean
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.widget.view.CircleImageView
import com.whzl.mengbi.util.*
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
    private var anchorId: Long = 0L
    private var datas = ArrayList<AnchorTopBean.ListBean>()

    override fun setupContentView() {
        type = intent.getIntExtra("type", ANCHOR)
        anchorId = intent.getLongExtra("anchorId", 0L)
        if (type == ANCHOR)
            setContentView(R.layout.activity_peak_rank, "主播巅峰榜", true)
        else
            setContentView(R.layout.activity_peak_rank, "富豪巅峰榜", true)
    }

    override fun setupView() {
        if (type == ANCHOR)
            ll_bottom_peak.setBackgroundResource(R.drawable.bg_bottom_peak)
        else {
            ll_bottom_peak.setBackgroundResource(R.drawable.bg_bottom_peak2)
        }

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
            if (type == ANCHOR) {
                itemView.tv_num_peak.setBackgroundResource(R.drawable.shape_anchor_peak)
            } else {
                itemView.tv_num_peak.setBackgroundResource(R.drawable.shape_anchor_peak_rich)
            }
            val listBean = datas[position + 2]
            itemView.tv_num_peak.text = "${position + 3}"


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

            if (type == ANCHOR) {
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                        ResourceMap.getResourceMap().getAnchorLevelIcon(listBean.anchorLevelValue), itemView.iv_level_peak)
                itemView.tv_nick_peak.text = listBean.anchorNickname
            } else {
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                        ResourceMap.getResourceMap().getUserLevelIcon(listBean.userLevel), itemView.iv_level_peak)
                itemView.tv_nick_peak.text = listBean.nickname
            }

        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            if (type != ANCHOR) {
                return
            }
            val listBean = datas[position + 2]
            startActivity(Intent(this@PeakRankActivity, LiveDisplayActivity::class.java)
                    .putExtra(LiveDisplayActivity.PROGRAMID, listBean.programId))
        }

    }

    inner class TopViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val ivAvatars = arrayOf<CircleImageView>(itemView.iv_avatar_1_peak, itemView.iv_avatar_2_peak, itemView.iv_avatar_3_peak)
        private val tvNicks = arrayOf<TextView>(itemView.tv_nick_1_peak, itemView.tv_nick_2_peak, itemView.tv_nick_3_peak)
        private val ivLevels = arrayOf<ImageView>(itemView.iv_level_1_peak, itemView.iv_level_2_peak, itemView.iv_level_3_peak)
        private val containers = arrayOf<ConstraintLayout>(itemView.container_1_peak, itemView.container_2_peak, itemView.container_3_peak)

        override fun onBindViewHolder(position: Int) {
            if (type == ANCHOR) {
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity, R.drawable.bg_top_peak, itemView.iv_top_peak)
                itemView.tv_nick_1_peak.setTextColor(Color.parseColor("#ff3a5a"))
                itemView.tv_nick_2_peak.setTextColor(Color.parseColor("#ff3a5a"))
                itemView.tv_nick_3_peak.setTextColor(Color.parseColor("#ff3a5a"))
            } else {
                GlideImageLoader.getInstace().displayImage(this@PeakRankActivity, R.drawable.bg_top_peak2, itemView.iv_top_peak)
                itemView.tv_nick_1_peak.setTextColor(Color.parseColor("#ffffff"))
                itemView.tv_nick_2_peak.setTextColor(Color.parseColor("#ffffff"))
                itemView.tv_nick_3_peak.setTextColor(Color.parseColor("#ffffff"))
            }
            for (i in 0 until datas.size) {
                if (i > 2) {
                    break
                }
                val listBean = datas[i]
                if (type == ANCHOR) {
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity, listBean.anchorAvatar, ivAvatars[i])
                    tvNicks[i].text = listBean.anchorNickname
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            ResourceMap.getResourceMap().getAnchorLevelIcon(listBean.anchorLevelValue), ivLevels[i])
                    containers[i].clickDelay {
                        startActivity(Intent(this@PeakRankActivity, LiveDisplayActivity::class.java)
                                .putExtra(LiveDisplayActivity.PROGRAMID, listBean.programId))
                    }
                } else {
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            ImageUrl.getAvatarUrl(listBean.userId, "jpg", DateUtils.dateStrToMillis(listBean.lastUpdateTime, "yyyy-MM-dd HH:mm:ss")), ivAvatars[i])
                    tvNicks[i].text = listBean.nickname
                    GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                            ResourceMap.getResourceMap().getUserLevelIcon(listBean.userLevel), ivLevels[i])
                }
            }
        }

    }

    override fun loadData() {
        if (type == ANCHOR) {
            getAnchorTop()
        } else {
            getRichTop()
        }
    }

    private fun getAnchorTop() {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = anchorId.toString()
        ApiFactory.getInstance()
                .getApi(Api::class.java)
                .anchorTopRank(ParamsUtils.getSignPramsMap(hashMap))
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
                        if (t.userRankInfo.rankIndex < 0) {
                            tv_num_peak.text = "9999+"
                        } else {
                            tv_num_peak.text = t.userRankInfo.rankIndex.toString()
                        }

                        GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                                ResourceMap.getResourceMap().getAnchorLevelIcon(t.userRankInfo.level), iv_level_peak)
                        tv_contribute_peak.text = AmountConversionUitls.amountConversionFormat(t.userRankInfo.preCharmGap)
                        tv_nick_peak.text = t.userRankInfo.nickname
                    }
                })
    }

    private fun getRichTop() {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString()
        ApiFactory.getInstance()
                .getApi(Api::class.java)
                .userTopRank(ParamsUtils.getSignPramsMap(hashMap))
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
                        if (t.userRankInfo.rankIndex < 0) {
                            tv_num_peak.text = "9999+"
                        } else {
                            tv_num_peak.text = t.userRankInfo.rankIndex.toString()
                        }
                        GlideImageLoader.getInstace().displayImage(this@PeakRankActivity,
                                ResourceMap.getResourceMap().getUserLevelIcon(t.userRankInfo.level), iv_level_peak)
                        tv_contribute_peak.text = AmountConversionUitls.amountConversionFormat(t.userRankInfo.preCharmGap)
                        tv_nick_peak.text = t.userRankInfo.nickname
                    }
                })
    }

    companion object {
        const val ANCHOR = 1
        const val RICH = 2
    }


}