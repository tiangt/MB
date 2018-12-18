package com.whzl.mengbi.ui.dialog.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.whzl.mengbi.R;
import com.whzl.mengbi.util.UIUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author cliang
 * @date 2018.12.17
 */
public abstract class BaseFullScreenDialog extends DialogFragment {

    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;
    private Unbinder unbinder;

    private int position = 0;

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder, BaseFullScreenDialog dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        layoutId = intLayoutId();

        //恢复保存的数据
        if (savedInstanceState != null) {
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        unbinder = ButterKnife.bind(this, view);
        convertView(ViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();

            if(position > 0){
                lp.y = position;
                window.setGravity(Gravity.CENTER_VERTICAL);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
    }

    public BaseFullScreenDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public BaseFullScreenDialog setPosition(int position) {
        this.position = position;
        return this;
    }

    public BaseFullScreenDialog show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
