package com.whzl.mengbi.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.lljjcoder.citywheel.CityConfig;
import com.whzl.mengbi.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 滚动选择器
 */
public class SelectorUtils {

    /**
     * 时间选择器
     * @param context
     * @param strDate
     * @return
     */
    public static String[] time(Context context,String  strDate){
        String [] time  = new String[2];
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = DateUtils.getDate(strDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean type[] = new boolean[]{true,true,true,false,false,false};
        TimePickerView timePickerView = new TimePickerView
                .Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                        String time1 = DateUtils.getTime(date);
                        String time2 = DateUtils.getTime2(date);
                        time[0] = time1;
                        time[1] = time2;
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
        return time;
    }


    public static CityConfig address(){
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
                .setLineHeigh(2)//中间横线的高度
                .setShowGAT(true)//是否显示港澳台数据，默认不显示
                .build();
        return cityConfig;
    }
}
