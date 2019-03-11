package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.JsonBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.view.UserInfoView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.JsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.PhotoUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StorageUtil;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
    @BindView(R.id.tv_loyal)
    TextView tvLoyal;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.iv_royal)
    ImageView ivRoyal;
    @BindView(R.id.tv_chongzhi)
    TextView tvChongzhi;
    private UserInfoPresenter userInfoPresenter;
    private String tempCapturePath;
    private String tempCropPath;
    private UserInfo mUserInfo;
    private String mSex;
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
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mUserInfo = intent.getParcelableExtra("userbean");
        userInfoPresenter = new UserInfoPresenterImpl(this);
        initJsonData();
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_user_info_layout, R.string.personal_info, true);
    }

    @Override
    protected void setupView() {
        if (mUserInfo != null && mUserInfo.getData() != null) {
            GlideImageLoader.getInstace().circleCropImage(this, mUserInfo.getData().getAvatar(), ivAvatar);
        }
        tvAccount.setText(mUserInfo.getData().getUserId() + "");
        tvNickName.setText(mUserInfo.getData().getNickname() == null ? "" : mUserInfo.getData().getNickname());
        mSex = mUserInfo.getData().getGender();
        setupSex(mSex);
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
            if ("ANCHOR_LEVEL".equals(levelType) && !levelList.getExpList().isEmpty()) {
                if (sjNeedValue >= 0) {
                    tvAnchorLevel.setText("离升级还差");
                    tvAnchorLevel.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#f1275b")));
                    tvAnchorLevel.append("主播经验");
                } else {
                    tvAnchorLevel.setText("您已达到最高主播等级");
                }
                ivAnchorLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelList.getLevelValue()));
            } else if ("USER_LEVEL".equals(levelType) && !levelList.getExpList().isEmpty()) {
                if (sjNeedValue >= 0) {
                    tvUserLevel.setText("离升级还差");
                    tvUserLevel.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#4facf3")));
                    tvUserLevel.append("富豪经验");
                } else {
                    tvUserLevel.setText("您已达到最高用户等级");
                }
                int userLevel = levelList.getLevelValue();
                ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(userLevel));
            } else if ("ROYAL_LEVEL".equals(levelType) && !levelList.getExpList().isEmpty()) {
                int royalLevel = levelList.getLevelValue();
                if (sjNeedValue == 0 || royalLevel == SpConfig.MAX_LEVEL) {
                    tvUpdate.setText("您已达到最高贵族等级");
                } else {
                    tvUpdate.setText("离升级还需充值");
                    tvUpdate.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#FF7901")));
                    tvUpdate.append("元");
                }
                int userLevel = levelList.getLevelValue();
                if (userLevel != 0) {
//                    ivRoyal.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(userLevel));
                    Glide.with(this).asGif().load(ResourceMap.getResourceMap().
                            getRoyalLevelIcon(userLevel)).into(ivRoyal);
                }
                tvTotal.setText("本月已累计充值");
                tvTotal.append(LightSpanString.getLightString(levelList.getExpList().get(0).getSjExpvalue() + "", Color.parseColor("#FF7901")));
                tvTotal.append("元");
            }
        }
        if (mUserInfo.getData().getUserType().equals("VIEWER")) {
            rlAnchorContainer.setVisibility(View.GONE);
        }

        tvChongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, WXPayEntryActivity.class));
            }
        });
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
        switch (v.getId()) {
            case R.id.rl_avatar_container:
                showOptionDialog();
                break;
            case R.id.rl_nick_name_container:
                Intent nicknameIntent = new Intent(UserInfoActivity.this, NickNameModifyActivity.class);
                nicknameIntent.putExtra("nickname", mUserInfo.getData().getNickname());
                startActivityForResult(nicknameIntent, NICKNAME_CODE);
                break;
            case R.id.rl_gender_container:
                Intent genderIntent = new Intent(this, GenderModifyActivity.class);
                genderIntent.putExtra("gender", mSex);
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

    private void showOptionDialog() {
        AwesomeDialog.init()
                .setLayoutId(R.layout.dialog_modify_avatar)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        holder.setOnClickListener(R.id.btn_capture, v1 -> {
                            tempCapturePath = StorageUtil.getTempDir() + File.separator
                                    + System.currentTimeMillis()
                                    + "avatar_captured.jpg";
                            tempCropPath = StorageUtil.getTempDir()
                                    + File.separator + System.currentTimeMillis()
                                    + "avatar_crop.jpg";
                            RxPermisssionsUitls.getPicFromCamera(UserInfoActivity.this, tempCapturePath);
                            dialog.dismiss();
                        });
                        holder.setOnClickListener(R.id.btn_album, v12 -> {
                            tempCropPath = StorageUtil.getTempDir() + File.separator
                                    + System.currentTimeMillis()
                                    + "avatar_crop.jpg";
                            RxPermisssionsUitls.getPicFromAlbm(UserInfoActivity.this);
                            dialog.dismiss();
                        });
                        holder.setOnClickListener(R.id.btn_cancel, v13 -> dialog.dismiss());
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
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
                mSelectedPosition1 = options1;
                mSelectedPosition2 = options2;
            }
        })

                .setSelectOptions(mSelectedPosition1, mSelectedPosition2)
                .setSubCalSize(15)
                .setTitleSize(17)
                .setSubmitColor(Color.parseColor("#007aff"))
                .setCancelColor(Color.parseColor("#007aff"))
                .setTitleText("城市选择")
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(19)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }


    @Override
    public void showPortrait(String filename) {
        GlideImageLoader.getInstace().displayImageNoCache(this, tempCropPath, ivAvatar);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    public void showSuccess(String msg) {
        EventBus.getDefault().post(new UserInfoUpdateEvent());
        setupSex(mSex);
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
                () -> {
                    userInfoPresenter.onUpdataPortrait(mUserInfo.getData().getUserId() + "", tempCropPath);
                });
        switch (requestCode) {
            case NICKNAME_CODE:
                if (resultCode == RESULT_OK) {
                    String nickname = data.getStringExtra("nickname");
                    userInfoPresenter.onUpdataNickName(mUserInfo.getData().getUserId() + "", nickname);
                }
                break;
            case GENDER_CODE:
                if (resultCode == RESULT_OK) {
                    mSex = data.getStringExtra("gender");
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId", mUserInfo.getData().getUserId());
                    hashMap.put("sex", mSex);
                    userInfoPresenter.onUpdataUserInfo(hashMap);
                }
                break;
            default:
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
                .map((Function<String, Object>) str -> {
                    //获取assets目录下的json文件数据
                    String JsonData = JsonUtils.getJsonFromAssert(UserInfoActivity.this, str);
                    //用Gson 转成实体
                    ArrayList<JsonBean> jsonBean = parseData(JsonData);

                    /**
                     * 添加省份数据
                     *
                     * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
                     * PickerView会通过getPickerViewText方法获取字符串显示出来。
                     */
                    options1Items = jsonBean;
                    //遍历省份
                    for (int i = 0; i < jsonBean.size(); i++) {
                        //该省的城市列表（第二级）
                        ArrayList<String> CityList = new ArrayList<>();
                        //该省的所有地区列表（第三极）
                        ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();
                        //遍历该省份的所有城市
                        for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {
                            String CityName = jsonBean.get(i).getCityList().get(c).getName();
                            //添加城市
                            CityList.add(CityName);
                            //该城市的所有地区列表
                            ArrayList<String> City_AreaList = new ArrayList<>();

                            //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                            if (jsonBean.get(i).getCityList().get(c).getArea() == null
                                    || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                                City_AreaList.add("");
                            } else {
                                City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                            }
                            //添加该省所有地区数据
                            Province_AreaList.add(City_AreaList);

                            if (jsonBean.get(i).getCityList().get(c).getName().equals(mUserInfo.getData().getCity())) {
                                mSelectedPosition2 = c;
                            }
                        }

                        if (jsonBean.get(i).getName().equals(mUserInfo.getData().getProvince())) {
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
                })
                .subscribe(o -> {

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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoUpdateEvent userInfoUpdateEvent) {
        HashMap paramsMap = new HashMap();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        paramsMap.put("userId", userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            for (UserInfo.DataBean.LevelListBean levelList : mUserInfo.getData().getLevelList()) {
                                String levelType = levelList.getLevelType();
                                long sjNeedValue = levelList.getExpList().get(0).getSjNeedExpValue() - levelList.getExpList().get(0).getSjExpvalue();
                                if ("ROYAL_LEVEL".equals(levelType) && !levelList.getExpList().isEmpty()) {
                                    if (sjNeedValue >= 0) {
                                        tvUpdate.setText("离升级还需充值");
                                        tvUpdate.append(LightSpanString.getLightString(sjNeedValue + "", Color.parseColor("#FF7901")));
                                        tvUpdate.append("元");
                                    } else {
                                        tvUpdate.setText("您已达到最高贵族等级");
                                    }
                                    int userLevel = levelList.getLevelValue();
                                    if (userLevel != 0) {
//                                        ivRoyal.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(userLevel));
                                        Glide.with(UserInfoActivity.this).asGif().load(ResourceMap.getResourceMap().
                                                getRoyalLevelIcon(userLevel)).into(ivRoyal);
                                    }
                                    tvTotal.setText("本月已累计充值");
                                    tvTotal.append(LightSpanString.getLightString(levelList.getExpList().get(0).getSjExpvalue() + "", Color.parseColor("#FF7901")));
                                    tvTotal.append("元");
                                }
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg);
                    }
                });
    }
}
