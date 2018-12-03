package com.whzl.mengbi.ui.activity;

import android.graphics.Color;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.widget.view.TextProgressBar;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2018.11.30
 */
public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.tp_anchor_level)
    TextProgressBar tpAnchorLevel;
    @BindView(R.id.tp_user_level)
    TextProgressBar tpUserLevel;
    @BindView(R.id.tp_royal_level)
    TextProgressBar tpRoyalLevel;

    private int mAnchorGrade;
    private int mUserGrade;
    private int mRoyalGrade;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void setupView() {
        tpAnchorLevel.setText(getString(R.string.anchor_grade, mAnchorGrade));
        tpAnchorLevel.setMaxCount(100);
        tpAnchorLevel.setCurrentCount(50);
        tpAnchorLevel.setPercentColor(Color.rgb(236,81,227));
        tpUserLevel.setText(getString(R.string.user_grade, mUserGrade));
        tpUserLevel.setMaxCount(100);
        tpUserLevel.setCurrentCount(50);
        tpUserLevel.setPercentColor(Color.rgb(236,194,56));
        tpRoyalLevel.setText(getString(R.string.royal_grade, mRoyalGrade));
        tpRoyalLevel.setMaxCount(100);
        tpRoyalLevel.setCurrentCount(50);
        tpRoyalLevel.setPercentColor(Color.rgb(246,55,73));
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }

}
