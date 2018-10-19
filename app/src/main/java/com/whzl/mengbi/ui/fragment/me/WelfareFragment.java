package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.presenter.WelfarePresenter;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfareFragment extends BasePullListFragment<Object, WelfarePresenter> implements WelfareContract.View {
    private Map<Integer, Integer> map = new HashMap<>();

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldRefresh() {
        return false;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(getMyActivity(), Color.parseColor("#252525"));
        ((FrgActivity) getMyActivity()).setTitle("新手任务");
        ((FrgActivity) getMyActivity()).setTitleColor(ContextCompat.getColor(getMyActivity(), R.color.comm_white));
        ((FrgActivity) getMyActivity()).setTitleBlack();
        mPresenter = new WelfarePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_walfare, getPullView(), false);
        getAdapter().addHeaderView(view);
        initIcon();
    }

    private void initIcon() {
        map.put(0, R.drawable.ic_walfare_0);
        map.put(1, R.drawable.ic_walfare_0);
        map.put(2, R.drawable.ic_walfare_2);
        map.put(3, R.drawable.ic_walfare_3);
        map.put(4, R.drawable.ic_walfare_4);
        map.put(5, R.drawable.ic_walfare_5);
        map.put(6, R.drawable.ic_walfare_6);
        map.put(7, R.drawable.ic_walfare_7);
        map.put(8, R.drawable.ic_walfare_8);
        map.put(9, R.drawable.ic_walfare_9);
    }

    @Override
    protected void loadData(int action, int mPage) {
        List list = new ArrayList();
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        loadSuccess(list);
        mPresenter.pretty(SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "", 1, 20);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_walfare, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_state)
        TextView tvState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), map.get(position), ivIcon);
        }
    }

    @Override
    public void onPrettySuccess(PackPrettyBean bean) {
        ToastUtils.toastMessage(getMyActivity(), "sssss");
    }

}
