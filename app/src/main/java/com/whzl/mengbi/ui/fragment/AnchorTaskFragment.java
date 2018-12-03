package com.whzl.mengbi.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class AnchorTaskFragment extends BaseFragment {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv)
    TextView tv;
    private AnchorTaskBean bean;

    public static AnchorTaskFragment newInstance(AnchorTaskBean dataBean) {
        AnchorTaskFragment anchorTaskFragment = new AnchorTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", dataBean);
        anchorTaskFragment.setArguments(bundle);
        return anchorTaskFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_anchor_task;
    }

    @Override
    public void init() {
        bean = getArguments().getParcelable("bean");
        Glide.with(this).load(bean.pic).into(iv);
        switch (bean.operation) {
            case "MUL":
                tv.setText(bean.completion * bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion * bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "DIV":
                tv.setText(bean.completion / bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion / bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "ADD":
                tv.setText(bean.completion + bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "SUB":
                tv.setText(bean.completion - bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion - bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            default:
                tv.setText(bean.completion + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
        }
    }

}
