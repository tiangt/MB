package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class LiveHouseAudienceListFragmnet extends BaseAwesomeDialog {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_audience_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

    }

}
