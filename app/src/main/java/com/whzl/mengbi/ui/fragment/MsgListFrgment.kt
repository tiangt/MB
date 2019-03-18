package com.whzl.mengbi.ui.fragment

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.GetGoodMsgBean
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_anchor_task.*
import kotlinx.android.synthetic.main.item_msg_list.*
import java.util.*

/**
 * @author nobody
 * @date 2019/3/18
 */
class MsgListFrgment : BasePullListFragment<GetGoodMsgBean.ListBean, BasePresenter<BaseView>>() {
    override fun initEnv() {
        super.initEnv()
        val title = activity?.intent?.getStringExtra("title")
        val frgActivity = activity as FrgActivity
        when (title) {
            "GOODS_TYPE" -> frgActivity.setTitle("系统通知")
        }
    }

    override fun setLoadMoreEndShow(): Boolean {
        return false
    }

    override fun init() {
        super.init()
        val empty = LayoutInflater.from(activity).inflate(R.layout.empty_msg_main, pullView, false)
        setEmptyView(empty)
        pullView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.bgGray))
        val divider = LayoutInflater.from(activity).inflate(R.layout.divider_shawdow_gray, pullView, false)
        hideDividerShawdow(divider)
    }

    override fun loadData(action: Int, mPage: Int) {
        val userid = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L)
        val params = mutableMapOf<String, String>()
        params.put("userId", userid.toString())
        params.put("page", mPage.toString())
        params.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE.toString())
        ApiFactory.getInstance().getApi(Api::class.java)
                .getGoodMsg(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GetGoodMsgBean>(this) {
                    override fun onSuccess(t: GetGoodMsgBean?) {
                        loadSuccess(t?.list)
                    }

                })
    }

    override fun setViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder? {
        val view = layoutInflater.inflate(R.layout.item_msg_list, parent, false)
        return ViewHolder(view)
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var tvDate: TextView? = null
        var tvGood: TextView? = null
        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]
            tvDate = itemView.findViewById(R.id.tv_date_item_msg)
            tvGood = itemView.findViewById(R.id.tv_good_item_msg)
            tvDate?.text=listBean.createTime
            tvGood?.text="您的${listBean.goodsName}将于${listBean.surplusDay}"
        }

    }
}
