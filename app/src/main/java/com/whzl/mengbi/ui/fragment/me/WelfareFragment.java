package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.view.ViewGroup;

import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.presenter.WelfarePresenter;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfareFragment extends BasePullListFragment<Object, WelfarePresenter> implements WelfareContract.View {
    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        ((FrgActivity) getMyActivity()).setTitle("新手任务");
        ((FrgActivity) getMyActivity()).setTitleColor(Color.parseColor("#ffffff"));
        ((FrgActivity) getMyActivity()).setTitleBlack();
        mPresenter = new WelfarePresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void loadData(int action, int mPage) {
        mPresenter.pretty(SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "", 1, 20);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onPrettySuccess(PackPrettyBean bean) {
        ToastUtils.toastMessage(getMyActivity(), "sssss");
    }

}
