package com.whzl.mengbi.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.whzl.mengbi.ui.widget.view.CustomPopWindow;

public class CustomPopWindowUtils {

    /**
     * 聊天表情
     * @param context
     * @param layoutId
     * @param view
     * @param width
     * @param height
     * @return
     */
    public static CustomPopWindow talkCustomPopWindow(Context context,View layoutId, View view, int width, int height){
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(layoutId)//显示的布局，还可以通过设置一个View
                .size(width,height) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAtLocation(view, Gravity.BOTTOM,0,0);
        return customPopWindow;
    }

    public static CustomPopWindow giftCustomPopWindow(Context context,View layoutId, View view, int width, int height){
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(layoutId)//显示的布局，还可以通过设置一个View
                .size(width,height) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAtLocation(view, Gravity.BOTTOM,0,0);
        return customPopWindow;
    }


    /**
     * 相机 相册选择 取消
     * @param context
     * @param layoutId
     * @param view
     * @param width
     * @param height
     * @return
     */
    public  static CustomPopWindow profile(Context context,View layoutId, View view, int width, int height){
                CustomPopWindow mCustomPopWindow  = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(layoutId)//显示的布局，还可以通过设置一个View
                .size(width,height) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAtLocation(view, Gravity.BOTTOM,0,0);
                return mCustomPopWindow;
    }
}
