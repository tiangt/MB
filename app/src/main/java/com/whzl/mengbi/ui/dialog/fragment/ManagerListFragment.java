package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseListFragment;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 房管列表
 *
 * @author cliang
 * @date 2018.12.19
 */
public class ManagerListFragment extends BasePullListFragment {

    public static ManagerListFragment newInstance(){
        ManagerListFragment fragment = new ManagerListFragment();
        return fragment;
    }

    @Override
    public void init() {
        super.init();
        getPullView().setShouldRefresh(false);
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_follow_sort, getPullView(), false);
        TextView content = view.findViewById(R.id.tv_content);
        content.setText("暂无房管");
        content.setTextColor(Color.parseColor("#70ffffff"));
        setEmptyView(view);

        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    @Override
    protected void loadData(int action, int mPage) {

    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_manager, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.ll_manager_container)
        LinearLayout managerLayout;
        @BindView(R.id.tv_pretty_num)
        PrettyNumText tvPrettyNum;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_level_container)
        LinearLayout levelLayout;
        @BindView(R.id.iv_car)
        ImageView ivCar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }
    }
}
