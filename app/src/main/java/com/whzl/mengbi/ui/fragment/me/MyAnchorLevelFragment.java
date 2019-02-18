package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的--主播等级
 *
 * @author cliang
 * @date 2019.2.18
 */
public class MyAnchorLevelFragment extends BaseFragment {

    @BindView(R.id.iv_anchor_level)
    CircleImageView ivAnchorImage;
    @BindView(R.id.iv_anchor_image)
    ImageView ivAnchorLevel;
    @BindView(R.id.tv_anchor_level)
    TextView tvAnchorLevel;

    private PersonalInfoBean.DataBean dataBean;
    private int levelValue;
    private String levelType;
    private List<PersonalInfoBean.DataBean.LevelListBean> listBeans = new ArrayList<>();
    private List<PersonalInfoBean.DataBean.LevelListBean.ExpListBean> expListBeans = new ArrayList<>();
    private long sjExpvalue;
    private long sjNeedExpValue;

    public static MyAnchorLevelFragment newInstance(PersonalInfoBean.DataBean dataBean){
        Bundle args = new Bundle();
        args.putParcelable("userInfo", dataBean);
        MyAnchorLevelFragment fragment = new MyAnchorLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_anchor_level;
    }

    @Override
    public void init() {
        dataBean = getArguments().getParcelable("userInfo");
        if (dataBean != null) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), dataBean.getAvatar(), ivAnchorImage);
            listBeans = dataBean.getLevelList();
            if (listBeans != null) {
                for (int i = 0; i < listBeans.size(); i++) {
                    levelType = listBeans.get(i).getLevelType();
                    levelValue = listBeans.get(i).getLevelValue();
                    if ("ANCHOR_LEVEL".equals(levelType)) {
                        Glide.with(this).load(ResourceMap.getResourceMap().getUserLevelIcon(levelValue)).into(ivAnchorLevel);
                        expListBeans = listBeans.get(i).getExpList();
                        for (int j = 0; j < expListBeans.size(); j++) {
                            sjExpvalue = expListBeans.get(j).getSjExpvalue();
                            sjNeedExpValue = expListBeans.get(j).getSjNeedExpValue();

                            SpannableString sj = StringUtils.spannableStringColor("当前主播经验 ", Color.parseColor("#ffffff"));
                            SpannableString sj2 = StringUtils.spannableStringColor(sjExpvalue + "", Color.parseColor("#F7FF00"));
                            SpannableString sj3 = StringUtils.spannableStringColor(" 点，离下一级还差 ", Color.parseColor("#ffffff"));
                            SpannableString sj4 = StringUtils.spannableStringColor(sjNeedExpValue + "", Color.parseColor("#F7FF00"));
                            SpannableString sj5 = StringUtils.spannableStringColor(" 点", Color.parseColor("#ffffff"));

                            tvAnchorLevel.setText(sj);
                            tvAnchorLevel.append(sj2);
                            tvAnchorLevel.append(sj3);
                            tvAnchorLevel.append(sj4);
                            tvAnchorLevel.append(sj5);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}
