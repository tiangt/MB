package com.whzl.mengbi.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonElement
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.eventbus.event.MsgCenterRefreshEvent
import com.whzl.mengbi.model.entity.SysMsgListBean
import com.whzl.mengbi.ui.activity.JsBridgeActivity
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.ui.fragment.main.MessageFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.util.glide.GlideImageLoader
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_system_msg.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @author nobody
 * @date 2019/3/18
 */
class SystemMsgFragment : BasePullListFragment<SysMsgListBean.ListBean, BasePresenter<BaseView>>() {
    override fun initEnv() {
        super.initEnv()
        (activity as FrgActivity).setTitle("官方通知")
        (activity as FrgActivity).rightText.text = "清空"
        (activity as FrgActivity).rightText.setTextColor(Color.parseColor("#333333"))
        (activity as FrgActivity).rightText.clickDelay {
            val userid = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)
            val params = mutableMapOf<String, String>()
            params["userId"] = userid.toString()
            params["messageType"] = MessageFragment.SYSTEM_MESSAGE
            ApiFactory.getInstance().getApi(Api::class.java)
                    .clearMsgByType(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : ApiObserver<JsonElement>() {
                        override fun onSuccess(t: JsonElement?) {
                            mDatas.clear()
                            (activity as FrgActivity).rightText.visibility = View.GONE
                            loadSuccess(mDatas)
                        }
                    })
        }
    }


    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun init() {
        super.init()
        val empty = LayoutInflater.from(activity).inflate(R.layout.empty_system_msg, pullView, false)
        setEmptyView(empty)
        pullView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.bgGray))
        val divider = LayoutInflater.from(activity).inflate(R.layout.divider_shawdow_gray, pullView, false)
        hideDividerShawdow(divider)
    }

    override fun loadData(action: Int, mPage: Int) {
        val paramsMap = HashMap<String, String>()
        paramsMap["userId"] = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L).toString()
        paramsMap["page"] = mPage.toString()
        paramsMap["pageSize"] = NetConfig.DEFAULT_PAGER_SIZE.toString()
        val signPramsMap = ParamsUtils.getSignPramsMap(paramsMap)
        ApiFactory.getInstance().getApi(Api::class.java)
                .sysMsgList(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<SysMsgListBean>(this) {
                    override fun onSuccess(bean: SysMsgListBean?) {
                        loadSuccess(bean?.list)
                        if (bean?.list?.isEmpty()!!) {
                            (activity as FrgActivity).rightText.visibility = View.GONE
                        } else {
                            (activity as FrgActivity).rightText.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(code: Int) {
                    }
                })
    }

    override fun setViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder? {
        val view = layoutInflater.inflate(R.layout.item_system_msg, parent, false)
        return ViewHolder(view)
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]

            itemView.tv_date_item_msg.text = listBean?.createTime ?: ""
            itemView.tv_title_item_msg.text = listBean?.title ?: ""
            itemView.tv_content_item_msg.text = listBean?.content ?: ""

            when {
                "IMG" == listBean.contentType -> {
                    itemView.iv_img_item_msg.visibility = View.VISIBLE
                    GlideImageLoader.getInstace().displayImage(activity, listBean.content, itemView.iv_img_item_msg)

                }
                "TEXT" == listBean.contentType -> itemView.iv_img_item_msg.visibility = View.GONE
                else -> itemView.iv_img_item_msg.visibility = View.GONE
            }

            if (listBean.isRead == "T") {
                itemView.tv_title_item_msg.setTextColor(Color.parseColor("#999999"))
                itemView.tv_content_item_msg.setTextColor(Color.parseColor("#999999"))
            } else {
                itemView.tv_title_item_msg.setTextColor(Color.parseColor("#333333"))
                itemView.tv_content_item_msg.setTextColor(Color.parseColor("#666666"))
            }
        }

        override fun onItemClick(view: View?, position: Int) {
            super.onItemClick(view, position)
            val listBean = mDatas[position]
            if (listBean?.contentLink?.isNotEmpty() ?: return) {
                activity?.startActivity(Intent(activity, JsBridgeActivity::class.java)
                        .putExtra("url", listBean.contentLink))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val userid = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)
        val params = mutableMapOf<String, String>()
        params["userId"] = userid.toString()
        params["messageType"] = MessageFragment.SYSTEM_MESSAGE
        ApiFactory.getInstance().getApi(Api::class.java)
                .updateMsgReadByType(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>() {
                    override fun onSuccess(t: JsonElement?) {
                        EventBus.getDefault().post(MsgCenterRefreshEvent())
                    }
                })
    }


    override fun setShouldLoadMore() = true
}
