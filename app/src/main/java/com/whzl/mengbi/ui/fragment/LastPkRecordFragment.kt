package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.ImageUrl
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PkQualifyingBean
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.DateUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import kotlinx.android.synthetic.main.item_last_pk_record.view.*

/**
 *
 * @author nobody
 * @date 2019-08-15
 */
class LastPkRecordFragment : BasePullListFragment<PkQualifyingBean.LastestPkRecordBean, BasePresenter<BaseView>>() {
    override fun init() {
        super.init()
        hideDividerShawdow(null)
        val inflate = LayoutInflater.from(activity).inflate(R.layout.head_last_pk_record, pullView, false)
        addHeadTips(inflate)
    }

    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun setShouldRefresh(): Boolean {
        return false
    }

    override fun loadData(action: Int, mPage: Int) {
        var list = arguments?.getParcelableArrayList<PkQualifyingBean.LastestPkRecordBean>("bean")
        if (list == null) {
            list = ArrayList<PkQualifyingBean.LastestPkRecordBean>()
        }
        if (list.size > 3) {
            loadSuccess(list.subList(0, 3))
        } else {
            loadSuccess(list)
        }
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_last_pk_record, parent, false)
        return ViewHolder(inflate)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            val lastestPkRecordBean = mDatas[position] ?: return
            GlideImageLoader.getInstace().circleCropImage(activity, lastestPkRecordBean.anchorAvatar, itemView.iv_avatar_pk_record)
            itemView.anchor_nick_pk_record.text = lastestPkRecordBean.anchorNickname

            if (lastestPkRecordBean.recordInfo.score > 0) {
                itemView.tv_score_pk_record.text = "+${lastestPkRecordBean.recordInfo.score}"
            } else {
                itemView.tv_score_pk_record.text = lastestPkRecordBean.recordInfo.score.toString()
            }

            if (lastestPkRecordBean.recordInfo.mvpUserInfo != null) {
                GlideImageLoader.getInstace().circleCropImage(activity,
                        ImageUrl.getAvatarUrl(lastestPkRecordBean.recordInfo.mvpUserInfo.userId.toLong(),
                                "jpg", DateUtils.dateStrToMillis(lastestPkRecordBean.recordInfo.mvpUserInfo.lastUpdateTime, "yyyy-MM-dd HH:mm:ss")),
                        itemView.iv_mvp_pk_record)
                itemView.tv_mvp_pk_record.text = lastestPkRecordBean.recordInfo.mvpUserInfo.nickname
            } else {
                GlideImageLoader.getInstace().displayImage(activity, null, itemView.iv_mvp_pk_record)
                itemView.tv_mvp_pk_record.text = ""
            }

            val dateStrToMillis = DateUtils.dateStrToMillis(lastestPkRecordBean.recordInfo.date, "yyyy-MM-dd HH:mm:ss")
            val days = DateUtils.getDateToString(dateStrToMillis, "MM-dd")
            val hours = DateUtils.getDateToString(dateStrToMillis, "HH:mm")
            itemView.tv_date_pk_record.text = days
            itemView.tv_hour_pk_record.text = hours

            when (lastestPkRecordBean.recordInfo.result) {
                "V" -> {
                    itemView.tv_result_pk_record.setTextColor(Color.parseColor("#ffd634"))
                    itemView.tv_score_pk_record.setTextColor(Color.parseColor("#ffd634"))
                    itemView.tv_result_pk_record.text = "胜利"
                }
                "T" -> {
                    itemView.tv_result_pk_record.setTextColor(Color.parseColor("#B3FFFFFF"))
                    itemView.tv_score_pk_record.setTextColor(Color.parseColor("#B3FFFFFF"))
                    itemView.tv_result_pk_record.text = "平局"
                }
                "F" -> {
                    itemView.tv_result_pk_record.setTextColor(Color.parseColor("#ff2a3b"))
                    itemView.tv_score_pk_record.setTextColor(Color.parseColor("#ff2a3b"))
                    itemView.tv_result_pk_record.text = "失败"
                }
            }
        }

    }

    companion object {
        fun newInstance(lastestPkRecord: ArrayList<PkQualifyingBean.LastestPkRecordBean>?): LastPkRecordFragment {
            val anchorRankFragment = LastPkRecordFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("bean", lastestPkRecord)
            anchorRankFragment.arguments = bundle
            return anchorRankFragment
        }
    }
}