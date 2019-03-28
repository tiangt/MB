package com.whzl.mengbi.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.whzl.mengbi.R
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.model.entity.AnchorWishBean
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.clickDelay
import kotlinx.android.synthetic.main.dialog_anchor_wish.*

/**
 *
 * @author nobody
 * @date 2019/3/26
 */
class AnchorWishDialog : BaseAwesomeDialog() {
    private var noteDialog: BaseAwesomeDialog? = null
    private var listDialog: BaseAwesomeDialog? = null

    companion object {
        fun newInstance(mAnchorId: Int, bean: AnchorWishBean): AnchorWishDialog {
            val anchorWishDialog = AnchorWishDialog()
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            bundle.putInt("anchorId", mAnchorId)
            anchorWishDialog.arguments = bundle
            return anchorWishDialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.dialog_anchor_wish
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        val anchorWishBean = arguments?.get("bean") as AnchorWishBean
        tv_name_anchor_wish.text = anchorWishBean.giftName
        tv_price_anchor_wish.text = "价格："
        tv_price_anchor_wish.append(LightSpanString.getLightString(anchorWishBean.giftPrice.toString(), Color.rgb(255, 236, 144)))
        tv_price_anchor_wish.append(" 萌币")

        tv_need_anchor_wish.text = "需 "
        tv_need_anchor_wish.append(LightSpanString.getLightString(anchorWishBean.totalWishCard.toString(), Color.rgb(251, 67, 131)))
        tv_need_anchor_wish.append(" 张心愿卡实现")

        tv_wealth.text = "主播奖品："
        tv_wealth.append(LightSpanString.getLightString(anchorWishBean.awardInfo.awardWealth.toString(), Color.rgb(255, 236, 113)))
        tv_wealth.append(" 萌币")

        tv_wealth_offical.text = "官方奖品："
        tv_wealth_offical.append(LightSpanString.getLightString(anchorWishBean.awardInfo.officalAwardWealth.toString(), Color.rgb(255, 236, 113)))
        tv_wealth_offical.append(" 经验礼包")

        if (anchorWishBean.awardInfo.awardPeopleNumber == -1) tv_people_num.text = "中奖人数：所有"
        else tv_people_num.text = "中奖人数：${anchorWishBean.awardInfo.awardPeopleNumber}人"

        if (anchorWishBean.awardInfo.officalAwardPeopleNumber == -1) tv_people_num_offical.text = "中奖人数：所有"
        else tv_people_num_offical.text = "中奖人数：${anchorWishBean.awardInfo.officalAwardPeopleNumber}人"

        if (anchorWishBean.awardInfo.awardType == "ORDINARY") tv_type.text = "中奖规则：普通"
        else tv_type.text = "中奖规则：随机"
        if (anchorWishBean.awardInfo.officalAwardType == "ORDINARY") tv_type_offical.text = "中奖规则：普通"
        else tv_type_offical.text = "中奖规则：随机"

        ib_note_anchor_wish.setOnClickListener {
            if (noteDialog != null && noteDialog!!.isAdded) {
                return@setOnClickListener
            }
            noteDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_anchor_wish_note)
                    .setConvertListener(object : ViewConvertListener() {
                        override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
                            holder?.setOnClickListener(R.id.iv_close_anchor_wish_note) {
                                dialog?.dismissDialog()
                            }
                        }

                    })
                    .setShowBottom(true)
                    .setDimAmount(0f)
                    .show(fragmentManager)
        }

        ib_list_anchor_wish.setOnClickListener {
            if (listDialog != null && listDialog!!.isAdded) {
                return@setOnClickListener
            }
            listDialog = AnchorWishListDialog.newInstance(arguments?.get("anchorId") as Int)
                    .setShowBottom(true)
                    .setDimAmount(0f)
                    .show(fragmentManager)
        }
    }

}