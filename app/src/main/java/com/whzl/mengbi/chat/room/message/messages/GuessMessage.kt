package com.whzl.mengbi.chat.room.message.messages

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import com.whzl.mengbi.R

import com.whzl.mengbi.chat.room.message.messageJson.GuessJson
import com.whzl.mengbi.chat.room.util.LevelUtil
import com.whzl.mengbi.chat.room.util.LightSpanString
import com.whzl.mengbi.ui.activity.LiveDisplayActivity
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder

/**
 * @author nobody
 * @date 2019-06-17
 */
class GuessMessage(context: Context, guessJson: GuessJson) : FillHolderMessage {
    private var mContext: Context = context
    private var guessJson: GuessJson = guessJson

    override fun fillHolder(holder: RecyclerView.ViewHolder) {
        val viewHolder = holder as SingleTextViewHolder
        viewHolder.textView.movementMethod = LinkMovementMethod.getInstance()
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal)
        viewHolder.textView.text = ""
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_guess_msg))
        viewHolder.textView.append(" 主播发起了一个竞猜")
        viewHolder.textView.append(LightSpanString.getLightString("【${guessJson.context.UGameGuessDto.guessTheme}】", Color.rgb(255, 109, 34)))
        viewHolder.textView.append(LightSpanString.getClickSpan(mContext, "现在去竞猜",
                Color.rgb(255, 255, 38)) {
            (mContext as LiveDisplayActivity).showGuessDialog()
        })
    }

    override fun getHolderType(): Int {
        return FillHolderMessage.SINGLE_TEXTVIEW
    }
}
