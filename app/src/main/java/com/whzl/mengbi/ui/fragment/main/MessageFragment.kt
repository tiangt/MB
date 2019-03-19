package com.whzl.mengbi.ui.fragment.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.GetUnReadMsgBean
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.MsgListFrgment
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 消息
 *
 * @author cliang
 * @date 2019.2.14
 */
class MessageFragment : BasePullListFragment<GetUnReadMsgBean.ListBean, BasePresenter<BaseView>>() {

    override fun init() {
        super.init()
        val titleView = LayoutInflater.from(activity).inflate(R.layout.headtip_msg, pullView, false)
        addHeadTips(titleView)
        pullView.setBackgroundColor(Color.WHITE)
    }

    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun loadData(action: Int, mPage: Int) {
        val userid = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L)
        val param = HashMap<String, String>()
        param.put("userId", userid.toString())
        ApiFactory.getInstance().getApi(Api::class.java)
                .getUnreadMsg(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GetUnReadMsgBean>(this) {

                    override fun onSuccess(watchHistoryListBean: GetUnReadMsgBean?) {
                        loadSuccess(watchHistoryListBean?.list)
                    }

                    override fun onError(code: Int) {
                        loadFail()
                    }
                })

    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_msg_main, parent, false)
        return ViewHolder(itemView)
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        var tvName: TextView? = null
        var tvNum: TextView? = null
        var imageView: ImageView? = null

        override fun onBindViewHolder(position: Int) {
            tvName = itemView?.findViewById(R.id.tv_name_item_msg_main)
            tvNum = itemView?.findViewById(R.id.tv_num_item_msg_main)
            imageView = itemView?.findViewById(R.id.iv_item_mag_main)
            val bean = mDatas[position]
            when (bean.messageType) {
                "EXPIRATION_MESSAGE" -> {
                    tvName?.text = "系统消息"
                    GlideImageLoader.getInstace().circleCropImage(activity, R.drawable.ic_system_msg, imageView)
                }
            }

            when (bean.messageNum) {
                0 -> tvNum?.text = "暂无消息"
                else -> tvNum?.text = "${bean.messageNum}条未读消息"
            }
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            startActivity(Intent(activity, FrgActivity::class.java)
                    .putExtra(FrgActivity.FRAGMENT_CLASS, MsgListFrgment::class.java)
                    .putExtra("title",mDatas[position].messageType))
        }
    }


    companion object {
        fun newInstance(): MessageFragment {
            val messageFragment = MessageFragment()
            val bundle = Bundle()
            messageFragment.arguments = bundle
            return messageFragment
        }
    }

}
