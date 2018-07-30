package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.JsonBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.view.UserInfoView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.CustomPopWindow;
import com.whzl.mengbi.util.CustomPopWindowUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.JsonUtils;
import com.whzl.mengbi.util.PhotoUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StorageUtil;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    //修改昵称
    public static final int NICKNAME_CODE = 3;
    //修改性别
    private static final int GENDER_CODE = 2;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_anchor_level)
    TextView tvAnchorLevel;
    @BindView(R.id.iv_anchor_level)
    ImageView ivAnchorLevel;
    @BindView(R.id.tv_user_level)
    TextView tvUserLevel;
    @BindView(R.id.iv_user_level)
    ImageView ivUserLevel;
    @BindView(R.id.rl_anchor_container)
    RelativeLayout rlAnchorContainer;
    private CustomPopWindow mCustomPopWindow;
    private UserInfoPresenter userInfoPresenter;
    private String tempCapturePath;
    private String tempCropPath;
    private UserInfo mUserInfo;
    private String sex;
    private TimePickerView pvTime;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private int mSelectedPosition1;
    private int mSelectedPosition2;

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
        initJsonData();
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_user_info_layout, R.string.personal_info, true);
    }

    @Override
    protected void setupView() {
        GlideImageLoader.getInstace().circleCropImage(this, mUserInfo.getData().getAvatar(), ivAvatar);
        tvAccount.setText(mUserInfo.getData().getUserId() + "");
        tvNickName.setText(mUserInfo.getData().getNickname());
        sex = mUserInfo.getData().getGender();
        setupSex(sex);
        StringBuilder stringBuilder = new StringBuilder();
        if (mUserInfo.getData().getProvince() != null) {
            stringBuilder.append(mUserInfo.getData().getProvince());
        }
        if (mUserInfo.getData().getCity() != null) {
            stringBuilder.append("-" + mUserInfo.getData().getCity());
        }
        tvAddress.setText(stringBuilder);
        tvBirthday.setText(DateUtils.getTime(mUserInfo.getData().getBirthday()));
        for (UserInfo.DataBean.LevelListBean levelList : mUserInfo.getData().getLevelList()) {
            String levelType = levelList.getLevelType();
            long sjNeedValue = levelList.getExpList().get(0).getSjNeedExpValue() - levelList.getExpList().get(0).getSjExpvalue();
            if (levelType.equals("ANCHOR_LEVEL") && !levelList.getExpList().isEmpty()) {
                tvAnchorLevel.setText("离升级还差");
                tvAnchorLevel.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#f1275b")));
                tvAnchorLevel.append("主播经验");
                ivAnchorLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelList.getLevelValue()));
            } else if (levelType.equals("USER_LEVEL") && !levelList.getExpList().isEmpty()) {
                tvUserLevel.setText("离升级还差");
                tvUserLevel.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#4facf3")));
                tvUserLevel.append("富豪经验");
                int userLevel = levelList.getLevelValue();
                ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(userLevel));
            }
        }
        if (mUserInfo.getData().getUserType().equals("VIEWER")) {
            rlAnchorContainer.setVisibility(View.GONE);
        }
    }

    private void setupSex(String sex) {
        if (sex.equals("M")) {
            tvGender.setText("男");
        } else if (sex.equals("W")) {
            tvGender.setText("女");
        } else {
            tvGender.setText("保密");
        }
    }

    @Override
    protected void loadData() {

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
            case R.id.rl_avatar_container:
                View view = getLayoutInflater().inflate(R.layout.activity_user_info_photo_pop_layout, null);
                mCustomPopWindow = CustomPopWindowUtils.profile(this, view, ivAvatar, width, height);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.rl_nick_name_container:
                Intent nicknameIntent = new Intent(UserInfoActivity.this, NickNameModifyActivity.class);
                nicknameIntent.putExtra("nickname", mUserInfo.getData().getNickname());
                startActivityForResult(nicknameIntent, NICKNAME_CODE);
                break;
            case R.id.rl_gender_container:
                Intent genderIntent = new Intent(this, GenderModifyActivity.class);
                genderIntent.putExtra("gender", mUserInfo.getData().getGender());
                startActivityForResult(genderIntent, GENDER_CODE);
                break;
            case R.id.rl_address_container:
                showAddressPick();
                break;
            case R.id.rl_birthday_container:
                showDatePick();
                break;
            default:
                break;
        }
    }

    private void showDatePick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateUtils.dateStrToMillis(mUserInfo.getData().getBirthday()));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvBirthday.setText(DateUtils.getTime(date));
                HashMap hashMap = new HashMap();
                hashMap.put("userId", mUserInfo.getData().getUserId());
                hashMap.put("birthday", DateUtils.getTime2(date));
                userInfoPresenter.onUpdataUserInfo(hashMap);
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(15)
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(17)//标题文字大小
                .setTitleText("生日选择")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#007aff"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#007aff"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(19)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();

    }

    private void showAddressPick() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                tvAddress.setText(options1Items.get(options1).getPickerViewText() + "-" + options2Items.get(options1).get(options2));
                HashMap hashMap = new HashMap();
                hashMap.put("userId", mUserInfo.getData().getUserId());
                hashMap.put("province", options1Items.get(options1).getPickerViewText());
                hashMap.put("city", options2Items.get(options1).get(options2));
                userInfoPresenter.onUpdataUserInfo(hashMap);
            }
        })

                .setSelectOptions(mSelectedPosition1, mSelectedPosition2)
                .setSubCalSize(15)
                .setTitleSize(17)//标题文字大小
                .setSubmitColor(Color.parseColor("#007aff"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#007aff"))//取消按钮文字颜色
                .setTitleText("城市选择")
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(19)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 更换头像
     * PopWindows view 事件
     *
     * @param view
     */
    private void initPopView(View view) {
        Button butPhoto = view.findViewById(R.id.user_info_photo);
        Button butAlbum = view.findViewById(R.id.user_info_album);
        Button butCancel = view.findViewById(R.id.user_info_cancel);
        //拍照
        butPhoto.setOnClickListener(v -> {
            tempCapturePath = StorageUtil.getTempDir() + File.separator + System.currentTimeMillis() + "avatar_captured.jpg";
            tempCropPath = StorageUtil.getTempDir() + File.separator + System.currentTimeMillis() + "avatar_crop.jpg";
            RxPermisssionsUitls.getPicFromCamera(UserInfoActivity.this, tempCapturePath);
            mCustomPopWindow.dissmiss();
        });
        butAlbum.setOnClickListener(v -> {
            tempCropPath = StorageUtil.getTempDir() + File.separator + System.currentTimeMillis() + "avatar_crop.jpg";
            RxPermisssionsUitls.getPicFromAlbm(UserInfoActivity.this);
            mCustomPopWindow.dissmiss();
        });
        butCancel.setOnClickListener(v -> mCustomPopWindow.dissmiss());
    }


    @Override
    public void showPortrait(String filename) {
        GlideImageLoader.getInstace().displayImage(this, filename, ivAvatar);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    public void showSuccess(String msg) {
        EventBus.getDefault().post(new UserInfoUpdateEvent());
        setupSex(sex);
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void onModifyNickname(String nickname) {
        SPUtils.put(this, SpConfig.KEY_USER_NAME, nickname);
        tvNickName.setText(nickname);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtil.onActivityResult(this, tempCapturePath, tempCropPath, requestCode, resultCode, data,
                () -> userInfoPresenter.onUpdataPortrait(mUserInfo.getData().getUserId() + "", tempCropPath));
        switch (requestCode) {
            case NICKNAME_CODE:
                if (resultCode == RESULT_OK) {
                    String nickname = data.getStringExtra("nickname");
                    userInfoPresenter.onUpdataNickName(mUserInfo.getData().getUserId() + "", nickname);
                }
                break;
            case GENDER_CODE:
                if (resultCode == RESULT_OK) {
                    sex = data.getStringExtra("gender");
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId", mUserInfo.getData().getUserId());
                    hashMap.put("sex", sex);
                    userInfoPresenter.onUpdataUserInfo(hashMap);
                }
                break;
        }
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */

        Observable.just("province.json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String str) throws Exception {
                        String JsonData = JsonUtils.getJsonFromAssert(UserInfoActivity.this, str);//获取assets目录下的json文件数据

                        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

                        /**
                         * 添加省份数据
                         *
                         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
                         * PickerView会通过getPickerViewText方法获取字符串显示出来。
                         */
                        options1Items = jsonBean;

                        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
                            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                                CityList.add(CityName);//添加城市
                                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                                    City_AreaList.add("");
                                } else {
                                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                                }
                                Province_AreaList.add(City_AreaList);//添加该省所有地区数据

                                if(jsonBean.get(i).getCityList().get(c).getName().equals(mUserInfo.getData().getProvince())){
                                    mSelectedPosition2 = c;
                                }
                            }

                            if(jsonBean.get(i).getName().equals(mUserInfo.getData().getProvince())){
                                mSelectedPosition1 = i;
                            }
                            /**
                             * 添加城市数据
                             */
                            options2Items.add(CityList);

                            /**
                             * 添加地区数据
                             */
                            options3Items.add(Province_AreaList);
                        }

                        return "OK";
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoPresenter.onDestory();
    }
}
