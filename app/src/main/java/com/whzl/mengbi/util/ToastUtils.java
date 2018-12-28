package com.whzl.mengbi.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.common.BaseApplication;

public class ToastUtils {
    private static Toast toast;

    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }

    public static void showToast(int text) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }

    public static void toastMessage(Activity activity, String content) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_shop, null);
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(content);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, UIUtil.dip2px(activity, 100));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static void showCustomToast(Activity activity, String content) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_custom, null);
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(content);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, UIUtil.dip2px(activity, 100));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static void showRedToast(Activity activity, String content) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_red, null);
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(content);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, UIUtil.dip2px(activity, 100));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
