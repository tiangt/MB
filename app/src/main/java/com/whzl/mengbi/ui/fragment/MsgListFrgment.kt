package com.whzl.mengbi.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.MainMsgContract
import com.whzl.mengbi.model.entity.GetGoodMsgBean
import com.whzl.mengbi.presenter.MainMsgPresenter
import com.whzl.mengbi.ui.activity.BuySuccubusActivity
import com.whzl.mengbi.ui.activity.LiveDisplayActivity
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.activity.me.GoodnumFragment
import com.whzl.mengbi.ui.activity.me.MyChipActivity
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.ui.fragment.me.PackPrettyFragment
import com.whzl.mengbi.ui.fragment.me.PackVipFragment
import com.whzl.mengbi.ui.fragment.me.PropFragment
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.wxapi.WXPayEntryActivity

/**
 * @author nobody
 * @date 2019/3/18
 */
class MsgListFrgment : BasePullListFragment<GetGoodMsgBean.ListBean, MainMsgPresenter>(), MainMsgContract.View {
    override fun initEnv() {
        super.initEnv()
        mPresenter = MainMsgPresenter()
        mPresenter.attachView(this)
        val title = activity?.intent?.getStringExtra("title")
        val frgActivity = activity as FrgActivity
        when (title) {
            "EXPIRATION_MESSAGE" -> frgActivity.setTitle("系统通知")
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
        mPresenter.getMsgList(mPage)
    }

    override fun setViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder? {
        val view = layoutInflater.inflate(R.layout.item_msg_list, parent, false)
        return ViewHolder(view)
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var tvDate: TextView? = null
        private var tvGood: TextView? = null
        private var tvMove: TextView? = null
        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]
            tvDate = itemView.findViewById(R.id.tv_date_item_msg)
            tvGood = itemView.findViewById(R.id.tv_good_item_msg)
            tvMove = itemView.findViewById(R.id.tv_move_item_msg)
            tvDate?.text = listBean.createTime

            if (listBean.isRead == "F")
                tvGood?.setTextColor(Color.parseColor("#323232"))
            else tvGood?.setTextColor(Color.parseColor("#50000000"))
            when (listBean.goodsType) {
                "NORMAL_DEBRIS", "SERVICE", "UNIVERSAL_DEBRIS" -> {
                    tvGood?.text = "您的${listBean.goodsName}将于${listBean.surplusDay}天后到期，请及时使用！"
                    tvMove?.text = "现在去使用"
                }
                "DEMON_CARD", "GUARD", "VIP" -> {
                    tvGood?.text = "您的${listBean.goodsName}将于${listBean.surplusDay}天后到期，请及时续费！"
                    tvMove?.text = "现在去续费"
                }
                "COUPON" -> {
                    tvGood?.text = "您的${listBean.goodsName}将于${listBean.surplusDay}天后到期，请及时充值！"
                    tvMove?.text = "现在去充值"
                }
                "NOBILITY_EXP" -> {
                    tvGood?.text = "您的贵族："
                    //${}将于${listBean.surplusDay}天后到期，请及时充值！"
                    tvGood?.append(LightSpanString.getLightString(listBean.goodsName, Color.parseColor("#FFFFBFB3")))
                    tvGood?.append(" 将于")
                    tvGood?.append(listBean.surplusDay.toString() + "天后降级，请及时充值！")
                    tvMove?.text = "现在去充值"
                }
                "PRETTY_NUM" -> {
                    tvGood?.text = "您的靓号：${listBean.goodsName}将于${listBean.surplusDay}天后到期，请及时续费！"
                    tvMove?.text = "现在去续费"
                }
            }

            tvMove?.clickDelay {
                tvGood?.setTextColor(Color.parseColor("#50000000"))
                if (listBean.isRead == "F") {
                    mPresenter.updateMsgRead(listBean.messageId, listBean.messageType)
                }
                when (listBean.goodsType) {
                    "COUPON", "NOBILITY_EXP" ->
                        startActivity(Intent(activity, WXPayEntryActivity::class.java))
                    "DEMON_CARD" ->
                        startActivity(Intent(activity, BuySuccubusActivity::class.java))
                    "GUARD" ->
                        startActivity(Intent(activity, LiveDisplayActivity::class.java).putExtra(LiveDisplayActivity.PROGRAMID, listBean.bindProgramId))
                    "NORMAL_DEBRIS", "UNIVERSAL_DEBRIS" ->
                        startActivity(Intent(activity, MyChipActivity::class.java))
                    "PRETTY_NUM" ->
                        startActivity(Intent(activity, FrgActivity::class.java)
                                .putExtra(FrgActivity.FRAGMENT_CLASS, PackPrettyFragment::class.java))
                    "SERVICE" ->
                        startActivity(Intent(myActivity, FrgActivity::class.java)
                                .putExtra(FrgActivity.FRAGMENT_CLASS, PropFragment::class.java))
                    "VIP" ->
                        startActivity(Intent(myActivity, FrgActivity::class.java)
                                .putExtra(FrgActivity.FRAGMENT_CLASS, PackVipFragment::class.java))
                }
            }
        }

    }

    override fun onUpdateMsgReadSuccess() {
        activity?.setResult(Activity.RESULT_OK)
    }

    override fun onGetMsgListSuccess(getGoodMsgBean: GetGoodMsgBean) {
        loadSuccess(getGoodMsgBean.list)
    }

}
