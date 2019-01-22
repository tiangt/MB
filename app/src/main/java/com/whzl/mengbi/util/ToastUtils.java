package com.whzl.mengbi.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

    public static void showToastUnify(Activity activity, String content) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_unify, null);
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(content);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static Snackbar snack(View view, String text) {
        return snack(view, text, null, null);
    }

    public static Snackbar snackLong(View view, String text) {
        return snackLong(view, text, null, null);
    }

    public static Snackbar snack(View view, String text, @Nullable String action, @Nullable View
            .OnClickListener onClickListener) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(Snackbar.LENGTH_SHORT);
        snackbar.setAction(action, onClickListener);
        snackbar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        setSnackbarColor(snackbar,Color.parseColor("#fefefe"),Color.parseColor("#181818"),Color.parseColor("#fefefe"));
        snackbar.show();
        return snackbar;
    }

    public static Snackbar snackLong(View view, String text, @Nullable String action, @Nullable View
            .OnClickListener onClickListener) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.setAction(action, onClickListener);
        snackbar.addCallback(new Snackbar.Callback());
        snackbar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        setSnackbarColor(snackbar,Color.parseColor("#fefefe"),Color.parseColor("#181818"),Color.parseColor("#fefefe"));
        snackbar.show();
        return snackbar;
    }

    private static void setSnackbarColor(Snackbar snackbar, @ColorInt int messageColor, @ColorInt int
            backgroundColor, @ColorInt int actionColor) {
        View view = snackbar.getView();//获取Snackbar的view
        view.setBackgroundColor(backgroundColor);//修改view的背景色
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
        snackbar.setActionTextColor(actionColor);
    }

    public static void showCenterToast(String text){
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
