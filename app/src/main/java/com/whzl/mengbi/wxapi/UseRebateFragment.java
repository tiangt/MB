package com.whzl.mengbi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2019/2/15
 */
public class UseRebateFragment extends BasePullListFragment<RebateBean.ListBean, BasePresenter> {
    @Override
    protected void initEnv() {
        super.initEnv();
        FrgActivity myActivity = (FrgActivity) getMyActivity();
        myActivity.setTitle("使用返利券");
        StatusBarUtil.setColor(myActivity, ContextCompat.getColor(myActivity, R.color.status_white_toolbar));
    }

    @Override
    public void init() {
        super.init();
        getPullView().setBackgroundColor(Color.parseColor("#ffffff"));
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_refresh_layout, getPullView(), false);
        ((TextView) view.findViewById(R.id.tv_content)).setText("你还没有奖励券哦");
        setEmptyView(view);
        View view1 = LayoutInflater.from(getMyActivity()).inflate(R.layout.divider_shawdow_white, getPullView(), false);
        addHeadTips(view1);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_use_rebate, getPullView(), false);
        TextView tvNoUse = view2.findViewById(R.id.tv_no_use);
        tvNoUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyActivity().setResult(Activity.RESULT_OK, new Intent().putExtra("rebate", (Parcelable) null));
                getMyActivity().finish();
            }
        });
        getAdapter().addHeaderView(view2);
    }

    @Override
    protected void loadData(int action, int mPage) {
        ArrayList<RebateBean.ListBean> data = getMyActivity().getIntent().getParcelableArrayListExtra("data");
        loadSuccess(data);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_use_rebate, getPullView(), false);
        return new RebateViewHolder(item);
    }

    class RebateViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_use_rebate)
        LinearLayout llUse;

        public RebateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            RebateBean.ListBean listBean = mDatas.get(position);
            String[] split = listBean.goodsName.split("%");
            if (split[0]!=null) {
                tvName.setText(split[0] + "%");
            }
            tvTime.setText("有效期截止："+listBean.expDate);
            llUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMyActivity().setResult(Activity.RESULT_OK, new Intent().putExtra("rebate", mDatas.get(position)));
                    getMyActivity().finish();
                }
            });
        }

    }
}
