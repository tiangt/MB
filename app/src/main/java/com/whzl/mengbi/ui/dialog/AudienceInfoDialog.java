package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.UserIdentity.ROOM_MANAGER;
import static com.whzl.mengbi.util.UserIdentity.getCanChatPaivate;

/**
 * @author shaw
 * @date 2018/7/11
 */
public class AudienceInfoDialog extends BaseAwesomeDialog {

    private RoomUserInfo.DataBean user;
    private String nickName;
    private BaseAwesomeDialog operateMoreDialog;
    private int programId;

    public static AudienceInfoDialog newInstance(String nickName, int programId, RoomUserInfo.DataBean user) {
        AudienceInfoDialog dialog = new AudienceInfoDialog();
        Bundle args = new Bundle();
        args.putString("nickName", nickName);
        args.putInt("programId", programId);
        args.putParcelable("user", user);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_audience_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        nickName = getArguments().getString("nickName");
        programId = getArguments().getInt("programId");
        user = getArguments().getParcelable("user");
        holder.setText(R.id.tv_nick_name, nickName);
    }


    @OnClick({R.id.btn_close, R.id.rl_at, R.id.rl_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.rl_at:
                if (user == null || user.getUserId() == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                ((LiveDisplayActivity) getActivity()).showAtChat("@" + nickName + " ");
                dismiss();
                break;
            case R.id.rl_more:
                if (user == null || user.getUserId() == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                if (operateMoreDialog != null && operateMoreDialog.isAdded()) {
                    return;
                }
                operateMoreDialog = OperateMoreDialog.newInstance(0, user.getUserId(), programId, user,nickName)
                        .setShowBottom(true)
                        .setOutCancel(false)
                        .show(getActivity().getSupportFragmentManager());
                break;
        }
    }

}
