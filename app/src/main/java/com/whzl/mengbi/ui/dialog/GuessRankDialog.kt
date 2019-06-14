package com.whzl.mengbi.ui.dialog

import android.support.v4.app.Fragment
import com.whzl.mengbi.R
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.GuessRankMotherFragment
import kotlinx.android.synthetic.main.dialog_guess_rank.*


/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class GuessRankDialog : BaseAwesomeDialog() {
    private var currentSelectedIndex = 0
    private lateinit var fragments: Array<Fragment>

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        fragments = arrayOf(GuessRankMotherFragment.newInstance("DSC"), GuessRankMotherFragment.newInstance("ASC"))
        val fragmentTransaction = childFragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.container_guess_rank, fragments[0])?.commit()

        ib_guess_rank.setOnClickListener {
            dismissDialog()
        }

        btn_asc.setOnClickListener {
            setTabChange(0)
        }

        btn_dsc.setOnClickListener {
            setTabChange(1)
        }
    }

    private fun setTabChange(index: Int) {
        if (index == currentSelectedIndex) {
            return
        }
        val fragmentTransaction = childFragmentManager?.beginTransaction()
        fragmentTransaction?.hide(fragments[currentSelectedIndex])
        if (fragments[index].isAdded) {
            fragmentTransaction?.show(fragments[index])
        } else {
            fragmentTransaction?.add(R.id.container_guess_rank, fragments[index])
        }
        fragmentTransaction?.commitAllowingStateLoss()
        currentSelectedIndex = index
    }

    override fun intLayoutId() = R.layout.dialog_guess_rank

    companion object {
        fun newInstance(): GuessRankDialog {
            return GuessRankDialog()
        }
    }
}