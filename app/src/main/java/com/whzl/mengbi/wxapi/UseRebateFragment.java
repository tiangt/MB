package com.whzl.mengbi.wxapi;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    protected void loadData(int action, int mPage) {
        ArrayList<RebateBean.ListBean> data = getMyActivity().getIntent().getParcelableArrayListExtra("data");
        loadSuccess(data);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_use_rebate, null);
        return new RebateViewHolder(item);
    }

    class RebateViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public RebateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            RebateBean.ListBean listBean = mDatas.get(position);
            tvName.setText(listBean.goodsName);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
        }
    }
}
