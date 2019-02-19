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
import com.whzl.mengbi.ui.widget.view.MyLevelProgressLayout;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的--富豪等级
 *
 * @author cliang
 * @date 2019.2.18
 */
public class MyRegalLevelFragment extends BaseFragment {

    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.iv_user_level)
    ImageView ivUserLevel;
    @BindView(R.id.tv_user_level)
    TextView tvUserLevel;
    @BindView(R.id.evl_user_level)
    MyLevelProgressLayout evlUserLevel;

    private PersonalInfoBean.DataBean dataBean;
    private int levelValue;
    private String levelType;
    private List<PersonalInfoBean.DataBean.LevelListBean> listBeans = new ArrayList<>();
    private List<PersonalInfoBean.DataBean.LevelListBean.ExpListBean> expListBeans = new ArrayList<>();
    private long sjExpvalue;
    private long sjNeedExpValue;

    public static MyRegalLevelFragment newInstance(PersonalInfoBean.DataBean dataBean) {
        Bundle args = new Bundle();
        args.putParcelable("userInfo", dataBean);
        MyRegalLevelFragment fragment = new MyRegalLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_regal_level;
    }

    @Override
    public void init() {
        dataBean = getArguments().getParcelable("userInfo");
        if (dataBean != null) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), dataBean.getAvatar(), ivUserImage);
            listBeans = dataBean.getLevelList();
            if (listBeans != null) {
                for (int i = 0; i < listBeans.size(); i++) {
                    levelType = listBeans.get(i).getLevelType();
                    levelValue = listBeans.get(i).getLevelValue();
                    evlUserLevel.setLevelValue(levelType, levelValue, listBeans.get(i).getLevelName(), listBeans.get(i).getExpList());
                    evlUserLevel.initView();

                    if ("USER_LEVEL".equals(levelType)) {
                        Glide.with(this).load(ResourceMap.getResourceMap().getUserLevelIcon(levelValue)).into(ivUserLevel);
                        expListBeans = listBeans.get(i).getExpList();
                        for (int j = 0; j < expListBeans.size(); j++) {
                            sjExpvalue = expListBeans.get(j).getSjExpvalue();
                            sjNeedExpValue = expListBeans.get(j).getSjNeedExpValue();

                            if(levelValue == 37){
                                tvUserLevel.setText("您已达到最高富豪等级");
                            }else{
                                SpannableString sj = StringUtils.spannableStringColor("当前富豪经验 ", Color.parseColor("#ffffff"));
                                SpannableString sj2 = StringUtils.spannableStringColor(sjExpvalue + "", Color.parseColor("#F7FF00"));
                                SpannableString sj3 = StringUtils.spannableStringColor(" 点，离下一级还差 ", Color.parseColor("#ffffff"));
                                SpannableString sj4 = StringUtils.spannableStringColor(sjNeedExpValue - sjExpvalue + "", Color.parseColor("#F7FF00"));
                                SpannableString sj5 = StringUtils.spannableStringColor(" 点", Color.parseColor("#ffffff"));

                                tvUserLevel.setText(sj);
                                tvUserLevel.append(sj2);
                                tvUserLevel.append(sj3);
                                tvUserLevel.append(sj4);
                                tvUserLevel.append(sj5);
                            }
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}
