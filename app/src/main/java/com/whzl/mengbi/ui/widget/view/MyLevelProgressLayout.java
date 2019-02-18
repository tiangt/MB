package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;

/**
 * @author cliang
 * @date 2019.2.18
 */
public class MyLevelProgressLayout extends LinearLayout {

    private Context context;
    private View inflate;
    private ImageView ivLevelNow;
    private CustomProgressBar tpMyLevel;
    private ImageView ivLevelNext;
    private String levelType;
    private int levelValue;
    private String levelName;
    private List<PersonalInfoBean.DataBean.LevelListBean.ExpListBean> listBeans;
    private String expType;
    private long sjNeedExpValue;
    private long sjExpvalue;
    private long sjExpvalue1;
    private long sjNeedExpValue1;
    private long sjExpvalue2;
    private long sjNeedExpValue2;
    private TextView tvPb;

    public MyLevelProgressLayout(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MyLevelProgressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public MyLevelProgressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public void setLevelValue(String levelType, int levelValue, String levelName,
                              List<PersonalInfoBean.DataBean.LevelListBean.ExpListBean> listBeans) {
        this.levelType = levelType;
        this.levelValue = levelValue;
        this.levelName = levelName;
        this.listBeans = listBeans;
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_my_level, this, false);
        addView(inflate);
        ivLevelNow = inflate.findViewById(R.id.iv_level_now);
        tpMyLevel = inflate.findViewById(R.id.tp_my_level);
        ivLevelNext = inflate.findViewById(R.id.iv_level_next);
        tvPb = inflate.findViewById(R.id.tv_pb);
    }

    public void initView() {
        if (listBeans == null) {
            return;
        }
        if ("ANCHOR_LEVEL".equals(levelType)) {
            ivLevelNow.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
            if (levelValue == 50) {
                ivLevelNext.setVisibility(INVISIBLE);
            } else {
                ivLevelNext.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue + 1));
            }
        } else if ("ROYAL_LEVEL".equals(levelType)) {
            if (levelValue == 0) {
                ivLevelNow.setVisibility(INVISIBLE);
            } else {
                ivLevelNow.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
            }
            if (levelValue == 7) {
                ivLevelNext.setVisibility(INVISIBLE);
            } else {
                ivLevelNext.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue + 1));
            }
        } else if ("USER_LEVEL".equals(levelType)) {
            ivLevelNow.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
            if (levelValue == 37) {
                ivLevelNext.setVisibility(INVISIBLE);
            } else {
                ivLevelNext.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue + 1));
            }
        }
    }

}
