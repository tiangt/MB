package com.whzl.mengbi.ui.dialog;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.adapter.AudienceListAdapter;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class LiveHouseAudienceListFragment extends BaseAwesomeDialog {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ArrayList<AudienceListBean.DataBean.AudienceInfoBean> mDatas = new ArrayList<>();
    private AudienceListAdapter adapter;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_audience_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        recycler.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));

        adapter = new AudienceListAdapter(getContext(), R.layout.item_audience, mDatas);

        recycler.setAdapter(adapter);

        getAudienceList();

    }

    private void getAudienceList() {
    }

}
