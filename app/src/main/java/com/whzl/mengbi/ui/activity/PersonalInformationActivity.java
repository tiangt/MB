package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.widget.view.CircleImageView;
import com.whzl.mengbi.widget.view.CustomPopWindow;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

public class PersonalInformationActivity extends BaseAtivity implements View.OnClickListener{

    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 2;

    CityPickerView cityPickerView = new CityPickerView();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_layout);
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            mUserInfo = (UserInfo) bundle.getSerializable("userbean");
        }
        cityPickerView.init(this);
        initView();
        initData();
        initToolBar("个人信息");
    }


    /**
     * 实例化控件
     */
   public void  initView(){
        mCircleImageView = (CircleImageView) findViewById(R.id.personal_information_head_img);
        mProfileTV = (TextView)findViewById(R.id.personal_information_head_text);
        mSpout =(TextView)  findViewById(R.id.personal_information_sprount);
        mNickName = (TextView) findViewById(R.id.personal_information_nickname);
        mSex = (TextView) findViewById(R.id.personal_information_sex);
        mAddress = (TextView) findViewById(R.id.personal_information_address);
        mBirthday = (TextView) findViewById(R.id.personal_information_birthday);
        mAnchorLevel = (TextView) findViewById(R.id.personal_information_anchorlevel);
        mAnchorImg = (ImageView) findViewById(R.id.personal_information_anchorlevel_img);
        mUserLevel = (TextView) findViewById(R.id.personal_information_userlevel);
        mUserImg = findViewById(R.id.personal_information_userlevel_img);
        mQuit = (Button) findViewById(R.id.personal_information_quit_but);
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
    private void  initData(){
        GlideImageLoader.getInstace().displayImage(this,mUserInfo.getData().getAvatar(),mCircleImageView);
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
        if(mUserInfo.getData().getProvince()!=null||mUserInfo.getData().getCity()!=null){
            mAddress.setText(mUserInfo.getData().getProvince()+"-"+mUserInfo.getData().getCity());
        }
        mBirthday.setText(mUserInfo.getData().getBirthday()+"");
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
        switch (v.getId()){
            case R.id.personal_information_head_text:
                View view = getLayoutInflater().inflate(R.layout.activity_personal_information_photo_pop_layout,null);
                mCustomPopWindow  = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(view)//显示的布局，还可以通过设置一个View
                        .size(width,height) //设置显示的大小，不设置就默认包裹内容
                        .setFocusable(true)//是否获取焦点，默认为ture
                        .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                        .create()//创建PopupWindow
                        .showAtLocation(mCircleImageView, Gravity.BOTTOM,0,0);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.personal_information_nickname:

                 break;
            case R.id.personal_information_sex:

                 break;
            case R.id.personal_information_birthday:
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
                        String time2 = DateUtils.getTime2(date);
                        String time = DateUtils.getTime(date);
                        mBirthday.setText(time);
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
            case R.id.personal_information_address:
                CityConfig cityConfig = new CityConfig.Builder()
                        .title("城市选择")//标题
                        .titleTextSize(22)//标题文字大小
                        .titleTextColor("#000000")//标题文字颜  色
                        .titleBackgroundColor("#f6f6f6")//标题栏背景色
                        .confirTextColor("#007aff")//确认按钮文字颜色
                        .confirmText("完成")//确认按钮文字
                        .confirmTextSize(22)//确认按钮文字大小
                        .cancelTextColor("#007aff")//取消按钮文字颜色
                        .cancelText("取消")//取消按钮文字
                        .cancelTextSize(22)//取消按钮文字大小
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                        .showBackground(true)//是否显示半透明背景
                        .visibleItemsCount(7)//显示item的数量
                        .province("浙江省")//默认显示的省份
                        .city("杭州市")//默认显示省份下面的城市
                        .district("滨江区")//默认显示省市下面的区县数据
                        .provinceCyclic(true)//省份滚轮是否可以循环滚动
                        .cityCyclic(true)//城市滚轮是否可以循环滚动
                        .districtCyclic(true)//区县滚轮是否循环滚动
                        .setCustomItemLayout(R.layout.item_city_layout)//自定义item的布局
                        .setCustomItemTextViewId(R.id.item_city_name_tv)//自定义item布局里面的textViewid
                        .drawShadows(false)//滚轮不显示模糊效果
                        .setLineColor("#cdcdcd")//中间横线的颜色
                        .setLineHeigh(5)//中间横线的高度
                        .setShowGAT(true)//是否显示港澳台数据，默认不显示
                        .build();
                        cityPickerView.setConfig(cityConfig);
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
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        cityPickerView.showCityPicker();
                break;
            case R.id.personal_information_quit_but:
                Intent mIntent = new Intent(PersonalInformationActivity.this,LoginActivity.class);
                mIntent.putExtra("touristFlag","0");
                startActivity(mIntent);
                finish();
                SPUtils.clear(this);
                break;
        }
    }

    /**
     * 更换头像
     * PopWindows view 事件
     * @param view
     */
    private void initPopView(View view){
        butPhoto = (Button) view.findViewById(R.id.personal_information_photo);
        butAlbum = (Button) view.findViewById(R.id.personal_information_album);
        butCancel = (Button) view.findViewById(R.id.personal_information_cancel);
        //拍照
        butPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromCamera();
            }
        });
        //相册选择
        butAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromAlbm();
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

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        RxPermissions rxPermissions = new RxPermissions(PersonalInformationActivity.this);
        rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            LogUtils.d("用户已同意该权限");
                            //用于保存调用相机拍照后所生成的文件
                            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
                            //跳转到调用系统相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //判断版本
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(PersonalInformationActivity.this, "com.whzl.mengbi.fileprovider", tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            } else {    //否则使用Uri.fromFile(file)方法获取Uri
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                            }
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogUtils.d("用户拒绝了该权限，没有选中『不再询问』");
                        }else {
                            //用户拒绝了该权限，并且选中『不再询问』
                            LogUtils.d("用户拒绝了该权限，并且选中『不再询问』");
                        }
                    }
                });

    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm(){
        RxPermissions rxPermissions = new RxPermissions(PersonalInformationActivity.this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            //用户已同意该权限
                            Matisse.from(PersonalInformationActivity.this)
                                    .choose(MimeType.allOf())//选择mime类型
                                    .countable(true)
                                    .maxSelectable(1)//图片选择最多的数量
                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new PicassoEngine())
                                    .theme(R.style.Matisse_Dracula)
                                    .forResult(ALBUM_REQUEST_CODE);
                            LogUtils.d("用户已同意该权限");
                        }else if(permission.shouldShowRequestPermissionRationale){
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogUtils.d("用户拒绝了该权限，没有选中『不再询问』");
                        }else {
                            //用户拒绝了该权限，并且选中『不再询问』
                            LogUtils.d("用户拒绝了该权限，并且选中『不再询问』");
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    System.out.println("CAMERA>>>>>>>>>"+data);
                }
                break;
            case ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    List list = Matisse.obtainResult(data);
                    for (int i=0;i<list.size();i++){
                        String strfile = list.get(i).toString();
                    }
                }
                break;
        }
    }
}
