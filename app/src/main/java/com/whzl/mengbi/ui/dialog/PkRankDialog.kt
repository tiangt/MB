package com.whzl.mengbi.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.PkRankSortFragment
import com.whzl.mengbi.util.clickDelay
import kotlinx.android.synthetic.main.dialog_pk_rank.*

/**
 *
 * @author nobody
 * @date 2019-06-25
 */
class PkRankDialog : BaseAwesomeDialog() {
    override fun intLayoutId() = R.layout.dialog_pk_rank

    @SuppressLint("CommitTransaction")
    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val fragments = listOf(PkRankSortFragment.newInstance(), PkRankSortFragment.newInstance())
        val beginTransaction = childFragmentManager.beginTransaction()
        var currentIndex = arguments?.getInt("index")
        beginTransaction?.add(R.id.container_pk_rank, fragments[0])
        beginTransaction?.commitAllowingStateLoss()

        rb_wofang.clickDelay {
            if (currentIndex == 0) {
                return@clickDelay
            }
            val beginTransaction = childFragmentManager.beginTransaction()
            beginTransaction?.hide(fragments[1])
            if (fragments[0].isAdded) {
                beginTransaction?.show(fragments[0])
            } else {
                beginTransaction?.add(R.id.container_pk_rank, fragments[0])
            }
            beginTransaction?.commitAllowingStateLoss()
            currentIndex = 0
        }

        rb_duifang.clickDelay {
            if (currentIndex == 1) {
                return@clickDelay
            }
            val beginTransaction = childFragmentManager.beginTransaction()
            beginTransaction?.hide(fragments[0])
            if (fragments[1].isAdded) {
                beginTransaction?.show(fragments[1])
            } else {
                beginTransaction?.add(R.id.container_pk_rank, fragments[1])
            }
            beginTransaction?.commitAllowingStateLoss()
            currentIndex = 1
        }
    }

    companion object {
        fun newInstance(i: Int): PkRankDialog {
            val pkRankDialog = PkRankDialog()
            val bundle = Bundle()
            bundle.putInt("index", i)
            pkRankDialog.arguments = bundle
            return pkRankDialog
        }
    }
}