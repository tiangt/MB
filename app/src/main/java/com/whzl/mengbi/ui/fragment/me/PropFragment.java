package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.PropBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class PropFragment extends BasePullListFragment<PropBean.ListBean, BasePresenter> {
    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColor(getMyActivity(), ContextCompat.getColor(getMyActivity(), R.color.status_white_toolbar));
        ((FrgActivity) getMyActivity()).setTitle("我的道具");
        ((FrgActivity) getMyActivity()).setTitleMenuIcon(R.drawable.ic_jump_shop_mine, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("sss");
            }
        });
    }

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldLoadMore() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_prop, getPullView(), false);
        getAdapter().addHeaderView(view);
        View view1 = LayoutInflater.from(getMyActivity()).inflate(R.layout.divider_shawdow_white, getPullView(), false);
        addHeadTips(view1);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_prop_pack, getPullView(), false);
        setEmptyView(view2);
        getPullView().setRefBackgroud(Color.parseColor("#ffffff"));
        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    public static PropFragment newInstance() {
        Bundle args = new Bundle();
        PropFragment propFragment = new PropFragment();
        propFragment.setArguments(args);
        return propFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
            paramsMap.put("page", mPage);
            paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
            ApiFactory.getInstance().getApi(Api.class)
                    .myService(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<PropBean>(this) {

                        @Override
                        public void onSuccess(PropBean bean) {
                            loadSuccess(bean.list);
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_prop_pack, parent, false);
        return new ViewHolder(itemView);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PropBean.ListBean bean = mDatas.get(position);
            tvName.setText(bean.goodsName);
            tvNum.setText(String.valueOf(bean.goodsSum));
            tvTime.setText("剩余" + String.valueOf(bean.surplusDay) + "天");
        }

    }
}
