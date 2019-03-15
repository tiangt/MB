package com.whzl.mengbi.model.entity

/**
 * @author nobody
 * @date 2019/3/15
 */
data class GetUnreadMsgBean(
    val list: List<X>
)

data class X(
    val messageNum: Int,
    val messageType: String,
    val userId: Int
)