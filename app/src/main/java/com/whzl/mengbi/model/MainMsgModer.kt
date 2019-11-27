package com.whzl.mengbi.model

import com.google.gson.JsonElement
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.model.entity.ApiResult
import com.whzl.mengbi.model.entity.GetGoodMsgBean
import com.whzl.mengbi.ui.common.BaseApplication
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.Observable
import java.util.*

/**
 *
 * @author nobody
 * @date 2019/3/18
 */
class MainMsgModer {
    fun getMainMsgList(page: Int): Observable<ApiResult<GetGoodMsgBean>> {
        val userid = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)
        val params = mutableMapOf<String, String>()
        params.put("userId", userid.toString())
        params.put("page", page.toString())
        params.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE.toString())
        val goodMsg = ApiFactory.getInstance().getApi(Api::class.java)
                .getGoodMsg(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
        return goodMsg
    }

    fun updateMsgRead(messageId: Int, messageType: String): Observable<ApiResult<JsonElement>> {
        val userid = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)
        val params = mutableMapOf<String, String>()
        params.put("userId", userid.toString())
        params.put("messageId", messageId.toString())
        params.put("messageType", messageType)
        val goodMsg = ApiFactory.getInstance().getApi(Api::class.java)
                .updateMsgRead(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
        return goodMsg
    }

    fun updateMsgReadByType( messageType: String): Observable<ApiResult<JsonElement>> {
        val userid = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)
        val params = mutableMapOf<String, String>()
        params["userId"] = userid.toString()
        params["messageType"] = messageType
        return ApiFactory.getInstance().getApi(Api::class.java)
                .updateMsgReadByType(ParamsUtils.getSignPramsMap(params as HashMap<String, String>?))
    }
}