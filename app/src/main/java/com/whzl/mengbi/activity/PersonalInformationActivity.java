package com.whzl.mengbi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.widget.view.GenericToolbar;

public class PersonalInformationActivity extends BaseAtivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_layout);
        initToolBar();
    }

   public void  initToolBar(){
       new GenericToolbar.Builder(this)
               //.addTitleImage(R.mipmap.ic_launcher)// 标题头像
               .addTitleText("个人信息")// 标题文本
               .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
               .addLeftIcon(1, R.mipmap.ic_login_return,this)// 响应左部图标的点击事件
               //.addLeftText(2, "",this)// 响应左部文本的点击事件
               //.addRightText(3, "",this)// 响应右部文本的点击事件
               //.addRightIcon(4, R.mipmap.ic_login_return,this)// 响应右部图标的点击事件
               .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
               .apply();

   }

    @Override
    public void onClick(View v) {

    }
}
