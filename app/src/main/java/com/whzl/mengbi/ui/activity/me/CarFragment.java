package com.whzl.mengbi.ui.activity.me;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import butterknife.BindView;

/**
 * @author niko
 * @date 2018/9/19
 */
public class CarFragment extends BaseFragment {
    @BindView(R.id.rb_qingtong)
    RadioButton rbQingtong;
    @BindView(R.id.rb_baiyin)
    RadioButton rbBaiyin;
    @BindView(R.id.rb_huangjin)
    RadioButton rbHuangjin;
    @BindView(R.id.rb_bojin)
    RadioButton rbBojin;
    @BindView(R.id.rb_zuanshi)
    RadioButton rbZuanshi;
    @BindView(R.id.rb_xingyao)
    RadioButton rbXingyao;
    @BindView(R.id.rb_wangzhe)
    RadioButton rbWangzhe;
    @BindView(R.id.rg_nobility)
    RadioGroup rgNobility;
    @BindView(R.id.iv_info)
    ImageView ivInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_car_shop;
    }

    @Override
    public void init() {
        rbQingtong.setChecked(true);
        rgNobility.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_qingtong:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_qingtong,ivInfo);
                    break;
                case R.id.rb_baiyin:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_baiyin,ivInfo);
                    break;
                case R.id.rb_huangjin:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_huangjin,ivInfo);
                    break;
                case R.id.rb_bojin:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_bojin,ivInfo);
                    break;
                case R.id.rb_zuanshi:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_zuanshi,ivInfo);
                    break;
                case R.id.rb_xingyao:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_xingyao,ivInfo);
                    break;
                case R.id.rb_wangzhe:
                    GlideImageLoader.getInstace().displayImage(getContext(),R.drawable.info_wangzhe,ivInfo);
                    break;
            }
        });
    }
}
