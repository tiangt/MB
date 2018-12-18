package com.whzl.mengbi.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/18
 */
public class FollowSortFragment extends BasePullListFragment {

    private String type;

    public static FollowSortFragment newInstance(String type) {
        FollowSortFragment followSortFragment = new FollowSortFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        followSortFragment.setArguments(bundle);
        return followSortFragment;
    }

    @Override
    public void init() {
        super.init();
        type = getArguments().getString("type");
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_follow_sort, getPullView(), false);
        TextView tv = view.findViewById(R.id.tv_content);
        switch (type) {
            case "guard":
                tv.setText("您还没有守护的主播");
                break;
            case "manage":
                tv.setText("还没有主播给您设过房管");
                break;
            case "watch":
                tv.setText("最近没有观看记录");
                break;
        }
        setEmptyView(view);
        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    @Override
    protected void loadData(int action, int mPage) {
        List list = new ArrayList();
        list.add("a");
        loadSuccess(list);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_follow_follow, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
//        @BindView(R.id.tv_busname)
//        TextView tvBusName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
        }
    }
}
