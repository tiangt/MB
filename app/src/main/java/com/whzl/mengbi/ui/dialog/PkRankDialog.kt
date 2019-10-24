package com.whzl.mengbi.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.model.entity.PKResultBean
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.fragment.PkRankSortFragment
import com.whzl.mengbi.util.clickDelay
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_pk_rank.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019-06-25
 */
class PkRankDialog : BaseAwesomeDialog() {
    private lateinit var pkRankSortFragment: PkRankSortFragment
    private lateinit var pkRankSortFragment2: PkRankSortFragment

    override fun intLayoutId() = R.layout.dialog_pk_rank

    @SuppressLint("CommitTransaction")
    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val programId = arguments?.getInt("mProgramId")
        pkRankSortFragment = PkRankSortFragment.newInstance()
        pkRankSortFragment2 = PkRankSortFragment.newInstance()
        val fragments = listOf(pkRankSortFragment, pkRankSortFragment2)
        val beginTransaction = childFragmentManager.beginTransaction()
        var currentIndex = arguments?.getInt("index")
        if (currentIndex == 0) {
            beginTransaction?.add(R.id.container_pk_rank, fragments[1])
            beginTransaction?.hide(fragments[1])
            beginTransaction?.add(R.id.container_pk_rank, fragments[0])
        } else {
            beginTransaction?.add(R.id.container_pk_rank, fragments[0])
            beginTransaction?.hide(fragments[0])
            beginTransaction?.add(R.id.container_pk_rank, fragments[1])
        }

        beginTransaction?.commitAllowingStateLoss()

        if (currentIndex == 0) {
            rb_wofang.isChecked = true
        } else if (currentIndex == 1) {
            rb_duifang.isChecked = true
        }

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

        iv_close_pk_rank.clickDelay {
            dismissDialog()
        }

        loadData(programId!!)
    }

    private fun loadData(programId: Int) {
        val map = HashMap<String, String>()
        map["programId"] = programId.toString()
        val signPramsMap = ParamsUtils.getSignPramsMap(map)
        ApiFactory.getInstance().getApi(Api::class.java)
                .pkInfo(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<PKResultBean>() {
                    override fun onSuccess(t: PKResultBean?) {
                        if (programId == t?.launchPkUserProgramId) {
                            pkRankSortFragment.setData(t.launchPkUserFans)
                            pkRankSortFragment2.setData(t.pkUserFans)
                        } else {
                            pkRankSortFragment.setData(t?.pkUserFans!!)
                            pkRankSortFragment2.setData(t.launchPkUserFans)
                        }

                    }
                })
    }


    companion object {
        fun newInstance(i: Int, mProgramId: Int): PkRankDialog {
            val pkRankDialog = PkRankDialog()
            val bundle = Bundle()
            bundle.putInt("index", i)
            bundle.putInt("mProgramId", mProgramId)
            pkRankDialog.arguments = bundle
            return pkRankDialog
        }
    }
}