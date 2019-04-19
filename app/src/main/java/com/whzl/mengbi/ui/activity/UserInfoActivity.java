package com.whzl.mengbi.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.JsonBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.view.UserInfoView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.JsonUtils;
import com.whzl.mengbi.util.PhotoUtil;
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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    //修改昵称
    public static final int NICKNAME_CODE = 3;
    //修改性别
    private static final int GENDER_CODE = 2;
    //修改个签
    private static final int SIGN_CODE = 4;
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
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.rl_account_container)
    RelativeLayout rlAccount;
    private UserInfoPresenter userInfoPresenter;
    private String tempCapturePath;
    private String tempCropPath;
    private UserInfo.DataBean mUserInfo;
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
        userInfoPresenter = new UserInfoPresenterImpl(this);

    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_user_info_layout, R.string.personal_info, true);
    }

    @Override
    protected void setupView() {
        BusinessUtils.getUserInfo(this, SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString(), new BusinessUtils.UserInfoListener() {
            @Override
            public void onSuccess(UserInfo.DataBean bean) {
                mUserInfo = bean;
                initView();
                initJsonData();
            }

            @Override
            public void onError(int code) {

            }
        });
        rlAccount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", tvAccount.getText().toString());
                cm.setPrimaryClip(mClipData);
                ToastUtils.showToastUnify(UserInfoActivity.this, "复制成功");
                return false;
            }
        });
    }

    private void initView() {
        if (mUserInfo != null) {
            GlideImageLoader.getInstace().circleCropImage(this, mUserInfo.getAvatar(), ivAvatar);
        }
        tvAccount.setText(mUserInfo.getUserId() + "");
        tvNickName.setText(mUserInfo.getNickname() == null ? "" : mUserInfo.getNickname());
        mSex = mUserInfo.getGender();
        setupSex(mSex);
        StringBuilder stringBuilder = new StringBuilder();
        if (mUserInfo.getProvince() != null) {
            stringBuilder.append(mUserInfo.getProvince());
        }
        if (mUserInfo.getCity() != null) {
            stringBuilder.append(mUserInfo.getCity());
        }
        tvAddress.setText(stringBuilder);
        tvBirthday.setText(DateUtils.getTime(mUserInfo.getBirthday()));
        if (TextUtils.isEmpty(mUserInfo.getIntroduce())) {
            tvSign.setText("未设置");
        } else {
            tvSign.setText(mUserInfo.getIntroduce());
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
    @OnClick({R.id.rl_avatar_container, R.id.rl_nick_name_container, R.id.rl_gender_container,
            R.id.rl_address_container, R.id.rl_birthday_container, R.id.rl_sign_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar_container:
                showOptionDialog();
                break;
            case R.id.rl_nick_name_container:
                Intent nicknameIntent = new Intent(UserInfoActivity.this, NickNameModifyActivity.class);
                nicknameIntent.putExtra("nickname", mUserInfo.getNickname());
                startActivityForResult(nicknameIntent, NICKNAME_CODE);
                break;
            case R.id.rl_gender_container:
                showMaleDialog();
                break;
            case R.id.rl_address_container:
                showAddressPick();
                break;
            case R.id.rl_birthday_container:
                showDatePick();
                break;
            case R.id.rl_sign_container:
                if (tvSign.getText().toString().equals("未设置") || TextUtils.isEmpty(tvSign.getText())) {
                    startActivityForResult(new Intent(this, SignActivity.class).putExtra("sign", ""), SIGN_CODE);
                } else
                    startActivityForResult(new Intent(this, SignActivity.class).putExtra("sign", tvSign.getText().toString()), SIGN_CODE);
                break;
            default:
                break;
        }
    }

    private void showMaleDialog() {
        AwesomeDialog.init()
                .setLayoutId(R.layout.dialog_modify_sex)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        holder.setOnClickListener(R.id.btn_male, v1 -> {
                            if (mSex.equals("M")) {
                                return;
                            }
                            mSex = "M";
                            HashMap hashMap = new HashMap();
                            hashMap.put("userId", mUserInfo.getUserId());
                            hashMap.put("sex", mSex);
                            userInfoPresenter.onUpdataUserInfo(hashMap);
                            dialog.dismiss();
                        });
                        holder.setOnClickListener(R.id.btn_female, v12 -> {
                            if (mSex.equals("W")) {
                                return;
                            }
                            mSex = "W";
                            HashMap hashMap = new HashMap();
                            hashMap.put("userId", mUserInfo.getUserId());
                            hashMap.put("sex", mSex);
                            userInfoPresenter.onUpdataUserInfo(hashMap);
                            dialog.dismiss();
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
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
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    private void showDatePick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateUtils.dateStrToMillis(mUserInfo.getBirthday()));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvBirthday.setText(DateUtils.getTime(date));
                HashMap hashMap = new HashMap();
                hashMap.put("userId", mUserInfo.getUserId());
                hashMap.put("birthday", DateUtils.getTime2(date));
                userInfoPresenter.onUpdataUserInfo(hashMap);
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(13)
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(15)//标题文字大小
                .setTitleText("出生日期")//标题文字
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
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
                tvAddress.setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2));
                HashMap hashMap = new HashMap();
                hashMap.put("userId", mUserInfo.getUserId());
                hashMap.put("province", options1Items.get(options1).getPickerViewText());
                hashMap.put("city", options2Items.get(options1).get(options2));
                userInfoPresenter.onUpdataUserInfo(hashMap);
                mSelectedPosition1 = options1;
                mSelectedPosition2 = options2;
            }
        })
                .setTitleText("城市")
                .setSelectOptions(mSelectedPosition1, mSelectedPosition2)
                .setSubCalSize(13)
                .setTitleSize(15)
                .setLineSpacingMultiplier(2)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
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
    public void onSignSuccess(String sign) {
        tvSign.setText(sign);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtil.onActivityResult(this, tempCapturePath, tempCropPath, requestCode, resultCode, data,
                () -> {
                    userInfoPresenter.onUpdataPortrait(mUserInfo.getUserId() + "", tempCropPath);
                });
        switch (requestCode) {
            case NICKNAME_CODE:
                if (resultCode == RESULT_OK) {
                    String nickname = data.getStringExtra("nickname");
                    tvNickName.setText(nickname);
                    if (mUserInfo != null) {
                        mUserInfo.setNickname(nickname);
                    }
                }
                break;
            case GENDER_CODE:
                if (resultCode == RESULT_OK) {
                    mSex = data.getStringExtra("gender");
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId", mUserInfo.getUserId());
                    hashMap.put("sex", mSex);
                    userInfoPresenter.onUpdataUserInfo(hashMap);
                }
                break;
            case SIGN_CODE:
                if (resultCode == RESULT_OK) {
                    String sign = data.getStringExtra("sign");
                    userInfoPresenter.onUpdateSign(mUserInfo.getUserId() + "", sign);
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

                            if (jsonBean.get(i).getCityList().get(c).getName().equals(mUserInfo.getCity())) {
                                mSelectedPosition2 = c;
                            }
                        }

                        if (jsonBean.get(i).getName().equals(mUserInfo.getProvince())) {
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
    }

}
