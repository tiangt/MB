package com.whzl.mengbi.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.google.gson.JsonElement
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.ToastUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import com.whzl.mengbi.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_guess_bet.*

/**
 *
 * @author nobody
 * @date 2019-06-13
 */
class GuessBetDialog : BaseAwesomeDialog() {
    private var userId: Long = 0
    private var guessId: Int = 0
    private var programId: Int = 0
    private var odds: Double = 0.0
    private lateinit var bettingType: String

    override fun intLayoutId(): Int {
        return R.layout.dialog_guess_bet
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        userId = arguments?.getLong("userId")!!
        guessId = arguments?.getInt("guessId")!!
        programId = arguments?.getInt("programId")!!
        bettingType = arguments?.getString("bettingType").toString()
        odds = arguments?.getDouble("odds")!!

        tv_mengdou_guess_bet.paint.flags = Paint.UNDERLINE_TEXT_FLAG

        tv_odd_guess_bet.text = odds.toString()
        tv_get_guess_bet.text = (tv_odd_guess_bet.text.toString().toDouble() * 1000).toString()
        et_guess_bet.setSelection(4)

        et_guess_bet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s)) {
                    tv_get_guess_bet.text = "0"
                    return
                }
                val toDouble = tv_odd_guess_bet.text.toString().toDouble()
                val toInt = et_guess_bet.text.toString().toInt()
                tv_get_guess_bet.text = (toDouble * toInt).toString()
            }
        })

        tv_1000_guess_bet.setOnClickListener {
            tv_get_guess_bet.text = (tv_odd_guess_bet.text.toString().toDouble() * 1000).toString()
            et_guess_bet.text = Editable.Factory.getInstance().newEditable("1000")
            et_guess_bet.setSelection(4)
        }
        tv_10000_guess_bet.setOnClickListener {
            tv_get_guess_bet.text = (tv_odd_guess_bet.text.toString().toDouble() * 10000).toString()
            et_guess_bet.text = Editable.Factory.getInstance().newEditable("10000")
            et_guess_bet.setSelection(5)
        }
        tv_50000_guess_bet.setOnClickListener {
            tv_get_guess_bet.text = (tv_odd_guess_bet.text.toString().toDouble() * 50000).toString()
            et_guess_bet.text = Editable.Factory.getInstance().newEditable("50000")
            et_guess_bet.setSelection(5)
        }
        tv_100000_guess_bet.setOnClickListener {
            tv_get_guess_bet.text = (tv_odd_guess_bet.text.toString().toDouble() * 100000).toString()
            et_guess_bet.text = Editable.Factory.getInstance().newEditable("100000")
            et_guess_bet.setSelection(6)
        }

        btn_guess_bet.setOnClickListener {
            if (et_guess_bet.text.toString().toInt() < 100) {
                toast(activity, "最小数量为100")
                return@setOnClickListener
            }
            val hashMap = HashMap<String, String>()
            hashMap["userId"] = userId.toString()
            hashMap["guessId"] = guessId.toString()
            hashMap["programId"] = programId.toString()
            hashMap["bettingType"] = bettingType
            hashMap["fee"] = et_guess_bet.text.toString()
            ApiFactory.getInstance().getApi(Api::class.java)
                    .gameGuess(ParamsUtils.getSignPramsMap(hashMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : ApiObserver<JsonElement>() {
                        override fun onSuccess(t: JsonElement?) {
                            dismissDialog()
                        }
                    })
        }
    }

    companion object {
        fun newInstance(userId: Long, guessId: Int, programId: Int, bettingType: String, odds: Double): GuessBetDialog {
            val guessDialog = GuessBetDialog()
            val bundle = Bundle()
            bundle.putLong("userId", userId)
            bundle.putInt("guessId", guessId)
            bundle.putInt("programId", programId)
            bundle.putString("bettingType", bettingType)
            bundle.putDouble("odds", odds)
            guessDialog.arguments = bundle
            return guessDialog
        }
    }
}