package com.whzl.mengbi.ui.activity.me

import android.content.Intent
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_msg_push.*

/**
 *
 * @author nobody
 * @date 2019/4/24
 */
class MsgPushActivity : BaseActivity<BasePresenter<BaseView>>() {
    override fun setupContentView() {
        setContentView(R.layout.activity_msg_push, "消息推送", true)
    }

    override fun setupView() {
        tv_open_notify.setOnClickListener {
            startActivity(Intent(this, PlayNotifyActivity::class.java))
        }
    }

    override fun loadData() {
    }
}