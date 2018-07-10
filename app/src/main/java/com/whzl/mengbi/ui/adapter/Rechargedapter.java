package com.whzl.mengbi.ui.adapter;

import android.content.Context;

import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class Rechargedapter extends CommonAdapter<RechargeRuleListBean>{

    public Rechargedapter(Context context, int layoutId, List<RechargeRuleListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RechargeRuleListBean rechargeRuleListBean, int position) {

    }
}
