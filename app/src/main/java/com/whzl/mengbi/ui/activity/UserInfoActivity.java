package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
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
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SelectorUtils;
import com.whzl.mengbi.util.StorageUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivityNew implements UserInfoView {

    //修改昵称
    public static final int NICKNAME_CODE = 3;
    //修改性别
    public static final int SEX_CODE = 4;
    @BindView(R.id.user_info_head_img)
    CircleImageView userInfoHeadImg;
    @BindView(R.id.user_info_head_text)
    TextView userInfoHeadText;
    @BindView(R.id.user_info_sprount)
    TextView userInfoSprount;
    @BindView(R.id.user_info_nickname)
    TextView userInfoNickname;
    @BindView(R.id.user_info_sex)
    TextView userInfoSex;
    @BindView(R.id.user_info_address)
    TextView userInfoAddress;
    @BindView(R.id.user_info_birthday)
    TextView userInfoBirthday;
    @BindView(R.id.user_info_anchorlevel)
    TextView userInfoAnchorlevel;
    @BindView(R.id.user_info_anchorlevel_img)
    ImageView userInfoAnchorlevelImg;
    @BindView(R.id.user_info_userlevel)
    TextView userInfoUserlevel;
    @BindView(R.id.user_info_userlevel_img)
    ImageView userInfoUserlevelImg;

    private UserInfo mUserInfo;


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
    private TimePickerView timePickerView;

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
        initModelAndView();
    }

    @Override
    protected void loadData() {

    }


    /**
     * 赋予控件数据
     */
    private void initModelAndView() {
        GlideImageLoader.getInstace().circleCropImage(this, mUserInfo.getData().getAvatar(), userInfoHeadImg);
        userInfoSprount.setText(mUserInfo.getData().getUserId() + "");
        userInfoNickname.setText(mUserInfo.getData().getNickname());
        if (mUserInfo.getData().getGender().equals("M")) {
            userInfoSex.setText("男");
        } else if (mUserInfo.getData().getGender().equals("W")) {
            userInfoSex.setText("女");
        } else {
            userInfoSex.setText("保密");
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (mUserInfo.getData().getProvince() != null) {
            stringBuilder.append(mUserInfo.getData().getProvince());
        }
        if (mUserInfo.getData().getCity() != null) {
            stringBuilder.append("-" + mUserInfo.getData().getCity());
        }
        userInfoAddress.setText(stringBuilder);
        userInfoBirthday.setText(mUserInfo.getData().getBirthday());
        for (UserInfo.DataBean.LevelListBean levelList : mUserInfo.getData().getLevelList()) {
            if (levelList.getLevelType().equals("ROYAL_LEVEL")) {
                for (UserInfo.DataBean.LevelListBean.ExpListBean expListBean : levelList.getExpList()) {
                    String anchorsjexp = "离升级还差" + String.valueOf(expListBean.getSjNeedExpValue());
                    SpannableStringBuilder anchorSpan = new SpannableStringBuilder(anchorsjexp);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#f1275b"));
                    anchorSpan.setSpan(colorSpan, 5, anchorsjexp.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    anchorSpan.append("主播经验");
                    userInfoAnchorlevel.setText(anchorSpan);
                }
            } else {
                for (UserInfo.DataBean.LevelListBean.ExpListBean expListBean : levelList.getExpList()) {
                    String usersjexp = "离升级还差" + String.valueOf(expListBean.getSjNeedExpValue());
                    SpannableStringBuilder userSpan = new SpannableStringBuilder(usersjexp);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4facf3"));
                    userSpan.setSpan(colorSpan, 5, usersjexp.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    userSpan.append("富豪经验");
                    userInfoUserlevel.setText(userSpan);
                }
                String levelVal = String.valueOf(levelList.getLevelValue());
                int resId = FileUtils.userLevelDrawable(levelVal);
                GlideImageLoader.getInstace().displayImage(this, resId, userInfoUserlevelImg);
            }
        }
    }

    /**
     * 监听view触发事件
     *
     * @param v
     */
    @OnClick({R.id.rl_avatar_container, R.id.rl_nick_name_container, R.id.rl_gender_container, R.id.rl_address_container, R.id.rl_birthday_container})
    public void onClick(View v) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = DensityUtil.dp2px(200);
        Calendar calendar = Calendar.getInstance();
        long userId = mUserInfo.getData().getUserId();
        switch (v.getId()) {
            case R.id.rl_avatar_container://修改头像
                View view = getLayoutInflater().inflate(R.layout.activity_user_info_photo_pop_layout, null);
                mCustomPopWindow = CustomPopWindowUtils.profile(this, view, userInfoHeadImg, width, height);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.rl_nick_name_container://修改昵称
                Intent nicknameIntent = new Intent(UserInfoActivity.this, UserInfoNickNameActivity.class);
                nicknameIntent.putExtra("nickname", mUserInfo.getData().getNickname());
                startActivityForResult(nicknameIntent, NICKNAME_CODE);
                break;
            case R.id.rl_gender_container://修改性别
                Intent sexIntent = new Intent(UserInfoActivity.this, UserInfoSexActivity.class);
                sexIntent.putExtra("sex", mUserInfo.getData().getGender());
                startActivityForResult(sexIntent, SEX_CODE);
                break;
            case R.id.rl_birthday_container://修改生日
                String strDate = mUserInfo.getData().getBirthday();
                try {
                    Date date = DateUtils.getDate(strDate);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean type[] = new boolean[]{true, true, true, false, false, false};
                if (timePickerView == null) {
                    timePickerView = new TimePickerView
                            .Builder(this, new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            String time1 = DateUtils.getTime(date);
                            String time2 = DateUtils.getTime2(date);
                            HashMap hashMap = new HashMap();
                            hashMap.put("userId", userId);
                            hashMap.put("birthday", time2);
                            userInfoPresenter.onUpdataUserInfo(hashMap);
                        }
                    })
                            .setType(type)
                            .setCancelText("取消")
                            .setTitleText("日期选择")
                            .setSubmitText("完成")
                            .setContentSize(22)
                            .setTitleSize(22)
                            .setSubCalSize(22)
                            .setOutSideCancelable(true)
                            .isCyclic(true)
                            .setTextColorCenter(Color.BLACK)
                            .setTitleColor(Color.BLACK)
                            .setSubmitColor(Color.parseColor("#4facf3"))
                            .setCancelColor(Color.parseColor("#4facf3"))
                            .isCenterLabel(false)
                            .build();
                    timePickerView.setDate(calendar);
                }
                timePickerView.show();
                break;
            case R.id.rl_address_container://修改地区
                if (cityPickerView == null) {
                    cityPickerView = new CityPickerView();
                    cityPickerView.init(this);
                    cityPickerView.setConfig(SelectorUtils.address());
                    cityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
                        @Override
                        public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                            StringBuilder strBuilder = new StringBuilder();
                            //省份
                            if (province != null) {
                                strBuilder.append(province.getName());
                            }
                            //城市
                            if (city != null) {
                                strBuilder.append("-" + city.getName());
                            }
                            userInfoAddress.setText(strBuilder);
                            String provinceStr = province.getName();
                            String cityStr = city.getName();
                            HashMap hashMap = new HashMap();
                            hashMap.put("userId", userId);
                            hashMap.put("province", provinceStr);
                            hashMap.put("city", cityStr);
                            userInfoPresenter.onUpdataUserInfo(hashMap);
                        }

                        @Override
                        public void onCancel() {
                            cityPickerView.hide();
                        }
                    });
                }
                cityPickerView.showCityPicker();
                break;
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
        GlideImageLoader.getInstace().displayImage(this, filename, userInfoHeadImg);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    public void showSuccess(String msg) {
        //ToastUtils.showToast(msg);
    }

    @Override
    public void showError(String msg) {
        //ToastUtils.showToast(msg);
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
