package com.whzl.mengbi.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.PKFansBean
import com.whzl.mengbi.model.entity.PKResultBean
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.ui.fragment.base.BaseFragment
import com.whzl.mengbi.util.AmountConversionUitls
import com.whzl.mengbi.util.glide.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_pk_rank_sort.*
import kotlinx.android.synthetic.main.item_pk_rank_sort.view.*

/**
 *
 * @author nobody
 * @date 2019-06-25
 */
class PkRankSortFragment : BaseFragment<BasePresenter<BaseView>>() {
    private lateinit var adapter: BaseListAdapter
    private var list = ArrayList<PKFansBean>()

    override fun getLayoutId() = R.layout.fragment_pk_rank_sort

    override fun init() {
        recycler_pk_rank.layoutManager = LinearLayoutManager(activity)
        adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return if (list.size > 3) {
                    3
                } else {
                    list.size
                }
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(context).inflate(R.layout.item_pk_rank_sort, parent, false)
                return FansViewHolder(inflate)
            }

        }
        recycler_pk_rank.adapter = adapter
    }

    inner class FansViewHolder(item: View) : BaseViewHolder(item) {
        override fun onBindViewHolder(position: Int) {
            if (position > 3) {
                return
            }
            val pkFansBean = list[position]
            when (position) {
                0 -> {
                    GlideImageLoader.getInstace().displayImage(BaseApplication.getInstance(), R.drawable.ic_1_pk_rank, itemView.iv_rank_pk_rank)
                }
                1 -> {
                    GlideImageLoader.getInstace().displayImage(BaseApplication.getInstance(), R.drawable.ic_2_pk_rank, itemView.iv_rank_pk_rank)
                }
                2 -> {
                    GlideImageLoader.getInstace().displayImage(BaseApplication.getInstance(), R.drawable.ic_3_pk_rank, itemView.iv_rank_pk_rank)
                }
            }
            GlideImageLoader.getInstace().displayImage(BaseApplication.getInstance(), pkFansBean.avatar, itemView.iv_avatar_pk_rank)
            itemView.tv_nick_pk_rank.text = pkFansBean.nickname
            itemView.tv_num_pk_rank.text = AmountConversionUitls.amountConversionFormat(pkFansBean.score)
        }

    }

    companion object {
        fun newInstance(): PkRankSortFragment {
            return PkRankSortFragment()
        }
    }

    fun setData(launchPkUserFans: ArrayList<PKFansBean>) {
        list.addAll(launchPkUserFans)
        adapter.notifyDataSetChanged()
    }

    fun setFirstUser(launchPkUserFans: PKResultBean.FirstBloodBean) {
        GlideImageLoader.getInstace().displayImage(activity, launchPkUserFans.avatar, iv_avatar_pk_rank)
        tv_nick_pk_rank.text = launchPkUserFans.nickname
    }


}