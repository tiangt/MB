package com.whzl.mengbi.ui.activity.me

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_modify_psw.*

/**
 *
 * @author nobody
 * @date 2019/4/28
 */
class ModifyPswActivity : BaseActivity<BasePresenter<BaseView>>() {
    override fun setupContentView() {
        setContentView(R.layout.activity_modify_psw, "修改密码", "保存", true)
    }

    override fun setupView() {
        titleRightText.setTextColor(ContextCompat.getColor(this, R.color.textcolor_white_toolbar_right))
        tv_tips_modify_psw.text = "密码格式为"
        tv_tips_modify_psw.append(LightSpanString.getLightString("6-16", Color.parseColor("#50FF2B3F")))
        tv_tips_modify_psw.append("位数字、字符组合，不能为纯数字")
    }

    override fun loadData() {
    }
}