package com.whzl.mengbi.contract

import com.whzl.mengbi.model.entity.GetGoodMsgBean

/**
 *
 * @author nobody
 * @date 2019/3/18
 */
interface MainMsgContract {
    interface View : BaseView {
        fun onGetMsgListSuccess(getGoodMsgBean: GetGoodMsgBean)
        fun onUpdateMsgReadSuccess()
    }

    interface Presenter {
        fun getMsgList(page: Int)
        fun updateMsgRead(messageId:Int,messageType:String)
    }
}