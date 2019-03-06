package com.whzl.mengbi.ui.dialog.fragment

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.eventbus.event.MengdouChangeEvent
import com.whzl.mengbi.model.entity.UserInfo
import com.whzl.mengbi.ui.activity.me.ShopActivity
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.AmountConversionUitls
import com.whzl.mengbi.util.BusinessUtils
import com.whzl.mengbi.util.ClickUtil
import kotlinx.android.synthetic.main.dialog_snatch.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author nobody
 * @date 2019/3/6
 */
class SnatchDialog : BaseAwesomeDialog() {
    private lateinit var tvHisPrize: TextView
    private var ibReduce: ImageButton? = null
    private lateinit var ibAdd: ImageButton
    private var tvWant: TextView? = null
    private var mUserId: Long = 0
    private var tvMengdou: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_snatch
    }

    override fun convertView(holder: ViewHolder, dialog: BaseAwesomeDialog) {
        mUserId = arguments!!.getLong("mUserId")

        tvHisPrize = holder.getView(R.id.tv_his_prize)
        tvHisPrize!!.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvWant = holder.getView(R.id.tv_want_join)
        tvMengdou = holder.getView(R.id.tv_mengdou)
        ibReduce = holder.getView(R.id.ib_reduce_want)
        ibAdd = holder.getView(R.id.ib_add_want)
        ibReduce?.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant!!.text.toString()) <= 1) {
                return@OnClickListener
            }
            tvWant?.text = (Integer.parseInt(tvWant!!.text.toString()) - 1).toString()
        })
        ibAdd!!.setOnClickListener(View.OnClickListener {
            if (Integer.parseInt(tvWant!!.text.toString()) >= 10) {
                return@OnClickListener
            }
            tvWant?.text = (Integer.parseInt(tvWant!!.text.toString()) + 1).toString()
        })
        holder.setOnClickListener(R.id.tv_five) { tvWant?.text = "5" }
        holder.setOnClickListener(R.id.tv_ten) { tvWant?.text = "10" }
        holder.setOnClickListener(R.id.iv_mengdou_snatch) {
            if (ClickUtil.isFastClick()) {
                startActivity(Intent(activity, ShopActivity::class.java))
            }
        }
        tvHisPrize.setOnClickListener { showHisDialog(mUserId) }
        loadData()
    }

    private fun showHisDialog(mUserId: Long) {
        dismiss()
        AwesomeDialog.init().setLayoutId(R.layout.dialog_snatch_his).show(fragmentManager)
    }

    private fun loadData() {
        getData()
        getMengdou()
    }

    private fun getData() {
    }

    private fun getMengdou() {
        BusinessUtils.getUserInfo(activity, mUserId.toString() + "", object : BusinessUtils.UserInfoListener {
            override fun onSuccess(bean: UserInfo.DataBean?) {
                if (bean != null && bean.wealth != null) {
                    val mengDou = bean.wealth.mengDou
                    tvMengdou?.text = AmountConversionUitls.amountConversionFormat(mengDou)
                }
            }

            override fun onError(code: Int) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MengdouChangeEvent) {
        getMengdou()
    }

    companion object {

        fun newInstance(mUserId: Long): SnatchDialog {
            val dialog = SnatchDialog()
            val bundle = Bundle()
            bundle.putLong("mUserId", mUserId)
            dialog.arguments = bundle
            return dialog
        }
    }
}
