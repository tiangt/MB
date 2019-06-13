package com.whzl.mengbi.ui.dialog

import android.graphics.Paint
import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
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
    private lateinit var bettingType: String

    override fun intLayoutId(): Int {
        return R.layout.dialog_guess_bet
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        userId = arguments?.getLong("userId")!!
        guessId = arguments?.getInt("guessId")!!
        programId = arguments?.getInt("programId")!!
        bettingType = arguments?.getString("bettingType").toString()

        tv_mengdou_guess_bet.paint.flags = Paint.UNDERLINE_TEXT_FLAG
    }

    companion object {
        fun newInstance(userId: Long, guessId: Int, programId: Int, bettingType: String): GuessBetDialog {
            val guessDialog = GuessBetDialog()
            val bundle = Bundle()
            bundle.putLong("userId", userId)
            bundle.putInt("guessId", guessId)
            bundle.putInt("programId", programId)
            bundle.putString("bettingType", bettingType)
            guessDialog.arguments = bundle
            return guessDialog
        }
    }
}