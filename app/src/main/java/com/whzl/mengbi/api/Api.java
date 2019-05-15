package com.whzl.mengbi.api;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorInfo;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AnchorWishBean;
import com.whzl.mengbi.model.entity.AnchorWishRank;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.AppDataBean;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.BillAwardBean;
import com.whzl.mengbi.model.entity.BillGiftBean;
import com.whzl.mengbi.model.entity.BillPayBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.BroadCastNumBean;
import com.whzl.mengbi.model.entity.CheckMsgRemindBean;
import com.whzl.mengbi.model.entity.DailyTaskBean;
import com.whzl.mengbi.model.entity.DemonCarBean;
import com.whzl.mengbi.model.entity.FollowSortBean;
import com.whzl.mengbi.model.entity.FreeGiftBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GetGoodMsgBean;
import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.GetPrettyBean;
import com.whzl.mengbi.model.entity.GetProsListBean;
import com.whzl.mengbi.model.entity.GetUnReadMsgBean;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.model.entity.GetVipPriceBean;
import com.whzl.mengbi.model.entity.GiftBetPeriodInfo;
import com.whzl.mengbi.model.entity.GiftBetRecordsBean;
import com.whzl.mengbi.model.entity.GoodNumBean;
import com.whzl.mengbi.model.entity.GoodsPriceBatchBean;
import com.whzl.mengbi.model.entity.GuardPriceBean;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.ImgUploadBean;
import com.whzl.mengbi.model.entity.JumpRandomRoomBean;
import com.whzl.mengbi.model.entity.ModifyNameCardBean;
import com.whzl.mengbi.model.entity.MyCouponBean;
import com.whzl.mengbi.model.entity.NewTaskBean;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.model.entity.PackcarBean;
import com.whzl.mengbi.model.entity.PackvipBean;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.model.entity.ProgramInfoByAnchorBean;
import com.whzl.mengbi.model.entity.PropBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.QueryBagByGoodsTypeBean;
import com.whzl.mengbi.model.entity.QuickChannelBean;
import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.model.entity.RechargeOrderBean;
import com.whzl.mengbi.model.entity.RetroInfoBean;
import com.whzl.mengbi.model.entity.RoomAnnouceBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomUserBean;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.RunWayValueBean;
import com.whzl.mengbi.model.entity.SearchAnchorBean;
import com.whzl.mengbi.model.entity.SignAwardBean;
import com.whzl.mengbi.model.entity.SignInfoBean;
import com.whzl.mengbi.model.entity.SystemConfigBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
import com.whzl.mengbi.model.entity.UpdownAnchorBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.WatchHistoryListBean;
import com.whzl.mengbi.model.entity.WeekRankBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * @author shaw
 * @date 2018/7/27
 */
public interface Api {


    /**
     * 观看记录
     *
     * @param params
     * @return
     */
    @POST("v1/user/watch-record")
    @FormUrlEncoded
    Observable<ApiResult<WatchHistoryListBean>> getWatchHistory(@FieldMap Map<String, String> params);

    /**
     * 背包
     *
     * @param params
     * @return
     */
    @POST("v1/my/query-bag-by-goods-type")
    @FormUrlEncoded
    Observable<ApiResult<BackpackListBean>> getBackpack(@FieldMap Map<String, String> params);

    /**
     * 跑道
     *
     * @param params
     * @return
     */
    @POST("v1/room/runway")
    @FormUrlEncoded
    Observable<ApiResult<RunWayListBean>> getRunWayList(@FieldMap Map<String, String> params);

