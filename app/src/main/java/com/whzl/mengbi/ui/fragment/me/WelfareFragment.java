package com.whzl.mengbi.ui.fragment.me;

import android.widget.Button;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.contract.WelfarePresenter;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfareFragment extends BaseFragment<WelfarePresenter> implements WelfareContract.View {
    @BindView(R.id.btn)
    Button btn;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        ((FrgActivity) getMyActivity()).setTitle("新手任务");
        mPresenter = new WelfarePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void init() {

    }


    @Override
    public void onPrettySuccess(PackPrettyBean bean) {
        ToastUtils.toastMessage(getMyActivity(), "sssss");
    }


    @OnClick(R.id.btn)
    public void onViewClicked() {
        mPresenter.pretty(SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "", 1, 20);
    }
}
