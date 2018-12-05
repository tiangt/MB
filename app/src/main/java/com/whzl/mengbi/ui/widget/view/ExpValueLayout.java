package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.util.ResourceMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cliang
 * @date 2018.12.5
 */
public class ExpValueLayout extends LinearLayout {

    private Context context;
    private View inflate;
    private ImageView ivLevelNow;
    private TextProgressBar tpUserLevel;
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

    public ExpValueLayout(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public ExpValueLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public ExpValueLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        inflate = from.inflate(R.layout.layout_exp_value, this, false);
        addView(inflate);
        ivLevelNow = inflate.findViewById(R.id.iv_level_now);
        tpUserLevel = inflate.findViewById(R.id.tp_user_level);
        ivLevelNext = inflate.findViewById(R.id.iv_level_next);
    }

    public void initView() {
        if (listBeans == null) {
            return;
        }
        getLevel();
        if ("ANCHOR_LEVEL".equals(levelType)) {
            ivLevelNow.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
            ivLevelNext.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue + 1));
        } else if ("ROYAL_LEVEL".equals(levelType)) {
            ivLevelNow.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
            ivLevelNext.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue + 1));
        } else if ("USER_LEVEL".equals(levelType)) {
            ivLevelNow.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
            ivLevelNext.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue + 1));
        }
    }

    private void getLevel() {
        for (int j = 0; j < listBeans.size(); j++) {
            expType = listBeans.get(j).getExpType();
            if ("GIFT_EXP".equals(expType)) {
                sjExpvalue = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue = listBeans.get(j).getSjNeedExpValue();
                tpUserLevel.setText(context.getString(R.string.anchor_grade, sjNeedExpValue));
                tpUserLevel.setMaxCount(sjExpvalue + sjNeedExpValue);
                tpUserLevel.setCurrentCount(sjExpvalue);
                tpUserLevel.setPercentColor(Color.rgb(236, 81, 227));
            } else if ("ROYAL_EXP".equals(expType)) {
                sjExpvalue1 = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue1 = listBeans.get(j).getSjNeedExpValue();
                tpUserLevel.setText(context.getString(R.string.royal_grade, sjNeedExpValue1));
                tpUserLevel.setMaxCount(sjExpvalue1 + sjNeedExpValue1);
                tpUserLevel.setCurrentCount(sjExpvalue1);
                tpUserLevel.setPercentColor(Color.rgb(246, 55, 73));
            } else if ("USER_EXP".equals(expType)) {
                sjExpvalue2 = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue2 = listBeans.get(j).getSjNeedExpValue();
                tpUserLevel.setText(context.getString(R.string.user_grade, sjNeedExpValue2));
                tpUserLevel.setMaxCount(sjExpvalue2 + sjNeedExpValue2);
                tpUserLevel.setCurrentCount(sjExpvalue2);
                tpUserLevel.setPercentColor(Color.rgb(236, 194, 56));
            }
        }
    }
}
