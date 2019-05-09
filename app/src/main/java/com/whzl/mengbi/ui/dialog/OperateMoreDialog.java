package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.UserIdentity.ROOM_MANAGER;

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
    @BindView(R.id.ll_offline)
    LinearLayout llOffline;

    private long visitorId;
    private long mUserId;
    private int mProgramId;
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
        mProgramId = getArguments().getInt("programId");
        getUserInfo(mUserId, mProgramId, visitorId);
        if (mUser == null || mUser.getUserId() <= 0 || mUser.getUserId() == visitorId) {
            llOptionContainer.setVisibility(View.GONE);
        }

        if (mUser != null) {
            if (mUser.getIdentityId() == UserIdentity.ROOM_MANAGER
                    || mUser.getIdentityId() == UserIdentity.OPTR_MANAGER
                    || mUser.getIdentityId() == UserIdentity.ANCHOR) {
                llOptionContainer.setVisibility(View.VISIBLE);
            }
            if (mUser.getIdentityId() == UserIdentity.OPTR_MANAGER) {
                llOffline.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick({R.id.tv_cancel, R.id.tv_room, R.id.tv_global, R.id.tv_kick_out,
            R.id.tv_manager, R.id.tv_report, R.id.ll_offline})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_room:
                //本房间禁言
                if (mViewedUser != null && mViewedUser.getDisabledService() != null) {
                    for (int i = 0; i < mViewedUser.getDisabledService().size(); i++) {
                        if (2 == (mViewedUser.getDisabledService().get(i))) {
                            serverOperate("CANCEL_MUTE");
                            dismiss();
                            return;
                        }
                    }
                }
                serverOperate("MUTE");
                dismiss();
                break;
            case R.id.tv_global:
                //全局禁言
                if (mViewedUser != null && mViewedUser.getDisabledService() != null) {
                    for (int i = 0; i < mViewedUser.getDisabledService().size(); i++) {
                        if (2 == (mViewedUser.getDisabledService().get(i))) {
                            serverOperate("CANCEL_GLOBAL_MUTE");
                            dismiss();
                            return;
                        }
                    }
                }
                serverOperate("GLOBAL_MUTE");
                dismiss();
                break;
            case R.id.tv_kick_out:
                //踢人
                serverOperate("KICK");
                dismiss();
                break;
            case R.id.tv_manager:
                if (mViewedUser.getIdentityId() == UserIdentity.ROOM_MANAGER) {
                    cancelManager();
                } else {
                    setManager();
                }
                dismiss();
                break;
            case R.id.tv_report:
                TipOffDialog.newInstance(mUserId, visitorId, mProgramId)
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());
                dismiss();
                break;
            case R.id.ll_offline:
                offline(mUserId, visitorId);
                dismissDialog();
                break;
            default:
                break;
        }
    }

    private void offline(long mUserId, long visitorId) {
        HashMap map = new HashMap();
        map.put("userId", visitorId);
        map.put("toUserId", mUserId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        ApiFactory.getInstance().getApi(Api.class)
                .offLine(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement jsonElement) {

                    }

                });
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
                    if (mViewedUser == null || mViewedUser.getUserId() <= 0 || mViewedUser.getUserId() == visitorId) {
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
        if (user.getIdentityId() == ROOM_MANAGER) {
            tvManager.setText(R.string.cancel_room_manager);
        }
        //本房间禁言
        if (user.getDisabledService() != null) {
            for (int i = 0; i < user.getDisabledService().size(); i++) {
                if (2 == user.getDisabledService().get(i)) {
                    tvRoom.setText(R.string.cancel_mute);
                }
            }
        }
        //全局禁言
        if (user.getDisabledService() != null) {
            for (int i = 0; i < user.getDisabledService().size(); i++) {
                if (2 == user.getDisabledService().get(i)) {
                    tvGlobal.setText(R.string.cancel_global_mute);
                }
            }
        }
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
    private void serverOperate(String operate) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("serviceCode", operate);
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mUserId);
        if (!TextUtils.isEmpty(mUser.getNickname())) {
            paramsMap.put("nickname", mUser.getNickname());
        }
        ApiFactory.getInstance().getApi(Api.class)
                .serverOprate(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(OperateMoreDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void setManager() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mViewedUser.getUserId());
        ApiFactory.getInstance().getApi(Api.class)
                .setManager(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(OperateMoreDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void cancelManager() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mViewedUser.getUserId());
        ApiFactory.getInstance().getApi(Api.class)
                .cancleManager(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(OperateMoreDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
