package com.whzl.mengbi.ui.activity.me;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author niko
 * @date 2018/9/19
 */
public class NobilityFragment extends BaseFragment {
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nobility_shop;
    }

    @Override
    public void init() {
        rbQingtong.setChecked(true);
        rgNobility.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_qingtong:
                    break;
                case R.id.rb_baiyin:
                    break;
                case R.id.rb_huangjin:
                    break;
                case R.id.rb_bojin:
                    break;
                case R.id.rb_zuanshi:
                    break;
                case R.id.rb_xingyao:
                    break;
                case R.id.rb_wangzhe:
                    break;
            }
        });
    }

}
