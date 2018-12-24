package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;

/**
 * @author cliang
 * @date 2018.12.24
 */
public class HeadlineLayout extends RelativeLayout {

    private Context mContext;
    private View mView;
    private TextView tvHeadline;

    public HeadlineLayout(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public HeadlineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public HeadlineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);
    }

    public TextView getTextView(){
        return tvHeadline;
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        mView = from.inflate(R.layout.layout_headline, this, false);
        addView(mView);
        tvHeadline = mView.findViewById(R.id.tv_headline_info);
    }


}
