package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;

/**
 * @author cliang
 * @date 2018.12.5
 */
public class PrettyNumText extends LinearLayout {

    private Context context;
    private View inflate;
    private String prettyNum;
    private float size;
    private TextView tvPretty;
    private TextView tvNum;

    public PrettyNumText(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public PrettyNumText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public PrettyNumText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public void setPrettyNum(String prettyNum) {
        this.prettyNum = prettyNum;
    }

    public void setPrettyTextSize(float size) {
        this.size = size;
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_pretty_num, this, false);
        addView(inflate);
        tvPretty = inflate.findViewById(R.id.tv_pretty);
        tvNum = inflate.findViewById(R.id.tv_num);
    }

    public void setPrettyType(String type) {
        int startColor;
        int normalColor;
        if ("A".equals(type)) {
            startColor = Color.rgb(255, 0, 255);
            normalColor = Color.rgb(255, 43, 63);
            tvNum.setBackgroundResource(R.drawable.shape_pretty_a);
        } else if ("B".equals(type)) {
            startColor = Color.rgb(255, 69, 0);
            normalColor = Color.rgb(255, 165, 0);
            tvNum.setBackgroundResource(R.drawable.shape_pretty_b);
        } else if ("C".equals(type)) {
            startColor = Color.rgb(152, 139, 249);
            normalColor = startColor;
            tvNum.setBackgroundResource(R.drawable.shape_pretty_c);
        } else if ("D".equals(type)) {
            startColor = Color.rgb(93, 186, 246);
            normalColor = startColor;
            tvNum.setBackgroundResource(R.drawable.shape_pretty_d);
        } else if ("E".equals(type)) {
            startColor = Color.rgb(94, 202, 194);
            normalColor = startColor;
            tvNum.setBackgroundResource(R.drawable.shape_pretty_e);
        } else {
            startColor = Color.rgb(94, 202, 194);
            normalColor = startColor;
            tvNum.setBackgroundResource(R.drawable.shape_pretty_e);
        }
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, normalColor});
        tvPretty.setBackground(drawable);
        tvNum.setText(prettyNum);
        tvNum.setTextColor(normalColor);
        tvPretty.setTextSize(size);
        tvNum.setTextSize(size);
    }

}
