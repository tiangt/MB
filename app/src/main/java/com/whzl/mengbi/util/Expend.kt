package com.whzl.mengbi.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import java.util.concurrent.TimeUnit


/**
 *
 * @author nobody
 * @date 2019/3/14
 */

@SuppressLint("CheckResult")
infix fun View.clickDelay(clickAction: () -> Unit) {
    this.clicks()
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe { clickAction() }
}

fun toast(activity: Activity?, content: String?) {
    ToastUtils.showToastUnify(activity, content)
}