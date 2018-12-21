package com.whzl.mengbi.ui.dialog.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;

/**
 * 周星榜本周，上周
 *
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarListFragment extends BaseFragment {

    private final int TYPE_GROUP = 0xa01;
    private final int TYPE_CHILD = 0xa02;
    private int[] groupIcons = {R.drawable.ic_week_anchor, R.drawable.ic_week_rich};

    public static WeekStarListFragment newInstance() {
        WeekStarListFragment fragment = new WeekStarListFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_star_list;
    }

    @Override
    public void init() {

    }

    class RankRecyclerViewAdapter extends RecyclerView.Adapter<ItemVH> {

        @NonNull
        @Override
        public ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            ItemVH itemVH = null;
            switch (viewType) {
                case TYPE_GROUP:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
                    itemVH = new GroupVH(view);
                    break;

                case TYPE_CHILD:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_star, parent, false);
                    itemVH = new ChildVH(view);
                    break;
            }
            return itemVH;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemVH holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_GROUP:
                    break;

                case TYPE_CHILD:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    private class GroupVH extends ItemVH {
        private ImageView ivGroupTip;

        public GroupVH(View itemView) {
            super(itemView);
            ivGroupTip = itemView.findViewById(R.id.iv_group_tip);
        }

        @Override
        public int getType() {
            return TYPE_GROUP;
        }
    }

    private class ChildVH extends ItemVH {
        private TextView tvRank;
        private CircleImageView ivAvatar;
        private TextView tvNickName;
        private ImageView ivLevel;
        private TextView tvValue;

        public ChildVH(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_ranking);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNickName = itemView.findViewById(R.id.tv_nick_name);
            ivLevel = itemView.findViewById(R.id.iv_level);
            tvValue = itemView.findViewById(R.id.tv_value);
        }

        @Override
        public int getType() {
            return TYPE_CHILD;
        }
    }

    private abstract class ItemVH extends RecyclerView.ViewHolder {
        public ItemVH(View itemView) {
            super(itemView);
        }

        public abstract int getType();
    }

    private class Group extends Item {
        public int icon;

        @Override
        public int getType() {
            return TYPE_GROUP;
        }
    }

    private class Child extends Item{

        @Override
        public int getType() {
            return TYPE_CHILD;
        }
    }

    private abstract class Item {
        public int position;
        public abstract int getType();
    }
}
