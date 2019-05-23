package com.whzl.mengbi.ui.activity.me

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.whzl.mengbi.R
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.UserInfo
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.util.GsonUtils
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.RequestManager
import com.whzl.mengbi.util.network.URLContentUtils
import kotlinx.android.synthetic.main.activity_account_security.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019/4/24
 */
class AccountSecurityActivity : BaseActivity<BasePresenter<BaseView>>() {
    private val REQUEST_BINDING = 101
    private var mMobile: String = ""
    override fun setupContentView() {
        setContentView(R.layout.activity_account_security, "账号与安全", true)
    }

    override fun setupView() {
        rl_binding_phone.setOnClickListener {
            if (TextUtils.isEmpty(mMobile)) {
                jumpToBindingPhoneActivity()
            } else {
                jumpToChangePhoneActivity()
            }
        }

        rl_modify_psw.setOnClickListener {
            jumpToModifyPsweActivity()
        }
    }


    override fun loadData() {
        getUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BINDING) {
            if (resultCode == Activity.RESULT_OK) {
                getUserInfo()
            }
        }
    }

    private fun getUserInfo() {
        val paramsMap = HashMap<String, String>()
        val userId = java.lang.Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0.toLong())!!.toString())
        paramsMap["userId"] = userId.toString()
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, object : RequestManager.ReqCallBack<Any> {
            @SuppressLint("SetTextI18n")
            override fun onReqSuccess(result: Any) {
                val userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo::class.java)
                if (userInfo!!.code == 200) {
                    mMobile = userInfo.data.bindMobile
                    if (!TextUtils.isEmpty(mMobile)) {
                        val maskNum = mMobile.substring(0, 3) + "xxxx" + mMobile.substring(7, mMobile.length)
                        tv_bind_phone.text = "已绑定 $maskNum"
                    } else {
                        if (tv_bind_phone != null) {
                            tv_bind_phone.text = "未绑定"
                        }
                    }
                }
            }

            override fun onReqFailed(errorMsg: String) {
            }
        })
    }

    private fun jumpToBindingPhoneActivity() {
        val intent = Intent(this, BindingPhoneActivity::class.java)
        startActivityForResult(intent, REQUEST_BINDING)
    }

    private fun jumpToChangePhoneActivity() {
        val intent = Intent(this, ChangePhoneActivity::class.java)
        intent.putExtra("bindMobile", mMobile)
        startActivity(intent)
    }

    private fun jumpToModifyPsweActivity() {
        startActivity(Intent(this, ModifyPswActivity::class.java))
    }
}