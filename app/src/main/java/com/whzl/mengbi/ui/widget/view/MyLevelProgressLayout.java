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
import com.whzl.mengbi.util.LevelMap;
import com.whzl.mengbi.util.ResourceMap;

import org.w3c.dom.Text;

import java.util.List;

/**
 * @author cliang
 * @date 2019.2.18
 */
public class MyLevelProgressLayout extends LinearLayout {

    private Context context;
    private View inflate;
    private TextView tvLevelNow;
    private CustomProgressBar tpMyLevel;
    private TextView tvLevelNext;
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
        tvLevelNow = inflate.findViewById(R.id.tv_level_now);
        tpMyLevel = inflate.findViewById(R.id.tp_my_level);
        tvLevelNext = inflate.findViewById(R.id.tv_level_next);
    }

    public void initView() {
        if (listBeans == null) {
            return;
        }
        getLevel();
        if ("ANCHOR_LEVEL".equals(levelType)) {
            tvLevelNow.setText(LevelMap.getLevelMap().getAnchorLevel(levelValue));
            if (levelValue == 50) {
                tvLevelNext.setVisibility(INVISIBLE);
            } else {
                tvLevelNext.setText(LevelMap.getLevelMap().getAnchorLevel(levelValue + 1));
            }
        } else if ("ROYAL_LEVEL".equals(levelType)) {
            if (levelValue == 0) {
                tvLevelNow.setText("无等级");
            } else {
                tvLevelNow.setText(LevelMap.getLevelMap().getRoyalLevel(levelValue));
            }
            if (levelValue == 7) {
                tvLevelNext.setVisibility(INVISIBLE);
            } else {
                tvLevelNext.setText(LevelMap.getLevelMap().getRoyalLevel(levelValue + 1));
            }
        } else if ("USER_LEVEL".equals(levelType)) {
            tvLevelNow.setText(LevelMap.getLevelMap().getUserLevel(levelValue));
            if (levelValue == 37) {
                tvLevelNext.setVisibility(INVISIBLE);
            } else {
                tvLevelNext.setText(LevelMap.getLevelMap().getUserLevel(levelValue + 1));
            }
        }
    }

    private void getLevel() {
        for (int j = 0; j < listBeans.size(); j++) {
            expType = listBeans.get(j).getExpType();
            if ("GIFT_EXP".equals(expType)) {
                sjExpvalue = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue = listBeans.get(j).getSjNeedExpValue();
                tpMyLevel.setProgressDrawable(getResources().getDrawable(R.drawable.pb_my_anchor_level));
                if (levelValue == 50) {
                    tpMyLevel.setMax(100);
                    tpMyLevel.setProgress(100);
                } else {
                    tpMyLevel.setMax((int) (sjNeedExpValue));
                    if (sjExpvalue < sjNeedExpValue) {
                        tpMyLevel.setProgress((int) sjExpvalue);
                    }
                }
            } else if ("ROYAL_EXP".equals(expType)) {
                sjExpvalue1 = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue1 = listBeans.get(j).getSjNeedExpValue();
                tpMyLevel.setProgressDrawable(getResources().getDrawable(R.drawable.pb_my_royal_level));
                if (levelValue == 7) {
                    tpMyLevel.setMax(100);
                    tpMyLevel.setProgress(100);
                } else {
                    tpMyLevel.setMax((int) (sjNeedExpValue1));
                    if (sjExpvalue1 < sjNeedExpValue1) {
                        tpMyLevel.setProgress((int) sjExpvalue1);
                    }
                }
                tpMyLevel.setVisibility(VISIBLE);
            } else if ("USER_EXP".equals(expType)) {
                sjExpvalue2 = listBeans.get(j).getSjExpvalue();
                sjNeedExpValue2 = listBeans.get(j).getSjNeedExpValue();
                tpMyLevel.setProgressDrawable(getResources().getDrawable(R.drawable.pb_my_regal_level));
                if (levelValue == 37) {
                    tpMyLevel.setMax(100);
                    tpMyLevel.setProgress(100);
                } else {
                    tpMyLevel.setMax((int) (sjNeedExpValue2));
                    if (sjExpvalue2 < sjNeedExpValue2) {
                        tpMyLevel.setProgress((int) sjExpvalue2);
                    }
                }
                tpMyLevel.setVisibility(VISIBLE);
            }
        }
    }

}
