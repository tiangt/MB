package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.sahasbhop.apngview.ApngImageLoader;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.ExpValueLayout;
import com.whzl.mengbi.ui.widget.view.MyLevelProgressLayout;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的--贵族等级
 *
 * @author cliang
 * @date 2019.2.18
 */
public class MyRoyalLevelFragment extends BaseFragment {

    @BindView(R.id.iv_royal_level)
    ImageView ivRoyalLevel;
    @BindView(R.id.tv_royal_level)
    TextView tvRoyalLevel;
    @BindView(R.id.tv_keep_level)
    TextView tvKeepLevel;
    @BindView(R.id.evl_royal_level)
    MyLevelProgressLayout evlRoyalLevel;

    private PersonalInfoBean.DataBean dataBean;
    private int levelValue;
    private String levelType;
    private List<PersonalInfoBean.DataBean.LevelListBean> listBeans = new ArrayList<>();
    private List<PersonalInfoBean.DataBean.LevelListBean.ExpListBean> expListBeans = new ArrayList<>();
    private long sjExpvalue;
    private long sjNeedExpValue;
    private long bjExpValue;
    private long bjNeedExpValue;

    public static MyRoyalLevelFragment newInstance(PersonalInfoBean.DataBean dataBean) {
        Bundle args = new Bundle();
        args.putParcelable("userInfo", dataBean);
        MyRoyalLevelFragment fragment = new MyRoyalLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_royal_level;
    }

    @Override
    public void init() {
        dataBean = getArguments().getParcelable("userInfo");
        if (dataBean != null) {
            listBeans = dataBean.getLevelList();
            if (listBeans != null) {
                for (int i = 0; i < listBeans.size(); i++) {
                    levelType = listBeans.get(i).getLevelType();
                    levelValue = listBeans.get(i).getLevelValue();
                    evlRoyalLevel.setLevelValue(levelType, levelValue, listBeans.get(i).getLevelName(), listBeans.get(i).getExpList());
                    evlRoyalLevel.initView();

                    if ("ROYAL_LEVEL".equals(levelType)) {
//                        Glide.with(this).asGif().load(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue)).into(ivRoyalLevel);
                        ApngImageLoader.getInstance()
                                .displayApng(LevelUtil.getRoyalBigApng(levelValue), ivRoyalLevel, new ApngImageLoader.ApngConfig(0, true));
                        expListBeans = listBeans.get(i).getExpList();
                        for (int j = 0; j < expListBeans.size(); j++) {
                            sjExpvalue = expListBeans.get(j).getSjExpvalue();
                            sjNeedExpValue = expListBeans.get(j).getSjNeedExpValue();
                            bjExpValue = expListBeans.get(j).getBjExpValue();
                            bjNeedExpValue = expListBeans.get(j).getBjNeedExpValue();

                            if (levelValue == 8) {
                                tvRoyalLevel.setText("您已达到最高贵族等级");
                            } else {
                                SpannableString sj = StringUtils.spannableStringColor("当前贵族经验 ", Color.parseColor("#ffffff"));
                                SpannableString sj2 = StringUtils.spannableStringColor(sjExpvalue + "", Color.parseColor("#F7FF00"));
                                SpannableString sj3 = StringUtils.spannableStringColor(" 点，离升级还差 ", Color.parseColor("#ffffff"));
                                SpannableString sj4 = StringUtils.spannableStringColor(sjNeedExpValue - sjExpvalue + "", Color.parseColor("#F7FF00"));
                                SpannableString sj5 = StringUtils.spannableStringColor(" 点", Color.parseColor("#ffffff"));

                                tvRoyalLevel.setText(sj);
                                tvRoyalLevel.append(sj2);
                                tvRoyalLevel.append(sj3);
                                tvRoyalLevel.append(sj4);
                                tvRoyalLevel.append(sj5);
                            }

                            if (bjNeedExpValue - bjExpValue > 0) {
                                SpannableString bj = StringUtils.spannableStringColor("本月保级经验 ", Color.parseColor("#ffffff"));
                                SpannableString bj2 = StringUtils.spannableStringColor(bjExpValue + "", Color.parseColor("#F7FF00"));
                                SpannableString bj3 = StringUtils.spannableStringColor(" 点，离保级还差 ", Color.parseColor("#ffffff"));
                                SpannableString bj4 = StringUtils.spannableStringColor(bjNeedExpValue - bjExpValue + "", Color.parseColor("#F7FF00"));
                                SpannableString bj5 = StringUtils.spannableStringColor(" 点", Color.parseColor("#ffffff"));

                                tvKeepLevel.setText(bj);
                                tvKeepLevel.append(bj2);
                                tvKeepLevel.append(bj3);
                                tvKeepLevel.append(bj4);
                                tvKeepLevel.append(bj5);
                            } else {
                                tvKeepLevel.setText("本月无需保级");
                            }

                            break;
                        }
                        break;
                    }
                }
            }
        }
    }

    @OnClick(R.id.btn_recharge)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge:
                Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                startActivity(intent);
                break;
        }
    }


}
