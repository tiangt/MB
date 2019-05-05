package com.whzl.mengbi.ui.activity.me

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import com.google.gson.JsonElement
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.util.EncryptUtils
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.ToastUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_psw.*
import java.util.HashMap

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

        iv_old_modify_psw.setOnClickListener {
            if (et_old_psw_modify_psw.inputType == 128) {//如果现在是显示密码模式
                et_old_psw_modify_psw.inputType = 129//设置为隐藏密码
                iv_old_modify_psw.isSelected = false
            } else {
                et_old_psw_modify_psw.inputType = 128//设置为显示密码
                iv_old_modify_psw.isSelected = true
            }
            et_old_psw_modify_psw.setSelection(et_old_psw_modify_psw.text.length)
        }

        iv_new_modify_psw.setOnClickListener {
            if (et_new_psw_modify_psw.inputType == 128) {//如果现在是显示密码模式
                et_new_psw_modify_psw.inputType = 129//设置为隐藏密码
                iv_new_modify_psw.isSelected = false
            } else {
                et_new_psw_modify_psw.inputType = 128//设置为显示密码
                iv_new_modify_psw.isSelected = true
            }
            et_new_psw_modify_psw.setSelection(et_new_psw_modify_psw.text.length)
        }

        iv_confirm_modify_psw.setOnClickListener {
            if (et_confirm_psw_modify_psw.inputType == 128) {//如果现在是显示密码模式
                et_confirm_psw_modify_psw.inputType = 129//设置为隐藏密码
                iv_confirm_modify_psw.isSelected = false
            } else {
                et_confirm_psw_modify_psw.inputType = 128//设置为显示密码
                iv_confirm_modify_psw.isSelected = true
            }
            et_confirm_psw_modify_psw.setSelection(et_confirm_psw_modify_psw.text.length)
        }
    }

    override fun onToolbarMenuClick() {
        super.onToolbarMenuClick()
        if (et_new_psw_modify_psw.text.toString() != et_confirm_psw_modify_psw.text.toString()) {
            ToastUtils.showToastUnify(this@ModifyPswActivity, "两次输入密码不一致")
            return
        }
        val map = mutableMapOf<String, String>()
        map["oldPasswd"] = EncryptUtils.md5Hex(et_old_psw_modify_psw.text.toString())
        map["newPasswd"] = EncryptUtils.md5Hex(et_new_psw_modify_psw.text.toString())
        map["userId"] = SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .editPassword(ParamsUtils.getSignPramsMap(map as HashMap<String, String>?))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>(this) {
                    override fun onSuccess(t: JsonElement?) {
                        ToastUtils.showToastUnify(this@ModifyPswActivity, "密码修改成功")
                        finish()
                    }

                    override fun onError(body: ApiResult<JsonElement>?) {
                        ToastUtils.showToastUnify(this@ModifyPswActivity, body?.msg)
                    }
                })
    }

    override fun loadData() {
    }
}