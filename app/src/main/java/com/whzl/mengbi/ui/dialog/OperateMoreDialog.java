package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2018.12.5
 */
public class OperateMoreDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_option_container)
    LinearLayout llOptionContainer;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    @BindView(R.id.tv_global)
    TextView tvGlobal;
    @BindView(R.id.tv_kick_out)
    TextView tvKick;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    @BindView(R.id.tv_report)
    TextView tvReport;

    private long visitorId;
    private long mUserId;
    private int mProgremId;
    private RoomUserInfo.DataBean mUser;
    private RoomUserInfo.DataBean mViewedUser;

    public static OperateMoreDialog newInstance(long userId, long visitorId, int programId, RoomUserInfo.DataBean user) {
        Bundle args = new Bundle();
        args.putLong("visitorId", visitorId);
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        args.putParcelable("user", user);
        OperateMoreDialog dialog = new OperateMoreDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_operate_more;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUser = getArguments().getParcelable("user");
        visitorId = getArguments().getLong("visitorId");
        mUserId = getArguments().getLong("userId");
        mProgremId = getArguments().getInt("programId");
        getUserInfo(mUserId, mProgremId, visitorId);
        if (mUser == null || mUser.getUserId() <= 0 || mUser.getUserId() == visitorId) {
            llOptionContainer.setVisibility(View.GONE);
            return;
        }

        if (visitorId == 0) {
            if (mUser != null) {
                if (mUser.getIdentityId() == UserIdentity.ROOM_MANAGER
                        || mUser.getIdentityId() == UserIdentity.OPTR_MANAGER
                        || mUser.getIdentityId() == UserIdentity.ANCHOR) {
                    llOptionContainer.setVisibility(View.VISIBLE);
                    return;
                }
            }
            llOptionContainer.setVisibility(View.GONE);
            return;
        }
    }

    private void getUserInfo(long userId, int programId, long visitorId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        paramsMap.put("visitorId", visitorId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                if (isDetached() || getContext() == null) {
                    return;
                }
                RoomUserInfo roomUserInfoData = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfoData.getCode() == 200) {
                    if (roomUserInfoData.getData() != null) {
                        mViewedUser = roomUserInfoData.getData();
                        setupView(mViewedUser);
                    }
                    if (mUser == null || mUser.getUserId() <= 0 || mUser.getUserId() == visitorId) {
                        return;
                    }
                    setupOperations();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setupView(RoomUserInfo.DataBean user) {

    }

    private void setupOperations() {
        if (mUser.getIdentityId() == UserIdentity.OPTR_MANAGER) {
            if (mViewedUser.getIdentityId() != UserIdentity.ANCHOR
                    && mViewedUser.getIdentityId() != UserIdentity.OPTR_MANAGER) {
                llOptionContainer.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (mUser.getIdentityId() == UserIdentity.ANCHOR) {
            if (mViewedUser.getIdentityId() != UserIdentity.OPTR_MANAGER) {
                llOptionContainer.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (mUser.getIdentityId() == UserIdentity.ROOM_MANAGER) {
            if (mViewedUser.getIdentityId() != UserIdentity.OPTR_MANAGER
                    && mViewedUser.getIdentityId() != UserIdentity.ANCHOR
                    && mViewedUser.getIdentityId() != UserIdentity.ROOM_MANAGER) {
                llOptionContainer.setVisibility(View.VISIBLE);
            }
            return;
        }

    }

    /**
     * 用户服务器操作
     * 服务代码：MUTE禁言，GLOBAL_MUTE全局禁言，KICK踢人，CANCEL_MUTE取消禁言，CANCEL_GLOBAL_MUTE取消全局禁言
     */
    private void serverOperate() {

    }
}