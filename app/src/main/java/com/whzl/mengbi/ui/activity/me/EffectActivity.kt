package com.whzl.mengbi.ui.activity.me

import android.content.Context
import android.content.Intent
import com.whzl.mengbi.R
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.activity.base.BaseActivity
import com.whzl.mengbi.util.SPUtils
import kotlinx.android.synthetic.main.activity_effect.*

/**
 *
 * @author nobody
 * @date 2019-06-27
 */
class EffectActivity : BaseActivity<BasePresenter<BaseView>>() {
    override fun setupContentView() {
        setContentView(R.layout.activity_effect, "直播特效设置", true)
    }

    override fun setupView() {
        val gift = SPUtils.get(this, SpConfig.GIFT_EFFECT, true) as Boolean
        val car = SPUtils.get(this, SpConfig.CAR_EFFECT, true) as Boolean
        val fly = SPUtils.get(this, SpConfig.FLY_EFFECT, true) as Boolean
        val combo = SPUtils.get(this, SpConfig.COMBO_EFFECT, true) as Boolean
        val chat = SPUtils.get(this, SpConfig.CHAT_EFFECT, false) as Boolean

        switch_gift.isChecked = gift
        switch_car.isChecked = car
        switch_fly.isChecked = fly
        switch_combo.isChecked = combo
        switch_chat.isChecked = chat

        switch_gift.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(this, SpConfig.GIFT_EFFECT, isChecked)
        }
        switch_car.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(this, SpConfig.CAR_EFFECT, isChecked)
        }
        switch_fly.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(this, SpConfig.FLY_EFFECT, isChecked)
        }
        switch_combo.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(this, SpConfig.COMBO_EFFECT, isChecked)
        }
        switch_chat.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(this, SpConfig.CHAT_EFFECT, isChecked)
        }
    }

    override fun loadData() {
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, EffectActivity::class.java)
            context.startActivity(starter)
        }
    }
}