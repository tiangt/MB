package com.whzl.mengbi.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.activity.base.BaseActivity

/**
 *
 * @author nobody
 * @date 2019/3/20
 */
class SignActivity : BaseActivity<BasePresenter<BaseView>>() {
    private lateinit var sign: String
    private lateinit var tvSign: TextView
    private lateinit var etSign: EditText
    override fun loadData() {
    }


    override fun setupView() {
        titleRightText.setTextColor(Color.parseColor("#ff2b3f"))
        sign = intent.getStringExtra("sign")
        tvSign = findViewById(R.id.tv_sign)
        etSign = findViewById(R.id.et_sign)
        etSign.setText(sign)
        tvSign.text = "签名长度限制"
        tvSign.append(LightSpanString.getLightString("25", Color.parseColor("#FFFF8B89")))
        tvSign.append("个汉字。")
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_sign, "编辑签名", "保存", true)
    }

    override fun onToolbarMenuClick() {
        if (sign == etSign.text.toString().trim()) {
            finish()
            return
        }
        val intent = Intent()
        intent.putExtra("sign", etSign.text.toString().trim())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}