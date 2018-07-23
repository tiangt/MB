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
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.view.UserInfoView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.CustomPopWindow;
import com.whzl.mengbi.util.CustomPopWindowUtils;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.SelectorUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserInfoActivity extends BaseAtivity implements UserInfoView, View.OnClickListener{

    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_layout);
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            mUserInfo = (UserInfo) bundle.getSerializable("userbean");
        }
        userInfoPresenter = new UserInfoPresenterImpl(this);
        cityPickerView = new CityPickerView();
        cityPickerView.init(this);
        initView();
        initModelAndView();
    }

    /**
     * 实例化控件
     */
   public void  initView(){
        mCircleImageView = (CircleImageView) findViewById(R.id.user_info_head_img);
        mProfileTV = (TextView)findViewById(R.id.user_info_head_text);
        mSpout =(TextView)  findViewById(R.id.user_info_sprount);
        mNickName = (TextView) findViewById(R.id.user_info_nickname);
        mSex = (TextView) findViewById(R.id.user_info_sex);
        mAddress = (TextView) findViewById(R.id.user_info_address);
        mBirthday = (TextView) findViewById(R.id.user_info_birthday);
        mAnchorLevel = (TextView) findViewById(R.id.user_info_anchorlevel);
        mAnchorImg = (ImageView) findViewById(R.id.user_info_anchorlevel_img);
        mUserLevel = (TextView) findViewById(R.id.user_info_userlevel);
        mUserImg = findViewById(R.id.user_info_userlevel_img);
        mQuit = (Button) findViewById(R.id.user_info_quit_but);
        mNickName.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mProfileTV.setOnClickListener(this);
        mBirthday.setOnClickListener(this);
        mQuit.setOnClickListener(this);
    }

    /**
     *赋予控件数据
     */
    private void  initModelAndView(){
        GlideImageLoader.getInstace().circleCropImage(this,mUserInfo.getData().getAvatar(),mCircleImageView);
        mSpout.setText(mUserInfo.getData().getUserId()+"");
        mNickName.setText(mUserInfo.getData().getNickname());
        mSex.setText(mUserInfo.getData().getGender());
         if(mUserInfo.getData().getGender().equals("M")){
            mSex.setText("男");
        }else if(mUserInfo.getData().getGender().equals("W")){
            mSex.setText("女");
        }else{
            mSex.setText("保密");
        }
        StringBuilder stringBuilder = new StringBuilder();
        if(mUserInfo.getData().getProvince()!=null){
            stringBuilder.append(mUserInfo.getData().getProvince());
        }
        if(mUserInfo.getData().getCity()!=null){
            stringBuilder.append("-"+mUserInfo.getData().getCity());
        }
        mAddress.setText(stringBuilder);
        mBirthday.setText(mUserInfo.getData().getBirthday());
        for (UserInfo.DataBean.LevelListBean levelList:mUserInfo.getData().getLevelList()){
            if(levelList.getLevelType().equals("ROYAL_LEVEL")){
                for (UserInfo.DataBean.LevelListBean.ExpListBean expListBean:levelList.getExpList()){
                    String anchorsjexp = "离升级还差"+String.valueOf(expListBean.getSjNeedExpValue());
                    SpannableStringBuilder anchorSpan = new SpannableStringBuilder(anchorsjexp);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#f1275b"));
                    anchorSpan.setSpan(colorSpan,5,anchorsjexp.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    anchorSpan.append("主播经验");
                    mAnchorLevel.setText(anchorSpan);
                }
            }else {
                for (UserInfo.DataBean.LevelListBean.ExpListBean expListBean:levelList.getExpList()){
                    String usersjexp = "离升级还差"+String.valueOf(expListBean.getSjNeedExpValue());
                    SpannableStringBuilder userSpan = new SpannableStringBuilder(usersjexp);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4facf3"));
                    userSpan.setSpan(colorSpan,5,usersjexp.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    userSpan.append("富豪经验");
                    mUserLevel.setText(userSpan);
                }
                String levelVal = String.valueOf(levelList.getLevelValue());
                int resId = FileUtils.userLevelDrawable(levelVal);
                GlideImageLoader.getInstace().displayImage(this,resId,mUserImg);
            }
        }
    }

    /**
     * 监听view触发事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = DensityUtil.dp2px(200);
        Calendar calendar = Calendar.getInstance();
        long userId = mUserInfo.getData().getUserId();
        switch (v.getId()){
            case R.id.user_info_head_text://修改头像
                View view = getLayoutInflater().inflate(R.layout.activity_user_info_photo_pop_layout,null);
                mCustomPopWindow = CustomPopWindowUtils.profile(this,view,mCircleImageView,width,height);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.user_info_nickname://修改昵称
                 Intent nicknameIntent = new Intent(UserInfoActivity.this,UserInfoNickNameActivity.class);
                 nicknameIntent.putExtra("nickname",mUserInfo.getData().getNickname());
                 startActivityForResult(nicknameIntent,NICKNAME_CODE);
                 break;
            case R.id.user_info_sex://修改性别
                Intent sexIntent = new Intent(UserInfoActivity.this,UserInfoSexActivity.class);
                sexIntent.putExtra("sex",mUserInfo.getData().getGender());
                startActivityForResult(sexIntent,SEX_CODE);
                 break;
            case R.id.user_info_birthday://修改生日
                String  strDate = mUserInfo.getData().getBirthday();
                try {
                    Date date = DateUtils.getDate(strDate);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean type[] = new boolean[]{true,true,true,false,false,false};
                TimePickerView timePickerView = new TimePickerView
                        .Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time1 = DateUtils.getTime(date);
                        String time2 = DateUtils.getTime2(date);
                        HashMap hashMap = new HashMap();
                        hashMap.put("userId",userId);
                        hashMap.put("birthday",time2);
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
                timePickerView.show();
                break;
            case R.id.user_info_address://修改地区
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
                                    strBuilder.append("-"+city.getName());
                                }
                                mAddress.setText(strBuilder);
                                String provinceStr = province.getName();
                                String cityStr = city.getName();
                                HashMap hashMap = new HashMap();
                                hashMap.put("userId",userId);
                                hashMap.put("province",provinceStr);
                                hashMap.put("city",cityStr);
                                userInfoPresenter.onUpdataUserInfo(hashMap);
                            }
                            @Override
                            public void onCancel() {
                                cityPickerView.hide();
                            }
                        });
                        cityPickerView.showCityPicker();
                break;
            case R.id.user_info_quit_but://退出登录
                Intent mIntent = new Intent(UserInfoActivity.this,LoginActivityNew.class);
                mIntent.putExtra("visitor",true);
                startActivity(mIntent);
                SPUtils.remove(this,"islogin");//清除用户登录状态
                SPUtils.remove(this,"visitor");//清除游客登录状态
                SPUtils.remove(this,"userId");//清除用户Id
                SPUtils.remove(this,"username");//清除用户用户名
                SPUtils.remove(this,"password");//清除用户密码
                SPUtils.remove(this,"third_party");//清除第三方登录名称
                SPUtils.remove(this,"qq_openid");//清除第QQ登录openid
                SPUtils.remove(this,"qq_access_token");//清除QQ登录access_token
                SPUtils.remove(this,"weixin_openid");//清除微信登录openid
                SPUtils.remove(this,"weixin_access_token");//清除微信登录access_token
                finish();
                break;
        }
    }

    /**
     * 更换头像
     * PopWindows view 事件
     * @param view
     */
    private void initPopView(View view){
        butPhoto = (Button) view.findViewById(R.id.user_info_photo);
        butAlbum = (Button) view.findViewById(R.id.user_info_album);
        butCancel = (Button) view.findViewById(R.id.user_info_cancel);
        //拍照
        butPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermisssionsUitls.getPicFromCamera(UserInfoActivity.this);
            }
        });
        //相册选择
        butAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermisssionsUitls.getPicFromAlbm(UserInfoActivity.this);
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
        switch (requestCode){
            case CAMERA_REQUEST_CODE://相机
                if(resultCode == RESULT_OK){

                }
                break;
            case ALBUM_REQUEST_CODE://相册
                if(resultCode == RESULT_OK){
                    List list = Matisse.obtainResult(data);
                    for (int i=0;i<list.size();i++){
                        String strfile = list.get(i).toString();
                        LogUtils.d(strfile);
                        userInfoPresenter.onUpdataPortrait(mUserInfo.getData().getUserId()+"",strfile);
                    }
                }
                break;
            case NICKNAME_CODE://昵称
                if(resultCode == RESULT_OK){
                    String nickname = data.getStringExtra("nickname");
                    userInfoPresenter.onUpdataNickName(mUserInfo.getData().getUserId()+"",nickname);
                }
                break;
            case SEX_CODE://性别
                if(resultCode == RESULT_OK){
                    String sex = data.getStringExtra("sex");
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId",mUserInfo.getData().getUserId());
                    hashMap.put("sex",sex);
                    userInfoPresenter.onUpdataUserInfo(hashMap);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoPresenter.onDestory();
    }
}
