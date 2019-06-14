package com.whzl.mengbi.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_guess_rank.*

/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class GuessRankFragment : BaseFragment<BasePresenter<BaseView>>() {
    private var data = ArrayList<Any>()

    override fun getLayoutId() = R.layout.fragment_guess_rank

    override fun init() {
        val sortType = arguments?.getString("sortType")
        initRv(recycler_guess_rank)
    }

    private fun initRv(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return if (data.size <= 3) {
                    1
                } else {
                    data.size - 3
                }
            }

            override fun getDataViewType(position: Int): Int {
                return if (position < 3) {
                    1
                } else {
                    2
                }
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                return if (viewType == 1) {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.item_3_guess_rank, parent, false)
                    TopViewHolder(inflate)
                } else {
                    val inflate = LayoutInflater.from(activity).inflate(R.layout.item_normal_guess_rank, parent, false)
                    NormalViewHolder(inflate)
                }
            }
        }
    }

    inner class TopViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {

        }

    }

    inner class NormalViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {

        }

    }

    companion object {
        fun newInstance(sortType: String?): GuessRankFragment {
            val guessRankFragment = GuessRankFragment()
            val bundle = Bundle()
            bundle.putString("sortType", sortType)
            guessRankFragment.arguments = bundle
            return guessRankFragment
        }
    }
}