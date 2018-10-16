package com.whzl.mengbi.ui.fragment.me;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class PropFragment extends BasePullListFragment {
    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_prop, null);
        addHeadTips(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_prop, null);
//        setEmptyView(view2);
    }

    public static PropFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putString("rankName", rankName);
//        args.putString("rankType", rankType);
//        args.putString("preCycle", preCycle);
        PropFragment propFragment = new PropFragment();
//        rankListFragment.setArguments(args);
        return propFragment;
    }

    @Override
    protected void loadData(int action) {
        ArrayList list = new ArrayList();
        list.add("ss");
        loadSuccess(null);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_prop_me, parent, false);
        return new NormalViewHolder(itemView);
    }

    class NormalViewHolder extends BaseViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
//            tvRank.setText(position + 1 + "");
//            RankListBean.DetailBean detailBean = mDatas.get(position);
//            ivStatus.setVisibility(detailBean.program != null && "T".equals(detailBean.program.status) ? View.VISIBLE : View.GONE);
//            tvNickName.setTextColor(detailBean.program != null && "T".equals(detailBean.program.status)
//                    ? Color.parseColor("#1edd8e")
//                    : Color.parseColor("#404040"));
//            if (detailBean.user != null) {
//                tvNickName.setText(detailBean.user.nickname);
//                if ("ANCHOR_LEVEL".equals(detailBean.user.levelType)) {
//                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(detailBean.user.level));
//                } else {
//                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(detailBean.user.level));
//                }
//            }
//            if (detailBean.rank != null) {
//                tvGap.setText(detailBean.rank.gap + "");
//            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
//            RankListBean.DetailBean detailBean = mDatas.get(position);
//            if (detailBean.program != null && detailBean.program.programId > 0) {
//                Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
//                intent.putExtra("programId", detailBean.program.programId);
//                startActivity(intent);
//            }
        }
    }
}
