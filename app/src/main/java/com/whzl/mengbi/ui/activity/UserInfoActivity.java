package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lljjcoder.style.citypickerview.CityPickerView;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.view.UserInfoView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.CustomPopWindow;
import com.whzl.mengbi.util.CustomPopWindowUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.PhotoUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StorageUtil;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivityNew implements UserInfoView {

    //修改昵称
    public static final int NICKNAME_CODE = 3;
    //修改性别
    public static final int SEX_CODE = 4;


    private UserInfo mUserInfo;
    private CircleImageView mCircleImageView;
    private TextView mProfileTV;
    private TextView mSpout;
    private TextView mNickName;
    private TextView mSex;
    private TextView mAddress;
    private TextView mBirthday;
    private RelativeLayout mAnchorInfoLayout;
    private TextView mAnchorLevel;
    private ImageView mAnchorImg;
    private TextView mUserLevel;
    private ImageView mUserImg;
    private Button mQuit;
    //更换头像
    private CustomPopWindow mCustomPopWindow;
    private Button butPhoto;
    private Button butAlbum;
    private Button butCancel;
    //调用照相机返回图片文件
    private File tempFile;
    private UserInfoPresenter userInfoPresenter;
    private CityPickerView cityPickerView;//地区选择器
    private String tempCapturePath;
    private String tempCropPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        Intent intent = getIntent();
        mUserInfo = (UserInfo) intent.getSerializableExtra("userbean");
        userInfoPresenter = new UserInfoPresenterImpl(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_user_info_layout, R.string.personal_info, true);
    }

    @Override
    protected void setupView() {
        mCircleImageView = (CircleImageView) findViewById(R.id.user_info_head_img);
        mProfileTV = (TextView) findViewById(R.id.user_info_head_text);
        mSpout = (TextView) findViewById(R.id.user_info_sprount);
        mNickName = (TextView) findViewById(R.id.user_info_nickname);
//        mSex = (TextView) findViewById(R.id.user_info_sex);
//        mAddress = (TextView) findViewById(R.id.user_info_address);
//        mBirthday = (TextView) findViewById(R.id.user_info_birthday);
        mAnchorInfoLayout = (RelativeLayout) findViewById(R.id.user_anchor_layout);
        mAnchorLevel = (TextView) findViewById(R.id.user_info_anchorlevel);
        mAnchorImg = (ImageView) findViewById(R.id.user_info_anchorlevel_img);
        mUserLevel = (TextView) findViewById(R.id.user_info_userlevel);
        mUserImg = findViewById(R.id.user_info_userlevel_img);
        initModelAndView();
    }

    @Override
    protected void loadData() {

    }


    /**
     * 赋予控件数据
     */
    private void initModelAndView() {
        GlideImageLoader.getInstace().circleCropImage(this, mUserInfo.getData().getAvatar(), mCircleImageView);
        mSpout.setText(mUserInfo.getData().getUserId() + "");
        mNickName.setText(mUserInfo.getData().getNickname());
//        mSex.setText(mUserInfo.getData().getGender());
//        if (mUserInfo.getData().getGender().equals("M")) {
//            mSex.setText("男");
//        } else if (mUserInfo.getData().getGender().equals("W")) {
//            mSex.setText("女");
//        } else {
//            mSex.setText("保密");
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        if (mUserInfo.getData().getProvince() != null) {
//            stringBuilder.append(mUserInfo.getData().getProvince());
//        }
//        if (mUserInfo.getData().getCity() != null) {
//            stringBuilder.append("-" + mUserInfo.getData().getCity());
//        }
//        mAddress.setText(stringBuilder);
//        mBirthday.setText(mUserInfo.getData().getBirthday());
        for (UserInfo.DataBean.LevelListBean levelList : mUserInfo.getData().getLevelList()) {
            String levelType = levelList.getLevelType();
            if (levelType.equals("ANCHOR_LEVEL") && !levelList.getExpList().isEmpty()) {
                mAnchorLevel.append("离升级还差");
                mAnchorLevel.append(LightSpanString.getLightString(levelList.getExpList().get(0).getSjNeedExpValue() + "", Color.parseColor("#f1275b")));
                mAnchorLevel.append("主播经验");
                mAnchorImg.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelList.getLevelValue()));
            } else if (levelType.equals("USER_LEVEL") && !levelList.getExpList().isEmpty()) {
                mUserLevel.append("离升级还差");
                mUserLevel.append(LightSpanString.getLightString(levelList.getExpList().get(0).getSjNeedExpValue() + "", Color.parseColor("#4facf3")));
                mUserLevel.append("富豪经验");
                int userLevel = levelList.getLevelValue();
                mUserImg.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(userLevel));
            }
        }
        if (mUserInfo.getData().getUserType().equals("VIEWER")) {
            mAnchorInfoLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 监听view触发事件
     *
     * @param v
     */
    //@OnClick({R.id.rl_avatar_container, R.id.rl_nick_name_container, R.id.rl_gender_container, R.id.rl_address_container, R.id.rl_birthday_container})
    @OnClick({R.id.rl_avatar_container, R.id.rl_nick_name_container})
    public void onClick(View v) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = DensityUtil.dp2px(200);
        Calendar calendar = Calendar.getInstance();
        long userId = mUserInfo.getData().getUserId();
        switch (v.getId()) {
            case R.id.rl_avatar_container://修改头像
                View view = getLayoutInflater().inflate(R.layout.activity_user_info_photo_pop_layout, null);
                mCustomPopWindow = CustomPopWindowUtils.profile(this, view, mCircleImageView, width, height);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.rl_nick_name_container://修改昵称
                Intent nicknameIntent = new Intent(UserInfoActivity.this, UserInfoNickNameActivity.class);
                nicknameIntent.putExtra("nickname", mUserInfo.getData().getNickname());
                startActivityForResult(nicknameIntent, NICKNAME_CODE);
                break;
//            case R.id.rl_gender_container://修改性别
//                Intent sexIntent = new Intent(UserInfoActivity.this, UserInfoSexActivity.class);
//                sexIntent.putExtra("sex", mUserInfo.getData().getGender());
//                startActivityForResult(sexIntent, SEX_CODE);
//                break;
//            case R.id.rl_birthday_container://修改生日
//                String strDate = mUserInfo.getData().getBirthday();
//                try {
//                    Date date = DateUtils.getDate(strDate);
//                    calendar.setTime(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                boolean type[] = new boolean[]{true, true, true, false, false, false};
//                TimePickerView timePickerView = new TimePickerView
//                        .Builder(this, new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {
//                        String time1 = DateUtils.getTime(date);
//                        String time2 = DateUtils.getTime2(date);
//                        HashMap hashMap = new HashMap();
//                        hashMap.put("userId", userId);
//                        hashMap.put("birthday", time2);
//                        userInfoPresenter.onUpdataUserInfo(hashMap);
//                    }
//                })
//                        .setType(type)
//                        .setCancelText("取消")
//                        .setTitleText("日期选择")
//                        .setSubmitText("完成")
//                        .setContentSize(22)
//                        .setTitleSize(22)
//                        .setSubCalSize(22)
//                        .setOutSideCancelable(true)
//                        .isCyclic(true)
//                        .setTextColorCenter(Color.BLACK)
//                        .setTitleColor(Color.BLACK)
//                        .setSubmitColor(Color.parseColor("#4facf3"))
//                        .setCancelColor(Color.parseColor("#4facf3"))
//                        .isCenterLabel(false)
//                        .build();
//                timePickerView.setDate(calendar);
//                timePickerView.show();
//                break;
//            case R.id.rl_address_container://修改地区
//                cityPickerView.setConfig(SelectorUtils.address());
//                cityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
//                    @Override
//                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
//                        StringBuilder strBuilder = new StringBuilder();
//                        //省份
//                        if (province != null) {
//                            strBuilder.append(province.getName());
//                        }
//                        //城市
//                        if (city != null) {
//                            strBuilder.append("-" + city.getName());
//                        }
//                        mAddress.setText(strBuilder);
//                        String provinceStr = province.getName();
//                        String cityStr = city.getName();
//                        HashMap hashMap = new HashMap();
//                        hashMap.put("userId", userId);
//                        hashMap.put("province", provinceStr);
//                        hashMap.put("city", cityStr);
//                        userInfoPresenter.onUpdataUserInfo(hashMap);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        cityPickerView.hide();
//                    }
//                });
//                cityPickerView.showCityPicker();
//                break;
            default:
                break;
        }
    }

    /**
     * 更换头像
     * PopWindows view 事件
     *
     * @param view
     */
    private void initPopView(View view) {
        butPhoto = view.findViewById(R.id.user_info_photo);
        butAlbum = view.findViewById(R.id.user_info_album);
        butCancel = view.findViewById(R.id.user_info_cancel);
        //拍照
        butPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tempCapturePath = StorageUtil.getTempDir() + "/avatar_captured";
                tempCropPath = StorageUtil.getTempDir() + "/avatar_crop.jpg";
                RxPermisssionsUitls.getPicFromCamera(UserInfoActivity.this, tempCapturePath);
                mCustomPopWindow.dissmiss();
            }
        });
        //相册选择
        butAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempCropPath = StorageUtil.getTempDir() + "/avatar_crop.jpg";
                RxPermisssionsUitls.getPicFromAlbm(UserInfoActivity.this);
                mCustomPopWindow.dissmiss();
            }
        });
        //取消
        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomPopWindow.dissmiss();
            }
        });
    }


    @Override
    public void showPortrait(String filename) {
        GlideImageLoader.getInstace().displayImage(this, filename, mCircleImageView);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    public void showSuccess(String msg) {
        //ToastUtils.showToast(msg);
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void onModifyNickname(String nickname) {
        SPUtils.put(this, SpConfig.KEY_USER_NAME, nickname);
        mNickName.setText(nickname);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NICKNAME_CODE://昵称
                if (resultCode == RESULT_OK) {
                    String nickname = data.getStringExtra("nickname");
                    userInfoPresenter.onUpdataNickName(mUserInfo.getData().getUserId() + "", nickname);
                }
                break;
            case SEX_CODE://性别
                if (resultCode == RESULT_OK) {
                    String sex = data.getStringExtra("sex");
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId", mUserInfo.getData().getUserId());
                    hashMap.put("sex", sex);
                    userInfoPresenter.onUpdataUserInfo(hashMap);
                }
                break;
        }

        PhotoUtil.onActivityResult(this, tempCapturePath, tempCropPath, requestCode, resultCode, data, new PhotoUtil.UploadListener() {
            @Override
            public void upload() {
                userInfoPresenter.onUpdataPortrait(mUserInfo.getData().getUserId() + "", tempCropPath);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoPresenter.onDestory();
    }
}
