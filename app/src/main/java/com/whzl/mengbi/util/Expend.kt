package com.whzl.mengbi.util

import android.app.Activity
import android.content.Context
import android.view.View
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.util.Expend.SPACE_TIME
import com.whzl.mengbi.util.Expend.hash
import com.whzl.mengbi.util.Expend.lastClickTime

/**
 *
 * @author nobody
 * @date 2019/3/14
 */
object Expend {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 300
}

infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}

fun toast(activity: Activity?, content: String?) {
    ToastUtils.showToastUnify(activity, content)
}