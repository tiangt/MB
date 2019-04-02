package com.whzl.mengbi.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.model.entity.AnchorWishRank
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.ui.widget.recyclerview.RecyclerViewDivider
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_anchor_wish_list.*
import java.util.*

/**
 *
 * @author nobody
 * @date 2019/3/27
 */
class AnchorWishListDialog : BaseAwesomeDialog() {
    private var mDatas = mutableListOf<AnchorWishRank.ListBean>()
    private var sendGiftPrice: Int? = 0

    companion object {
        fun newInstance(anchorId: Int, sendGiftPrice: Int): AnchorWishListDialog {
            val anchorWishListDialog = AnchorWishListDialog()
            val bundle = Bundle()
            bundle.putInt("anchorId", anchorId)
            bundle.putInt("sendGiftPrice", sendGiftPrice)
            anchorWishListDialog.arguments = bundle
            return anchorWishListDialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_anchor_wish_list
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        sendGiftPrice = arguments?.getInt("sendGiftPrice")
        holder?.setOnClickListener(R.id.iv_close_anchor_wish_list) {
            dialog?.dismissDialog()
        }
        initRv()
        loadData()
    }

    private fun loadData() {
        val param = HashMap<String, String>()
        param["userId"] = arguments?.getInt("anchorId").toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .anchorWishRank(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<AnchorWishRank>() {
                    override fun onSuccess(jsonElement: AnchorWishRank) {
                        mDatas.addAll(jsonElement.list)
                        recycler.adapter.notifyDataSetChanged()
                    }
                })
    }

    private fun initRv() {
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler.setHasFixedSize(true)
        recycler.addItemDecoration(RecyclerViewDivider(activity, LinearLayoutManager.HORIZONTAL, R.drawable.divider_anchor_wish))
        recycler.adapter = object : BaseListAdapter() {
            override fun getDataCount(): Int {
                return if (mDatas.size > 8) 8 else mDatas.size
            }

            override fun onCreateNormalViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val inflate = LayoutInflater.from(activity).inflate(R.layout.item_anchor_wish, parent, false)
                return ListViewHolder(inflate)
            }

        }
    }

    internal inner class ListViewHolder(view: View) : BaseViewHolder(view) {
        private var tvPosition: TextView? = null
        private var tvName: TextView? = null
        private var tvNum: TextView? = null

        init {
            tvPosition = view.findViewById(R.id.tv_position)
            tvName = view.findViewById(R.id.tv_name)
            tvNum = view.findViewById(R.id.tv_num)
        }

        override fun onBindViewHolder(position: Int) {
            val listBean = mDatas[position]
            when (position) {
                0 -> tvPosition?.setBackgroundResource(R.drawable.ic_anchor_wish_1)
                1 -> tvPosition?.setBackgroundResource(R.drawable.ic_anchor_wish_2)
                2 -> tvPosition?.setBackgroundResource(R.drawable.ic_anchor_wish_3)
                else -> tvPosition?.text = (position + 1).toString()
            }

            tvName?.text = listBean.nickName
            tvNum?.text = "帮主播完成了 "
            val i: Int = Math.ceil(listBean.score * 100 / sendGiftPrice!!).toInt()
            tvNum?.append(LightSpanString.getLightString("$i%", Color.rgb(126, 105, 180)))
            tvNum?.append(" 的心愿")
        }
    }


}