    /**
     * 守护列表
     *
     * @param params
     * @return
     */
    @POST("v1/room/guard-self-list")
    @FormUrlEncoded
    Observable<ApiResult<GuardListBean>> getGuardList(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/room/online")
    @FormUrlEncoded
    Observable<ApiResult<GuardListBean>> getAudienceList(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-guard-prices")
    @FormUrlEncoded
    Observable<ApiResult<GuardPriceBean>> getGuardPrice(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/consume/mall-buy")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> buy(@FieldMap Map<String, String> params);

    /**
     * 意见反馈接口
     *
     * @param params
     * @return
     */
    @POST("v1/feedback/feedback")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> feedback(@FieldMap Map<String, String> params);

    /**
     * 用户服务器操作
     *
     * @param params
     * @return
     */
    @POST("v1/room/user-server-operate")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> serverOprate(@FieldMap Map<String, String> params);

    /**
     * 升房管
     *
     * @param params
     * @return
     */
    @POST("v1/room/set-manager")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> setManager(@FieldMap Map<String, String> params);

    /**
     * 取消房管
     *
     * @param params
     * @return
     */
    @POST("v1/room/remove-manager")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> cancleManager(@FieldMap Map<String, String> params);


    /**
     * 上传头像
     *
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("v1/user/modify-avatar")
    Observable<ApiResult<JsonElement>> uploadFile(@PartMap Map<String, RequestBody> params,
                                                  @Part MultipartBody.Part parts);

    /**
     * 取消房管
     *
     * @param params
     * @return
     */
    @POST("v1/rank/rank")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> getRankList(@FieldMap Map<String, String> params);

    /**
     * 场榜第一
     *
     * @param params
     * @return
     */
    @POST("v1/rank/room-rank-one")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> getProgramFirst(@FieldMap Map<String, String> params);

    /**
     * app
     *
     * @param params
     * @return
     */
    @POST("v1/common/get-app-html")
    @FormUrlEncoded
    Observable<ApiResult<AppDataBean>> getImgUrl(@FieldMap Map<String, String> params);


    /**
     * 在线宝箱状态信息
     *
     * @param params
     * @return
     */
    @POST("v1/activity/online-box-status")
    @FormUrlEncoded
    Observable<ApiResult<TreasureBoxStatusBean>> getTreasureBoxStatus(@FieldMap Map<String, String> params);

    /**
     * 在线宝箱状态信息
     *
     * @param params
     * @return
     */
    @POST("v1/activity/online-receive")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> receiveTreasure(@FieldMap Map<String, String> params);

    /**
     * 获取用户详情接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/get-user-info")
    @FormUrlEncoded
    Observable<ApiResult<UserInfo.DataBean>> getUserInfo(@FieldMap Map<String, String> params);

    /**
     * 靓号接口
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-prettys")
    @FormUrlEncoded
    Observable<ApiResult<GoodNumBean>> getPrettys(@FieldMap Map<String, String> params);

    /**
     * 获取守护价格接口
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-vip-prices")
    @FormUrlEncoded
    Observable<ApiResult<GetVipPriceBean>> getVipprices(@FieldMap Map<String, String> params);

    /**
     * 商城购买接口
     *
     * @param params
     * @return
     */
    @POST("v1/consume/mall-buy")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> mallBuy(@FieldMap Map<String, String> params);

    /**
     * 直播间的基本信息
     *
     * @param params
     * @return
     */
    @POST("v1/room/get-room-info")
    @FormUrlEncoded
    Observable<ApiResult<AnchorInfo>> getRoomInfo(@FieldMap Map<String, String> params);

    /**
     * 主页搜索
     *
     * @param params
     * @return
     */
    @POST("v1/anchor/search")
    @FormUrlEncoded
    Observable<ApiResult<SearchAnchorBean>> anchorSearch(@FieldMap Map<String, String> params);

    /**
     * 直播间的活动（转盘，游戏，等）
     *
     * @param params
     * @return
     */
    @POST("v1/room/activity-list")
    @FormUrlEncoded
    Observable<ApiResult<GetActivityBean>> activityList(@FieldMap Map<String, String> params);

    /**
     * 当前房间PK信息
     *
     * @param params
     * @return
     */
    @POST("v1/pk/pk-info")
    @FormUrlEncoded
    Observable<ApiResult<PKResultBean>> pkInfo(@FieldMap Map<String, String> params);

    /**
     * 我的服务（道具）
     *
     * @param params
     * @return
     */
    @POST("v1/my/service")
    @FormUrlEncoded
    Observable<ApiResult<PropBean>> myService(@FieldMap Map<String, String> params);

    /**
     * 我的VIP
     *
     * @param params
     * @return
     */
    @POST("v1/my/vip")
    @FormUrlEncoded
    Observable<ApiResult<PackvipBean>> myVip(@FieldMap Map<String, String> params);

    /**
     * 我的座驾
     *
     * @param params
     * @return
     */
    @POST("v1/my/card")
    @FormUrlEncoded
    Observable<ApiResult<PackcarBean>> myCard(@FieldMap Map<String, String> params);

    /**
     * 装备物品
     *
     * @param params
     * @return
     */
    @POST("v1/my/equip")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> equip(@FieldMap Map<String, String> params);

    /**
     * VIP每日奖励
     *
     * @param params
     * @return
     */
    @POST("v1/my/vip-award")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> vipAward(@FieldMap Map<String, String> params);

    /**
     * 我的服务（道具）
     *
     * @param params
     * @return
     */
    @POST("v1/my/coupon")
    @FormUrlEncoded
    Observable<ApiResult<MyCouponBean>> myCoupon(@FieldMap Map<String, String> params);

    /**
     * 我的靓号
     *
     * @param params
     * @return
     */
    @POST("v1/my/pretty")
    @FormUrlEncoded
    Observable<ApiResult<PackPrettyBean>> pretty(@FieldMap Map<String, String> params);

    /**
     * 靓号价格接口
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-pretty-prices")
    @FormUrlEncoded
    Observable<ApiResult<GetPrettyBean>> getprettyprices(@FieldMap Map<String, String> params);

    /**
     * 新手任务活动信息
     *
     * @param params
     * @return
     */
    @POST("v1/activity/user-task")
    @FormUrlEncoded
    Observable<ApiResult<NewTaskBean>> newTask(@FieldMap Map<String, String> params);

    /**
     * 通过奖励序列号领奖
     *
     * @param params
     * @return
     */
    @POST("v1/activity/receive")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> receive(@FieldMap Map<String, String> params);

    /**
     * 修改昵称接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/modify-nickname")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> nickName(@FieldMap Map<String, String> params);

    /**
     * 新手任务可领任务数量
     *
     * @param params
     * @return
     */
    @POST("v1/activity/new-task-num")
    @FormUrlEncoded
    Observable<ApiResult<GetNewTaskBean>> getNewTask(@FieldMap Map<String, String> params);

    /**
     * 随机获取一个正在直播的主播
     *
     * @param params
     * @return
     */
    @POST("v1/anchor/random")
    @FormUrlEncoded
    Observable<ApiResult<JumpRandomRoomBean>> random(@FieldMap Map<String, String> params);

    /**
     * 我的消费记录
     *
     * @param params
     * @return
     */
    @POST("v1/bill/consume")
    @FormUrlEncoded
    Observable<ApiResult<BillPayBean>> billConsume(@FieldMap Map<String, String> params);

    /**
     * 我的送礼记录
     *
     * @param params
     * @return
     */
    @POST("v1/bill/send-gift")
    @FormUrlEncoded
    Observable<ApiResult<BillGiftBean>> sendGift(@FieldMap Map<String, String> params);

    /**
     * 我的奖励记录
     *
     * @param params
     * @return
     */
    @POST("v1/bill/award")
    @FormUrlEncoded
    Observable<ApiResult<BillAwardBean>> billAward(@FieldMap Map<String, String> params);

    /**
     * 直播间的活动（常规活动）
     *
     * @param params
     * @return
     */
    @POST("v1/room/activity-grand")
    @FormUrlEncoded
    Observable<ApiResult<ActivityGrandBean>> activityGrand(@FieldMap Map<String, String> params);

    /**
     * 有效返利券查找
     *
     * @param params
     * @return
     */
    @POST("v1/recharge/find-coupon")
    @FormUrlEncoded
    Observable<ApiResult<RebateBean>> findCoupon(@FieldMap Map<String, String> params);

    /**
     * 主播展示
     *
     * @param params
     * @return
     */
    @POST("v1/anchor/show-anchor")
    @FormUrlEncoded
    Observable<ApiResult<RebateBean>> getAnchorInfo(@FieldMap Map<String, String> params);

    /**
     * 直播间的守护人数
     *
     * @param params
     * @return
     */
    @POST("v1/room/total-guard")
    @FormUrlEncoded
    Observable<ApiResult<GuardTotalBean>> getTotalGuard(@FieldMap Map<String, String> params);

    /**
     * 获取惩罚方式
     *
     * @param params
     * @return
     */
    @POST("v1/pk/punish-ways")
    @FormUrlEncoded
    Observable<ApiResult<PunishWaysBean>> getPunishWays(@FieldMap Map<String, String> params);

    /**
     * 直播间指定类型的榜单的总值
     *
     * @param params
     * @return
     */
    @POST("v1/rank/room-rank-total")
    @FormUrlEncoded
    Observable<ApiResult<RoomRankTotalBean>> getRoomRankTotal(@FieldMap Map<String, String> params);

    /**
     * 直播间指定类型的榜单的总值
     *
     * @param params
     * @return
     */
    @POST("v1/rank/week-personal-ranking")
    @FormUrlEncoded
    Observable<ApiResult<WeekRankBean>> getWeekRank(@FieldMap Map<String, String> params);

    /**
     * 主播任务
     *
     * @param params
     * @return
     */
    @POST("v1/activity/anchor-task")
    @FormUrlEncoded
    Observable<ApiResult<AnchorTaskBean>> getAnchorTask(@FieldMap Map<String, String> params);

    /**
     * 获取用户广播数量
     *
     * @param params
     * @return
     */
    @POST("v1/consume/broadcast-num")
    @FormUrlEncoded
    Observable<ApiResult<BroadCastNumBean>> getBroadNum(@FieldMap Map<String, String> params);

    /**
     * 发送广播
     *
     * @param params
     * @return
     */
    @POST("v1/consume/send-broadcast")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> sendBroadcast(@FieldMap Map<String, String> params);

    /**
     * 每日任务
     *
     * @param params
     * @return
     */
    @POST("v1/activity/daily-task")
    @FormUrlEncoded
    Observable<ApiResult<DailyTaskBean>> dailyTask(@FieldMap Map<String, String> params);

    /**
     * 每日奖励领取
     *
     * @param params
     * @return
     */
    @POST("v1/activity/daily-task-receive")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> dailyTaskReceive(@FieldMap Map<String, String> params);

    /**
     * 获取守护价格接口
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-demon-car-prices")
    @FormUrlEncoded
    Observable<ApiResult<DemonCarBean>> getDemonCar(@FieldMap Map<String, String> params);

    /**
     * 每日奖励领取状态
     *
     * @param params
     * @return
     */
    @POST("v1/activity/daily-task-is-receive")
    @FormUrlEncoded
    Observable<ApiResult<GetDailyTaskStateBean>> getDailyTaskState(@FieldMap Map<String, String> params);

    /**
     * 直播间用户信息
     *
     * @param params
     * @return
     */
    @POST("v1/room/room-user")
    @FormUrlEncoded
    Observable<ApiResult<RoomUserBean>> roomUser(@FieldMap Map<String, String> params);


    /**
     * 守护列表接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/get-guards-programs")
    @FormUrlEncoded
    Observable<ApiResult<FollowSortBean>> getGuardProgram(@FieldMap Map<String, String> params);

    /**
     * 管理列表接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/get-manager-programs")
    @FormUrlEncoded
    Observable<ApiResult<FollowSortBean>> getManageProgram(@FieldMap Map<String, String> params);

    /**
     * 直播间当前跑道的萌币数
     *
     * @param params
     * @return
     */
    @POST("v1/room/runway-value")
    @FormUrlEncoded
    Observable<ApiResult<RunWayValueBean>> getRunWayValue(@FieldMap Map<String, String> params);


    /**
     * 主播的头条排名
     *
     * @param params
     * @return
     */
    @POST("v1/rank/head-line-rank")
    @FormUrlEncoded
    Observable<ApiResult<HeadlineRankBean>> getHeadlineRank(@FieldMap Map<String, String> params);

    /**
     * 获取主播总的胜负平次数
     *
     * @param params
     * @return
     */
    @POST("v1/pk/pk-times")
    @FormUrlEncoded
    Observable<ApiResult<PkTimeBean>> getPkTimes(@FieldMap Map<String, String> params);

    /**
     * 获取主播最近的pk记录
     *
     * @param params
     * @return
     */
    @POST("v1/pk/pk-record-list")
    @FormUrlEncoded
    Observable<ApiResult<PkRecordListBean>> getPkRecordList(@FieldMap Map<String, String> params);

    /**
     * 获取主播的小黑屋时间
     *
     * @param params
     * @return
     */
    @POST("v1/pk/block-room-time")
    @FormUrlEncoded
    Observable<ApiResult<BlackRoomTimeBean>> getRoomTime(@FieldMap Map<String, String> params);

    /**
     * 从小黑屋中解救主播
     *
     * @param params
     * @return
     */
    @POST("v1/pk/rescue")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> rescue(@FieldMap Map<String, String> params);

    /**
     * 观看记录接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/clear-watch-record")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> clearWatchRecord(@FieldMap Map<String, String> params);

    /**
     * 根据物品id获取物品的价格信息
     *
     * @param params
     * @return
     */
    @POST("v1/goods/goods-price-batch")
    @FormUrlEncoded
    Observable<ApiResult<GoodsPriceBatchBean>> goodsPriceBatch(@FieldMap Map<String, String> params);

    /**
     * 根据免费礼物goodsId列表
     *
     * @param params
     * @return
     */
    @POST("v1/goods/free-goods-ids")
    @FormUrlEncoded
    Observable<ApiResult<FreeGiftBean>> freeGoodsIds(@FieldMap Map<String, String> params);


    /**
     * 获取用户偏好设置接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/get-user-set")
    @FormUrlEncoded
    Observable<ApiResult<GetUserSetBean>> getUserSet(@FieldMap Map<String, String> params);

    /**
     * 获取用户偏好设置接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/add-user-set")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> addUserSet(@FieldMap Map<String, String> params);

    /**
     * 发送红包接口
     *
     * @param params
     * @return
     */
    @POST("v1/redpacket/send-redpacket")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> sendRedPacket(@FieldMap Map<String, String> params);

    /**
     * 查询直播间红包池金额
     *
     * @param params
     * @return
     */
    @POST("v1/redpacket/room-redpacket-treasure")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> getRoomRedpacketTreasure(@FieldMap Map<String, String> params);

    /**
     * 查询直播间红包列表
     *
     * @param params
     * @return
     */
    @POST("v1/redpacket/room-redpacket-list")
    @FormUrlEncoded
    Observable<ApiResult<RoomRedpackList>> getRoomRedpacketList(@FieldMap Map<String, String> params);

    /**
     * 根据主播id查询房间信息
     *
     * @param params
     * @return
     */
    @POST("v1/anchor/program-info-by-anchorid")
    @FormUrlEncoded
    Observable<ApiResult<ProgramInfoByAnchorBean>> programInfoByAnchorid(@FieldMap Map<String, String> params);

    /**
     * 分页获取靓号信息
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-prettys-by-page")
    @FormUrlEncoded
    Observable<ApiResult<GoodNumBean>> getPrettysByPage(@FieldMap Map<String, String> params);

    /**
     * 获取道具列表
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-pros-list")
    @FormUrlEncoded
    Observable<ApiResult<GetProsListBean>> getProsList(@FieldMap Map<String, String> params);

    /**
     * 获取夺宝游戏中当前期的信息
     *
     * @param params
     * @return
     */
    @POST("v1/game/gift-bet-period-info")
    @FormUrlEncoded
    Observable<ApiResult<GiftBetPeriodInfo>> giftBetPeriodInfo(@FieldMap Map<String, String> params);

    /**
     * 夺宝获奖记录
     *
     * @param params
     * @return
     */
    @POST("v1/game/gift-bet-records")
    @FormUrlEncoded
    Observable<ApiResult<GiftBetRecordsBean>> giftBetRecords(@FieldMap Map<String, String> params);

    /**
     * 夺宝下注
     *
     * @param params
     * @return
     */
    @POST("v1/game/gift-bet")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> giftBet(@FieldMap Map<String, String> params);

    /**
     * 直播间的活动（原生活动）
     *
     * @param params
     * @return
     */
    @POST("v1/room/activity-native")
    @FormUrlEncoded
    Observable<ApiResult<GetActivityBean>> activityNative(@FieldMap Map<String, String> params);

    /**
     * 获取所有大消息类型的未读消息数
     *
     * @param params
     * @return
     */
    @POST("v1/msgcenter/get-unread-msg-num")
    @FormUrlEncoded
    Observable<ApiResult<GetUnReadMsgBean>> getUnreadMsg(@FieldMap Map<String, String> params);

    /**
     * 获取所有大消息类型的未读消息数
     *
     * @param params
     * @return
     */
    @POST("v1/msgcenter/get-goods-msg")
    @FormUrlEncoded
    Observable<ApiResult<GetGoodMsgBean>> getGoodMsg(@FieldMap Map<String, String> params);

    /**
     * 获取所有大消息类型的未读消息数
     *
     * @param params
     * @return
     */
    @POST("v1/msgcenter/update-msg-read")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> updateMsgRead(@FieldMap Map<String, String> params);

    /**
     * 消息小红点
     *
     * @param params
     * @return
     */
    @POST("v1/msgcenter/check-msg-num")
    @FormUrlEncoded
    Observable<ApiResult<CheckMsgRemindBean>> checkMsgRemind(@FieldMap Map<String, String> params);

    /**
     * 修改用户基本信息接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/modify-user-info")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> modifyUserInfo(@FieldMap Map<String, String> params);

    /**
     * 获取消息小红点一键已读
     *
     * @param params
     * @return
     */
    @POST("v1/msgcenter/reset-msg-notify")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> resetMsgNotify(@FieldMap Map<String, String> params);

    /**
     * 获取主播的心愿
     *
     * @param params
     * @return
     */
    @POST("v1/room/anchor-wish-gift")
    @FormUrlEncoded
    Observable<ApiResult<AnchorWishBean>> anchorWishGift(@FieldMap Map<String, String> params);

    /**
     * 获取主播心愿榜单
     *
     * @param params
     * @return
     */
    @POST("v1/rank/anchor-wish-rank")
    @FormUrlEncoded
    Observable<ApiResult<AnchorWishRank>> anchorWishRank(@FieldMap Map<String, String> params);

    /**
     * 根据goodsType查询用户相关物品
     *
     * @param params
     * @return
     */
    @POST("v1/my/query-bag-by-goods-type")
    @FormUrlEncoded
    Observable<ApiResult<QueryBagByGoodsTypeBean>> queryBagByGoodsType(@FieldMap Map<String, String> params);

    /**
     * 上下拉获取主播列表
     *
     * @param params
     * @return
     */
    @POST("v1/anchor/up-down-anchors")
    @FormUrlEncoded
    Observable<ApiResult<UpdownAnchorBean>> updownAnchor(@FieldMap Map<String, String> params);

    /**
     * 通过Key获取后台配置的一些参数
     *
     * @param params
     * @return
     */
    @POST("v1/common/key-to-value")
    @FormUrlEncoded
    Observable<ApiResult<SystemConfigBean>> systemConfig(@FieldMap Map<String, String> params);

    /**
     * 获取直播间公告
     *
     * @param params
     * @return
     */
    @POST("v1/room/room-announcement")
    @FormUrlEncoded
    Observable<ApiResult<RoomAnnouceBean>> roomAnnounce(@FieldMap Map<String, String> params);

    /**
     * 移动端快捷支付充值规则接口
     *
     * @param params
     * @return
     */
    @POST("v1/recharge/quick-channel")
    @FormUrlEncoded
    Observable<ApiResult<QuickChannelBean>> quickChannel(@FieldMap Map<String, String> params);

    /**
     * 统一下单接口
     *
     * @param params
     * @return
     */
    @POST("v1/recharge/order")
    @FormUrlEncoded
    Observable<ApiResult<RechargeOrderBean>> rechargeOrder(@FieldMap Map<String, String> params);

    /**
     * 根据旧密码修改密码接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/edit-password")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> editPassword(@FieldMap Map<String, String> params);

    /**
     * 上传图片接口
     *
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("v1/upload/img-upload")
    Observable<ApiResult<ImgUploadBean>> imgUpload(@PartMap Map<String, RequestBody> params,
                                                   @Part MultipartBody.Part parts);

    /**
     * 开启pk经验卡
     *
     * @param params
     * @return
     */
    @POST("v1/pk/open-exp-card")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> expOpenCard(@FieldMap Map<String, String> params);

    /**
     * 获取签到信息
     *
     * @param params
     * @return
     */
    @POST("v1/sign/user-sign-info")
    @FormUrlEncoded
    Observable<ApiResult<SignInfoBean>> signInfo(@FieldMap Map<String, String> params);

    /**
     * 用户签到
     *
     * @param params
     * @return
     */
    @POST("v1/sign/user-sign")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> sign(@FieldMap Map<String, String> params);


    /**
     * 用户签到
     *
     * @param params
     * @return
     */
    @POST("v1/sign/user-retroactive")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> retroactive(@FieldMap Map<String, String> params);

    /**
     * 获取补签消费的财富信息
     *
     * @param params
     * @return
     */
    @POST("v1/sign/retroactive-consume-info")
    @FormUrlEncoded
    Observable<ApiResult<RetroInfoBean>> retroactiveInfo(@FieldMap Map<String, String> params);

    /**
     * 一键下线用户
     *
     * @param params
     * @return
     */
    @POST("v1/user/one-key-offline")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> offLine(@FieldMap Map<String, String> params);

    /**
     * 获取全勤的奖励
     *
     * @param params
     * @return
     */
    @POST("v1/sign/query-sign-award")
    @FormUrlEncoded
    Observable<ApiResult<SignAwardBean>> signAward(@FieldMap Map<String, String> params);

    /**
     * 保存观看记录接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/save-watch-record")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> saveWatchRecord(@FieldMap Map<String, String> params);

    /**
     * 根据节目id批量获取节目信息
     *
     * @param params
     * @return
     */
    @POST("v1/program/program-info-batch")
    @FormUrlEncoded
    Observable<ApiResult<FollowSortBean>> programInfoBatch(@FieldMap Map<String, String> params);

    /**
     * 查询用户改名卡信息
     *
     * @param params
     * @return
     */
    @POST("v1/my/query-modify-name-card")
    @FormUrlEncoded
    Observable<ApiResult<ModifyNameCardBean>> modifyNameCard(@FieldMap Map<String, String> params);

}
