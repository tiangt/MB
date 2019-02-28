package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.PropBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.SPUtils;
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
        ((FrgActivity) getMyActivity()).setTitle("我的道具");
        ((FrgActivity) getMyActivity()).setTitleMenuIcon(R.drawable.ic_jump_shop_mine, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getMyActivity(), ShopActivity.class));
            }
        });
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
        getPullView().setBackgroundColor(Color.parseColor("#ffffff"));
    }

    public static PropFragment newInstance() {
        Bundle args = new Bundle();
        PropFragment propFragment = new PropFragment();
        propFragment.setArguments(args);
        return propFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(PullRecycler.ACTION_PULL_TO_REFRESH, 1);
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
            tvTime.setText("剩余");
            tvTime.append(LightSpanString.getLightString(String.valueOf(bean.surplusDay), Color.parseColor("#ff2d4e")));
            tvTime.append("天");
        }

    }
}
