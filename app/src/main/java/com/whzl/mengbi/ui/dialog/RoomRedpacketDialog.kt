package com.whzl.mengbi.ui.dialog

import android.os.Bundle
import com.whzl.mengbi.R
import com.whzl.mengbi.model.entity.RoomRedpacketBean
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog
import com.whzl.mengbi.ui.dialog.base.ViewHolder
import com.whzl.mengbi.util.clickDelay
import kotlinx.android.synthetic.main.dialog_room_redpacket.*

/**
 *
 * @author nobody
 * @date 2019-09-11
 */
class RoomRedpacketDialog : BaseAwesomeDialog() {
    override fun intLayoutId(): Int {
        return R.layout.dialog_room_redpacket
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseAwesomeDialog?) {
        iv_close.clickDelay { dismissDialog() }
    }

    companion object {
        fun newInstance(jsonElement: RoomRedpacketBean): RoomRedpacketDialog {
            val roomRedpacketDialog = RoomRedpacketDialog()
            val bundle = Bundle()
            bundle.putParcelable("json", jsonElement)
            roomRedpacketDialog.arguments = bundle
            return roomRedpacketDialog
        }
    }
}