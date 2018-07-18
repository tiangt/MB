package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/11
 */
public class AudienceInfoDialog extends BaseAwesomeDialog {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_level_icon)
    ImageView ivLevelIcon;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;

    public static BaseAwesomeDialog newInstance(int userId, int programId) {
        AudienceInfoDialog dialog = new AudienceInfoDialog();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putInt("programId", programId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_audience_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        int userId = getArguments().getInt("userId");
        int programId = getArguments().getInt("programId");
        getUserInfo(userId, programId);
    }

    private void getUserInfo(int userId, int programId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomUserInfo roomUserInfo = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfo.getCode() == 200) {
                    if (roomUserInfo.getData() != null) {
                        RoomUserInfo.DataBean dataBean = roomUserInfo.getData();
                        GlideImageLoader.getInstace().displayImage(getContext(), dataBean.getAvatar(), ivAvatar);
                        tvName.setText(dataBean.getNickname());
                        tvUserId.setText("ID " + dataBean.getUserId());
                        tvLocation.setText(dataBean.getCity());
                        String introduce = dataBean.getIntroduce();
                        if (!TextUtils.isEmpty(introduce)) {
                            tvIntroduce.setText(introduce);
                        }
                        tvAge.setText(DateUtils.getAge(dataBean.getBirthday()) + "岁");
                        int identityId = dataBean.getIdentityId();
                        List<RoomUserInfo.DataBean.LevelMapBean> levelMap = dataBean.getLevelMap();
                        for (int i = 0; i < levelMap.size(); i++) {
                            if (identityId == 10 && "ANCHOR_LEVEL".equals(levelMap.get(i).getLevelType())) {
                                int levelValue = levelMap.get(i).getLevelValue();
                                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                            } else if ("USER_LEVEL".equals(levelMap.get(i).getLevelType())) {
                                int levelValue = levelMap.get(i).getLevelValue();
                                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                            }
                        }
                    }
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });


    }

    @OnClick(R.id.btn_dismiss)
    public void onClick() {
        dismiss();
    }
}
