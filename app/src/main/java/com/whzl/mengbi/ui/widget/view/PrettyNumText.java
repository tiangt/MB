package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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
    private int backgroundColor;
    private String prettyNum;
    private int numColor;
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

    public void setPrettyBgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPrettyNum(String prettyNum) {
        this.prettyNum = prettyNum;
    }

    public void setNumColor(int numColor) {
        this.numColor = numColor;
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_pretty_num, this, false);
        addView(inflate);
        tvPretty = inflate.findViewById(R.id.tv_pretty);
        tvNum = inflate.findViewById(R.id.tv_num);
    }

    public void setNumber(){
        tvPretty.setBackgroundColor(numColor);
        tvNum.setText(prettyNum);
        tvNum.setTextColor(numColor);
        tvNum.setBackgroundResource(backgroundColor);
    }


}
