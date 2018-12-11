package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.PersonalInfoActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.29
 */
public class PersonalInfoDialog extends BaseAwesomeDialog {

    @BindView(R.id.iv_bg_personal)
    ImageView ivBgPersonal;
    @BindView(R.id.btn_buy_royal)
    ImageButton btnBuyRoyal;
    @BindView(R.id.btn_personal)
    ImageButton mPersonalPage;
    @BindView(R.id.btn_close)
    ImageButton mClose;
    @BindView(R.id.iv_user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.tv_nick_name)
    TextView mTvNickName;
    @BindView(R.id.tv_anchor_id)
    TextView mTvAnchorId;
    @BindView(R.id.tv_introduce)
    TextView mTvIntroduce;
    @BindView(R.id.ll_level_container)
    LinearLayout linearLayout;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_ranking)
    TextView tVRank;
    @BindView(R.id.tv_pretty_num)
    PrettyNumText tvPrettyNum;
    @BindView(R.id.tv_private_chat)
    TextView tvPrivateChat;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.rl_at)
    RelativeLayout rlAt;
    @BindView(R.id.rl_follow)
    RelativeLayout rlFollow;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.tv_at)
    TextView tvAt;

    private long mUserId;
    private int mProgramId;
    private RoomUserInfo.DataBean mViewedUser;
    private RoomUserInfo.DataBean mUser;
    private List<RoomUserInfo.LevelMapBean> levelMapBeans;
    private int levelValue;
    private String levelType;
    private String mIsFollowed;
    private long mVisitorId;
    private String liveState;
    private PersonalInfoBean.DataBean userBean;

    public static PersonalInfoDialog newInstance(RoomUserInfo.DataBean user, long userId, int programId, long visitorId) {
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        args.putLong("visitorId", visitorId); //当前用户ID
        args.putParcelable("user", user);
        PersonalInfoDialog dialog = new PersonalInfoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public static PersonalInfoDialog newInstance(RoomUserInfo.DataBean user, long userId, int programId, long visitorId, String isFollowed, String liveState) {
        Bundle args = new Bundle();
        args.putLong("userId", userId); //被访者ID
        args.putInt("programId", programId);
        args.putLong("visitorId", visitorId); //当前用户ID
        args.putString("isFollowed", isFollowed);
        args.putString("liveState", liveState);
        args.putParcelable("user", user);
        PersonalInfoDialog dialog = new PersonalInfoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public BaseAwesomeDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnClickListener listener;

    public interface OnClickListener {
        void onPrivateChatClick();
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_personal_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUserId = getArguments().getLong("userId");
        mProgramId = getArguments().getInt("programId");
        mIsFollowed = getArguments().getString("isFollowed");
        mVisitorId = getArguments().getLong("visitorId");
        liveState = getArguments().getString("liveState");
        mUser = getArguments().getParcelable("user");
        mTvAnchorId.setText("ID " + mUserId);
        tvAt.setText("@Ta");
        getUserInfo(mUserId, mProgramId, mVisitorId);
        getHomePageInfo(mUserId, mVisitorId);
//        isRoyal();
    }

    @OnClick({R.id.btn_personal, R.id.btn_buy_royal, R.id.tv_follow, R.id.btn_close,
            R.id.tv_private_chat, R.id.rl_more, R.id.rl_at})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", mUserId);
                bundle.putLong("visitorId", mVisitorId);
                bundle.putString("liveState", liveState);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
                dismiss();
                break;
            case R.id.btn_buy_royal:
                if (mVisitorId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                Intent intentShop = new Intent(getActivity(), ShopActivity.class);
                intentShop.putExtra("select", 2);
                startActivity(intentShop);
                dismiss();
                break;
            case R.id.tv_follow:
                //关注
                if (mVisitorId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                follow(mVisitorId, mUserId);
                mIsFollowed = "T";
                break;
            case R.id.tv_private_chat:
                if (mVisitorId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                EventBus.getDefault().post(new PrivateChatSelectedEvent(mViewedUser));
                if (listener != null) {
                    listener.onPrivateChatClick();
                }
                dismiss();
                break;
            case R.id.rl_more:
                if (mVisitorId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                OperateMoreDialog.newInstance(mUserId, mVisitorId, mProgramId, mUser)
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());
                break;
            case R.id.rl_at:
                if (mVisitorId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    dismiss();
                    return;
                }
                if (listener != null) {
                    listener.onPrivateChatClick();
                }
                ((LiveDisplayActivity) getActivity()).showAtChat("@" + mViewedUser.getNickname());
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
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


                    if (mUser == null || mUser.getUserId() <= 0 || mUser.getUserId() == mViewedUser.getUserId()) {
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

    private void getHomePageInfo(long userId, long visitorId) {
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("visitorId", visitorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.HOME_PAGE, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                PersonalInfoBean personalInfoBean = GsonUtils.GsonToBean(result.toString(), PersonalInfoBean.class);
                if (personalInfoBean.getCode() == 200) {
                    if (personalInfoBean.getData() != null) {
                        userBean = personalInfoBean.getData();
                        setPrettyNum(userBean);
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setupOperations() {
//        if (mUser.getIdentityId() == UserIdentity.OPTR_MANAGER) {
//            tvPrivateChat.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        if (mUser.getIdentityId() == UserIdentity.ANCHOR) {
//            tvPrivateChat.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        if (mUser.getIdentityId() == UserIdentity.ROOM_MANAGER) {
//            tvPrivateChat.setVisibility(View.VISIBLE);
//            return;
//        }
        //贵族可私聊
        if (mUser.getLevelList() != null) {
            for (int i = 0; i < mUser.getLevelList().size(); i++) {
                String levelType = mUser.getLevelList().get(i).getLevelType();
                int levelValue = mUser.getLevelList().get(i).getLevelValue();
                if ("ROYAL_LEVEL".equals(levelType)) {
                    if (levelValue == 0) {
                        break;
                    } else {
                        rlChat.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        //名士5及以上可私聊
        if (mUser.getLevelList() != null) {
            for (int i = 0; i < mUser.getLevelList().size(); i++) {
                int levelValue = mUser.getLevelList().get(i).getLevelValue();
                int indentityId = mUser.getIdentityId();
                if (indentityId == 10) {
                    return;
                } else {
                    if (levelValue > 4) {
                        rlChat.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        //守护，VIP可私聊
        if (mUser.getGoodsList() != null) {
            for (int i = 0; i < mUser.getGoodsList().size(); i++) {
                RoomUserInfo.GoodsListBean goodsBean = mUser.getGoodsList().get(i);
                if ("GUARD".equals(goodsBean.getGoodsType()) || "VIP".equals(goodsBean.getGoodsType())) {
                    rlChat.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setPrettyNum(PersonalInfoBean.DataBean userBean) {
        if (userBean.getGoodsList() == null) {
            return;
        }
        for (int i = 0; i < userBean.getGoodsList().size(); i++) {
            PersonalInfoBean.DataBean.GoodsListBean goodsListBean = userBean.getGoodsList().get(i);
            if ("PRETTY_NUM".equals(goodsListBean.getGoodsType())) {
                tvPrettyNum.setVisibility(View.VISIBLE);
                mTvAnchorId.setVisibility(View.GONE);
                if (goodsListBean.getGoodsName().length() == 5) {
                    tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                    tvPrettyNum.setNumColor(Color.rgb(255, 43, 63));
                    tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_five);
                    tvPrettyNum.setPrettyTextSize(9);
                    tvPrettyNum.setNumber();
                } else if (goodsListBean.getGoodsName().length() == 6) {
                    tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                    tvPrettyNum.setNumColor(Color.rgb(255, 165, 0));
                    tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_six);
                    tvPrettyNum.setPrettyTextSize(9);
                    tvPrettyNum.setNumber();
                } else if (goodsListBean.getGoodsName().length() == 7) {
                    tvPrettyNum.setPrettyNum(goodsListBean.getGoodsName());
                    tvPrettyNum.setNumColor(Color.rgb(49, 161, 255));
                    tvPrettyNum.setPrettyBgColor(R.drawable.shape_pretty_seven);
                    tvPrettyNum.setPrettyTextSize(9);
                    tvPrettyNum.setNumber();
                }
            }
        }
    }

    private void setupView(RoomUserInfo.DataBean user) {
        GlideImageLoader.getInstace().displayImage(getContext(), user.getAvatar(), mUserAvatar);
        mTvNickName.setText(user.getNickname());
        mIsFollowed = user.getIsFollowed();
        int rank = user.getRank();
        if (rank < 0) {
            tVRank.setText("万名之外");
        } else {
            tVRank.setText(getString(R.string.ranking, rank));
        }
        String introduce = user.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            mTvIntroduce.setText(introduce);
        }
        int identityId = user.getIdentityId();
        linearLayout.removeAllViews();
        ImageView imageView = new ImageView(getContext());
        levelMapBeans = new ArrayList<>();

        //贵族等级
        if (user.getLevelList() != null) {
            for (int i = 0; i < user.getLevelList().size(); i++) {
                levelType = user.getLevelList().get(i).getLevelType();
                levelValue = user.getLevelList().get(i).getLevelValue();
                if ("ROYAL_LEVEL".equals(levelType)) {
                    setRoyalBackground(levelValue);
                    if (levelValue == 0) {
                        break;
                    } else {
                        btnBuyRoyal.setVisibility(View.VISIBLE);
                        ImageView royalImage = new ImageView(getContext());
                        royalImage.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
                        LinearLayout.LayoutParams royalParams = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 35), UIUtil.dip2px(getContext(), 16));
                        royalParams.leftMargin = UIUtil.dip2px(getContext(), 6);
                        linearLayout.addView(royalImage, royalParams);
                    }
                }
            }
        }

        //主播用户等级
        if (user.getLevelList() != null) {
            for (int i = 0; i < user.getLevelList().size(); i++) {
                levelType = user.getLevelList().get(i).getLevelType();
                levelValue = user.getLevelList().get(i).getLevelValue();
                if (identityId == 10) {
                    rlFollow.setVisibility(View.VISIBLE);
                    if ("T".equals(mIsFollowed)) {
                        tvFollow.setText(R.string.followed);
                        tvFollow.setTextColor(Color.GRAY);
                        mIsFollowed = "T";
                    } else {
                        tvFollow.setText(R.string.not_followed);
                        tvFollow.setTextColor(Color.RED);
                        mIsFollowed = "F";
                    }
                    imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                } else {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 38), UIUtil.dip2px(getContext(), 16));
            params.leftMargin = UIUtil.dip2px(getContext(), 6);
            linearLayout.addView(imageView, params);
        }

        //守护，VIP
        if (user.getGoodsList() != null) {
            for (int i = 0; i < user.getGoodsList().size(); i++) {
                RoomUserInfo.GoodsListBean goodsListBean = user.getGoodsList().get(i);
//                if ("GUARD".equals(goodsListBean.getGoodsType()) || "VIP".equals(goodsListBean.getGoodsType())) {
//                    Glide.with(getContext())
//                            .load(goodsListBean.getGoodsIcon())
//                            .into(new SimpleTarget<Drawable>() {
//                                @Override
//                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                    if (getContext() == null) {
//                                        return;
//                                    }
//                                    int intrinsicHeight = resource.getIntrinsicHeight();
//                                    int intrinsicWidth = resource.getIntrinsicWidth();
//                                    ImageView goodImage = new ImageView(getContext());
//                                    goodImage.setImageDrawable(resource);
//                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 16), UIUtil.dip2px(getContext(), 16));
//                                    params.leftMargin = UIUtil.dip2px(getContext(), 6);
//                                    linearLayout.addView(goodImage, params);
//                                }
//                            });
//                }
                if ("GUARD".equals(goodsListBean.getGoodsType())) {
                    ImageView guardImage = new ImageView(getContext());
                    guardImage.setImageDrawable(getResources().getDrawable(R.drawable.guard));
                    LinearLayout.LayoutParams guard = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 15), UIUtil.dip2px(getContext(), 15));
                    guard.leftMargin = UIUtil.dip2px(getContext(), 6);
                    linearLayout.addView(guardImage, guard);
                }
                if ("VIP".equals(goodsListBean.getGoodsType())) {
                    ImageView vipImage = new ImageView(getContext());
                    vipImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_vip));
                    LinearLayout.LayoutParams vip = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 15), UIUtil.dip2px(getContext(), 15));
                    vip.leftMargin = UIUtil.dip2px(getContext(), 6);
                    linearLayout.addView(vipImage, vip);
                }
            }
        }

        //房管
        if (identityId == UserIdentity.ROOM_MANAGER) {
            ImageView mgrView = new ImageView(getContext());
            mgrView.setImageResource(R.drawable.room_manager);
            LinearLayout.LayoutParams mgrViewParams = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 15), UIUtil.dip2px(getContext(), 15));
            mgrViewParams.leftMargin = UIUtil.dip2px(getContext(), 6);
            linearLayout.addView(mgrView, mgrViewParams);
        }
    }

    private void setRoyalBackground(int royalLevel) {
        switch (royalLevel) {
            case 0:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_civilian, ivBgPersonal);
                break;
            case 1:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_bronze, ivBgPersonal);
                break;
            case 2:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_silver, ivBgPersonal);
                break;
            case 3:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_gold, ivBgPersonal);
                break;
            case 4:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_platinum, ivBgPersonal);
                break;
            case 5:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_diamond, ivBgPersonal);
                break;
            case 6:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_star, ivBgPersonal);
                break;
            case 7:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_king, ivBgPersonal);
                break;
            default:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_civilian, ivBgPersonal);
                break;
        }
    }

    private void follow(long userId, long followingUserId) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("followingUserId", followingUserId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SOCIAL_FOLLOW, RequestManager.TYPE_POST_JSON, map,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
                            tvFollow.setText(R.string.followed);
                            tvFollow.setTextColor(Color.GRAY);
                        } else {
                            Toast.makeText(getActivity(), responseInfo.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        ToastUtils.showToast(errorMsg);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);
    }

    private void isRoyal() {
        String userId = String.valueOf(mVisitorId);
        BusinessUtils.getUserInfo(getActivity(), userId, new BusinessUtils.UserInfoListener() {
            @Override
            public void onSuccess(UserInfo.DataBean bean) {
                if (bean.getLevelList() != null) {
                    for (int i = 0; i < bean.getLevelList().size(); i++) {
                        if ("ROYAL_LEVEL".equals(bean.getLevelList().get(i).getLevelType())) {
                            if (0 == bean.getLevelList().get(i).getLevelValue()) {

                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
